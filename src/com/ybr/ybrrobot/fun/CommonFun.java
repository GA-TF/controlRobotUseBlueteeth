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
	public static int danceIdReduce = 0;// 序列差值
	private static String fileName = "/sdcard/dance.lis";// 配置文件在sd卡的什么位置
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
	        // 内层高度超过外层
	        int offset = inner.getMeasuredHeight()
	            - scroll.getMeasuredHeight();
	        if (offset < 0) {
	          System.out.println("定位...");
	          offset = 0;
	        }
	        scroll.scrollTo(0, offset);
	      }
	    });
	}
	
	
	public static byte[] sendch(byte[] data) {
		/*
		 * 1~4位：由协议文档中制定，定值。 5位：帧长度。 6位：命令字，由协议文档制定
		 */
		byte[] destobj2 = { (byte) 0xef, (byte) 0x55, (byte) 0Xaa, (byte) 0Xfe,
				(byte) 0X07, (byte) 0X20 };
		int len = data.length;// 获取editText转换成的的byte数组的长度
		/*
		 * 下面两行是后面做拼接用的
		 */
		int count = len + 3;// 把这个数组长度加3，估计是要算上预留字节的长度
		destobj2[4] = (byte) count;// 更新帧长度

		int sum = len + 8;// 6位destobj内容，2位预留字符串,加上editText的长度
		byte[] con = new byte[sum];// 根据sum，就是拼接后的总长度，重新new一个数组
		byte[] obj = new byte[len + 1];// 新建一个比editText多1位的数组，存放要发给校验码的数组。
		/*
		 * 参数： src - 源数组。 srcPos - 源数组中的起始位置。 dest - 目标数组。 destPos - 目标数据中的起始位置。
		 * length - 要复制的数组元素的数量。
		 */
		System.arraycopy(destobj2, 0, con, 0, 6);// 把destobj2的第1,2,3,4,5,6位复制到con(1位开始)
		System.arraycopy(data, 0, con, 6, len);// 把data全部复制到con(7位开始)
		System.arraycopy(con, 5, obj, 0, len + 1);// 把con从6位开始复制到obj，待会用来生产校验码
		/*
		 * 下面是生产校验码的过程，暂时不研究
		 */
		int k = getCRC(obj);
		con[sum - 2] = (byte) k;
		con[sum - 1] = (byte) (k >> 8);

		return con;
	}

	
	
	public static int getCRC(byte[] buffer) {

		int crc = 0x0;// 
//		通信采用CCITT校验。校验公式CRC = X16 + X12 + X5 + 1；
//		波特率为38400，起始位1位，数据位8位，停止位1位，无奇偶校验。
		int polynomial = 0x1021;//2^12+2^5+2^0,这个地方定义的polynomial有自定的校验公式决定的 
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
	
	
	
	
	// 配置spinners
	public static void getSpinners(ArrayList<String> danceArrayList) {
		// TODO Auto-generated method stub

		/*
		 * 从配置文件读取舞蹈列表
		 */

		File file = new File(fileName);
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "gb2312"));
			String temp = null;
			String tag = null;
			String tag2 = null;
			while ((temp = reader.readLine()) != null) {
				int start = temp.indexOf(";");// 获取第一个分号的索引
				tag = temp.substring(start - 1, start);// 获取标示符

				int start2 = temp.indexOf(" ");// 返回第一个空格的索引
				tag2 = temp.substring(0, start2);// 获取标标号

				// 输出标示符为0的动作
				if (tag.equals("0")) {
					int start3 = 0;// 获取music编号用的索引
					if ((start3 = temp.indexOf("music1.mp3")) > 0) {
						danceIdReduce = Integer.parseInt(tag2) - 1;// 算出序号差值，待会要改序号用。-1表示第一个mp3音乐
					}
					tag2 = String.valueOf(Integer.parseInt(tag2)
							- danceIdReduce);// 更新tag2成自然序列号
					temp = temp.substring(start + 1, temp.length());// 更新temp,从第一个分号开始往后
					int end = temp.indexOf(";");// 记录第二个分号位置
					temp = temp.substring(0, end);// 两个分号之间的内容;
					danceArrayList.add(tag2 + " " + temp);
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	
	
	// 把跳舞的执行命令参数转换成byte数组形式，不然机器人不能识别
	public static byte[] danceToByte(String string, String string2, int danceId) {

		/*
		 * 1~2位：发送的命令的内容，根据协议文档制定。 3~4位：预留字节，默认FF
		 */
		byte[] destobj = { (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff };
		/*
		 * 1~4位：由协议文档中制定，定值。 5位：帧长度。 6位：命令字，由协议文档制定
		 */
		byte[] destobj2 = { (byte) 0xef, (byte) 0x55, (byte) 0xaa, (byte) 0xfe,
				(byte) 0x07, (byte) 0x01 };
		// 下面两个obj数组应该是用来存放之后拼接好的byte数组
		byte[] obj = null;
		byte[] obj1 = null;

		// danceId要用来生成校验码
		destobj[0] = (byte) danceId;
		destobj[1] = (byte) (danceId >> 8);
		destobj2[5] = (byte) 0x13;// 0x13根据协议文档规定的

		int sum = destobj.length;
		obj = new byte[sum + 8];// 初始化一个比destobj多8位的byte数组，4位+6位+2位(校验码)
		obj1 = new byte[sum + 1];// 初始化一个比destobj多1位的byte数组, 5位
		/*
		 * 参数： src - 源数组。 srcPos - 源数组中的起始位置。 dest - 目标数组。 destPos - 目标数据中的起始位置。
		 * length - 要复制的数组元素的数量。
		 */
		System.arraycopy(destobj2, 0, obj, 0, 6);// 把destobj2的第1,2,3,4,5,6位复制到obj(1位开始)
		System.arraycopy(destobj, 0, obj, 6, sum);// 把destobj的第1,2,3,4位复制到obj(7位开始)
		System.arraycopy(obj, 5, obj1, 0, obj.length - 7);// 把obj第6,7,8位复制到obj1(1位开始)

		/*
		 * 生成校验码
		 */
		int k = getCRC(obj1);// obj1的可能值110100
		obj[sum + 6] = (byte) k;// 把k赋值给obj的11位
		obj[sum + 7] = (byte) (k >> 8);// 把k右移8位赋值给obj的12位

		return obj;
	}
	
	
	
	
	
	public static byte[] actionToByte(int actionId) {

		
		byte[] destobj = { (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff };
		
		byte[] destobj2 = { (byte) 0xef, (byte) 0x55, (byte) 0xaa, (byte) 0xfe,
				(byte) 0x07, (byte) 0x11 };
	
		byte[] obj = null;
		byte[] obj1 = null;

		// danceId要用来生成校验码
		destobj[0] = (byte) actionId;
		destobj[1] = (byte) (actionId >> 8);
		destobj2[5] = (byte) 0x11;

		int sum = destobj.length;
		obj = new byte[sum + 8];// 初始化一个比destobj多8位的byte数组，4位+6位+2位(校验码)
		obj1 = new byte[sum + 1];// 初始化一个比destobj多1位的byte数组, 5位
		
		System.arraycopy(destobj2, 0, obj, 0, 6);// 把destobj2的第1,2,3,4,5,6位复制到obj(1位开始)
		System.arraycopy(destobj, 0, obj, 6, sum);// 把destobj的第1,2,3,4位复制到obj(7位开始)
		System.arraycopy(obj, 5, obj1, 0, obj.length - 7);// 把obj第6,7,8位复制到obj1(1位开始)

		/*
		 * 生成校验码
		 */
		int k = getCRC(obj1);// obj1的可能值110100
		obj[sum + 6] = (byte) k;// 把k赋值给obj的11位
		obj[sum + 7] = (byte) (k >> 8);// 把k右移8位赋值给obj的12位

		return obj;
	}
}




