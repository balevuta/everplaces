package com.zulu.places.model;

import java.io.Serializable;

public class ReviewDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String authorName;
	private String authorURL;
	private int rating;
	private String text;
	private long time;
	
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getAuthorURL() {
		return authorURL;
	}
	public void setAuthorURL(String authorURL) {
		this.authorURL = authorURL;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
}
