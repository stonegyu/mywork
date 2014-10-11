package com.bsent.actbeat1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class GetAccountInfo extends Activity {

	private static final String TAG = "PlayHelloActivity";
	private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";
	public static final String EXTRA_ACCOUNTNAME = "extra_accountname";

	static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
	static final int REQUEST_CODE_RECOVER_FROM_AUTH_ERROR = 1001;
	static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1002;

	private String mEmail;
	Handler h;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0) {// id is empty
				Request_UserInfo();
			} else if (msg.what == 1) {
				
				NextPage(false);
			}
		}
	};
	MediaScannerConnection mediaScanner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		try {
			GetBasicMusicData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		h = new Handler();
		h.postDelayed(irun, 2000);
	}

	Runnable irun = new Runnable() {
		public void run() {
			Login_Thread login_thread = new Login_Thread();
			login_thread.start();
		}
	};
	Runnable trun = new Runnable() {
		public void run() {
		}
	};
	private void registerToMediaScanner(Uri uri) {
		Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, uri);
		sendBroadcast(intent);
	}
	
	
	
	private Handler getBasicMusicDataHandler = new Handler() {
		public void handleMessga(Message msg) {
			super.handleMessage(msg);
			
			Uri contentUri = thisuri;
			Intent mediaScanIntent = new Intent(
					Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			mediaScanIntent.setData(contentUri);
			sendBroadcast(mediaScanIntent);
		}
	};

	List<String> rootary = new ArrayList<String>();
	Uri thisuri;
	public void GetBasicMusicData() throws IOException {
		
		int rawid[] = { R.raw.bittutorial, R.raw.beethovenvirus, R.raw.comback,
				R.raw.oblivion };
		String music_name[] = { "/bittutorial.mp3", "/베토벤바이러스.mp3",
				"/comeback.mp3", "/OBLIVION.mp3" };
		String note_name[] = { "unknown_bittutorial.txt",
				"_unknown__베토벤바이러스.txt", "젝스키스_comback.txt",
				"_unknown__OBLIVION.txt" };
		int noteid[] = { R.raw.notebittutorial, R.raw.notevirus,
				R.raw.notecomback, R.raw.noteoblivion };

		for (int i = 0; i < rawid.length; i++) {
			InputStream in = getResources().openRawResource(rawid[i]);
			byte[] data = new byte[1024];
			String root = Environment.getExternalStorageDirectory().getPath();
			root += "/Music_Data";
			File myDir = new File(root);
			if (!myDir.exists())
				myDir.mkdir();
			root += music_name[i];
			FileOutputStream out = new FileOutputStream(root);
			while (in.read(data) != -1) {
				out.write(data);
			}
			rootary.add(root);
			File file = new File(root);
			MediaScannerConnection.scanFile(this,
					new String[] { file.toString() }, null,
					new MediaScannerConnection.OnScanCompletedListener() {
						public void onScanCompleted(String path, Uri uri) {
							Log.i("ExternalStorage", "Scanned " + path + ":");
							Log.i("ExternalStorage", "-> uri=" + uri);
							thisuri = uri;
							
							getBasicMusicDataHandler.sendEmptyMessage(0);
						}
						
					});
			in.close();
			out.close();
		}
		
		for (int i = 0; i < noteid.length; i++) {
			InputStream in = getResources().openRawResource(noteid[i]);
			String root = getExternalFilesDir(null).toString();
			root = root.substring(0, root.length() - 5);
			File myDir = new File(root + "/Note_Data");
			if (!myDir.exists())
				myDir.mkdir();
			File file = new File(myDir, note_name[i]);
			if (file.exists())
				file.delete();
			try {
				InputStreamReader stream = new InputStreamReader(in, "utf-8");
				BufferedReader buffer = new BufferedReader(stream);
				String read = "";
				StringBuilder sb = new StringBuilder("");
				while ((read = buffer.readLine()) != null) {
					sb.append(read);
				}
				Log.d("llog", sb.toString());
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(sb.toString().getBytes());
				fos.close();
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	Handler savemusichandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == 1) {
				UnityPlayerNativeActivity Call_Unity = new UnityPlayerNativeActivity();
				String root = Environment.getExternalStorageDirectory().getPath();
				root += "/Music_Data";
				root += "/bittutorial.mp3";
				SharedPreferences prefs = getSharedPreferences("game_data",
						MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("next_scene", "TutorialScene");
				editor.commit();
				Call_Unity.Set_Mp3Root(root);
				Call_Unity.Set_NoteFilename("unknown_bittutorial.txt");
				Call_Unity.Set_RhythmMode("normal");
				Intent intent = new Intent(GetAccountInfo.this, MainMenu.class);
				startActivity(intent);
			}
			super.handleMessage(msg);
		}

	};

	public class BasicMusicSaveThread extends Thread {
		Message msg = new Message();
		Handler handler;

		public BasicMusicSaveThread(Handler handler) {
			// TODO Auto-generated constructor stub
			this.handler = handler;
			msg.what = 1;
		}

		@Override
		public void run() {
			/*
			// TODO Auto-generated method stub
			try {
				GetBasicMusicData();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			handler.sendMessage(msg);
			super.run();
		}

	}

	public void NextPage(Boolean isFirst) {
		if (isFirst) {
			BasicMusicSaveThread thread = new BasicMusicSaveThread(
					savemusichandler);
			thread.start();
			// 처음이면 튜토리얼 모드를 가야함..

		} else {
			// 처음이 아니면 메인화며으로 가야함..
			Intent intent = new Intent(GetAccountInfo.this, MainMenu.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
			finish();
		}
	}

	protected void Request_UserInfo() {
		int statusCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (statusCode == ConnectionResult.SUCCESS) {
			getUsername();
		} else if (GooglePlayServicesUtil.isUserRecoverableError(statusCode)) {
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusCode,
					this, 0 /* request code not used */);
			dialog.show();
		} else {
			Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
			if (resultCode == RESULT_OK) {
				mEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
				getUsername();
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "You must pick an account",
						Toast.LENGTH_SHORT).show();
			}
		} else if ((requestCode == REQUEST_CODE_RECOVER_FROM_AUTH_ERROR || requestCode == REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR)
				&& resultCode == RESULT_OK) {
			handleAuthorizeResult(resultCode, data);
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void handleAuthorizeResult(int resultCode, Intent data) {
		if (data == null) {
			// show("Unknown error, click the button again");
			return;
		}
		if (resultCode == RESULT_OK) {
			Log.i("log", "Retrying");
			new GetNameInForeground(GetAccountInfo.this, mEmail, SCOPE)
					.execute();
			return;
		}
		if (resultCode == RESULT_CANCELED) {
			// show("User rejected authorization.");
			return;
		}
		// show("Unknown error, click the button again");
	}

	/**
	 * Attempt to get the user name. If the email address isn't known yet, then
	 * call pickUserAccount() method so the user can pick an account.
	 */
	private void getUsername() {
		if (mEmail == null) {
			pickUserAccount();
		} else {
			if (isDeviceOnline()) {
				new GetNameInForeground(GetAccountInfo.this, mEmail, SCOPE)
						.execute();
			} else {
				Toast.makeText(this, "No network connection available",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * Starts an activity in Google Play Services so the user can pick an
	 * account
	 */
	private void pickUserAccount() {
		String[] accountTypes = new String[] { "com.google" };
		Intent intent = AccountPicker.newChooseAccountIntent(null, null,
				accountTypes, false, null, null, null, null);
		startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
	}

	/** Checks whether the device currently has a network connection */
	private boolean isDeviceOnline() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}

	/**
	 * This method is a hook for background threads and async tasks that need to
	 * update the UI. It does this by launching a runnable under the UI thread.
	 */
	/*
	 * public void show(final String message) { runOnUiThread(new Runnable() {
	 * 
	 * @Override public void run() { mOut.setText(message); } }); }
	 */
	/**
	 * This method is a hook for background threads and async tasks that need to
	 * provide the user a response UI when an exception occurs.
	 */
	public void handleException(final Exception e) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (e instanceof GooglePlayServicesAvailabilityException) {
					// The Google Play services APK is old, disabled, or not
					// present.
					// Show a dialog created by Google Play services that allows
					// the user to update the APK
					int statusCode = ((GooglePlayServicesAvailabilityException) e)
							.getConnectionStatusCode();
					Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
							statusCode, GetAccountInfo.this,
							REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
					dialog.show();
				} else if (e instanceof UserRecoverableAuthException) {
					// Unable to authenticate, such as when the user has not yet
					// granted
					// the app access to the account, but the user can fix this.
					// Forward the user to an activity in Google Play services.
					Intent intent = ((UserRecoverableAuthException) e)
							.getIntent();
					startActivityForResult(intent,
							REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
				}
			}
		});
	}

	public class Login_Thread extends Thread {
		String id = "0";
		UserInfomation Userinfo = new UserInfomation();
		Message msg = new Message();

		@Override
		public void run() {
			Log.i("log", "start");
			// TODO Auto-generated method stub
			try {
				id = Userinfo.onGetId(GetAccountInfo.this);

				Log.i("log", "thread id : " + id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (id == null) {
				msg.what = 0;
			} else {
				msg.what = 1;
			}
			Log.d("log", "thread id : " + id);
			handler.sendMessage(msg);
			super.run();
		}

	}
}
