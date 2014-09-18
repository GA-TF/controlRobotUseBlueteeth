package com.ybr.ybrrobot.myfragment;

import java.util.ArrayList;

import com.ybr.ybrrobot.R;
import com.ybr.ybrrobot.data.RobotData;
import com.ybr.ybrrobot.fun.CommonFun;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class SupportFragment extends Fragment implements OnItemClickListener,OnTouchListener{
	ListView danceList=null;
	ArrayAdapter danceAdapter = null;
	ScrollView myScroll=null;
	ArrayList<String> danceArrayList = new ArrayList<String>();
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
        View view = inflater.inflate(R.layout.dance_fragment, null);    
        // 设置选项   
        return view;  
    }
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//在此获取dance列表并显示出来。     
		danceList=(ListView)getView().findViewById(R.id.danceList);
		myScroll=(ScrollView) getActivity().findViewById(R.id.myScroll);
		
		danceAdapter = new ArrayAdapter(getActivity(),R.layout.list_item,R.id.tv,danceArrayList); 
		
		danceList.setAdapter(danceAdapter);
		//在此设置监听器
		danceList.setOnItemClickListener(this);
		
		//添加内容
		danceArrayList.add("1 打招呼");
		danceArrayList.add("2 还不错");
		danceArrayList.add("3 很糟糕");
		danceArrayList.add("4 欢呼");
		danceArrayList.add("5 失望");
		danceArrayList.add("6 太棒了");
		
		
		
		
		
		danceAdapter.notifyDataSetChanged();
		
		danceList.setOnTouchListener(this);
		
		Button button=(Button) getView().findViewById(R.id.wrongbtn);
		Bitmap dstbmp;
		Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable._send);
		Bitmap _bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.item1);
		Matrix matrix=new Matrix();  
		matrix.postScale((float)RobotData.myLinearLayoutWidth*RobotData.myScale/_bitmap.getWidth(),(float)RobotData.myLinearLayoutHeight*RobotData.myScale/_bitmap.getHeight());//获取缩放比例  
	
	 
		 dstbmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);//按缩放比例生成适应屏幕的新的bitmap；  
		 button.setBackgroundDrawable(new BitmapDrawable(dstbmp));
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		String temp = (String) danceList.getAdapter().getItem(position);// dance列表中dance名称
		int actionId = 0; // 对应dance的id
		byte[] danceMsgBuffer = null;
		// 列表中dacne名称不为空的时候,把dance的相关信息发送给机器人
		if (temp != "") {
			int start = temp.indexOf(" ");
			temp = temp.substring(0, start);
			//view.setBackgroundResource(color.black);
			actionId = Integer.parseInt(temp) + 0x2F;//0x30表示，，，，，这种运动的开始
			//danceMsgBuffer = CommonFun.danceToByte("舞蹈执行", "gb2312", danceId);// 把跳舞的执行命令参数转换成byte数组形式，不然机器人不能识别

	
			danceMsgBuffer = CommonFun.actionToByte(actionId);
			
			
			/*
			 * 下面一段是创建输出流,把dancemsgbuffer发送给机器人
			 */
			if (RobotData.isSocketCreated==true) {
				RobotData.ct.connThread.write(danceMsgBuffer);
			} else {
				Toast.makeText(this.getActivity(), "请与蓝牙设备进行配对！", 0).show();
			}

		}
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			myScroll.requestDisallowInterceptTouchEvent(true);
		}
		return false;
	}	
		
}
