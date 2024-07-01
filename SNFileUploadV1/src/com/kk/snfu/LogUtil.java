package com.kk.snfu;


import java.io.File;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LogUtil {

	

	/* level can be WARNING,SEVERE,INFO or any other Levels of Level class */
	public static void writeToLog(String logString, String level,String className) {
		Logger logger = Logger.getLogger(className);
		String LOG_FILE = PropertiesUtil.getProperty("LOG_FILE") + "_" + DateUtil.getCurrentDateyyyyMMdd() + ".log";
		System.out.println(LOG_FILE);
		try {
			LogManager.getLogManager().reset();
			FileHandler fh = new FileHandler(LOG_FILE, true);
			logger.addHandler(fh);
			logger.log(Level.parse(level), logString);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void initializeLogFile() {
		String LOG_FILE = PropertiesUtil.getProperty("LOG_FILE");
		File logFile = new File(LOG_FILE + "_" + DateUtil.getCurrentDateyyyyMMdd() + ".log");
		File parentFolder = new File(logFile.getParent());
		parentFolder.mkdirs();
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		//initializeLogFile();
		//writeToLog("test", "WARNING",LogUtil.class.getName());
	}

}