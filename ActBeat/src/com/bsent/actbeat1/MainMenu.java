package com.bsent.actbeat1;


import java.io.IOException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

public class MainMenu extends Activity implements OnClickListener {
	ImageView Btn_GameStart, Btn_Option, Btn_Exit, Compose_Note;
	MediaPlayer mp;
	Intent intent;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		UnityPlayerNativeActivity Call_Unity = new UnityPlayerNativeActivity();
		if (!Call_Unity.note_filename.equals("unknown_bittutorial.txt")) {
			
			mp = new MediaPlayer().create(MainMenu.this, R.raw.mainmusic);
			if (!mp.isPlaying()) {
				
				try {
					mp.prepare();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mp.start();
			}else{
				mp.stop();
				mp.reset();
				try {
					mp.prepare();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mp.start();
			}
		}else{
			
			Intent intent = new Intent(MainMenu.this,
					UnityPlayerNativeActivity.class);
			startActivity(intent);
			
		}

		super.onResume();
	}

	Dialog optiondialog;
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		mp.stop();
		mp.reset();
		super.onPause();
	}

	@Override
	protected void onUserLeaveHint() {
		// TODO Auto-generated method stub
		mp.stop();
		mp.reset();
		super.onUserLeaveHint();
	}

	@Override
	public void onBackPressed() {
		mp.stop();
		mp.reset();
		finish();
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_mainmenu);
		
		Btn_GameStart = (ImageView) findViewById(R.id.GameStart);
		Btn_Option = (ImageView) findViewById(R.id.Option);
		Btn_Exit = (ImageView) findViewById(R.id.Exit);
		Compose_Note = (ImageView) findViewById(R.id.ComposeNote);

		Btn_GameStart.setOnClickListener(this);
		Btn_Option.setOnClickListener(this);
		Btn_Exit.setOnClickListener(this);
		Compose_Note.setOnClickListener(this);

		optiondialog = new Dialog(this);
		optiondialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		optiondialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		optiondialog.setContentView(R.layout.option);
		viberatorbtn = (ImageView) optiondialog.findViewById(R.id.Viberator);
		speed_fast = (ImageView) optiondialog
				.findViewById(R.id.option_fastSpeed);
		speed_normal = (ImageView) optiondialog
				.findViewById(R.id.option_NormalSpeed);
		speed_slow = (ImageView) optiondialog.findViewById(R.id.option_slow);
		SharedPreferences prefs = getSharedPreferences("game_data",
				MODE_PRIVATE);
		String viberator = prefs.getString("viberator", "on");
		if (viberator.equals("on")) {
			viberatorbtn.setImageResource(R.drawable.onbutton);
		} else if (viberator.equals("off")) {
			viberatorbtn.setImageResource(R.drawable.offbutton);
		}
		
		
	}

	ImageView viberatorbtn, speed_fast, speed_normal, speed_slow;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v.getId() == R.id.GameStart) {
			mp.stop();
			mp.reset();
			UserInfomation userinfo = new UserInfomation();
			String user_id = userinfo.getInputId();
			SharedPreferences prefs = getSharedPreferences("game_data",
					MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString("next_scene", "PlayRhythmGameScene");
			editor.putString("user_id", user_id);
			editor.commit();
			Request_Mp3List reqCompose = new Request_Mp3List();
			reqCompose.SetComposeMode(false);
			intent = new Intent(MainMenu.this, Request_Mp3List.class);
			startActivity(intent);
		} else if (v.getId() == R.id.ComposeNote) {
			mp.stop();
			mp.reset();
			SharedPreferences prefs = getSharedPreferences("game_data",
					MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString("next_scene", "EditToolScene");
			editor.commit();
			Request_Mp3List reqCompose = new Request_Mp3List();
			reqCompose.SetComposeMode(true);
			intent = new Intent(MainMenu.this, Request_Mp3List.class);
			startActivity(intent);
		} else if (v.getId() == R.id.Option) {
			optiondialog.show();

			viberatorbtn.setOnClickListener(optionListener);
			speed_fast.setOnClickListener(optionListener);
			speed_normal.setOnClickListener(optionListener);
			speed_slow.setOnClickListener(optionListener);
			// 옵션 뷰
		} else if (v.getId() == R.id.Exit) {
			onBackPressed();
		}
	}

	View.OnClickListener optionListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == R.id.Viberator) {
				SharedPreferences prefs = getSharedPreferences("game_data",
						MODE_PRIVATE);
				String viberator = prefs.getString("viberator", "on");
				if (viberator.equals("on")) {
					viberatorbtn.setImageResource(R.drawable.offbutton);
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString("viberator", "off");
					editor.commit();
					Toast.makeText(MainMenu.this, "진동모드가 OFF 되었습니다", 0).show();
				} else if (viberator.equals("off")) {
					viberatorbtn.setImageResource(R.drawable.onbutton);
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString("viberator", "on");
					editor.commit();
					Toast.makeText(MainMenu.this, "진동모드가 ON 되었습니다", 0).show();
				}
			} else if (v.getId() == R.id.option_fastSpeed) {
				SharedPreferences prefs = getSharedPreferences("game_data",
						MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("fallingTime", "1");
				editor.commit();
				Toast.makeText(MainMenu.this, "노트속도가 빠르게 설정되었습니다", 0).show();
			} else if (v.getId() == R.id.option_NormalSpeed) {
				SharedPreferences prefs = getSharedPreferences("game_data",
						MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("fallingTime", "1.5");
				editor.commit();
				Toast.makeText(MainMenu.this, "노트속도가 중간으로 설정되었습니다", 0).show();
			} else if (v.getId() == R.id.option_slow) {
				SharedPreferences prefs = getSharedPreferences("game_data",
						MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("fallingTime", "2");
				editor.commit();
				Toast.makeText(MainMenu.this, "노트속도가 느리게 설정되었습니다", 0).show();
			}
		}
	};

}
