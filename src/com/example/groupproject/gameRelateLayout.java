package com.example.groupproject;

import helper.helperSaveTeacher;
import helper.helperCheerUp;
import helper.helperImageButton;
import helper.helperShowAll;
import helper.helperShowTwoPair;
import helper.helperTimeStop;

import java.util.Timer;
import java.util.TimerTask;

import android.R.integer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class gameRelateLayout extends RelativeLayout implements OnClickListener{
	private TextView textViewTimeShow;
	private Context context;
	private cardImageButton imageButtonCard[];
	private cardImageButton imageButtonFirstSelectedCard = null;
	private cardImageButton imageButtonSecondSelectedCard = null;
	
	private helperImageButton helperShowAll;
	private helperImageButton helperShowTwoPair;
	private helperImageButton helperCheerUp;
	private helperImageButton helperTimeStop;
	private helperImageButton helperSaveTeacher;

	
	private SoundPool soundPool;
	private int soundID;
	private Handler uiHandler;
	private Timer timer;
	private TimerTask timerTask;
	
	private boolean firstCardSelected = false;

	private boolean isGameEnd = false;
	private boolean isNextLevel = false;
	private boolean isTimeStop = false;
	
	private int widthLayout ;
	private int heightLayout;
	
	private int cardNumber = 15;
	private int level = 1;
	private int widthCard = 120;
	private int widthPaddingImage = 5;
	private int heightCard = 140;
	private int heightPaddingImage = 5;
	private int widthCardNumber ;
	
	private int widthHelperImage = 90;
	private int widthPaddingHelperImage = -1;
	private int heightPaddingHelperImage = 30;
	private int helperButtonAddUP = -40;
	
	private int timeGameTotal = 0;
	private int totalClickCount = 1;
	private int pairCardFlapped ;
	
	private final int coverAll=1,showAll=2,timeStop=3,showTwoPair=4,saveTeacher=5,cheer=6,setTimeView=7,textColorChange=8;
	private final int nextLevel = 20;
	private int cardImageAll[] = { R.drawable.image01_red, R.drawable.image02_red,R.drawable.image03_red,R.drawable.image04_red,R.drawable.image05_red,R.drawable.image06_red,R.drawable.image07_red,R.drawable.teacher_red };
	private int cardImageAllBack[]= {R.drawable.image01_green, R.drawable.image02_green,R.drawable.image03_green,R.drawable.image04_green,R.drawable.image05_green,R.drawable.image06_green,R.drawable.image07_green,R.drawable.teacher_green };
	private int gameCardID[]; 
	
	public gameRelateLayout(Context _context,int _width,int _height) {
		super(_context);
		context = _context;
		widthLayout = _width;
		heightLayout = _height;
		Resources resources = context.getResources();
		Drawable btnDrawable = resources
				.getDrawable(R.drawable.background);
		this.setBackgroundDrawable(btnDrawable);
		gameInit();
		
		// TODO Auto-generated constructor stub
	}	
	public void runGameJudge() {
		if(pairCardFlapped == cardNumber/2) {
			if(level < 3) {
				isNextLevel = true;
				level+=1;
				isTimeStop = true;
				dialog_nextLevel_game();		
			}
			else {
				isNextLevel = false;
				dialog_end_game();
				timer.cancel();
			}
			
			Log.v("game","level"+level+" "+ String.valueOf(timeGameTotal));
			
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method 
		if(firstCardSelected == false) {
			/**
			 * 
			 * set button can't be click
			 * change button image
			 */
			
			imageButtonFirstSelectedCard = (cardImageButton) findViewById(v.getId());
			imageButtonFirstSelectedCard.setSelected();
			firstCardSelected = true;
		}
		else {
			imageButtonSecondSelectedCard = (cardImageButton)findViewById(v.getId());
			Log.v("clickFirst",String.valueOf(imageButtonSecondSelectedCard.getId()));
			imageButtonSecondSelectedCard.setSelected();
			if(imageButtonFirstSelectedCard.getPairID() == imageButtonSecondSelectedCard.getPairID()) {
				
				imageButtonFirstSelectedCard.setIsFindPairTure();
				imageButtonFirstSelectedCard.setSelected();
				imageButtonFirstSelectedCard.setOnClickListener(null);
				
				imageButtonSecondSelectedCard.setIsFindPairTure();
				imageButtonSecondSelectedCard.setSelected();
				imageButtonSecondSelectedCard.setOnClickListener(null);
				pairCardFlapped ++ ;
			}
			else {
				for (int i = 0; i < cardNumber; i++) {
					imageButtonCard[i].setClickable(false); 
				}
				setThreadControl(coverAll);
			}
			runGameJudge();
			firstCardSelected = false;
			
		}
		totalClickCount+=1;
	}
	public void gameInit() {
		soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 5);
		soundID = soundPool.load(context, R.raw.cheer, 1);
		
		initTimer();
		helperButtonInit();
		gameSetting();
		setHelperListener();
		setUiHandler();
		dialog_new_game();
		
	}
	public void gameSetting() {
		pairCardFlapped = 0;
		switch (level) {
		case 1:
			cardNumber = 6;
			gameCardID = new int[cardNumber/2];
			break;
		case 2:
			cardNumber = 10;
			gameCardID = new int[cardNumber/2];
			break;
		case 3:
			cardNumber = 15;
			gameCardID = new int[cardNumber/2+1];
			break;
		default:
			break;
		}
		
		for (int i = 0; i < gameCardID.length; i++) {
			gameCardID[i] = cardImageAll[i];
			Log.v("cardImageAll",String.valueOf(cardImageAll[i]));
		}
		if(level > 2) {
			gameCardID[ gameCardID.length-1] =  cardImageAll[cardImageAll.length-1];
		}
		
		widthCardNumber = widthLayout/widthCard;
		imageButtonInit();
		setPairImageButton();
		setHelperClickable();
		
	}
	public void gameDestroy() {
		imageButtonCard =null;
		imageButtonFirstSelectedCard = null;
		imageButtonSecondSelectedCard = null;
		gameCardID = null;
		isTimeStop = false;
		isNextLevel = false;
		
	}
	public void imageButtonInit() {
		
		imageButtonCard = new cardImageButton[cardNumber];
		for (int i = 0; i < cardNumber; i++) {
			imageButtonCard[i] = new cardImageButton(context);
			imageButtonCard[i].setId(i);		
			//
			imageButtonCard[i].setX((i%widthCardNumber)*widthCard+widthPaddingImage);
			imageButtonCard[i].setY((i/widthCardNumber)*heightCard+heightPaddingImage);
			imageButtonCard[i].setOnClickListener(this);
			this.addView(imageButtonCard[i]);
		}
		
		  
	}
	public void setPairImageButton() {
		int imageRandom[] = new int[cardNumber];
		
		imageRandom = generateRandomArray();

		for (int i = 0; i < cardNumber ; i++) {
			imageButtonCard[i].setPairID(imageRandom[i]);
			imageButtonCard[i].setImageID(gameCardID[imageRandom[i]],cardImageAllBack[imageRandom[i]]);
			imageButtonCard[i].drawImage();
		}
	}
	/**
	 * generate random array which value is cardNumber/2
	 * @return
	 */
	public int[] generateRandomArray() {
		int randomNumber[] = new int[cardNumber];
		int legalJudugeNumber[] = null;
		
		switch (level) {
		case 1:
		case 2:
			legalJudugeNumber = new int[cardNumber/2];
			for (int i = 0; i < legalJudugeNumber.length; i++) {
				legalJudugeNumber[i]=2;
			}
			break;
		case 3:
			legalJudugeNumber = new int[cardNumber/2+1];
			for (int i = 0; i < legalJudugeNumber.length; i++) {
				legalJudugeNumber[i]=2;
			}
			legalJudugeNumber[legalJudugeNumber.length-1] = 1;
			break;
		default:
			break;
		}
		
		int i = 0;
		while (i < randomNumber.length ) {
			int temp = (int) (Math.random() * legalJudugeNumber.length);
			if(legalJudugeNumber[temp] > 0) {
				randomNumber[i] = temp;
				legalJudugeNumber[temp]-=1;
				i+=1  ; 
			}
		}
	
		return randomNumber;
	}

	public void setHelperListener() {
		helperShowAll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < cardNumber; i++) {
					imageButtonCard[i].setClickable(false);	
					imageButtonCard[i].setSelected();	
				}

				helperShowAll.setUpY(helperButtonAddUP);
				helperShowAll.drawImage();
				helperShowAll.setSelected();
				setThreadControl(helperShowAll.getName());
			}
		});
		//Time stop
		helperTimeStop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isTimeStop = true;
				setThreadControl(helperTimeStop.getName());
				helperTimeStop.setUpY(helperButtonAddUP);
				helperTimeStop.drawImage();
				helperTimeStop.setSelected();
			}
		});
		
		helperShowTwoPair.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.v("test","test0");
				if(firstCardSelected == true ) {
					for (int i = 0; i < imageButtonCard.length; i++) {
						if(imageButtonFirstSelectedCard.getPairID() == imageButtonCard[i].getPairID() && imageButtonFirstSelectedCard.getId() != imageButtonCard[i].getId())  {
							
							imageButtonFirstSelectedCard.setIsFindPairTure();
							imageButtonFirstSelectedCard.setSelected();
							imageButtonFirstSelectedCard.setOnClickListener(null);
							imageButtonCard[i].setIsFindPairTure();
							imageButtonCard[i].setSelected();
							imageButtonCard[i].setOnClickListener(null);
							
							firstCardSelected = false;
							imageButtonFirstSelectedCard = null;
							pairCardFlapped +=1;
							break;
						}
					}
				}
				else {
					int temp = 0;
					for (int i = 0; i < imageButtonCard.length; i++) {
						if(imageButtonCard[i].getIsCovered() == true && imageButtonCard[i].getImageID() != R.drawable.teacher_red) {
							temp = i;
							Log.v("test1",String.valueOf(i));
							break;
						}
					} 
					for (int j = temp+1; j < imageButtonCard.length; j++) {
						if(imageButtonCard[temp].getPairID() == imageButtonCard[j].getPairID()) {
							imageButtonCard[j].setIsFindPairTure();
							imageButtonCard[j].setSelected();
							
							imageButtonCard[temp].setIsFindPairTure();
							imageButtonCard[temp].setSelected();
							
							firstCardSelected = false;
							pairCardFlapped +=1;
							Log.v("test",String.valueOf(j));
							break;
						}
					}
				}
				
				helperShowTwoPair.setSelected();
				helperShowTwoPair.drawImage();
				
			}
		});
		
		helperSaveTeacher.setOnClickListener(new  OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//helperSaveTeacher.setLocation(widthHelperImage*3-widthPaddingHelperImage, 4*heightCard);
				if (level >= 3) {
					helperSaveTeacher.setUpY(helperButtonAddUP);
					helperSaveTeacher.drawImage();
					timeGameTotal-= 10;
					for (int i = 0; i < imageButtonCard.length; i++) {
						if(imageButtonCard[i].getImageID() == R.drawable.teacher_red) {
							
							imageButtonCard[i].drawImage();
							imageButtonCard[i].setSelected();
							imageButtonCard[i].setIsFindPairTure();
							Toast.makeText(context,"拯救老師，獲得bouns -10 秒!! ",Toast.LENGTH_LONG).show();
							imageButtonCard[i].drawImage();
							
							break;
						}
					}
					
					
					setThreadControl(helperSaveTeacher.getName());
				}
				else {
					helperSaveTeacher.setSelected();
					helperSaveTeacher.drawImage();
				}
				runGameJudge();
			}
			
		});
		
		helperCheerUp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(level >= 3) {
					soundPool.play(soundID, 1.0F, 1.0F, 0, 0, 1.0F);
					helperCheerUp.setUpY(helperButtonAddUP);
					setThreadControl(cheer);
				}

			}
		});
		
			

			
	}
	public void setUiHandler() {
		uiHandler = new Handler() {
			 @Override
		        public void handleMessage(Message msg){
					switch (msg.what) {
					case coverAll:
						for (int i = 0; i < cardNumber; i++) {
							imageButtonCard[i].setClickable(true);		 
						}
						imageButtonFirstSelectedCard.setUnSelected();
						imageButtonSecondSelectedCard.setUnSelected();
						break;
					case showAll:
						for (int i = 0; i < cardNumber; i++) {
							if(imageButtonCard[i].getIsFindPair() == false) {
								imageButtonCard[i].setClickable(true);	
								imageButtonCard[i].setUnSelected();	
							}			
						}
						helperShowAll.setUpY(helperButtonAddUP*(-1));
						helperShowAll.setSelected();
						helperShowAll.drawImage();
						break;
					case timeStop:
						helperTimeStop.setUpY(helperButtonAddUP*(-1));
						helperTimeStop.setSelected();
						helperTimeStop.drawImage();
						
						break;
					case saveTeacher:
						helperSaveTeacher.setUpY(helperButtonAddUP*(-1));
						helperSaveTeacher.setSelected();
						helperSaveTeacher.drawImage();
				
						break;
					case cheer:
						helperCheerUp.setUpY(helperButtonAddUP*(-1));
						helperCheerUp.drawImage();
						Toast.makeText(context,"信信幫你加油哦<3  ",Toast.LENGTH_LONG).show();
						totalClickCount-=1;
						break;
					case nextLevel:
						for (int j = 0; j < imageButtonCard.length; j++) {
							removeView(imageButtonCard[j]);
						} 
						gameDestroy();
						gameSetting();
						isTimeStop = false;
						break;
					case setTimeView:
						
						textViewTimeShow.setText("Time:"+String.valueOf(timeGameTotal));
						break;
					case textColorChange:
						if(isTimeStop == true ) {
							textViewTimeShow.setBackgroundColor(Color.BLACK);
							textViewTimeShow.setTextColor(Color.RED);
						}
						else {
							textViewTimeShow.setBackgroundColor(0);
							textViewTimeShow.setTextColor(Color.WHITE);
						}
						
					default:
						break;
					}
				 
				 
				 
			 }
		};
	}
	
	public void setThreadControl(final int status) {
		new Thread(new Runnable() {
			Message msg = new Message();
			@Override
			public void run() {
				// TODO Auto-generated method stub
				switch (status) {
				case coverAll:
					try {
						Thread.sleep(700);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					msg.what = coverAll;
					uiHandler.sendMessage(msg);
					break;
				case showAll:
					try {
						Thread.sleep(800);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					msg.what = showAll;
					//Log.v("showAll", String.valueOf(coverAll));
					uiHandler.sendMessage(msg);
					break;
				case timeStop:
					try {
						msg.what = textColorChange;
						uiHandler.sendMessage(msg);
						msg = null;
						Thread.sleep(3500);
						msg = new Message();
						msg.what = timeStop;
						uiHandler.sendMessage(msg);
						msg = null;
						msg = new Message();
						msg.what = textColorChange;
						isTimeStop = false;
						uiHandler.sendMessage(msg);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case saveTeacher:
						msg.what = saveTeacher;
						uiHandler.sendMessage(msg);
					break;
				case cheer:
					try {
						Thread.sleep(300);
						msg.what = cheer;
						uiHandler.sendMessage(msg);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
			

				default:
					break;
				}
			}
		}).start();;
		
	}
	public void initTimer() {
		if(isNextLevel != true) {
			timeGameTotal = 0;
		}
		
		timer = new Timer();
		timerTask = new TimerTask() {
			@Override 
			public void run() {
				// TODO Auto-generated method stub
				if(isTimeStop == false ) {
					timeGameTotal+=1;
					Message msg = new Message();
					msg.what= setTimeView;
					uiHandler.sendMessage(msg);
				}
			}
		};
	}
	public void dialog_new_game(){  //  新遊戲提示
    	new AlertDialog.Builder(context)
     	.setTitle("新遊戲")
     	.setMessage("即將開始新遊戲!")
     	.setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener() {
     		public void onClick(DialogInterface dialog,int which){
     			timer.schedule(timerTask,0,1000);
     		} 
     	}).show();
     }
    public void dialog_end_game(){  //  結束遊戲提示
    	new AlertDialog.Builder(context)
     	.setTitle("遊戲結束")
     	.setMessage("花費秒數： "+timeGameTotal+"\n翻牌次數： "+totalClickCount+"\n總積分："+String.valueOf(timeGameTotal+totalClickCount*2))
     	.setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener(){public void onClick(DialogInterface dialog,int which){}})
     	.show();
     }
    public void dialog_nextLevel_game() {
    	new AlertDialog.Builder(context)
     	.setTitle("下一關")
     	.setMessage("目前秒數為： "+timeGameTotal+"\n目前翻牌次數："+totalClickCount)
     	.setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener(){
     		public void onClick(DialogInterface dialog,int which)
     		{
     			Message msg = new Message();
     			msg.what = nextLevel;
     			uiHandler.sendMessage(msg);
     			//stimer.schedule(timerTask,0,1000);
     		}}).show();
    }
    public void setHelperClickable() {
    	helperShowAll.setUnSelected();
    	helperShowAll.drawImage();
    	
    	helperShowTwoPair.setUnSelected();
    	helperShowTwoPair.drawImage();
    	
    	helperTimeStop.setUnSelected();
    	helperTimeStop.drawImage();
    	
    	helperSaveTeacher.setSelected();
    	helperSaveTeacher.drawImage();
    	
    	helperCheerUp.setSelected();
    	helperCheerUp.drawImage();
    	//space case
    	
    	if(level >= 3) {
    		helperSaveTeacher.setUnSelected();
    		helperSaveTeacher.drawImage();
    		
    		helperCheerUp.setUnSelected();
        	helperCheerUp.drawImage();
    	}
    	
    }
    
	
   	public void helperButtonInit() {
   		

   		
   		helperCheerUp = new helperCheerUp(context, cheer);
   		helperCheerUp.setLocation(widthHelperImage*0-widthPaddingHelperImage, 4*heightCard+heightPaddingHelperImage);
   		addView(helperCheerUp); 
   		
   		helperSaveTeacher = new helperSaveTeacher(context,saveTeacher);
   		helperSaveTeacher.setLocation(widthHelperImage*1-widthPaddingHelperImage, 4*heightCard+heightPaddingHelperImage);
   		addView(helperSaveTeacher); 
   		
   		helperShowAll = new helperShowAll(context,showAll);
   		helperShowAll.setLocation(widthHelperImage*2-widthPaddingHelperImage, 4*heightCard+heightPaddingHelperImage);
   		addView(helperShowAll); 
   		 
   		helperTimeStop = new helperTimeStop(context, timeStop);
   		helperTimeStop.setLocation(widthHelperImage*3-widthPaddingHelperImage, 4*heightCard+heightPaddingHelperImage);
   		addView(helperTimeStop); 
   		
   		helperShowTwoPair = new helperShowTwoPair(context, showTwoPair);
   		helperShowTwoPair.setLocation(widthHelperImage*4-widthPaddingHelperImage, 4*heightCard+heightPaddingHelperImage);
   		addView(helperShowTwoPair); 

   	}
   	public void setTextViewTimeInit(TextView textView) {
   		textViewTimeShow = textView;
   		
   	}
}
