package com.cn.request.model;

import java.io.File;
import java.io.Serializable;

public class DownloadModel implements Serializable {

    public File file;
    public String fileName;
    public long fileLength;  //单位 byte
    public long currentSize; //当前下载大小
    public int progress;     //当前下载进度
    public long speed;       //下载速率


    @Override
    public String toString() {
        return "DownloadModel{" +
                "file=" + file +
                ", fileName='" + fileName + '\'' +
                ", fileLength=" + fileLength +
                ", currentSize=" + currentSize +
                ", progress=" + progress +
                ", speed=" + speed +
                '}';
    }
}
