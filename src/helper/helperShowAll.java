package helper;


import com.example.groupproject.R;
import com.example.groupproject.R.drawable;

import android.content.Context;



public class helperShowAll extends helperImageButton {
	private int imageID;
	private int selectedImageID;
	private boolean isSelected = false;
	private int nameID;
	public helperShowAll(Context context,int _name) {
		super(context,_name);
		nameID = _name;
		imageID = R.drawable.gaga;
		selectedImageID = R.drawable.gaga_gray;
		drawImage();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void drawImage() {
		// TODO Auto-generated method stub
		if(isSelected == true) {
			//setImageResource(selectedImageID);
			setImageDrawable(getResources().getDrawable(selectedImageID));
		}
		else { 
			//setImageResource(imageID);
			setImageDrawable(getResources().getDrawable(imageID));
		}
	}

	
	@Override
	public void setImageID(int id) {
		// TODO Auto-generated method stub
		imageID = id;
	}


	@Override
	public void setSelected() {
		// TODO Auto-generated method stub
		isSelected = true;
		setClickable(false);
	}

	@Override
	public void setUnSelected() {
		// TODO Auto-generated method stub
		isSelected = false;
		setClickable(true);
	}

	@Override
	public int getName() {
		// TODO Auto-generated method stub
		return nameID;
	}

	@Override
	public void setLocation(int x, int y) {
		// TODO Auto-generated method stub
		setX(x);
		setY(y);
	}

	@Override
	public void setUpY(int upY) {
		// TODO Auto-generated method stub
		setY(getY()+upY);
	}

}
