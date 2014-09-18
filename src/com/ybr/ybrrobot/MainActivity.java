package com.ybr.ybrrobot;

import com.ybr.ybrrobot.myfragment.FragmentFactory;
import com.ybr.ybrrobot.myview.*;

import cn.waps.AppConnect;

import com.ybr.ybrrobot.data.RobotData;
import com.ybr.ybrrobot.fun.CommonFun;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	private static final int BLUETEETHID = 1;
	private static final int DIALOGID = 2;
	protected static final int GET_SOCKET_DATA = 0;
	private FragmentManager fragmentManager;  
	private RadioGroup radioGroup; 
	
	TurnTable myView;
	
	
	
	
	//��Ϣ�������
	
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==GET_SOCKET_DATA){
				//�����ڴ˻�ȡ�������յ������ݡ�
				//System.out.println(msg.obj);	
				/////////////////�ڴ˽��յ���Ϣ��Ȼ������ʾ��textview�С�
				//nowLength.append((String)(msg.obj));
			}
		}
		
	};
    @Override
    protected void onCreate(Bundle savedInstanceState) {	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // setContentView(new RoundSpinView(getApplicationContext(), 300, 300, 200));  
        
        RobotData.phoneWidth = getWindowManager().getDefaultDisplay().getWidth(); // ��Ļ�����أ��磺480px��  
        RobotData.phoneHeight = getWindowManager().getDefaultDisplay().getHeight(); // ��Ļ�ߣ����أ��磺800p��  
        RobotData.typeStyle=false;
        
        
       
        RobotData.isSocketCreated=false;
        
        RobotData.myScale = this.getResources().getDisplayMetrics().density; 
        /*************************
        *��ȡ�ֻ�����
        *
        **************************/
        
        LinearLayout linearLayout=(LinearLayout) findViewById(R.id.itempicset);  
        LayoutParams linearLayoutParams;        
        linearLayoutParams=(LayoutParams) linearLayout.getLayoutParams();
       // linearLayoutParams.width=100;
        linearLayoutParams.height=(int)((float)RobotData.phoneHeight*(float)0.354);  
        linearLayoutParams.width=(int)((float)linearLayoutParams.height*(float)1.18);
        
        RobotData.myLinearLayoutWidth=(int)((float)linearLayoutParams.height*(float)1.18);
        RobotData.myLinearLayoutHeight=(int)((float)RobotData.phoneHeight*(float)0.354);
        linearLayout.setLayoutParams(linearLayoutParams);
     //   linearLayout.
        
        
        
        
        
        fragmentManager = getSupportFragmentManager();  
        FragmentTransaction transaction =  fragmentManager.beginTransaction();  
        Fragment fragment =  FragmentFactory.getInstanceByIndex(R.id.dance);  
        transaction.replace(R.id.content, fragment);  
        transaction.commit();  
        
        
 
        
        
        radioGroup = (RadioGroup) findViewById(R.id.rg_tab);  

    	LayoutParams myParams=(LayoutParams) radioGroup.getLayoutParams();
        myParams.height=(int)((float)RobotData.myLinearLayoutHeight*(float)0.1929);
        radioGroup.setLayoutParams(myParams);
        
        
        
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {  
            @Override  
            public void onCheckedChanged(RadioGroup group, int checkedId) {
            	
            	LinearLayout layout; 
            	switch (checkedId) {
            	
				case R.id.dance:
				
					layout=(LinearLayout) findViewById(R.id.itempicset);
					layout.setBackgroundResource(R.drawable.item1);
					
					break;

				case R.id.speak:
					
					layout=(LinearLayout) findViewById(R.id.itempicset);
					layout.setBackgroundResource(R.drawable.item2);
					
					break;
				case R.id.move:
					
					layout=(LinearLayout) findViewById(R.id.itempicset);
					layout.setBackgroundResource(R.drawable.item3);
					
					break;
			
				}
                FragmentTransaction transaction = (FragmentTransaction) fragmentManager.beginTransaction();  
                Fragment fragment = (Fragment) FragmentFactory.getInstanceByIndex(checkedId);  
                transaction.replace(R.id.content, fragment);  
                transaction.commit();  
            }  
        });  
   
     
        
        RobotData.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();  
		if(RobotData.mBluetoothAdapter == null){  
		        //�������ֻ���֧������  
			Toast.makeText(this, "���豸��֧������", 0).show();
		   //     return;  
		}else{ 
			Toast.makeText(this, "���豸֧������", 0).show();
		}
		if(!RobotData.mBluetoothAdapter.isEnabled()){ //����δ��������������  
           Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);        
            startActivityForResult(enableIntent,BLUETEETHID); 
		} else{
			Toast.makeText(this, "�����Ѿ�����", 0).show();
		}
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub	
		super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == BLUETEETHID){  
        	if(resultCode==RESULT_OK){
        		//�����Ѿ�����   
            	Toast.makeText(this, "���������ɹ�", 0).show();
        	}else if(resultCode==RESULT_CANCELED){
        		//��ȡ�����������ұ���Ϊ���Ѿ�����ʹ�ô������
            	Toast.makeText(this, "����ȡ��������", 0).show();
            	System.out.println("����ȡ��������");
            //	
        	}
        	System.out.println(resultCode);
             
        }  	 
        
        if(requestCode == DIALOGID){  
        	if(resultCode==RESULT_OK){
            //�����Ѿ�����  
        		
        		String result = data.getExtras().getString("blueAddress");
        		Toast.makeText(this, result, 0).show();
        		if(BluetoothAdapter.checkBluetoothAddress(result)==true){
        			RobotData.device=RobotData.mBluetoothAdapter.getRemoteDevice(result);
        			RobotData.ct=new ConnectThread(RobotData.device, handler);
        			RobotData.ct.start();
        		}else{
        			Toast.makeText(this, "������Ч��mac��ַ", 0).show();
        		}		  		
        	}
       }  	
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(1,1,0, "��������");
		menu.add(1,2,1,"�˳�");
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case 1:
			
			
			if(!RobotData.mBluetoothAdapter.isEnabled()){ //����δ��������������  
	          
				Toast.makeText(this, "���ȴ���������", 0).show();
			}else{
		        Intent intent = new Intent(MainActivity.this, DialogActivity.class);
	            startActivityForResult(intent,DIALOGID); 
			}
			break;

		case 2:
			CommonFun.closeProgram(this);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onBackPressed() {
	       // TODO Auto-generated method stub
		CommonFun.closeProgram(this);
	}
	
	public void integralAction(View view){
		AppConnect.getInstance(this).showOffers(this);
	}
	
	
	
	
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		if(pKeyCode == KeyEvent.KEYCODE_BACK && pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			
			CommonFun.closeProgram(this);
			return true;
		} else {
			return super.onKeyDown(pKeyCode, pEvent);
		}
	}
	
	
}



















