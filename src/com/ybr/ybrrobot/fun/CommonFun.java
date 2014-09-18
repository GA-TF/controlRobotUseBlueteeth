package com.ybr.ybrrobot.fun;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cn.waps.AppConnect;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ScrollView;

public class CommonFun {
	public static int danceIdReduce = 0;// ���в�ֵ
	private static String fileName = "/sdcard/dance.lis";// �����ļ���sd����ʲôλ��
	public static void closeProgram(Context context){
		AppConnect.getInstance(context).close();
		android.os.Process.killProcess(android.os.Process.myPid()); 
		System.exit(0);
	}
	public static void scroll2Bottom(final ScrollView scroll, final View inner) {
	    Handler handler = new Handler();
	    handler.post(new Runnable() {
	      @Override
	      public void run() {
	        // TODO Auto-generated method stub
	        if (scroll == null || inner == null) {
	          return;
	        }
	        // �ڲ�߶ȳ������
	        int offset = inner.getMeasuredHeight()
	            - scroll.getMeasuredHeight();
	        if (offset < 0) {
	          System.out.println("��λ...");
	          offset = 0;
	        }
	        scroll.scrollTo(0, offset);
	      }
	    });
	}
	
	
	public static byte[] sendch(byte[] data) {
		/*
		 * 1~4λ����Э���ĵ����ƶ�����ֵ�� 5λ��֡���ȡ� 6λ�������֣���Э���ĵ��ƶ�
		 */
		byte[] destobj2 = { (byte) 0xef, (byte) 0x55, (byte) 0Xaa, (byte) 0Xfe,
				(byte) 0X07, (byte) 0X20 };
		int len = data.length;// ��ȡeditTextת���ɵĵ�byte����ĳ���
		/*
		 * ���������Ǻ�����ƴ���õ�
		 */
		int count = len + 3;// ��������鳤�ȼ�3��������Ҫ����Ԥ���ֽڵĳ���
		destobj2[4] = (byte) count;// ����֡����

		int sum = len + 8;// 6λdestobj���ݣ�2λԤ���ַ���,����editText�ĳ���
		byte[] con = new byte[sum];// ����sum������ƴ�Ӻ���ܳ��ȣ�����newһ������
		byte[] obj = new byte[len + 1];// �½�һ����editText��1λ�����飬���Ҫ����У��������顣
		/*
		 * ������ src - Դ���顣 srcPos - Դ�����е���ʼλ�á� dest - Ŀ�����顣 destPos - Ŀ�������е���ʼλ�á�
		 * length - Ҫ���Ƶ�����Ԫ�ص�������
		 */
		System.arraycopy(destobj2, 0, con, 0, 6);// ��destobj2�ĵ�1,2,3,4,5,6λ���Ƶ�con(1λ��ʼ)
		System.arraycopy(data, 0, con, 6, len);// ��dataȫ�����Ƶ�con(7λ��ʼ)
		System.arraycopy(con, 5, obj, 0, len + 1);// ��con��6λ��ʼ���Ƶ�obj��������������У����
		/*
		 * ����������У����Ĺ��̣���ʱ���о�
		 */
		int k = getCRC(obj);
		con[sum - 2] = (byte) k;
		con[sum - 1] = (byte) (k >> 8);

		return con;
	}

	
	
	public static int getCRC(byte[] buffer) {

		int crc = 0x0;// 
//		ͨ�Ų���CCITTУ�顣У�鹫ʽCRC = X16 + X12 + X5 + 1��
//		������Ϊ38400����ʼλ1λ������λ8λ��ֹͣλ1λ������żУ�顣
		int polynomial = 0x1021;//2^12+2^5+2^0,����ط������polynomial���Զ���У�鹫ʽ������ 
		for (int j = 0; j < buffer.length; j++) {
			char b = (char) buffer[j];
			for (int i = 0; i < 8; i++) {
				boolean bit = ((b >> (7 - i) & 1) == 1);
				boolean c15 = ((crc >> 15 & 1) == 1);
				crc <<= 1;
				if (c15 ^ bit) {
					crc ^= polynomial;
				}
			}

		}
		crc &= 0xffff;
		return crc;
	}
	
	
	
	
	// ����spinners
	public static void getSpinners(ArrayList<String> danceArrayList) {
		// TODO Auto-generated method stub

		/*
		 * �������ļ���ȡ�赸�б�
		 */

		File file = new File(fileName);
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "gb2312"));
			String temp = null;
			String tag = null;
			String tag2 = null;
			while ((temp = reader.readLine()) != null) {
				int start = temp.indexOf(";");// ��ȡ��һ���ֺŵ�����
				tag = temp.substring(start - 1, start);// ��ȡ��ʾ��

				int start2 = temp.indexOf(" ");// ���ص�һ���ո������
				tag2 = temp.substring(0, start2);// ��ȡ����

				// �����ʾ��Ϊ0�Ķ���
				if (tag.equals("0")) {
					int start3 = 0;// ��ȡmusic����õ�����
					if ((start3 = temp.indexOf("music1.mp3")) > 0) {
						danceIdReduce = Integer.parseInt(tag2) - 1;// �����Ų�ֵ������Ҫ������á�-1��ʾ��һ��mp3����
					}
					tag2 = String.valueOf(Integer.parseInt(tag2)
							- danceIdReduce);// ����tag2����Ȼ���к�
					temp = temp.substring(start + 1, temp.length());// ����temp,�ӵ�һ���ֺſ�ʼ����
					int end = temp.indexOf(";");// ��¼�ڶ����ֺ�λ��
					temp = temp.substring(0, end);// �����ֺ�֮�������;
					danceArrayList.add(tag2 + " " + temp);
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	
	
	// �������ִ���������ת����byte������ʽ����Ȼ�����˲���ʶ��
	public static byte[] danceToByte(String string, String string2, int danceId) {

		/*
		 * 1~2λ�����͵���������ݣ�����Э���ĵ��ƶ��� 3~4λ��Ԥ���ֽڣ�Ĭ��FF
		 */
		byte[] destobj = { (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff };
		/*
		 * 1~4λ����Э���ĵ����ƶ�����ֵ�� 5λ��֡���ȡ� 6λ�������֣���Э���ĵ��ƶ�
		 */
		byte[] destobj2 = { (byte) 0xef, (byte) 0x55, (byte) 0xaa, (byte) 0xfe,
				(byte) 0x07, (byte) 0x01 };
		// ��������obj����Ӧ�����������֮��ƴ�Ӻõ�byte����
		byte[] obj = null;
		byte[] obj1 = null;

		// danceIdҪ��������У����
		destobj[0] = (byte) danceId;
		destobj[1] = (byte) (danceId >> 8);
		destobj2[5] = (byte) 0x13;// 0x13����Э���ĵ��涨��

		int sum = destobj.length;
		obj = new byte[sum + 8];// ��ʼ��һ����destobj��8λ��byte���飬4λ+6λ+2λ(У����)
		obj1 = new byte[sum + 1];// ��ʼ��һ����destobj��1λ��byte����, 5λ
		/*
		 * ������ src - Դ���顣 srcPos - Դ�����е���ʼλ�á� dest - Ŀ�����顣 destPos - Ŀ�������е���ʼλ�á�
		 * length - Ҫ���Ƶ�����Ԫ�ص�������
		 */
		System.arraycopy(destobj2, 0, obj, 0, 6);// ��destobj2�ĵ�1,2,3,4,5,6λ���Ƶ�obj(1λ��ʼ)
		System.arraycopy(destobj, 0, obj, 6, sum);// ��destobj�ĵ�1,2,3,4λ���Ƶ�obj(7λ��ʼ)
		System.arraycopy(obj, 5, obj1, 0, obj.length - 7);// ��obj��6,7,8λ���Ƶ�obj1(1λ��ʼ)

		/*
		 * ����У����
		 */
		int k = getCRC(obj1);// obj1�Ŀ���ֵ110100
		obj[sum + 6] = (byte) k;// ��k��ֵ��obj��11λ
		obj[sum + 7] = (byte) (k >> 8);// ��k����8λ��ֵ��obj��12λ

		return obj;
	}
	
	
	
	
	
	public static byte[] actionToByte(int actionId) {

		
		byte[] destobj = { (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff };
		
		byte[] destobj2 = { (byte) 0xef, (byte) 0x55, (byte) 0xaa, (byte) 0xfe,
				(byte) 0x07, (byte) 0x11 };
	
		byte[] obj = null;
		byte[] obj1 = null;

		// danceIdҪ��������У����
		destobj[0] = (byte) actionId;
		destobj[1] = (byte) (actionId >> 8);
		destobj2[5] = (byte) 0x11;

		int sum = destobj.length;
		obj = new byte[sum + 8];// ��ʼ��һ����destobj��8λ��byte���飬4λ+6λ+2λ(У����)
		obj1 = new byte[sum + 1];// ��ʼ��һ����destobj��1λ��byte����, 5λ
		
		System.arraycopy(destobj2, 0, obj, 0, 6);// ��destobj2�ĵ�1,2,3,4,5,6λ���Ƶ�obj(1λ��ʼ)
		System.arraycopy(destobj, 0, obj, 6, sum);// ��destobj�ĵ�1,2,3,4λ���Ƶ�obj(7λ��ʼ)
		System.arraycopy(obj, 5, obj1, 0, obj.length - 7);// ��obj��6,7,8λ���Ƶ�obj1(1λ��ʼ)

		/*
		 * ����У����
		 */
		int k = getCRC(obj1);// obj1�Ŀ���ֵ110100
		obj[sum + 6] = (byte) k;// ��k��ֵ��obj��11λ
		obj[sum + 7] = (byte) (k >> 8);// ��k����8λ��ֵ��obj��12λ

		return obj;
	}
}




