package com.bsent.actbeat1;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class UserInfomation {
	private final String filename = "userid.txt";
	private static String inputId ="";

	void Userinfo() {
	}

	public String getInputId() {
		return inputId;
	}

	// external 저장공간에 읽기/쓰기가 가능한가?
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	// external 저장공간에서 최소한 일기리도 가능한가?
	public boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}
	Message msg = new Message();
	void Save_RhythmData(String Note_Name, String Note_Data, Context context, Handler handler, int msg) {
		
		this.msg.what = msg;
		String root = context.getExternalFilesDir(null).toString();
		root = root.substring(0, root.length()-5);
		File myDir = new File(root+"/Note_Data");
		myDir.mkdir();
		File file = new File(myDir, Note_Name);
		if(file.exists())file.delete();
		try{
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(Note_Data.getBytes());
			fos.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		handler.sendMessage(this.msg);
	}
	
	class TxtFilter implements FilenameFilter{

		@Override
		public boolean accept(File arg0, String arg1) {
			// TODO Auto-generated method stub
			return (filename.endsWith(".txt"));
		}
		
	}
	
	List<Rhythm_Data> Read_RhythmData(Context context){
		List<Rhythm_Data> data = new ArrayList<Rhythm_Data>();
		String ext = Environment.getExternalStorageState();
		String root = null;
		if(ext.equals(Environment.MEDIA_MOUNTED)){
			root = context.getExternalFilesDir(null).toString();
			root = root.substring(0, root.length()-5);
			root+="Note_Data";
		}else{
			root = Environment.MEDIA_UNMOUNTED;
			return null;
		}
		File files = new File(root);
		files.mkdir();
		if(files.listFiles(new TxtFilter()).length>0){
			for(File file : files.listFiles(new TxtFilter())){
				String str, str1="";
				try {
					FileInputStream fis = new FileInputStream(root+"/"+file.getName());
					BufferedReader bufferReader = new BufferedReader(new InputStreamReader(fis));
					while((str = bufferReader.readLine())!=null){
						str1+=str;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				data.add(new Rhythm_Data(file.getName(), str1));
			}
		}
		return data;
	}

	void onSaveId(String id, Context context) {
		try {
			Log.d("log", "save id "+id);
			FileOutputStream fos = context.openFileOutput(filename,
					Context.MODE_WORLD_READABLE);
			fos.write(id.getBytes());
			fos.close();
			Log.d("log", "end");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	String onGetId(Context context) {
		String id = null;
		Log.d("log",""+context.getExternalFilesDir(null));
		try {
			FileInputStream fis = context.openFileInput(filename);
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			id = new String(buffer);
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.d("log", "end " + id);
		
		inputId = id;
		return id;
	}
}
