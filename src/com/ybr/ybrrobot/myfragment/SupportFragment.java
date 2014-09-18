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
        // ����ѡ��   
        return view;  
    }
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//�ڴ˻�ȡdance�б���ʾ������     
		danceList=(ListView)getView().findViewById(R.id.danceList);
		myScroll=(ScrollView) getActivity().findViewById(R.id.myScroll);
		
		danceAdapter = new ArrayAdapter(getActivity(),R.layout.list_item,R.id.tv,danceArrayList); 
		
		danceList.setAdapter(danceAdapter);
		//�ڴ����ü�����
		danceList.setOnItemClickListener(this);
		
		//�������
		danceArrayList.add("1 ���к�");
		danceArrayList.add("2 ������");
		danceArrayList.add("3 �����");
		danceArrayList.add("4 ����");
		danceArrayList.add("5 ʧ��");
		danceArrayList.add("6 ̫����");
		
		
		
		
		
		danceAdapter.notifyDataSetChanged();
		
		danceList.setOnTouchListener(this);
		
		Button button=(Button) getView().findViewById(R.id.wrongbtn);
		Bitmap dstbmp;
		Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable._send);
		Bitmap _bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.item1);
		Matrix matrix=new Matrix();  
		matrix.postScale((float)RobotData.myLinearLayoutWidth*RobotData.myScale/_bitmap.getWidth(),(float)RobotData.myLinearLayoutHeight*RobotData.myScale/_bitmap.getHeight());//��ȡ���ű���  
	
	 
		 dstbmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);//�����ű���������Ӧ��Ļ���µ�bitmap��  
		 button.setBackgroundDrawable(new BitmapDrawable(dstbmp));
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		String temp = (String) danceList.getAdapter().getItem(position);// dance�б���dance����
		int actionId = 0; // ��Ӧdance��id
		byte[] danceMsgBuffer = null;
		// �б���dacne���Ʋ�Ϊ�յ�ʱ��,��dance�������Ϣ���͸�������
		if (temp != "") {
			int start = temp.indexOf(" ");
			temp = temp.substring(0, start);
			//view.setBackgroundResource(color.black);
			actionId = Integer.parseInt(temp) + 0x2F;//0x30��ʾ���������������˶��Ŀ�ʼ
			//danceMsgBuffer = CommonFun.danceToByte("�赸ִ��", "gb2312", danceId);// �������ִ���������ת����byte������ʽ����Ȼ�����˲���ʶ��

	
			danceMsgBuffer = CommonFun.actionToByte(actionId);
			
			
			/*
			 * ����һ���Ǵ��������,��dancemsgbuffer���͸�������
			 */
			if (RobotData.isSocketCreated==true) {
				RobotData.ct.connThread.write(danceMsgBuffer);
			} else {
				Toast.makeText(this.getActivity(), "���������豸������ԣ�", 0).show();
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
