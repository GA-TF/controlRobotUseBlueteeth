package com.ybr.ybrrobot;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

public class ConnectedThread extends Thread {
	private static final String MESSAGE_READ = null;
	private static final int GET_SOCKET_DATA = 0;
	public final BluetoothSocket mmSocket;
	private final InputStream mmInStream;
	private final OutputStream mmOutStream;
	private Handler mHandler;
	public ConnectedThread(BluetoothSocket socket,Handler handler){
		mmSocket=socket;
		mHandler=handler;
		InputStream tmpIn=null;
		OutputStream tmpOut=null;
		try{
			tmpIn=socket.getInputStream();
			tmpOut=socket.getOutputStream();
		}catch(IOException e){
		}
		mmInStream=tmpIn;
		mmOutStream=tmpOut;
	}
	public void run(){
		byte[] buffer=new byte[1024]; 
		int bytes;
		while(true){
		try{			
			bytes=mmInStream.read(buffer);
			Message msg=new Message();
			msg.what=GET_SOCKET_DATA;
			msg.obj=new String(buffer).substring(0, bytes);
			mHandler.sendMessage(msg);
		}catch(IOException e){
			e.printStackTrace();
			break;
		}
		}
	}
	public void write(byte[] bytes){
		try{
		mmOutStream.write(bytes);
	}catch(IOException e){
	}
	}
	public void cancel(){
		try{
		mmSocket.close();
		}catch(IOException e){
		}
	}
}
