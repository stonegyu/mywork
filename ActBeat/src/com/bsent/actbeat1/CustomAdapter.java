package com.bsent.actbeat1;


import java.io.IOException;
import java.util.ArrayList;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CustomAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList data;
	private static LayoutInflater inflater = null;
	public Resources res;
	ListModel tempValues = null;
	ListModel tempValues2 = null;
	ImageView music_playbtn;
	LinearLayout BestRhythmer_Layout;
	int i = 0;
	Context context;
	
	public CustomAdapter(Activity a, ArrayList d, Resources resLocal, Context context) {
		activity = a;
		data = d;
		this.context = context;
		res = resLocal;
		
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Rank_info = ((TextView) ((Activity)context).findViewById(R.id.BestRhythmer));
		music_playbtn = ((ImageView)((Activity)context).findViewById(R.id.music_playbtn));
		BestRhythmer_Layout = (LinearLayout)((LinearLayout)((Activity)context).findViewById(R.id.BestRhythmer_Layout));
	}
	
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(data.size()<=0){
			return 1;
		}
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public static class ViewHolder{
		public LinearLayout left_LL;
		public LinearLayout right_LL;
		public TextView tv_title;
		public TextView tv_singer;
		public ImageView iv_album;
		public TextView tv_title2;
		public TextView tv_singer2;
		public ImageView iv_album2;
		
		public String mp3_root;
		public String mp3_root2;
		public int TouchCountL=0;
		public int TouchCountR=0;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;
		ViewHolder holder;
		if(convertView == null){
			vi = inflater.inflate(R.layout.mp3item, null);
			
			holder = new ViewHolder();
			holder.left_LL = (LinearLayout)vi.findViewById(R.id.left_music);
			
			holder.tv_singer = (TextView) vi.findViewById(R.id.singer1);
			holder.tv_title = (TextView) vi.findViewById(R.id.title1);
			holder.iv_album = (ImageView) vi.findViewById(R.id.album_img1);
			
			holder.right_LL = (LinearLayout)vi.findViewById(R.id.right_music);
			
			holder.tv_singer2 = (TextView) vi.findViewById(R.id.singer2);
			holder.tv_title2 = (TextView) vi.findViewById(R.id.title2);
			holder.iv_album2 = (ImageView) vi.findViewById(R.id.album_img2);
			
			vi.setTag(holder);
		}else{
			holder = (ViewHolder)vi.getTag();
		}
		
		if(data.size()<=0){
			holder.tv_singer.setText("죄송합니다");
			holder.tv_title.setText("현재 보유하고 계신 MP3곡은 아직 노트를 지원하지 않습니다");
		}else{
			tempValues = null;
			tempValues = (ListModel) data.get(position);
							
			// set holder elements
			holder.tv_singer.setText(tempValues.getMp3_singer());
			holder.tv_title.setText(tempValues.getMp3_title());
			holder.iv_album.setImageURI(tempValues.getUrl());
			holder.mp3_root = tempValues.getMp3_root();
			
			if(tempValues.getMp3_root2().length() > 0){
				holder.right_LL.setVisibility(View.VISIBLE);
				holder.mp3_root2 = tempValues.getMp3_root2();
				holder.tv_singer2.setText(tempValues.getMp3_singer2());
				holder.tv_title2.setText(tempValues.getMp3_title2());
				holder.iv_album2.setImageURI(tempValues.getUrl2());
			}else{
				holder.right_LL.setVisibility(View.INVISIBLE);
			}
			//이미지뷰 어떻게넣을것인지 찾아봐야함
			
			vi.setOnClickListener(new OnItemClickListener(position, holder));
			vi.postInvalidate();
		}
		return vi;
	}
	String filename="";
	String root = "";
	DBInterface db;
	
	private TextView Rank_info;
	private ViewHolder mBefore_Holder=null;
	public MediaPlayer mp = new MediaPlayer();
	Message DialogDismissMessage;
	private class OnItemClickListener implements OnClickListener{
		private int mPosition;
		private ViewHolder mHolder;
		
		
		private Request_Mp3List Mp3List;
		Dialog dialog = new Dialog(context);
		Dialog LodingDialog = new Dialog(context);
		OnItemClickListener(int position, ViewHolder holder){
			
			mPosition = position;
			mHolder = holder;
			mHolder.right_LL.setOnClickListener(this);
			mHolder.left_LL.setOnClickListener(this);
			Mp3List = new Request_Mp3List();
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.gameplaymode_dialog);
			DialogDismissMessage = Message.obtain(handler);
			DialogDismissMessage.what = 3;
			dialog.setDismissMessage(DialogDismissMessage);
			
			LodingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			LodingDialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(Color.TRANSPARENT));
			LodingDialog.setContentView(R.layout.loading);
			LodingDialog.setCancelable(false);
			
		
			Button Hard_Btn = (Button)dialog.findViewById(R.id.hard_btn);
			Button Normal_Btn = (Button)dialog.findViewById(R.id.normal_btn);
			Button Easy_Btn = (Button)dialog.findViewById(R.id.easy_btn);
			Hard_Btn.setOnClickListener(GamePlay_ModeListener );
			Normal_Btn.setOnClickListener(GamePlay_ModeListener );
			Easy_Btn.setOnClickListener(GamePlay_ModeListener );
			
			music_playbtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(mp.isPlaying()){
						UnityPlayerNativeActivity Call_Unity = new UnityPlayerNativeActivity();
						Call_Unity.Set_Mp3Root(root);
						Call_Unity.Set_NoteFilename(filename);
						
						mBefore_Holder=mHolder;
						mHolder.TouchCountL=0;
						mp.reset();
						dialog.show();
					}
					else
						Toast.makeText(context, "곡을 선택해 주세요", 0).show();
				}
			});
			
			
		}
		View.OnClickListener GamePlay_ModeListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//BestRhythmer_Layout.setVisibility(View.GONE);
				
				UnityPlayerNativeActivity Call_Unity = new UnityPlayerNativeActivity();
				
				// TODO Auto-generated method stub
				if(v.getId() == R.id.hard_btn){
					Call_Unity.Set_RhythmMode("hard");
					dialog.dismiss();
				}else if(v.getId() == R.id.normal_btn){
					Call_Unity.Set_RhythmMode("normal");
					dialog.dismiss();
				}else if(v.getId() == R.id.easy_btn){
					Call_Unity.Set_RhythmMode("easy");
					dialog.dismiss();
				}
				Intent intent = new Intent (activity,UnityPlayerNativeActivity.class);
				activity.startActivity(intent);
			}
		};
		
		Handler handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				LodingDialog.dismiss();
				if(msg.what==5){
					Log.d("llog","msgwhat : "+5);
					String[][]  BestRhythmJson = db.getMusicInfo();
					Log.d("llog",BestRhythmJson[0][0]+"  죄송합니다");
					if(BestRhythmJson[0][0].equalsIgnoreCase("nodata") || BestRhythmJson[0][0].equalsIgnoreCase("true")){
						slide_down(BestRhythmer_Layout);
						Rank_info.setText("아직 랭커에 등록된 유저가 없습니다");
					}
					else{
						slide_down(BestRhythmer_Layout);
						Rank_info.setText(BestRhythmJson[0][0]+" / "+BestRhythmJson[0][1]);
					}
						
					
				}else if(msg.what==4){
					Log.d("llog","msgwhat : "+4);
					String[][]  BestRhythmJson = db.getMusicInfo();
					if(BestRhythmJson[0][0].equalsIgnoreCase("nodata") || BestRhythmJson[0][0].equalsIgnoreCase("true")){
						slide_down(BestRhythmer_Layout);
						Rank_info.setText("아직 랭커에 등록된 유저가 없습니다");
					}
					else{
						slide_down(BestRhythmer_Layout);
						Rank_info.setText(BestRhythmJson[0][0]+" / "+BestRhythmJson[0][1]);
					}
						
				}else if(msg.what==3){
					//BestRhythmer_Layout.setVisibility(View.GONE);
				}else if(msg.what<0){
					Toast.makeText(context, "인터넷 연결이 원활하지 않습니다.", 0).show();
				}
				super.handleMessage(msg);
			}
			
		};
		
		public void slide_down(View v){
			 v.setVisibility(View.VISIBLE);
			  Animation a = AnimationUtils.loadAnimation(context, R.anim.slide_down);
			  if(a != null){
			     a.reset();
			     if(v != null){
			      v.clearAnimation();
			      v.startAnimation(a);
			     }
			  }
			}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(Mp3List.GetComposeMode()){
				if(v.getId() == mHolder.left_LL.getId()){
					UnityPlayerNativeActivity Call_Unity = new UnityPlayerNativeActivity();
					Call_Unity.Set_Mp3Root(mHolder.mp3_root);
					
					filename = mHolder.tv_singer.getText()+"_"+mHolder.tv_title.getText()+".txt";
					Call_Unity.Set_NoteFilename(filename);
					
					Intent intent = new Intent (activity,UnityPlayerNativeActivity.class);
					activity.startActivity(intent);
					
					
					Log.d("log",""+mHolder.mp3_root);
				}else if(v.getId() == mHolder.right_LL.getId()){
					if(mHolder.tv_singer2.getText().length()>0){
						UnityPlayerNativeActivity Call_Unity = new UnityPlayerNativeActivity();
						Call_Unity.Set_Mp3Root(mHolder.mp3_root2);
						
						filename = mHolder.tv_singer2.getText()+"_"+mHolder.tv_title2.getText()+".txt";
						Call_Unity.Set_NoteFilename(filename);
						
						Intent intent = new Intent (activity,UnityPlayerNativeActivity.class);
						activity.startActivity(intent);
					}
				}
			}else{
				if(v.getId() == mHolder.left_LL.getId()){
					if(mHolder.TouchCountL == 0){
						if(mBefore_Holder!=null){
							mBefore_Holder.TouchCountL=0;
							mBefore_Holder.TouchCountR=0;
						}
						mBefore_Holder=mHolder;
						mHolder.TouchCountL++;
						mHolder.TouchCountR=0;
						UnityPlayerNativeActivity Call_Unity = new UnityPlayerNativeActivity();
						Call_Unity.Set_Mp3Root(mHolder.mp3_root);
						
						filename = mHolder.tv_singer.getText()+"_"+mHolder.tv_title.getText()+".txt";
						Call_Unity.Set_NoteFilename(filename);
						root = mHolder.mp3_root;
						Log.d("llog",root);
						db = new DBInterface("Load_BestRhythmer.php","music_filename='"+filename+"'", handler, 4);
						db.start();
						LodingDialog.show(); 
						try {
							if(mp.isPlaying()){
								mp.reset();
							}
							mp.setDataSource(mHolder.mp3_root);
							mp.prepare();
							mp.start();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if(mHolder.TouchCountL == 1){
						if(mBefore_Holder.tv_title.equals(mHolder.tv_title)){
							UnityPlayerNativeActivity Call_Unity = new UnityPlayerNativeActivity();
							Call_Unity.Set_Mp3Root(mHolder.mp3_root);
							
							filename = mHolder.tv_singer.getText()+"_"+mHolder.tv_title.getText()+".txt";
							Call_Unity.Set_NoteFilename(filename);
							
							mBefore_Holder=mHolder;
							mHolder.TouchCountL=0;
							mp.reset();
							dialog.show();
						}else{
							mBefore_Holder.TouchCountL=0;
							mBefore_Holder.TouchCountR=0;
						}
						
					}
				}else if(v.getId() == mHolder.right_LL.getId()){
					if(mHolder.tv_singer2.getText().length()>0){
						if(mHolder.TouchCountR == 0){
							if(mBefore_Holder!=null){
								mBefore_Holder.TouchCountL=0;
								mBefore_Holder.TouchCountR=0;
							}
							mBefore_Holder=mHolder;
							mHolder.TouchCountR++;
							mHolder.TouchCountL=0;
							
							filename = mHolder.tv_singer2.getText()+"_"+mHolder.tv_title2.getText()+".txt";
							root=mHolder.mp3_root2;
							Log.d("llog",root);
							db = new DBInterface("Load_BestRhythmer.php","music_filename='"+filename+"'", handler, 5);
							db.start();
							LodingDialog.show(); 
							try {
								if(mp.isPlaying()){
									mp.reset();
								}
								mp.setDataSource(mHolder.mp3_root2);
								mp.prepare();
								mp.start();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else if(mHolder.TouchCountR == 1){
							if(mBefore_Holder.tv_title2.equals(mHolder.tv_title2)){
								UnityPlayerNativeActivity Call_Unity = new UnityPlayerNativeActivity();
								Call_Unity.Set_Mp3Root(mHolder.mp3_root2);
								
								filename = mHolder.tv_singer2.getText()+"_"+mHolder.tv_title2.getText()+".txt";
								Call_Unity.Set_NoteFilename(filename);
								
								mBefore_Holder=mHolder;
								mHolder.TouchCountR=0;
								mp.reset();
								dialog.show();
							}else{
								mBefore_Holder.TouchCountL=0;
								mBefore_Holder.TouchCountR=0;
							}
							
						}
					}
					
				}
			}
		}
	}
	
}
