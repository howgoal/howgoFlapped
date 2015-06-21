package com.example.groupproject;


import android.R.integer;
import android.content.Context;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;



public class cardImageButton extends ImageButton    {
	private int imageID;
	private int imageBackID;
	private int pairID;
	private boolean isCovered = true;
	private boolean isFindPair = false;
	public cardImageButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		getBackground().setAlpha(00);
	}
	public void setImageID(int id,int backImageID) {
		imageID = id;
		imageBackID = backImageID;
		//imageID = id; 
	}
	public void setPairID(int id) {
		pairID = id;
	}
	public int getPairID() {
		return pairID;
	}
	public void setSelected() {
		isCovered = false;
		setClickable(false);
		drawImage();
	}
	public void setUnSelected() {
		isCovered = true;
		setClickable(true);
		drawImage();
	}
	public boolean getIsCovered () {
		return isCovered;
	}
	public boolean getIsFindPair() {
		return isFindPair;
	}
	public void setIsFindPairTure() {
		isFindPair = true;
	}
	public void drawImage() {
		if(isCovered == true) {

			setImageResource(R.drawable.background_card);
		}
		else {
			if(isFindPair == true ){
				setImageResource(imageBackID);
			}
			else {
				setImageResource(imageID);
			}
			
		}
		
	}
	public int getImageID() {
		return imageID;
	}
	
	
	

}
