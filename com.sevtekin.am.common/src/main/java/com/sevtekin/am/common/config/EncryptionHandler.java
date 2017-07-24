package com.sevtekin.am.common.config;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.sevtekin.am.common.utils.PropertiesParser;

public class EncryptionHandler {
	private static final byte[] keyValue = new byte[] { 'D', 'u', 'k', 'e',
			'O', 'f', 'M','a', 'k', 'e', 'F', 'i', 'e', 'l', 'd', '!' };
	
	public void encryptPropFile() {
		PropertiesParser parser = new PropertiesParser();
		String filePath = System.getProperty("user.home") + "/.am/am.properties";
		String oldValue = parser.getPropertyValue(filePath, "DBPASSWORD");
		try {
			String newValue = encrypt(oldValue);
			parser.setPropertyValue(filePath, "DBPASSWORD",newValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void decryptPropFile() {
		PropertiesParser parser = new PropertiesParser();
		String filePath = System.getProperty("user.home") + "/.am/am.properties";
		String oldValue = parser.getPropertyValue(filePath, "DBPASSWORD");
		try {
			String newValue = decrypt(oldValue);
			parser.setPropertyValue(filePath, "DBPASSWORD",newValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String encrypt(String Data) throws Exception {
		Key key = generateKey();
		Cipher c = Cipher.getInstance("AES");
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encVal = c.doFinal(Data.getBytes());
		String encryptedValue = new BASE64Encoder().encode(encVal);
		return encryptedValue;
	}

	public String decrypt(String encryptedData) throws Exception {
		Key key = generateKey();
		Cipher c = Cipher.getInstance("AES");
		c.init(Cipher.DECRYPT_MODE, key);
		byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
		byte[] decValue = c.doFinal(decordedValue);
		String decryptedValue = new String(decValue);
		return decryptedValue;
	}

	private static Key generateKey() throws Exception {
		Key key = new SecretKeySpec(keyValue, "AES");
		return key;
	}
}