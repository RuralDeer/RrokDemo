package com.cn.demo.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.HttpClient;
import com.cn.demo.R;
import com.cn.demo.events.SendEvent;
import com.cn.demo.fragment.base.BaseFragment;
import com.cn.request.model.DownloadModel;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class DownloadFragment extends BaseFragment {

    private TextView messageTv;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_download, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        messageTv = view.findViewById(R.id.messageTv);
        progressBar = view.findViewById(R.id.progressBar);
    }

    @SuppressLint("CheckResult")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSendEvent(SendEvent event) {
        Log.d("onSendEvent", "DownloadFragment - event.position:" + event.position);
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    download();
                } else {
                    Toast.makeText(getContext(), "未授权权限，部分功能不能使用", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void download() {
        final File[] file = new File[1];
        String url = "https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk";
        final String path = Environment.getExternalStorageDirectory() + File.separator + "1_test";
        String fileName = "mobileqq_android.apk";
        HttpClient.download(url)
                .tObservable(path, fileName)
                .subscribe(new Observer<DownloadModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DownloadModel downloadModel) {
                        file[0] = downloadModel.file;
                        progressBar.setProgress(downloadModel.progress);
                        messageTv.setText(downloadModel.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        messageTv.setText(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        messageTv.setText(file[0].getPath());
                    }
                });
    }
}
