package helper;

import android.content.Context;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public abstract class helperImageButton extends ImageButton  {

	public helperImageButton(Context context,int _name)  {
		super(context);
		getBackground().setAlpha(00);
		
		// TODO Auto-generated constructor stub
	}
	public abstract int getName();
	public abstract void drawImage();
	public abstract void setImageID(int id);
	public abstract void setSelected();
	public abstract void setUnSelected();
	public abstract void setLocation(int x, int y);
	public abstract void setUpY(int upY);
	
	

}
