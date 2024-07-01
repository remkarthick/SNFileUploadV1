package com.kk.snfu;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {

	public static String getCurrentDateTime() {
		return new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
	}
	
	public static String getCurrentDateyyyyMMdd() {
		return new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
	}

}
