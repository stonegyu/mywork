package com.bsent.actbeat1;


import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class Request_Mp3List extends Activity {
	private ListView mp3_listview;
	CustomAdapter adapter;
	public ArrayList<ListModel> CustomListViewValuesArr = new ArrayList<ListModel>();
	public Request_Mp3List CustomListView = null;
	DBInterface db;
	UserInfomation Userinfo;
	String[][] NoteData;
	ArrayList<String> DownloadNote_Filename = new ArrayList<String>();
	private static boolean ComposeMode = false;

	public void SetComposeMode(boolean mode) {
		ComposeMode = mode;
	}
	public boolean GetComposeMode(){
		return ComposeMode;
	}

	public class Save_Rhythm extends Thread {
		Handler handler;
		Message msg = new Message();
		boolean isfull;
		public Save_Rhythm(Handler handler, int msgWhat, boolean isFull) {
			this.handler = handler;
			msg.what = msgWhat;
			isfull=isFull;
		}
		@Override
		public void run() {
			if(isfull){ // 서버에서 받은 데이터가 있을때 저장해줌
				Userinfo = new UserInfomation();
				if (NoteData.length > 0) {
					for (int i = 0; i < NoteData.length; i++) {
						DownloadNote_Filename.add(NoteData[i][0]);
						String root = getExternalFilesDir(null).toString();
						root = root.substring(0, root.length()-5);
						File myDir = new File(root+"/Note_Data");
						myDir.mkdir();
						File file = new File(myDir, NoteData[i][0]);
						if(file.exists())file.delete();
						try{
							FileOutputStream fos = new FileOutputStream(file);
							fos.write(NoteData[i][1].getBytes());
							fos.close();
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			}
			handler.sendMessage(msg);
			super.run();
		}

	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == 1) {
				NoteData = db.getJsonArrayMsg();
				Log.d("llog","qry_ary_number : "+qry_ary_number+"  "+qry_ary.size());
				Log.d("llog","length : "+NoteData[0][0]);
				 if(qry_ary_number < qry_ary.size() && NoteData[0][0].equals("null")){
					 Log.d("llog","not save");
					Save_Rhythm save = new Save_Rhythm(handler, 3, false);
					save.start();
				}else if(qry_ary_number < qry_ary.size() && !NoteData[0][0].equals("null")){
					Log.d("llog","save");
					Save_Rhythm save = new Save_Rhythm(handler, 3, true);
					save.start();
				}else if(qry_ary_number == qry_ary.size()){
					if(NoteData[0][0].equals("null")){
						Save_Rhythm save = new Save_Rhythm(handler, 2, false);
						save.start();
					}else{
						Save_Rhythm save = new Save_Rhythm(handler, 2, true);
						save.start();
					}
					
				}

			} else if(msg.what==3){
				
				db = new DBInterface("Load_MusicList.php", qry_ary.get(qry_ary_number), handler, 1);
				db.start();
				qry_ary_number++;
				
				
			}else if (msg.what == 2) {
				for(int i=0; i<DownloadNote_Filename.size(); i++){
					Log.d("llog","down : "+DownloadNote_Filename.get(i));
				}
				if(DownloadNote_Filename.size() != 0){ // 서버에서 받아온 데이터 반영해야함
					for(int i=0; i<arr_NotExist_Videos.size(); i++){
						String filename = arr_NotExist_Videos.get(i).album + "_"
								+ arr_NotExist_Videos.get(i).title + ".txt";
						
						for(int j=0; j<DownloadNote_Filename.size(); j++){
							if(filename.equals(DownloadNote_Filename.get(j))){
								arr_sort_Videos.add(arr_NotExist_Videos.get(i));
								arr_NotExist_Videos.remove(i);
								i--;
								break;
							}
						}
					}
				}
				
				SetALL_Search();
				Resources res = getResources();
				adapter = new CustomAdapter(CustomListView,
						CustomListViewValuesArr, res,Request_Mp3List.this);
				mp3_listview.setAdapter(adapter);
				dialog.dismiss();
			}else if(msg.what < 0){
				dialog.dismiss();
				Toast.makeText(Request_Mp3List.this, "인터넷이 불안정하여 서버에 저장된 리듬 데이터를 불러오지 못하였습니다", Toast.LENGTH_LONG).show();
			}
			super.handleMessage(msg);
		}

		private Save_Rhythm Save_Rhythm(Handler handler, int i) {
			// TODO Auto-generated method stub
			return null;
		}

	};
	
	
	@Override
	protected void onUserLeaveHint() {
		// TODO Auto-generated method stub
		if(adapter.mp.isPlaying()){
			adapter.mp.stop();
			adapter.mp.reset();
		}
		super.onUserLeaveHint();
	}

	ImageView Search_Btn;
	EditText Search_Bar;
	InputMethodManager imm;
	
	Dialog dialog;
	List<Rhythm_Data> rhythm_data;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		 getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.music_listview);
		findViewById(R.id.BestRhythmer_Layout).setVisibility(View.GONE);
		mp3_listview = (ListView) findViewById(R.id.listView1);
		dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dialog.setContentView(R.layout.loading);
		dialog.setCancelable(false);
		dialog.show(); 
		
		
		CustomListView = this;
		UserInfomation info = new UserInfomation();
		rhythm_data = info.Read_RhythmData(Request_Mp3List.this);
		
		SetMp3ListData();
		
		imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		Search_Btn = (ImageView) findViewById(R.id.search_btn);
		
		Search_Bar = (EditText) findViewById(R.id.search_bar);
		
		Search_Btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				imm.hideSoftInputFromWindow(Search_Bar.getWindowToken(), 0);
				Search_Bar.setSelected(false);
				CustomListViewValuesArr.clear(); // 리스트 초기화
				arr_SearchResult_Vidios.clear();
				String Search_Str = Search_Bar.getText().toString();
				if (Search_Str.length() > 0) {
					if (ComposeMode) {
						for (int i = 0; i < arr_NotExist_Videos.size(); i++) {
							if (arr_NotExist_Videos.get(i).album
									.contains(Search_Str)
									|| arr_NotExist_Videos.get(i).title
											.contains(Search_Str)) {
								arr_SearchResult_Vidios.add(arr_NotExist_Videos
										.get(i));

							}
						}

					} else {
						for (int i = 0; i < arr_sort_Videos.size(); i++) {
							if (arr_sort_Videos.get(i).album
									.contains(Search_Str)
									|| arr_sort_Videos.get(i).title
											.contains(Search_Str)) {
								arr_SearchResult_Vidios.add(arr_sort_Videos
										.get(i));

							}
						}
					}
					
					
					for(int i=0; i<arr_SearchResult_Vidios.size(); i++){
						final ListModel mp3_item_info = new ListModel();
						mp3_item_info.setMp3_title(arr_SearchResult_Vidios.get(i).title
								.toString());
						mp3_item_info.setMp3_singer(arr_SearchResult_Vidios.get(i).album
								.toString());
						Uri ArtworkUri = Uri
								.parse("content://media/external/audio/albumart");
						Uri uri = ContentUris.withAppendedId(ArtworkUri,
								Long.parseLong(arr_SearchResult_Vidios.get(i).id));
						mp3_item_info.setMp3_root(arr_SearchResult_Vidios.get(i).data);
						mp3_item_info.setUrl(uri);
						// 여기에 이미지 추가해보기
						if ((i + 1) < arr_SearchResult_Vidios.size()) {
							mp3_item_info
									.setMp3_title2(arr_SearchResult_Vidios.get(i + 1).title
											.toString());
							mp3_item_info
									.setMp3_singer2(arr_SearchResult_Vidios.get(i + 1).album
											.toString());
							Uri ArtworkUri2 = Uri
									.parse("content://media/external/audio/albumart");
							Uri uri2 = ContentUris.withAppendedId(ArtworkUri2,
									Long.parseLong(arr_SearchResult_Vidios.get(i + 1).id));
							mp3_item_info
									.setMp3_root2(arr_SearchResult_Vidios.get(i + 1).data);
							mp3_item_info.setUrl2(uri2);
						}
						CustomListViewValuesArr.add(mp3_item_info);
					}
				}else {
					SetALL_Search();
				}

				Resources res = getResources();
				adapter = new CustomAdapter(CustomListView,
						CustomListViewValuesArr, res, Request_Mp3List.this);
				mp3_listview.setAdapter(adapter);
			}
		});
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		adapter.mp.stop();
		adapter.mp.reset();
		super.onPause();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		adapter.mp.stop();
		adapter.mp.reset();
		super.onBackPressed();
	}

	SongsManager manager = new SongsManager();
	ArrayList<Mp3ListItem> arr_Videos;
	ArrayList<Mp3ListItem> arr_sort_Videos = new ArrayList<Mp3ListItem>();
	ArrayList<Mp3ListItem> arr_NotExist_Videos = new ArrayList<Mp3ListItem>();
	ArrayList<Mp3ListItem> arr_SearchResult_Vidios = new ArrayList<Mp3ListItem>();

	void InternalDB_Assign_List() {
		ArrayList<Mp3ListItem> arr_Videos = manager
				.getMp3Datas(Request_Mp3List.this);
		for (int i = 0; i < arr_Videos.size(); i++) {
			String filename = arr_Videos.get(i).album + "_"
					+ arr_Videos.get(i).title + ".txt";
			boolean isExistNote = false;
			for (int j = 0; j < rhythm_data.size(); j++) {
				if (filename.equals(rhythm_data.get(j).file_name)) {
					isExistNote = true;
					break;
				}
			}
			
			if (isExistNote) {
				arr_sort_Videos.add(arr_Videos.get(i));
			} else {
				arr_NotExist_Videos.add(arr_Videos.get(i));
				//최종적으로 서버에 없는걸 넣어줘야할듯
			}
		}
	}
	ArrayList<String>qry_ary = new ArrayList<String>();
	int qry_ary_number = 0;
	void ExternalDB_Assign_List(){
		String qry = "";
		if(arr_NotExist_Videos.size() == 0) {
			db = new DBInterface("Load_MusicList.php", qry, handler, 1);
			db.start();
			return;
		}
		for(int i=0; i<arr_NotExist_Videos.size(); i++){
			String filename = arr_NotExist_Videos.get(i).album + "_"
					+ arr_NotExist_Videos.get(i).title + ".txt";
			if(i != 0 && i%20==0){
				qry+="'"+filename+"'";
				qry_ary.add(qry);
				qry="";
			}else if(i == arr_NotExist_Videos.size()-1){
				qry+="'"+filename+"'";
				qry_ary.add(qry);
				qry="";
				
			}else{
				qry+="'"+filename+"',";
			}
		}
		
		db = new DBInterface("Load_MusicList.php", qry_ary.get(qry_ary_number), handler, 1);
		db.start();
		qry_ary_number++;
		
	}
	void SetALL_Search(){
		Log.d("llog","arr_sortsize  "+arr_sort_Videos.size());
		Log.d("llog","arr_notexist  "+arr_NotExist_Videos.size());
		CustomListViewValuesArr.clear();
		if (ComposeMode == false) {
			
			for (int i = 0; i < arr_sort_Videos.size(); i = i + 2) {
				final ListModel mp3_item_info = new ListModel();
				if (arr_sort_Videos.get(i).title.toString().length() > 0) {

					mp3_item_info.setMp3_title(arr_sort_Videos.get(i).title
							.toString());
					mp3_item_info.setMp3_singer(arr_sort_Videos.get(i).album
							.toString());
					Uri ArtworkUri = Uri
							.parse("content://media/external/audio/albumart");
					Uri uri = ContentUris.withAppendedId(ArtworkUri,
							Long.parseLong(arr_sort_Videos.get(i).id));
					mp3_item_info.setMp3_root(arr_sort_Videos.get(i).data);
					mp3_item_info.setUrl(uri);
					// 여기에 이미지 추가해보기
					if ((i + 1) < arr_sort_Videos.size()) {
						mp3_item_info
								.setMp3_title2(arr_sort_Videos.get(i + 1).title
										.toString());
						mp3_item_info
								.setMp3_singer2(arr_sort_Videos.get(i + 1).album
										.toString());
						Uri ArtworkUri2 = Uri
								.parse("content://media/external/audio/albumart");
						Uri uri2 = ContentUris.withAppendedId(ArtworkUri2,
								Long.parseLong(arr_sort_Videos.get(i + 1).id));
						mp3_item_info
								.setMp3_root2(arr_sort_Videos.get(i + 1).data);
						mp3_item_info.setUrl2(uri2);
					}
				}
				CustomListViewValuesArr.add(mp3_item_info);
			}
		} else {
			for (int i = 0; i < arr_NotExist_Videos.size(); i = i + 2) {
				final ListModel mp3_item_info = new ListModel();
				if (arr_NotExist_Videos.get(i).title.toString().length() > 0) {

					mp3_item_info.setMp3_title(arr_NotExist_Videos.get(i).title
							.toString());
					mp3_item_info
							.setMp3_singer(arr_NotExist_Videos.get(i).album
									.toString());
					Uri ArtworkUri = Uri
							.parse("content://media/external/audio/albumart");
					Uri uri = ContentUris.withAppendedId(ArtworkUri,
							Long.parseLong(arr_NotExist_Videos.get(i).id));
					mp3_item_info.setMp3_root(arr_NotExist_Videos.get(i).data);
					mp3_item_info.setUrl(uri);
					// 여기에 이미지 추가해보기
					if ((i + 1) < arr_NotExist_Videos.size()) {
						mp3_item_info.setMp3_title2(arr_NotExist_Videos
								.get(i + 1).title.toString());
						mp3_item_info.setMp3_singer2(arr_NotExist_Videos
								.get(i + 1).album.toString());
						Uri ArtworkUri2 = Uri
								.parse("content://media/external/audio/albumart");
						Uri uri2 = ContentUris.withAppendedId(ArtworkUri2, Long
								.parseLong(arr_NotExist_Videos.get(i + 1).id));
						mp3_item_info.setMp3_root2(arr_NotExist_Videos
								.get(i + 1).data);
						mp3_item_info.setUrl2(uri2);
					}
				}
				CustomListViewValuesArr.add(mp3_item_info);
			}
		}
	}
	void SetMp3ListData() {
		InternalDB_Assign_List();
		ExternalDB_Assign_List();
		
	}

	public class SongsManager {
		final String MEDIA_PATH = Environment.getExternalStorageDirectory()
				.getPath() + "/";
		private ArrayList<HashMap<String, String>> songList = new ArrayList<HashMap<String, String>>();
		private String mp3Pattern = ".mp3";

		public SongsManager() {

		}

		public ArrayList<Mp3ListItem> getMp3Datas(Context context) {
			ArrayList<Mp3ListItem> arr_Videos = new ArrayList<Mp3ListItem>();
			Cursor videocursor = null;
			String[] proj = { MediaStore.Video.VideoColumns.ARTIST,
					MediaStore.Audio.Media.ALBUM_ID,
					MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
					MediaStore.Audio.Media.DISPLAY_NAME,
					MediaStore.Audio.Media.SIZE,
					MediaStore.Video.Media.DATE_ADDED };

			videocursor = ((Activity) context).managedQuery(
					MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj, null,
					null, null);

			if (videocursor.moveToFirst()) {
				int n_album = videocursor
						.getColumnIndex(MediaStore.Video.VideoColumns.ARTIST);
				int n_id = videocursor
						.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
				int n_title = videocursor
						.getColumnIndex(MediaStore.Audio.Media.TITLE);
				int n_data = videocursor
						.getColumnIndex(MediaStore.Audio.Media.DATA);
				int n_displayname = videocursor
						.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME);
				int n_size = videocursor
						.getColumnIndex(MediaStore.Video.Media.SIZE);
				int n_regdttm = videocursor
						.getColumnIndex(MediaStore.Video.Media.DATE_ADDED);
				do {
					Mp3ListItem item;
					String mp3_album = videocursor.getString(n_album);
					String mp3_id = videocursor.getString(n_id);
					String mp3_title = videocursor.getString(n_title);
					String mp3_data = videocursor.getString(n_data);
					String mp3_displayname = videocursor
							.getString(n_displayname);
					String mp3_size = videocursor.getString(n_size);
					String mp3_regdttm = videocursor.getString(n_regdttm);
					item = new Mp3ListItem(mp3_album, mp3_id, mp3_title,
							mp3_data, mp3_displayname, mp3_size, mp3_regdttm);
					arr_Videos.add(item);
				} while (videocursor.moveToNext());
			}

			return arr_Videos;
		}

		public ArrayList<HashMap<String, String>> getPlayList() {
			Log.d("log", MEDIA_PATH);
			if (MEDIA_PATH != null) {
				File home = new File(MEDIA_PATH);
				File[] listFiles = home.listFiles();
				if (listFiles != null && listFiles.length > 0) {
					for (File file : listFiles) {
						Log.d("log", file.getAbsolutePath());
						if (file.isDirectory()) {
							scanDirectory(file);
						} else {
							addSongToList(file);
						}
					}
				}
			}
			return songList;
		}

		private void scanDirectory(File directory) {
			if (directory != null) {
				File[] listFiles = directory.listFiles();
				if (listFiles != null && listFiles.length > 0) {
					for (File file : listFiles) {
						if (file.isDirectory()) {
							scanDirectory(file);
						} else {
							addSongToList(file);
						}
					}
				}
			}
		}

		private void addSongToList(File song) {
			if (song.getName().endsWith(mp3Pattern)) {
				HashMap<String, String> songMap = new HashMap<String, String>();
				songMap.put(
						"songTitle",
						song.getName().substring(0,
								(song.getName().length() - 4)));
				songMap.put("songPath", song.getPath());
				songList.add(songMap);
			}
		}
	}

	public class RhythmData_Name {
		String DataName;
		String Split_Name[];
		int Singer_Name = 0;
		int Music_Name = 1;

		RhythmData_Name(String DataName) {
			this.DataName = DataName;
			DataName_Split();
		}

		public void DataName_Split() {
			Split_Name = DataName.split("_");
		}
	}

	public class Mp3ListItem {
		String album;
		String id;
		String title;
		String data;
		String displayname;
		String size;
		String regdttm;

		public Mp3ListItem(String album, String id, String title, String data,
				String displayname, String size, String regdttm) {
			this.album = album;
			this.id = id;
			this.title = title;
			this.data = data;
			this.displayname = displayname;
			this.size = size;
			this.regdttm = regdttm;
		}
	}
	
}
