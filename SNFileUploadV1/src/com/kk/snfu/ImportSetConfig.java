package com.kk.snfu;


import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Base64;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ImportSetConfig {

	// Read SN Instance Properties and set the content of IMPORT_CONFIG_FILE
	public static void setImportCfg(String IMPORT_CONFIG_FILE) {
		try {
			String BASICAUTH = "Basic "
					+ Base64.getEncoder().encodeToString(PropertiesUtil.getDecryptedCreds().getBytes());
			String IMP_CFG_PROPERTIES_SYS_ID = PropertiesUtil.getProperty("IMP_CFG_PROPERTIES_SYS_ID");
			String queryParams = "?" + "sysparm_query=sys_id=" + IMP_CFG_PROPERTIES_SYS_ID + "&sysparm_fields=value";
			String TARGET_URL = PropertiesUtil.getProperty("TARGET_URL") + "/api/now/v2/table/sys_properties";

			System.out.println((TARGET_URL + queryParams));
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().header("Content-Type", "application/json")
					.header("Authorization", BASICAUTH).uri(URI.create(TARGET_URL + queryParams)).GET().build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			System.out.println("Status code: " + response.statusCode());
			System.out.println(response.body());

			String responseBody = response.body();
			if (response.statusCode() == 200 && responseBody != null) {
				responseBody = responseBody.replaceAll("\\\\r\\\\n", "\r\n").replaceAll("\\\"", "\"");
				JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
				JsonArray result = jsonResponse.get("result").getAsJsonArray();
				JsonElement value = JsonParser.parseString(result.get(0).getAsJsonObject().get("value").toString());
				FileUtil.writeToFile(IMPORT_CONFIG_FILE,value.getAsString());
			} else {
				throw new Exception("Status Code : " + response.statusCode() + " - Message : " + response.body());
			}

			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Read from the IMPORT_CONFIG_FILE in File System
	public static ArrayList<ImportSetTemplate> getImportConfig(String fullFileName) {
		ArrayList<ImportSetTemplate> alImportSetTemplate=new ArrayList<ImportSetTemplate>();
		ImportSetTemplate[] importSetTemplateArr = {};
		try {
			BufferedReader br = new BufferedReader(new FileReader(fullFileName));
			Gson gson = new Gson();
			importSetTemplateArr = gson.fromJson(br, ImportSetTemplate[].class);
			for(ImportSetTemplate i:importSetTemplateArr) {
				if(i!=null)
				{
					alImportSetTemplate.add(i);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return alImportSetTemplate;
	}

	// Get Import Set Name by providing the folder name
	public static String getImportSetName(String folderName) {
		ArrayList<ImportSetTemplate> importCfgArr = ImportSetConfig
				.getImportConfig(PropertiesUtil.getProperty(PropertiesUtil.getProperty("IMPORT_CONFIG_FILE")));
		for (ImportSetTemplate importCfg : importCfgArr) {
			if (importCfg.WATCHFOLDER.replace("\\\\", "\\").equals(folderName)) {
				return importCfg.ISTABLENAME;
			}
		}
		return null;
	}

}

class ImportSetTemplate {
	String ISTABLENAME;
	String WATCHFOLDER;
}