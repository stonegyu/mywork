package com.bsent.actbeat1;


import android.app.NativeActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.unity3d.player.UnityPlayer;

public class UnityPlayerNativeActivity extends NativeActivity
{
	protected UnityPlayer mUnityPlayer;		// don't change the name of this variable; referenced from native code
	public static String rhythm_mode="";
	public static String mp3_root="";
	public static String note_filename="";
	// UnityPlayer.init() should be called before attaching the view to a layout - it will load the native code.
	// UnityPlayer.quit() should be the last thing called - it will unload the native code.
	protected void onCreate (Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		getWindow().takeSurface(null);
		setTheme(android.R.style.Theme_NoTitleBar_Fullscreen);
		getWindow().setFormat(PixelFormat.RGB_565);

		mUnityPlayer = new UnityPlayer(this);
		if (mUnityPlayer.getSettings ().getBoolean ("hide_status_bar", true))
			getWindow ().setFlags (WindowManager.LayoutParams.FLAG_FULLSCREEN,
			                       WindowManager.LayoutParams.FLAG_FULLSCREEN);

		int glesMode = mUnityPlayer.getSettings().getInt("gles_mode", 1);
		boolean trueColor8888 = false;
		mUnityPlayer.init(glesMode, trueColor8888);
		
		
		View playerView = mUnityPlayer.getView();
		setContentView(playerView);
		playerView.requestFocus();
	}
	public void Set_RhythmMode(String mode){
		rhythm_mode=mode;
	}
	public void Set_Mp3Root(String root){
		mp3_root = root;
	}
	public void Set_NoteFilename(String filename){
		note_filename = filename;
	}
	public void AndroidFunction(final String message){
		//Call_Unity.AndroidFunction("Call_Mp3_Root", mHolder.mp3_root);
		if(message.equalsIgnoreCase("load_root")){
			Log.d("log","call Android");
			UnityPlayer.UnitySendMessage("DecibelCamera", "getmp3root", mp3_root);
			UnityPlayer.UnitySendMessage("Main Camera", "getmp3root", mp3_root);
			UnityPlayer.UnitySendMessage("DecibelCamera", "AndroidGet_NoteFileName", note_filename);
		}else if(message.equalsIgnoreCase("call_musicList")){
			Intent i  = new Intent(UnityPlayerNativeActivity.this, Request_Mp3List.class);
			startActivity(i);
			finish();
		}else if(message.equals("gamedata_load")){
			UnityPlayer.UnitySendMessage("Main Camera", "Receive_MP3FileRoot_Data", mp3_root);
			UnityPlayer.UnitySendMessage("Main Camera", "Receive_MusicFilename", note_filename);
			UnityPlayer.UnitySendMessage("Main Camera", "Receive_RhythmMode", rhythm_mode);
			
			SharedPreferences prefs = getSharedPreferences("game_data", MODE_PRIVATE);
			String user_id = prefs.getString("user_id", "EditToolScene");
			String fallingTime = prefs.getString("fallingTime", "2");
			String viberator = prefs.getString("viberator", "on");
			UnityPlayer.UnitySendMessage("Main Camera", "Receive_ViberatorMode", viberator);
			UnityPlayer.UnitySendMessage("Main Camera", "Receive_UserID", user_id);
			UnityPlayer.UnitySendMessage("Main Camera", "Receive_FallingTime", fallingTime);
			Log.d("llog", "note_filename : "+ note_filename +"  " +rhythm_mode+ "  "+user_id);
			Log.d("llog", "mp3_root  " +mp3_root);
		}else if(message.equals("sceneload")){
			SharedPreferences prefs = getSharedPreferences("game_data", MODE_PRIVATE);
			String next_scene = prefs.getString("next_scene", "EditToolScene");
			UnityPlayer.UnitySendMessage("Camera", "Load_Scene", next_scene);
			Log.d("llog","next_scene : "+next_scene);
		}
	}
	protected void onDestroy ()
	{
		mUnityPlayer.quit();
		super.onDestroy();
	}

	// onPause()/onResume() must be sent to UnityPlayer to enable pause and resource recreation on resume.
	protected void onPause()
	{
		super.onPause();
		mUnityPlayer.pause();
	}
	protected void onResume()
	{
		super.onResume();
		mUnityPlayer.resume();
	}
	protected void onUserLeaveHint() {
		// TODO Auto-generated method stub
		mUnityPlayer.pause();
		super.onUserLeaveHint();
	}
	
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		mUnityPlayer.configurationChanged(newConfig);
	}
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		mUnityPlayer.windowFocusChanged(hasFocus);
	}
	public boolean dispatchKeyEvent(KeyEvent event)
	{
		if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
			return mUnityPlayer.onKeyMultiple(event.getKeyCode(), event.getRepeatCount(), event);
		return super.dispatchKeyEvent(event);
	}
}
