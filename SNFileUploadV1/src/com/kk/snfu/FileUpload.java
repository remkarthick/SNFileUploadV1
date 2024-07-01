package com.kk.snfu;


import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class FileUpload {

	String TXFM_AFTER_LOAD = "true";

	public SNFileUploadResponse uploadFile(String impSetTblName, String fullFileName) {
		String BASICAUTH = "Basic " + Base64.getEncoder().encodeToString(PropertiesUtil.getDecryptedCreds().getBytes());
//System.out.println("BASICAUTH:"+BASICAUTH);
		String BOUNDARY = Long.toHexString(System.currentTimeMillis());
		String TARGET_URL = PropertiesUtil.getTargetURL() + "/sys_import.do";
		SNFileUploadResponse snfresponse = null;
		try {

			Path filePath = Paths.get(fullFileName);
			String fileName = filePath.getFileName().toString();
			System.out.println(fileName);

			fileName = URLEncoder.encode(fileName, "UTF-8");

// sysparm_import_set_tablename=u_test_upload&sysparm_transform_after_load=true
			String queryParams = "?" + "sysparm_import_set_tablename=" + impSetTblName
					+ "&sysparm_transform_after_load=" + TXFM_AFTER_LOAD;

			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder()
					.header("Content-Type", "multipart/form-data; boundary=" + BOUNDARY)
					.header("Authorization", BASICAUTH).uri(URI.create(TARGET_URL + queryParams))
					.POST(HttpRequest.BodyPublishers.ofFile(filePath)).build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			System.out.println("Status code: " + response.statusCode());
			System.out.println("\n Body: " + response.body());

			snfresponse = new SNFileUploadResponse(response.statusCode() + "", response.body().toString());

			if (response.statusCode() != 201) {
				LogUtil.writeToLog(response.body(), "SEVERE", FileUtil.class.getName());
			}

		} catch (Exception e) {
			LogUtil.writeToLog(e.toString(), "SEVERE", FileUtil.class.getName());
		}
		return snfresponse;

	}

}

class SNFileUploadResponse {
	String responseCode;
	String responseBody;

	public SNFileUploadResponse(String responseCode, String responseBody) {

		this.responseCode = responseCode;
		this.responseBody = responseBody;

	}
}