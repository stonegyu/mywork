package com.bsent.actbeat1;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class DBInterface extends Thread {
	private String whatPhp;
	Handler handler;
	private String qry;
	private int msgWhat;
	private String url="http://stonegyu.cafe24.com/ActBit/";
	Message msg = new Message();
	private String qry_result="";
	public DBInterface(String what, String qry, Handler handler, int msgWhat) {
		whatPhp = what;
		this.qry = qry;
		this.handler = handler;
		this.msgWhat = msgWhat;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		qry_result="";
		String result = "";
		DefaultHttpClient client = new DefaultHttpClient();
		ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
		post.add(new BasicNameValuePair("where",qry));
		Log.d("llog","qry : "+qry);
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 5000);
		HttpConnectionParams.setSoTimeout(params, 5000);
		
		HttpPost httpPost = new HttpPost("http://stonegyu.cafe24.com/ActBit/"+whatPhp);
		try{
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(post, "UTF-8");
			httpPost.setEntity(entity);
			HttpResponse response = client.execute(httpPost);
			BufferedReader bufreader = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent(),"UTF-8"));
		
			String line;
			while((line = bufreader.readLine()) != null)
				result +=line;
			Log.d("llog","result : "+result);
		}catch (ClientProtocolException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		try{
			JSONArray arr = new JSONArray(result);
			msg.what = msgWhat;
			msg.obj = arr;
		}catch(JSONException e){
			e.printStackTrace();
			if(result != null){
				msg.what = msgWhat;
			}else{
				msg.what = -2;
			}
		}
		qry_result=result;
		handler.sendMessage(msg);
		super.run();
	}
	String getQry_Result(){
		return qry_result;
	}
	
	String[][] getJsonArrayMsg(){
		try{
			JSONArray jArr = new JSONArray(qry_result);
			
			//받아온 Json 분석
			String[] jsonName = {"music_filename","music_notedata"};
			
			
			String[][] parseredData = new String[jArr.length()][jsonName.length];
			for(int i=0; i<jArr.length(); i++){
				JSONObject json = jArr.getJSONObject(i);
				if(json != null){
					for(int j=0; j<jsonName.length; j++){
						parseredData[i][j] = json.getString(jsonName[j]);
						Log.d("log","JSON "+i+" : "+parseredData[i][j]);
						
					}
				}
			}
			return parseredData;
		}catch(JSONException e){
			Log.d("log","JSON is NULL");
			e.printStackTrace();
			String[][] nullstr = {{"null","null"}};
			return nullstr;
		}
		
	}
	
	String[][] getMusicInfo(){
		try{
			JSONArray jArr = new JSONArray(qry_result);
			
			//받아온 Json 분석
			String[] jsonName = {"BestRhythmer","Score"};
			
			
			String[][] parseredData = new String[jArr.length()][jsonName.length];
			for(int i=0; i<jArr.length(); i++){
				JSONObject json = jArr.getJSONObject(i);
				if(json != null){
					for(int j=0; j<jsonName.length; j++){
						parseredData[i][j] = json.getString(jsonName[j]);
						Log.d("log","JSON "+i+" : "+parseredData[i][j]);
						
					}
				}
			}
			return parseredData;
		}catch(JSONException e){
			Log.d("log","JSON is NULL");
			e.printStackTrace();
			String[][] nullstr = {{"null","null"}};
			return nullstr;
		}
		
	}

}
