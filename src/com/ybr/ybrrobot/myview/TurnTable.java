package com.ybr.ybrrobot.myview;
import com.ybr.ybrrobot.R;
import com.ybr.ybrrobot.data.RobotData;
import com.ybr.ybrrobot.fun.CommonFun;

import android.content.Context;  
import android.graphics.Bitmap;  
import android.graphics.BitmapFactory;  
import android.graphics.Canvas;  
import android.graphics.Color;  
import android.graphics.Matrix;
import android.graphics.Paint;  
import android.util.AttributeSet;
import android.util.Log;  
import android.view.MotionEvent;  
import android.view.View;  
import android.widget.Toast;

/** 
 * 圆盘式的view 
 * @author chroya 
 * 
 */  
public class TurnTable extends View {  
	float scale;
	Context context;
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//可在此设置大小
		
		Bitmap bit=BitmapFactory.decodeResource(getResources(), R.drawable.bkground);
		
		setMeasuredDimension((int)((float)RobotData.phoneHeight*0.451),(int)(RobotData.phoneHeight*0.451));
		//setMeasuredDimension((int)((float)RobotData.phoneWidth),(int)(RobotData.phoneWidth));
		scale=(float)(RobotData.phoneHeight)*(float)0.44/bit.getWidth();
		//scale=(float)(RobotData.phoneWidth)/bit.getWidth();
	}
	private Paint mPaint = new Paint();    
	//stone列表  
	private BigStone[] mStones; 
	private BigStone[] smallStones; 
	private BigStone centerStone;
	//数目  
	private static final int STONE_COUNT = 8;  
	private static final int STONE_COUNT_SMALL = 4;  

	//圆心坐标  
	private int mPointX=0, mPointY=0;  
	//半径  
	private int mRadius = 0; //大圆半径。
	private int smallRadius=0;//小圆半径。
	//每两个点间隔的角度  
	private int mDegreeDelta;  
	private int smallDegreeDelta;  


	public TurnTable(Context context, AttributeSet attrs){//int px, int py, int radius) {  
		super(context);  
		this.context=context;
		mPaint.setColor(Color.BLACK);  
		mPaint.setStrokeWidth(2);  
		//setBackgroundResource(R.drawable.bkground);  
		
	}  

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		if(mRadius==0){
			mPointX = getWidth()/2;  
			mPointY = getHeight()/2;  
			mRadius = getHeight()/2*3/4;  
			smallRadius=getHeight()/2*29/80;

			setupStones();  
			computeCoordinates();  
		}


	}

	/** 
	 * 初始化每个点 
	 */  
	private void setupStones() {  
		mStones = new BigStone[STONE_COUNT];  
		BigStone stone;  
		int angle = 270;  
		mDegreeDelta = 360/STONE_COUNT;  
		for(int index=0; index<STONE_COUNT; index++) {  
			stone = new BigStone();  
			stone.angle = angle;  
			stone.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.menu1+index);
			stone._bitmap = BitmapFactory.decodeResource(getResources(), R.drawable._menu1+index);
			stone.isPress=false;
			angle += mDegreeDelta;  

			mStones[index] = stone;  
		}  
		centerStone=new BigStone();
		centerStone.x=mPointX;
		centerStone.y=mPointY;
		centerStone.bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.center);
		centerStone._bitmap = BitmapFactory.decodeResource(getResources(), R.drawable._center);
		centerStone.isPress=false;
		
		
		
		
		smallStones=new BigStone[STONE_COUNT_SMALL];
		angle=270;
		smallDegreeDelta=360/STONE_COUNT_SMALL;
		for(int index=0; index<STONE_COUNT_SMALL; index++) {  
			stone = new BigStone();  
			stone.angle = angle;  
			stone.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.small1+index); 
			stone._bitmap = BitmapFactory.decodeResource(getResources(), R.drawable._small1+index); 
			angle += smallDegreeDelta;  

			smallStones[index] = stone;  
		}  
	}  

	/** 
	 * 重新计算每个点的角度 
	 */  
	/*   private void resetStonesAngle(float x, float y) {  
		 int angle = computeCurrentAngle(x, y);  
		 Log.d("RoundSpinView", "angle:"+angle);  
		 for(int index=0; index<STONE_COUNT; index++) {             
		 mStones[index].angle = angle;         
		 angle += mDegreeDelta;  
		 }  
		 }  */

	/** 
	 * 计算每个点的坐标 
	 */  
	private void computeCoordinates() {  
		BigStone stone;  
		//for(int index=0; index<STONE_COUNT; index++) {  
		//	stone = mStones[index];  
		//	stone.x = mPointX+ (float)(mRadius * Math.cos(stone.angle*Math.PI/180));  
		//	stone.y = mPointY+ (float)(mRadius * Math.sin(stone.angle*Math.PI/180));  
		//	System.out.println("坐标"+index+"——"+stone.x+"——"+stone.y);
		//} 
		
		stone = mStones[0];  
		stone.x = mPointX+ (float)(mRadius * Math.cos(stone.angle*Math.PI/180));  
		stone.y = mPointY+ (float)(mRadius * Math.sin(stone.angle*Math.PI/180)); 
		stone = mStones[1];  
		stone.x = mPointX+ (float)((mRadius+(float)mStones[1].bitmap.getWidth()*0.06) * Math.cos(stone.angle*Math.PI/180));  
		stone.y = mPointY+ (float)((mRadius+(float)mStones[1].bitmap.getWidth()*0.06) * Math.sin(stone.angle*Math.PI/180)); 
		stone = mStones[2];  
		stone.x = mPointX+ (float)(mRadius * Math.cos(stone.angle*Math.PI/180));  
		stone.y = mPointY+ (float)(mRadius * Math.sin(stone.angle*Math.PI/180)); 
		stone = mStones[3];  
		stone.x = mPointX+ (float)((mRadius+(float)mStones[3].bitmap.getWidth()*0.06) * Math.cos(stone.angle*Math.PI/180));  
		stone.y = mPointY+ (float)((mRadius+(float)mStones[3].bitmap.getWidth()*0.06) * Math.sin(stone.angle*Math.PI/180)); 
		stone = mStones[4];  
		stone.x = mPointX+ (float)(mRadius * Math.cos(stone.angle*Math.PI/180));  
		stone.y = mPointY+ (float)(mRadius * Math.sin(stone.angle*Math.PI/180)); 
		stone = mStones[5];  
		stone.x = mPointX+ (float)((mRadius+(float)mStones[5].bitmap.getWidth()*0.06) * Math.cos(stone.angle*Math.PI/180));  
		stone.y = mPointY+ (float)((mRadius+(float)mStones[5].bitmap.getWidth()*0.06) * Math.sin(stone.angle*Math.PI/180)); 
		stone = mStones[6];  
		stone.x = mPointX+ (float)(mRadius * Math.cos(stone.angle*Math.PI/180));  
		stone.y = mPointY+ (float)(mRadius * Math.sin(stone.angle*Math.PI/180)); 
		stone = mStones[7];  
		stone.x = mPointX+ (float)((mRadius+(float)mStones[7].bitmap.getWidth()*0.06) * Math.cos(stone.angle*Math.PI/180));  
		stone.y = mPointY+ (float)((mRadius+(float)mStones[7].bitmap.getWidth()*0.06) * Math.sin(stone.angle*Math.PI/180)); 
		
		for(int index=0; index<STONE_COUNT_SMALL; index++) {  
			stone = smallStones[index];  
			stone.x = mPointX+ (float)(smallRadius * Math.cos(stone.angle*Math.PI/180));  
			stone.y = mPointY+ (float)(smallRadius * Math.sin(stone.angle*Math.PI/180));  
		}  
	}  

	/** 
	 * 计算第一个点的角度 
	 * @param x 
	 * @param y 
	 * @return 
	 */  
	/*   private int computeCurrentAngle(float x, float y) {       
		 float distance = (float)Math.sqrt(((x-mPointX)*(x-mPointX) + (y-mPointY)*(y-mPointY)));  
		 int degree = (int)(Math.acos((x-mPointX)/distance)*180/Math.PI);  
		 if(y < mPointY) {  
		 degree = -degree;  
		 }  

		 Log.d("RoundSpinView", "x:"+x+",y:"+y+",degree:"+degree);  
		 return degree;  
	}  
	*/
	@Override  
	public boolean dispatchTouchEvent(MotionEvent event) { 
		super.dispatchTouchEvent(event);
		float x,y;
		x=event.getX();
		y=event.getY();
		//得到点击坐标后，根据其进行不同的处理。
		BigStone stone;

		int action = event.getAction(); 

		switch (action) { 

			case MotionEvent.ACTION_DOWN: 
				//按键按下。
	            //在此检测是哪个按键按下了。
				
				for(int index=0;index<STONE_COUNT;index++){
					stone = mStones[index];
					int width;
					if(stone.bitmap.getWidth()<=stone.bitmap.getHeight()){
						width=stone.bitmap.getWidth();
					}
					else{
						width=stone.bitmap.getHeight();
					}
					if(((x-stone.x)*(x-stone.x)+(y-stone.y)*(y-stone.y))<=(scale*width/2)*(scale*width/2)){
						stone.isPress=true;
						
						switch (index) {
						case 0:
							
							//头部循环左右摇摆
							if (RobotData.isSocketCreated==true) {
								byte[] bytes={(byte)0XEF,0X55,(byte)0XAA,(byte)0XFE,0X07,0X11,0X07,0X00,(byte)0XFF,(byte)0XFF,0X29,(byte)0XE2};
								
								//int k = CommonFun.getCRC(bytes);// obj1的可能值110100
								//System.out.println("头部-->"+k);
								RobotData.ct.connThread.write(bytes);
							} else {
								Toast.makeText(this.getContext(), "请与蓝牙设备进行配对！", 0).show();
							}
							break;

						case 1:
							
							//左臂循环抬起放下
							if (RobotData.isSocketCreated==true) {
								byte[] bytes={(byte)0XEF,0X55,(byte)0XAA,(byte)0XFE,0X07,0X11,0X20,0X00,(byte)0XFF,(byte)0XFF,0X4A,(byte)0X84};
								//int k = CommonFun.getCRC(bytes);// obj1的可能值110100
								//System.out.println("左臂循环抬起放下-->"+k);
								RobotData.ct.connThread.write(bytes);
							} else {
								Toast.makeText(this.getContext(), "请与蓝牙设备进行配对！", 0).show();
							}
							break;
						case 2:
							//左手循环摆动
							if (RobotData.isSocketCreated==true) {
								//byte[] bytes={'O','N','A'};
								//RobotData.ct.connThread.write(bytes);
								byte[] bytes={(byte)0XEF,0X55,(byte)0XAA,(byte)0XFE,0X07,0X11,0X22,0X00,(byte)0XFF,(byte)0XFF,0X22,(byte)0X69};
								int k = CommonFun.getCRC(bytes);// obj1的可能值110100
								System.out.println("左手循环抬起放下-->"+k);
								RobotData.ct.connThread.write(bytes);
							} else {
								Toast.makeText(this.getContext(), "请与蓝牙设备进行配对！", 0).show();
							}
							break;
						case 3:
							//腰循环左右摆动
							if (RobotData.isSocketCreated==true) {
								byte[] bytes={(byte) 0XEF,0X55,(byte) 0XAA,(byte) 0XFE,0X07,0X11,0X08,0X00,(byte) 0XFF,(byte) 0XFF,(byte) 0XC7,0X36};
								RobotData.ct.connThread.write(bytes);
							} else {
								Toast.makeText(this.getContext(), "请与蓝牙设备进行配对！", 0).show();
							}
							break;
						case 4:
							//停止当前部位的运动，该部位返回初始位置
							if (RobotData.isSocketCreated==true) {
								byte[] bytes={(byte) 0XEF,0X55,(byte) 0XAA,(byte) 0XFE,0X07,0X30,0X02,0X02,0X02,(byte) 0XFF,0X74,(byte) 0XF7};
								RobotData.ct.connThread.write(bytes);
							} else {
								Toast.makeText(this.getContext(), "请与蓝牙设备进行配对！", 0).show();
							}
							break;
						case 5:
							//双脚并起向前滑行
							
						
							
							if (RobotData.isSocketCreated==true) {
								if(RobotData.typeStyle==false){
									byte[] bytes={(byte) 0XEF,0X55,(byte) 0XAA,(byte) 0XFE,0X07,0X11,0X03,0X00,(byte) 0XFF,(byte) 0XFF,(byte) 0XD8,0X28};
									RobotData.ct.connThread.write(bytes);
									RobotData.typeStyle=true;
								}else{
									byte[] bytes={(byte) 0xEF,0x55, (byte) 0xAA, (byte) 0xFE, 0x07, 0x11, 0x04, 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0xF5, 0x79};
									RobotData.ct.connThread.write(bytes);
									RobotData.typeStyle=false;
								}								
								
							} else {
								Toast.makeText(this.getContext(), "请与蓝牙设备进行配对！", 0).show();
							}
							break;
						case 6:
							//右手循环摆动
							if (RobotData.isSocketCreated==true) {
								//byte[] bytes={'O','N','A'};
								//RobotData.ct.connThread.write(bytes);
								byte[] bytes={(byte)0XEF,0X55,(byte)0XAA,(byte)0XFE,0X07,0X11,0X23,0X00,(byte)0XFF,(byte)0XFF,(byte) 0X96,(byte)0X1F};
								
								//int k = CommonFun.getCRC(bytes);// obj1的可能值110100
								//System.out.println("右手循环抬起放下-->"+k);
								RobotData.ct.connThread.write(bytes);
							} else {
								Toast.makeText(this.getContext(), "请与蓝牙设备进行配对！", 0).show();
							}
							break;
						case 7:
							//右臂循环抬起放下
							if (RobotData.isSocketCreated==true) {
								//byte[] bytes={'O','N','A'};
								//RobotData.ct.connThread.write(bytes);
								byte[] bytes={(byte)0XEF,0X55,(byte)0XAA,(byte)0XFE,0X07,0X11,0X21,0X00,(byte)0XFF,(byte)0XFF,(byte) 0XFE,(byte)0XF2};
								//int k = CommonFun.getCRC(bytes);// obj1的可能值110100
								//System.out.println("右臂循环抬起放下-->"+k);
								
								RobotData.ct.connThread.write(bytes);
							} else {
								Toast.makeText(this.getContext(), "请与蓝牙设备进行配对！", 0).show();
							}
							break;
						
	
						}
					}
				}
				for(int index=0;index<STONE_COUNT_SMALL;index++){
					stone = smallStones[index];
					if(((x-stone.x)*(x-stone.x)+(y-stone.y)*(y-stone.y))<=(scale*stone.bitmap.getWidth()/2)*(scale*stone.bitmap.getWidth()/2)){
						stone.isPress=true;
						switch (index) {
						case 0:
							//一直向前走
							if (RobotData.isSocketCreated==true) {
								byte[] bytes={(byte) 0xEF,0x55,(byte) 0xAA,(byte) 0xFE,0x07,0x11,0x01,0x00,(byte) 0xFF,(byte) 0xFF,(byte) 0xB0,(byte) 0xC5 };
								RobotData.ct.connThread.write(bytes);
							} else {
								Toast.makeText(this.getContext(), "请与蓝牙设备进行配对！", 0).show();
							}
							
							
							break;

						case 1:
							//一直向右转
							if (RobotData.isSocketCreated==true) {
								byte[] bytes={(byte) 0xEF,0x55,(byte) 0xAA,(byte) 0xFE,0x07,0x11,0x06,0x00,(byte) 0xFF,(byte) 0xFF,(byte) 0x9D,(byte) 0x94};
//										};
								RobotData.ct.connThread.write(bytes);
							} else {
								Toast.makeText(this.getContext(), "请与蓝牙设备进行配对！", 0).show();
							}
							break;
							
						case 2:
							//一直向后走
							if (RobotData.isSocketCreated==true) {
								byte[] bytes={(byte) 0xEF,0x55,(byte) 0xAA,(byte) 0xFE,0x07,0x11,0x02,0x00,(byte) 0xFF,(byte) 0xFF,0x6C,0x5E };
								RobotData.ct.connThread.write(bytes);
							} else {
								Toast.makeText(this.getContext(), "请与蓝牙设备进行配对！", 0).show();
							}
							break;
							
						case 3:
							//一直向左转
							if (RobotData.isSocketCreated==true) {
								byte[] bytes={(byte) 0xEF,0x55,(byte) 0xAA,(byte) 0xFE,0x07,0x11,0x05,0x00,(byte) 0xFF,(byte) 0xFF,0x41,0x0F};
								RobotData.ct.connThread.write(bytes);
							} else {
								Toast.makeText(this.getContext(), "请与蓝牙设备进行配对！", 0).show();
							}				
							break;
						}
					}
				}
				//判断是否在暂停。			
				if(((x-centerStone.x)*(x-centerStone.x)+(y-centerStone.y)*(y-centerStone.y))<=(scale*centerStone.bitmap.getWidth()/2)*(scale*centerStone.bitmap.getWidth()/2)){
					centerStone.isPress=true;
					if (RobotData.isSocketCreated==true) {
						//暂停
						byte[] bytes={(byte) 0XEF,0X55,(byte) 0XAA,(byte) 0XFE,0X07,0X30,0X01,0X01,0X01,(byte) 0XFF,(byte) 0XAB,(byte) 0X60};
						RobotData.ct.connThread.write(bytes);
					} else {
						Toast.makeText(this.getContext(), "请与蓝牙设备进行配对！", 0).show();
					}
				}	
				break; 

			case MotionEvent.ACTION_MOVE: 
				
				break; 

			case MotionEvent.ACTION_UP: 
				for(int index=0; index<STONE_COUNT; index++) {  
					stone = mStones[index];  
					stone.isPress=false;
				}
				for(int index=0; index<STONE_COUNT_SMALL; index++) {  
					stone = smallStones[index];  
					stone.isPress=false;
				}
				centerStone.isPress=false;
				break; 

			case MotionEvent.ACTION_CANCEL: 



				break; 
			

		} 
		invalidate();
		//return super.dispatchTouchEvent(event); 
		// resetStonesAngle(x, y);  
		// computeCoordinates();  
		// invalidate();  
		 return true;  
	}  

	@Override  
	public void onDraw(Canvas canvas) {  
		
		if(centerStone.isPress){
			drawInCenter(canvas, centerStone._bitmap, centerStone.x, centerStone.y);  
		}else{
			drawInCenter(canvas, centerStone.bitmap, centerStone.x, centerStone.y); 
		}
		for(int index=0; index<STONE_COUNT; index++) {  
			if(!mStones[index].isVisible) continue;  
			if(mStones[index].isPress){
				drawInCenter(canvas, mStones[index]._bitmap, mStones[index].x, mStones[index].y);  
			}else{
				drawInCenter(canvas, mStones[index].bitmap, mStones[index].x, mStones[index].y); 
			}

		} 

		for(int index=0; index<STONE_COUNT_SMALL; index++) {  
			if(!smallStones[index].isVisible) continue;  
			if(smallStones[index].isPress){
				drawInCenter(canvas, smallStones[index]._bitmap, smallStones[index].x, smallStones[index].y); 
			}else{
				drawInCenter(canvas, smallStones[index].bitmap, smallStones[index].x, smallStones[index].y); 
			}
		} 
	}  

	/** 
	 * 把中心点放到中心处 
	 * @param canvas 
	 * @param bitmap 
	 * @param left 
	 * @param top 
	 */  
	void drawInCenter(Canvas canvas, Bitmap bitmap, float left, float top) {   
		Bitmap dstbmp;
		
		
		Matrix matrix=new Matrix();  
		matrix.postScale(scale,scale);//获取缩放比例  
	//	System.out.println(scale);
		
		dstbmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);//按缩放比例生成适应屏幕的新的bitmap；  
		
		canvas.drawBitmap(dstbmp, left-scale*bitmap.getWidth()/2, top-scale*bitmap.getHeight()/2, null);  
	}     

	class BigStone {  

		//图片  
		Bitmap bitmap;  
		Bitmap _bitmap;

		boolean isPress;
		//角度  
		int angle;  

		//x坐标  
		float x;  

		//y坐标  
		float y;  
		
		//宽度
	//	float width;  
	
		//y坐标  
	//	float heigth;

		//是否可见  
		boolean isVisible = true;  
	}  
	}  
