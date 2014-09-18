package com.ybr.ybrrobot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import com.ybr.ybrrobot.R;
import com.ybr.ybrrobot.data.RobotData;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DialogActivity extends Activity implements OnItemClickListener{

	ListView matchedList=null;
	ListView searchedList=null;
	ArrayAdapter matchedAdapter = null;
	ArrayAdapter searchedAdapter = null;
	ArrayList<String> matchedArrayList = new ArrayList<String>();
	String myBluetoothInformation=null;
	
	ArrayList<String> searchedArrayList = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialog);
			
		matchedList=(ListView) findViewById(R.id.matchedList);
		searchedList=(ListView) findViewById(R.id.searchedList);
		//RobotData.mBluetoothAdapter
		
		
		  
		//初始化适配器
		matchedAdapter = new ArrayAdapter(this,R.layout.list_item,R.id.tv,matchedArrayList);    //这里使用的是自定义的layout和TextView控件,注意使用自定的layout时需要用对ArrayAdapter的构造函数	
		searchedAdapter = new ArrayAdapter(DialogActivity.this,R.layout.list_item,R.id.tv,searchedArrayList);    //这里使用的是自定义的layout和TextView控件,注意使用自定的layout时需要用对ArrayAdapter的构造函数
		
		
		//在此进行适配器设置
		matchedList.setAdapter(matchedAdapter);
		searchedList.setAdapter(searchedAdapter);
		//在此设置监听器
		matchedList.setOnItemClickListener(this);
		searchedList.setOnItemClickListener(this);
		
		
		//获取已配对设备
		Set<BluetoothDevice> devices = RobotData.mBluetoothAdapter.getBondedDevices();  
	/*	for(int i=0; i<devices.size(); i++)  
		{  
			BluetoothDevice device = (BluetoothDevice)devices.iterator().next();  
		    matchedArrayList.add(device.getName()+ "\n" + device.getAddress());
		    System.out.println(device.getName()+ "\n" + device.getAddress());
		    matchedAdapter.notifyDataSetChanged();
		}*/
		if(devices.size()>0){  
            for(Iterator<BluetoothDevice> iterator = devices.iterator();iterator.hasNext();){  
                BluetoothDevice device = (BluetoothDevice) iterator.next(); 
                matchedArrayList.add(device.getName()+ "\n" + device.getAddress());
                //System.out.println("已配对的设备："+device.getAddress());  
                System.out.println(device.getName()+ "\n" + device.getAddress());
                matchedAdapter.notifyDataSetChanged();
            }  
        }  
		
	}
	
	//点击搜索按钮进行下列操作
	public void onSearch(View view){
		//在此完成搜索蓝牙的程序。	
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);  
        this.registerReceiver(mReceiver, filter);  
  
		//当搜索结束后调用onReceive  
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);  
        this.registerReceiver(mReceiver, filter);
        searchedArrayList.clear();
        searchedAdapter.notifyDataSetChanged();
        RobotData.mBluetoothAdapter.startDiscovery();
        Button searchBlueteeth=(Button) this.findViewById(R.id.searchBlueteeth);
        TextView searchedTv=(TextView) this.findViewById(R.id.searchedTv);
        searchBlueteeth.setVisibility(View.GONE);
        searchedTv.setVisibility(View.VISIBLE);
	}
	
	

	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {  
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();  
			
            if(BluetoothDevice.ACTION_FOUND.equals(action)){  
            	BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);  
                  // 已经配对的则跳过  
                 if (device.getBondState() != BluetoothDevice.BOND_BONDED) {  	 
                	 searchedArrayList.add(device.getName() + "\n" + device.getAddress());  //保存设备地址与名字  
                	 searchedAdapter.notifyDataSetChanged();
                 }  
                 
            }else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {  //搜索结束  
            	
                if (searchedAdapter.getCount() == 0) {  
                	searchedArrayList.add("没有搜索到设备");    	
                }  
                searchedAdapter.notifyDataSetChanged();
                context.unregisterReceiver(this);
            }  
            
		}  
	};
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
			 int length;
			 String myResult;
             Intent intent = new Intent();
             Bundle bundle=new Bundle();
             if(parent.equals(matchedList)) {  
	            
            	 myBluetoothInformation=matchedArrayList.get(position);
            	 length=myBluetoothInformation.length();
            	 myResult=myBluetoothInformation.substring(length-17,length);
            	        	 
	             bundle.putCharSequence("blueAddress", myResult);
	             intent.putExtras(bundle);
	             
             }else if(parent.equals(searchedList)){    
            	 
            	 myBluetoothInformation=searchedArrayList.get(position);
            	 length=myBluetoothInformation.length();
            	 myResult=myBluetoothInformation.substring(length-17,length);
            	 
	             bundle.putCharSequence("blueAddress", myResult);
	             intent.putExtras(bundle);
             }
             setResult(RESULT_OK,intent);       
             finish();
        
	}
	
	public void onBackPressed() {
	       // TODO Auto-generated method stub
		     Intent backIntent = new Intent();
		     
		     setResult(RESULT_CANCELED,backIntent);
	         finish();
	}

}
