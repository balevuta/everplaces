package com.zulu.places.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.zulu.places.R;
import com.zulu.places.utils.GeneralUtils;

public class PlaceDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8985526471494253003L;
	
	private int id;
	private String address;
	private String name;
	private String phone;
	private double rating;
	private String url;
	private double latitude, longitude;
	private String icon;
	private int noOfReviews;
	private ArrayList<String> photoReference;
	private ArrayList<String> photoURL;
	private String website;
	private String email;
	private String category;
	private String arrPhotoDB;
	private List<ReviewDetail> lstReviews;
	private String reviewJson;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public int getNoOfReviews() {
		return noOfReviews;
	}
	public void setNoOfReviews(int noOfReviews) {
		this.noOfReviews = noOfReviews;
	}
	public ArrayList<String> getPhotoReference() {
		return photoReference;
	}
	public void setPhotoReference(ArrayList<String> photoReference) {
		this.photoReference = photoReference;
	}
	public ArrayList<String> getPhotoURL() {
		return photoURL;
	}
	public void setPhotoURL(ArrayList<String> photoURL) {
		this.photoURL = photoURL;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getArrPhotoDB() {
		return arrPhotoDB;
	}
	public void setArrPhotoDB(String arrPhotoDB) {
		this.arrPhotoDB = arrPhotoDB;
	}
	public List<ReviewDetail> getLstReviews() {
		return lstReviews;
	}
	public void setLstReviews(List<ReviewDetail> lstReviews) {
		this.lstReviews = lstReviews;
	}
	public String getReviewJson() {
		return reviewJson;
	}
	public void setReviewJson(String reviewJson) {
		this.reviewJson = reviewJson;
	}
	public static PlaceDetail jsonToDetailReferencia(JSONObject pontoReferencia, Context mContext) {
        try {
        	PlaceDetail result = new PlaceDetail();
            JSONObject geometry = (JSONObject) pontoReferencia.get("geometry");
            JSONObject location = (JSONObject) geometry.get("location");
            result.setLatitude((Double) location.get("lat"));
            result.setLongitude((Double) location.get("lng"));
            result.setIcon(pontoReferencia.getString("icon"));
            result.setName(pontoReferencia.getString("name"));
            if(!pontoReferencia.isNull("formatted_address"))
            	result.setAddress(pontoReferencia.getString("formatted_address"));
            if(!pontoReferencia.isNull("formatted_phone_number"))
            	result.setPhone(pontoReferencia.getString("formatted_phone_number"));
            if(!pontoReferencia.isNull("url"))
            	result.setUrl(pontoReferencia.getString("url"));
            if(!pontoReferencia.isNull("rating"))
            	result.setRating(pontoReferencia.getDouble("rating"));
            if(!pontoReferencia.isNull("website"))
            	result.setWebsite(pontoReferencia.getString("website"));
            if(!pontoReferencia.isNull("email"))
            	result.setEmail(pontoReferencia.getString("email"));
//            else 
//            	result.setEmail("");
            if(!pontoReferencia.isNull("reviews")) {
	            JSONArray reviewArr = pontoReferencia.getJSONArray("reviews");
	            List<ReviewDetail> lstReviews = new ArrayList<ReviewDetail>();
	            result.setNoOfReviews(reviewArr.length());
	            result.setReviewJson(reviewArr.toString());
	            if(reviewArr.length()>0) {
		            for(int k=0;k<reviewArr.length();k++) {
		            	JSONObject objReview = reviewArr.getJSONObject(k);
		            	ReviewDetail reviewModel = new ReviewDetail();
		            	if(!objReview.isNull("author_name")) {
		            		reviewModel.setAuthorName(objReview.getString("author_name"));
		            	}
		            	if(!objReview.isNull("author_url")) {
		            		reviewModel.setAuthorURL(objReview.getString("author_url"));
		            	}
		            	if(!objReview.isNull("text")) {
		            		reviewModel.setText(objReview.getString("text"));
		            	}
		            	if(!objReview.isNull("rating")) {
		            		reviewModel.setRating(objReview.getInt("rating"));
		            	}
		            	if(!objReview.isNull("time")) {
		            		reviewModel.setTime(objReview.getLong("time"));
		            	}
		            	lstReviews.add(reviewModel);
		            }
		            result.setLstReviews(lstReviews);
	            }
            } else {
            	result.setNoOfReviews(0);
            }
            if(!pontoReferencia.isNull("photos")) {
            	JSONArray photoArr = pontoReferencia.getJSONArray("photos");
            	if(photoArr.length()>0) {
            		ArrayList<String> lstPhotoReferences = new ArrayList<String>();
            		for(int i=0;i<photoArr.length();i++) {
            			JSONObject photoObj = photoArr.getJSONObject(i);
            			String reference = "";
            			if(!photoObj.isNull("photo_reference")) {
            				reference = photoObj.getString("photo_reference");
            			}
            			lstPhotoReferences.add(reference);
            		}
            		result.setPhotoReference(lstPhotoReferences);
            	}
            }
//            result.setVicinity(pontoReferencia.getString("vicinity"));
//            result.setId(pontoReferencia.getString("id"));
//            result.setReference(pontoReferencia.getString("reference"));
            return result;
        } catch (JSONException ex) {
            //Logger.getLogger(Place.class.getName()).log(Level.SEVERE, null, ex);
        	GeneralUtils.showShortToast(mContext, mContext.getResources().getString(R.string.msg_connect_fail));
        }
        return null;
    }
	
}
