package com.ybr.ybrrobot.data;

import com.ybr.ybrrobot.ConnectThread;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.widget.LinearLayout.LayoutParams;

public class RobotData {
	public static BluetoothAdapter mBluetoothAdapter;
	public static BluetoothDevice device;
	public static boolean isSocketCreated;
	public static ConnectThread ct;
	public static int phoneWidth;
	public static int phoneHeight;
	//public static Context context;
	public static int myLinearLayoutHeight;
	public static int myLinearLayoutWidth;
	public static float myScale;
	public static boolean typeStyle;
	
}
