package com.cn.demo.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cn.HttpClient;
import com.cn.demo.Api;
import com.cn.demo.R;
import com.cn.demo.adapter.FullyGridLayoutManager;
import com.cn.demo.adapter.GridImageAdapter;
import com.cn.demo.bean.UploadBean;
import com.cn.demo.events.SendEvent;
import com.cn.demo.fragment.base.BaseFragment;
import com.cn.request.call.ApiBaseFileResult;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class UploadFragment extends BaseFragment {

	private List<LocalMedia> selectList = new ArrayList<>();
	private RecyclerView recyclerView;
	private GridImageAdapter adapter;
	private TextView messageTv;
	private ProgressBar progressBar;
	private ProgressBar progressBar2;
	private int maxSelectNum = 3;
	private RxPermissions rxPermissions;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_up_load, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		rxPermissions = new RxPermissions(this);
		recyclerView = view.findViewById(R.id.recycler);
		messageTv = view.findViewById(R.id.messageTv);
		progressBar = view.findViewById(R.id.progressBar);
		progressBar2 = view.findViewById(R.id.progressBar2);
		FullyGridLayoutManager manager = new FullyGridLayoutManager(getActivity(), 4, GridLayoutManager.VERTICAL, false);
		recyclerView.setLayoutManager(manager);
		adapter = new GridImageAdapter(getActivity(), rxPermissions, onAddPicClickListener);
		adapter.setList(selectList);
		adapter.setSelectMax(maxSelectNum);
		recyclerView.setAdapter(adapter);
		adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int position, View v) {
				LocalMedia media = selectList.get(position);
				String pictureType = media.getPictureType();
				int mediaType = PictureMimeType.pictureToVideo(pictureType);
				switch (mediaType) {
					case 1:
						// 预览图片
						PictureSelector.create(UploadFragment.this).themeStyle(R.style.picture_QQ_style).openExternalPreview(position, selectList);
						break;
					case 2:
						// 预览视频
						PictureSelector.create(UploadFragment.this).externalPictureVideo(media.getPath());
						break;
					case 3:
						// 预览音频
						PictureSelector.create(UploadFragment.this).externalPictureAudio(media.getPath());
						break;
				}
			}
		});
	}

	private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
		@Override
		public void onAddPicClick() {
			// 进入相册 以下是例子：不需要的api可以不写
			PictureSelector.create(UploadFragment.this)
				.openGallery(PictureMimeType.ofAll())
				.theme(R.style.picture_QQ_style)
				.maxSelectNum(maxSelectNum)
				.minSelectNum(1)
				.selectionMode(PictureConfig.MULTIPLE)
				.previewImage(true)
				.previewVideo(true)
				.enablePreviewAudio(true) // 是否可播放音频
				.isCamera(true)
				.enableCrop(false)
				.compress(true)
				.glideOverride(160, 160)
				.previewEggs(true)
				.withAspectRatio(3, 4)
				.hideBottomControls(false)
				.isGif(true)
				.freeStyleCropEnabled(false)
				.circleDimmedLayer(false)
				.showCropFrame(false)
				.showCropGrid(false)
				.openClickSound(false)
				.selectionMedia(selectList)
				.forResult(PictureConfig.CHOOSE_REQUEST);
		}
	};


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
				case PictureConfig.CHOOSE_REQUEST:
					// 图片选择
					selectList = PictureSelector.obtainMultipleResult(data);
					adapter.setList(selectList);
					adapter.notifyDataSetChanged();
					break;
			}
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onSendEvent(SendEvent event) {

		HttpClient.upload(Api.BASE_URL + "upload")
			.param("name", "张三")
			.file("file", new File(selectList.get(0).getPath()))
			.file("file2", new File(selectList.get(1).getPath()))
			.enqueue(new ApiBaseFileResult<UploadBean>() {
				@Override
				public void onDisposable(Disposable disposable) {}
				@Override
				public void onProgress(File file, int progress, long total) {
					if (selectList.get(0).getPath().equalsIgnoreCase(file.getPath())) {
						progressBar.setProgress(progress);
					} else {
						progressBar2.setProgress(progress);
					}
				}
				@Override
				public void onSuccess(UploadBean uploadBean) {
					messageTv.setText(uploadBean.toString());
				}

				@Override
				public void onFailure(Throwable e) {
					messageTv.setText(e.getMessage());
				}

				@Override
				public void onComplete() {}
			});
	}
}
