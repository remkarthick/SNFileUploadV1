package com.kk.snfu;


import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class PropertiesUtil {
private final static String propFileName = "config.properties";
private final static String SALT = "12DER5gd";
private final static int ITERATIONS = 65536;
private final static String ALGORITHM = "PBEWithMD5AndDES";
private final static String PASSKEY = "DADs324234frer";
private final static Charset CHARSET = StandardCharsets.ISO_8859_1;

public static AllProperties getProperties() {
Properties properties = new Properties();
AllProperties allProp = new AllProperties();
try {
properties.load(new FileReader(propFileName));
String TARGET_URL = properties.getProperty("TARGET_URL");
String TABLE_NAME = properties.getProperty("TABLE_NAME");
String TABLE_SYS_ID = properties.getProperty("TABLE_SYS_ID");
String LOG_FILE = properties.getProperty("LOG_FILE");
String STATUS_LOG_FILE = properties.getProperty("STATUS_LOG_FILE");
String FOLDERS_TO_MONITOR = properties.getProperty("FOLDERS_TO_MONITOR");


allProp.TARGET_URL = TARGET_URL;
allProp.TABLE_NAME = TABLE_NAME;
allProp.TABLE_SYS_ID = TABLE_SYS_ID;
allProp.LOG_FILE = LOG_FILE;
allProp.STATUS_LOG_FILE = STATUS_LOG_FILE;
allProp.FOLDERS_TO_MONITOR = FOLDERS_TO_MONITOR;

} catch (Exception e) {
e.printStackTrace();
}
return allProp;
}

private static void encryptCreds() {
try {
Properties properties = new Properties();
properties.load(new FileReader(propFileName));
String USERNAME = properties.getProperty("USERNAME");
String PASSWORD = properties.getProperty("PASSWORD");

// String BASICAUTH = "Basic " + Base64.getEncoder().encodeToString((USERNAME +
// ":" + PASSWORD).getBytes());

String BASE64CRED = Base64.getEncoder().encodeToString((USERNAME + ":" + PASSWORD).getBytes());

byte[] arrSALT = SALT.getBytes();

PBEParameterSpec pbeParamSpec = new PBEParameterSpec(arrSALT, ITERATIONS);

SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
SecretKey secKey = skf.generateSecret(new PBEKeySpec(PASSKEY.toCharArray()));
Cipher cipher = Cipher.getInstance(ALGORITHM);
cipher.init(Cipher.ENCRYPT_MODE, secKey, pbeParamSpec);

byte[] securePassword = cipher.doFinal(BASE64CRED.getBytes());

FileWriter fw = new FileWriter(propFileName);
String encryptedKey = new String(securePassword, CHARSET);
properties.remove("USERNAME");
properties.remove("PASSWORD");
properties.setProperty("KEY", encryptedKey);
properties.store(fw, null);

} catch (Exception e) {
e.printStackTrace();
}
}

private static String decryptCreds() {
String decodedString="";
try {

Properties properties = new Properties();
properties.load(new FileReader(propFileName));
String KEY = properties.getProperty("KEY");
byte[] securePasswordArr = KEY.getBytes(CHARSET);

byte[] arrSALT = SALT.getBytes();
PBEParameterSpec pbeParamSpec = new PBEParameterSpec(arrSALT, ITERATIONS);

SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
SecretKey secKey = skf.generateSecret(new PBEKeySpec(PASSKEY.toCharArray()));

Cipher dcipher = Cipher.getInstance(ALGORITHM);
dcipher.init(Cipher.DECRYPT_MODE, secKey, pbeParamSpec);
byte decryptedKey[] = dcipher.doFinal(securePasswordArr);
//decodedString=Base64.getDecoder().decode(decryptedKey).toString();
decodedString=new String(Base64.getDecoder().decode(decryptedKey),CHARSET);
} catch (Exception e) {
e.printStackTrace();
}
return decodedString;
}



public static void setEncryptedCreds() {
try {
Properties properties = new Properties();
properties.load(new FileReader(propFileName));
String USERNAME = properties.getProperty("USERNAME");
String PASSWORD = properties.getProperty("PASSWORD");
if(USERNAME==null|| PASSWORD==null ) {
return;
}else {
encryptCreds();
}
} catch (Exception e) {
e.printStackTrace();
}

}

public static String getDecryptedCreds() {
try {
Properties properties = new Properties();
properties.load(new FileReader(propFileName));
String KEY = properties.getProperty("KEY");
if(KEY==null ) {
return null;
}else {
return decryptCreds();
}
} catch (Exception e) {
e.printStackTrace();
return null;
}

}

/*
public static void main(String[] args) {
//getDecryptedCreds();
}
*/
public static String getProperty(String propertyName) {
String PROP_NAME = "";
try {
Properties properties = new Properties();
properties.load(new FileReader(propFileName));

PROP_NAME = properties.getProperty(propertyName);

} catch (Exception e) {
e.printStackTrace();
}
return PROP_NAME;
}

public static String getTargetURL() {
String TARGET_URL = "";
try {
Properties properties = new Properties();
properties.load(new FileReader(propFileName));

TARGET_URL = properties.getProperty("TARGET_URL");

} catch (Exception e) {
e.printStackTrace();
}
return TARGET_URL;
}

}

class AllProperties {
String TARGET_URL;
String TABLE_NAME;
String TABLE_SYS_ID;
String LOG_FILE;
String STATUS_LOG_FILE;
String FOLDERS_TO_MONITOR;
}