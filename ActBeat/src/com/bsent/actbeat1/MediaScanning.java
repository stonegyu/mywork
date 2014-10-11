package com.bsent.actbeat1;


import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.util.Log;

public class MediaScanning implements MediaScannerConnectionClient {

	private MediaScannerConnection mConnection;

	private File mTargetFile;

	public MediaScanning(Context mContext, File targetFile) {

		this.mTargetFile = targetFile;

		mConnection = new MediaScannerConnection(mContext, this);
		Log.d("llog",mTargetFile.getAbsolutePath());
		mConnection.connect();

	}

	@Override
	public void onMediaScannerConnected() {
		Log.d("llog","con");
		mConnection.scanFile(mTargetFile.getAbsolutePath(), null);

	}

	@Override
	public void onScanCompleted(String path, Uri uri) {
		Log.d("llog","comple");
		mConnection.disconnect();
	}
}
