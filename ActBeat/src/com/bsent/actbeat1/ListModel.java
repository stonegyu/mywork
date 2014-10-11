package com.bsent.actbeat1;


import android.net.Uri;

public class ListModel {
	
	private String mp3_title = "";
	private String mp3_singer = "";
	private Uri mp3_image = null;
	private String mp3_title2 = "";
	private String mp3_singer2 = "";
	private Uri mp3_image2 = null;
	private String mp3_root = "";
	private String mp3_root2 = "";
	
	public void setMp3_root(String root){
		this.mp3_root = root;
	}
	public void setMp3_root2(String root){
		this.mp3_root2 = root;
	}
	public String getMp3_root(){
		return this.mp3_root;
	}
	public String getMp3_root2(){
		return this.mp3_root2;
	}
	
	public void setMp3_title(String title) {
		this.mp3_title = title;
	}

	public void setMp3_singer(String singer) {
		this.mp3_singer = singer;
	}

	public void setUrl(Uri image) {
		this.mp3_image = image;
	}

	public String getMp3_title() {
		return this.mp3_title;
	}

	public String getMp3_singer() {
		return this.mp3_singer;
	}

	public Uri getUrl() {
		return this.mp3_image;
	}
	

	public void setMp3_title2(String title) {
		this.mp3_title2 = title;
	}

	public void setMp3_singer2(String singer) {
		this.mp3_singer2 = singer;
	}

	public void setUrl2(Uri image) {
		this.mp3_image2 = image;
	}

	public String getMp3_title2() {
		return this.mp3_title2;
	}

	public String getMp3_singer2() {
		return this.mp3_singer2;
	}

	public Uri getUrl2() {
		return this.mp3_image2;
	}
}
