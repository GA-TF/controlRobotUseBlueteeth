package com.ybr.ybrrobot;

import java.io.IOException;
import java.util.UUID;

import com.ybr.ybrrobot.data.RobotData;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

public class ConnectThread extends Thread {
	private BluetoothSocket mmSocket;  
	private BluetoothDevice mmDevice; 
	private Handler mmHandler;
	public ConnectedThread connThread;
	private boolean connected;
	private int connectCount;
	static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB"; 
	UUID MY_UUID_SECURE=UUID.fromString(SPP_UUID);
	
	public ConnectThread(BluetoothDevice device,Handler handler){  
		mmDevice = device;  
		mmHandler=handler;
		BluetoothSocket tmp = null;  
		try {  
			tmp = device.createRfcommSocketToServiceRecord(MY_UUID_SECURE);  
		} catch (IOException e) {  
			e.printStackTrace();
		} 
		mmSocket=tmp;
	}  
	public void run() {
		// TODO Auto-generated method stub
		//super.run();
		connected=false;
		connectCount=0;
		RobotData.mBluetoothAdapter.cancelDiscovery();    //ȡ���豸����  
		while (!connected && connectCount <= 20) { 
			try {  
				mmSocket.connect(); 
				connected = true; 
				RobotData.isSocketCreated=true;
			} catch (IOException e) {  
				connectCount++;
				connected = false;  
				try {  
					mmSocket.close();  
				} catch (IOException e1) {  
					e1.printStackTrace();
				}  
				//connetionFailed();  //����ʧ��  
				return;  
			}finally{
				
			}
		}
		//��ʱ�����½�һ�����ݽ����̣߳��Ѵ�socket����ȥ 
		connThread=new ConnectedThread(mmSocket, mmHandler);
		connThread.start();
	}
	public void cancel() {  
		try {  
			mmSocket.close();  
		}catch (IOException e) {  
			e.printStackTrace();
		}  
	}  

} 







