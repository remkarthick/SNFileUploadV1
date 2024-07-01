package com.kk.snfu;


import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class FileUtil {

	public static void setStatus(String content) {
		try {
			FileWriter fw = new FileWriter(new File("status.txt"), true);
			fw.append(content + "\n");
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	public static void createFileAndDirs(String fullFileName) {
		try {

			Path pathfullFileName = Paths.get(fullFileName);
			Path parentPath = pathfullFileName.getParent();
			Files.createDirectories(parentPath);
			if (!Files.exists(pathfullFileName)) {
				Files.createFile(pathfullFileName);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeToFile(String fullFileName, String content) {
		try {

			Path pathfullFileName = Paths.get(fullFileName);
			Files.write(pathfullFileName, content.getBytes(), StandardOpenOption.CREATE);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}

