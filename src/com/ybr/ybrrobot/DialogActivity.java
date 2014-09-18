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
		
		
		  
		//��ʼ��������
		matchedAdapter = new ArrayAdapter(this,R.layout.list_item,R.id.tv,matchedArrayList);    //����ʹ�õ����Զ����layout��TextView�ؼ�,ע��ʹ���Զ���layoutʱ��Ҫ�ö�ArrayAdapter�Ĺ��캯��	
		searchedAdapter = new ArrayAdapter(DialogActivity.this,R.layout.list_item,R.id.tv,searchedArrayList);    //����ʹ�õ����Զ����layout��TextView�ؼ�,ע��ʹ���Զ���layoutʱ��Ҫ�ö�ArrayAdapter�Ĺ��캯��
		
		
		//�ڴ˽�������������
		matchedList.setAdapter(matchedAdapter);
		searchedList.setAdapter(searchedAdapter);
		//�ڴ����ü�����
		matchedList.setOnItemClickListener(this);
		searchedList.setOnItemClickListener(this);
		
		
		//��ȡ������豸
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
                //System.out.println("����Ե��豸��"+device.getAddress());  
                System.out.println(device.getName()+ "\n" + device.getAddress());
                matchedAdapter.notifyDataSetChanged();
            }  
        }  
		
	}
	
	//���������ť�������в���
	public void onSearch(View view){
		//�ڴ�������������ĳ���	
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);  
        this.registerReceiver(mReceiver, filter);  
  
		//���������������onReceive  
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
                  // �Ѿ���Ե�������  
                 if (device.getBondState() != BluetoothDevice.BOND_BONDED) {  	 
                	 searchedArrayList.add(device.getName() + "\n" + device.getAddress());  //�����豸��ַ������  
                	 searchedAdapter.notifyDataSetChanged();
                 }  
                 
            }else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {  //��������  
            	
                if (searchedAdapter.getCount() == 0) {  
                	searchedArrayList.add("û���������豸");    	
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
