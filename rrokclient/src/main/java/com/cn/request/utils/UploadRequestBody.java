package com.cn.request.utils;

import com.cn.request.call.UploadCallBack;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Date: 2019/8/1
 * <p>
 * Time: 4:55 PM
 * <p>
 * author: 鹿文龙
 */
public class UploadRequestBody<T> extends RequestBody {

	private final static MediaType MEDIA_TYPE = MediaType.parse("multipart/form-data; charset=utf-8");

	private File file;

	private RequestBody requestBody;

	private UploadCallBack<T> uploadCallBack;

	public UploadRequestBody(File file, UploadCallBack<T> uploadCallBack) {
		this.file = file;
		this.requestBody = RequestBody.create(MEDIA_TYPE, file);
		this.uploadCallBack = uploadCallBack;
	}

	@Override
	public MediaType contentType() {
		return requestBody.contentType();
	}

	@Override
	public void writeTo(BufferedSink sink) throws IOException {
		CountingSink countingSink = new CountingSink(sink);
		BufferedSink bufferedSink = Okio.buffer(countingSink);
		//写入
		requestBody.writeTo(bufferedSink);
		//刷新
		//必须调用flush，否则最后一部分数据可能不会被写入
		bufferedSink.flush();
	}

	protected final class CountingSink extends ForwardingSink {

		private long bytesWritten = 0;

		public CountingSink(Sink delegate) {
			super(delegate);
		}

		@Override
		public void write(Buffer source, long byteCount) throws IOException {
			super.write(source, byteCount);
			bytesWritten += byteCount;
			if (uploadCallBack != null) {
				uploadCallBack.onProgress(file, (int) (bytesWritten * 100 / requestBody.contentLength()), requestBody.contentLength());
			}
		}
	}
}
