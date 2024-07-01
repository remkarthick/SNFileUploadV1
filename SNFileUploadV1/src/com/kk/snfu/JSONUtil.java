package com.kk.snfu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONUtil {
	
	 
	public static void writeJsonToFile(String fullFileName,String jsonString, Object className)
	{
		GsonBuilder gBuilder = new GsonBuilder();
		gBuilder.setPrettyPrinting();

		Gson gson = gBuilder.create();
		//Student student = gson.fromJson(jsonString, Student.class);

		//jsonString = gson.toJson(student);
		System.out.println(jsonString);
	}

	public static void main(String[] args) {
		String sampleJSON="{'NAME':'Angel',"
				+ ""
				+ "'NUM':123}";
		
		writeJsonToFile("",sampleJSON,Student.class);

	}

}

class Student{
	String NAME;
	String NUM;
}
