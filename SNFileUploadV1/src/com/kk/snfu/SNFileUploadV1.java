package com.kk.snfu;


import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;

public class SNFileUploadV1 {

	public SNFileUploadV1() {

	}

	public static void main(String[] args) {
		// Create IMPORT_CONFIG_FILE if it doesn't exist
		FileUtil.createFileAndDirs(PropertiesUtil.getProperty("IMPORT_CONFIG_FILE"));
		
		// Set Values in IMPORT_CONFIG_FILE from SN Instance Properties
		ImportSetConfig.setImportCfg(PropertiesUtil.getProperty("IMPORT_CONFIG_FILE"));
		
		LogUtil.initializeLogFile();
		PropertiesUtil.setEncryptedCreds();

		FileUtil.setStatus(DateUtil.getCurrentDateTime() + " : Program Started...");
		System.out.println(DateUtil.getCurrentDateTime() + " : Program Started...");


		Thread sdHook = new ShutdownHook();
		Runtime.getRuntime().addShutdownHook(sdHook);

		ArrayList<ImportSetTemplate> alImportSetTemplate = ImportSetConfig.getImportConfig(PropertiesUtil.getProperty("IMPORT_CONFIG_FILE"));

		try {

			WatchService watchservice = FileSystems.getDefault().newWatchService();

			for (ImportSetTemplate importConfig : alImportSetTemplate) {
				Path path = Paths.get(importConfig.WATCHFOLDER);
				path.register(watchservice, StandardWatchEventKinds.ENTRY_CREATE);
			}

			WatchKey key;
			while ((key = watchservice.take()) != null) {

				for (WatchEvent<?> event : key.pollEvents()) {

					Path dir = (Path) key.watchable();
					System.out.println(dir.toString());
					System.out.println(ImportSetConfig.getImportSetName(dir.toString()));

					Path fullFilePath = dir.resolve(event.context().toString());
					String fullFileName = fullFilePath.toString();

// new FileUpload().uploadFile(impSetTblName,fullFileName);

					/*
					 * System.out.println( "Event kind:" + event.kind() +": File affected: " +
					 * event.context() + ": File Path: " + fullFilePath);
					 */
				}
				key.reset();
			}
			FileUtil.setStatus(DateUtil.getCurrentDateTime() + " : Program Terminated...");
			System.err.println(DateUtil.getCurrentDateTime() + " : Program Terminated...");
		} catch (Exception e) {

			FileUtil.setStatus(DateUtil.getCurrentDateTime() + " : Program Error...");
			System.err.println(DateUtil.getCurrentDateTime() + " : Program Error...");
			LogUtil.writeToLog(e.toString(), "SEVERE",SNFileUploadV1.class.getName());
			e.printStackTrace();
		}

	}

	

}

class ShutdownHook extends Thread {
	@Override
	public void run() {
		FileUtil.setStatus(DateUtil.getCurrentDateTime() + " : Program Terminated...");
		System.out.println(DateUtil.getCurrentDateTime() + " : Program Terminated...");

	}

}