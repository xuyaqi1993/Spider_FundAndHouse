package com.xyq.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 *  @Description: 生成ID
 *  @author:xuyaqi 
 *  @date:2018年5月17日
 */
public class IDGenerator {
	private static long time;
	
	public synchronized static long getId1() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		long curr = Long.parseLong(dateFormat.format(new Date()) + "00000");
		if (time != curr) {
			if (curr < time) {
				time = time + 1;
			} else {
				time = curr;
			}
		} else {
			time = time + 1;
		}
		return time;
	}
	
	/** 根据当前时间自动生成ID。*/
	public synchronized static long getId() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
		long curr = Long.parseLong(dateFormat.format(new Date()) + "000");
		if (time != curr) {
			if (curr < time) {
				time = time + 1;
			} else {
				time = curr;
			}
		} else {
			time = time + 1;
		}
		return time;
	}
	
	/** 根据当前时间自动生成ID。*/
	public synchronized static long getId2() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
		long curr = Long.parseLong(dateFormat.format(new Date()) + "0000");
		if (time != curr) {
			if (curr < time) {
				time = time + 1;
			} else {
				time = curr;
			}
		} else {
			time = time + 1;
		}
		return time;
	}
	
	public synchronized static long getTaskId() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		long curr = Long.parseLong(dateFormat.format(new Date()));
		if (time != curr) {
			if (curr < time) {
				time = time + 1;
			} else {
				time = curr;
			}
		} else {
			time = time + 1;
		}
		return time;
	}
	

	public static void main(String[] args) throws IOException {
		for(int i=0;i<1000;i++){
			System.out.println(IDGenerator.getId());
		}
	}
}