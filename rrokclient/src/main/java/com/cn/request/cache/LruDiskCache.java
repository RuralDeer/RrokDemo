package com.cn.request.cache;

import android.text.TextUtils;

import com.cn.HttpClient;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Date: 2019/7/15
 * <p>
 * Time: 4:08 PM
 * <p>
 * author: 鹿文龙
 *
 *  DiskLruCache 使用对象存储方式
 */
public class LruDiskCache extends ILruCache {

	private static LruDiskCache instance = new LruDiskCache();

	private DiskLruCache diskLruCache = null;

	private LruDiskCache() {
		File cacheDir = HttpClient.getContext().getCacheDir();
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
		try {
			diskLruCache = DiskLruCache.open(cacheDir, 1, 1, 50 * 1024 * 1024);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static LruDiskCache getInstance() {
		return instance;
	}

	@Override
	public <T> void put(String key, T value) {
		if (TextUtils.isEmpty(key) || null == value) return;
		try {
			DiskLruCache.Editor editor = diskLruCache.edit(key);
			OutputStream outputStream = editor.newOutputStream(0);
			if (writeObjectToStream(value, outputStream)) {
				editor.commit();
			} else {
				editor.abort();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		try {
			DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
			if (snapshot == null) {
				return null;
			}
			InputStream inputStream = snapshot.getInputStream(0);
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
			Object object = objectInputStream.readObject();
			return (T) object;
		} catch (IOException | ClassNotFoundException | ClassCastException e) {
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public void remove(String key) {
		try {
			diskLruCache.remove(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private <T> boolean writeObjectToStream(T obj, OutputStream outputStream) {
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(obj);
			objectOutputStream.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
