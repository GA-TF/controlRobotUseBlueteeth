package com.ybr.ybrrobot.myfragment;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import com.ybr.ybrrobot.DialogActivity;
import com.ybr.ybrrobot.MainActivity;
import com.ybr.ybrrobot.R;
import com.ybr.ybrrobot.data.RobotData;
import com.ybr.ybrrobot.fun.CommonFun;

import android.R.string;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class SpeakFragment extends Fragment implements OnClickListener, OnTouchListener{
	
	
	
	private EditText sendEdit;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
        View view = inflater.inflate(R.layout.speak_fragment, null);    

        //�ٴλ�ȡdance�б���ʾ������
        return view;  
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		sendEdit=(EditText) getView().findViewById(R.id.speakData);
		String dataTemp=sendEdit.getText().toString();
	
			// �����ť����edittext�е����ݷ��ͻ����ˣ������Ƿ�Ϊ���ж�

		if (!dataTemp.equals("")) {
			byte[] data = null;
			byte[] voiceMsgBuffer = null;
			try {
				data = dataTemp.getBytes("gbk");
				voiceMsgBuffer = CommonFun.sendch(data);
			} catch (Exception e) {
				// TODO: handle exception
			}
			/*
			 * ����һ���Ǵ������������������Ϣ���͸�������
			 */
			
			if (RobotData.isSocketCreated==true) {
				RobotData.ct.connThread.write(voiceMsgBuffer);
			} else {
				Toast.makeText(this.getActivity(), "���������豸������ԣ�", 0).show();
			}
		}
		else {
		Toast.makeText(this.getActivity(), "������������˵�Ļ�",Toast.LENGTH_SHORT).show();

		}

	}

	Button button;
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		 super.onStart();
		 button=(Button) getView().findViewById(R.id.imgbtn);
		 
		
		 Bitmap dstbmp;
		 Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.sendbefore);
		 Bitmap _bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.item1);
		 Matrix matrix=new Matrix();  
		 matrix.postScale((float)RobotData.myLinearLayoutWidth*RobotData.myScale/_bitmap.getWidth(),(float)RobotData.myLinearLayoutHeight*RobotData.myScale/_bitmap.getHeight());//��ȡ���ű���  
	
	 
		 dstbmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);//�����ű���������Ӧ��Ļ���µ�bitmap��  
		 button.setBackgroundDrawable(new BitmapDrawable(dstbmp));
		
		// button.setBackgroundResource(R.drawable.sendbefore);
		// button.setHeight((int)(((float)RobotData.phoneHeight/3)*51/284));
		// button.setWidth((int)((float)(RobotData.phoneWidth-140)*51/334));
		       
		        
		   //     button.setHeight(10);
		  //      button.setWidth(20);
		 
		        button.setOnClickListener(this);
		        button.setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		 if(v.getId() == R.id.imgbtn){  
             if(event.getAction() == MotionEvent.ACTION_UP){  
            	 Bitmap dstbmp;
        		 Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.sendbefore);

        		 Bitmap _bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.item1);
        		 Matrix matrix=new Matrix();  
        		 matrix.postScale((float)RobotData.myLinearLayoutWidth*RobotData.myScale/_bitmap.getWidth(),(float)RobotData.myLinearLayoutHeight*RobotData.myScale/_bitmap.getHeight());//��ȡ���ű���  

        	
        			
        		 dstbmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);//�����ű���������Ӧ��Ļ���µ�bitmap��  
        		 button.setBackgroundDrawable(new BitmapDrawable(dstbmp));
             }   
             if(event.getAction() == MotionEvent.ACTION_DOWN){  
            	Bitmap dstbmp;
         		Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.sendafter);

         		Bitmap _bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.item1);
         		Matrix matrix=new Matrix();  
         		matrix.postScale((float)RobotData.myLinearLayoutWidth*RobotData.myScale/_bitmap.getWidth(),(float)RobotData.myLinearLayoutHeight*RobotData.myScale/_bitmap.getHeight());//��ȡ���ű���  

         	
         			
         		dstbmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);//�����ű���������Ӧ��Ļ���µ�bitmap��  
         		button.setBackgroundDrawable(new BitmapDrawable(dstbmp));
         		  
             }  
         }  
		return false;
	}
	
	
}
