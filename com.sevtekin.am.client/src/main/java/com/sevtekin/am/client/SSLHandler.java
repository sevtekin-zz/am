package com.sevtekin.am.client;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

public class SSLHandler {
	private TrustManager[] trustManager = new TrustManager[] { new X509TrustManager() {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(X509Certificate[] certs, String authType) {
		}

		public void checkServerTrusted(X509Certificate[] certs, String authType) {
		}
	} };
	private HostnameVerifier hostnameVerifier = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	public SSLHandler() {
		try {
			installTrustManager();
			installHostnameVerifier();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void installTrustManager() throws Exception {
		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustManager, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}

	public void installHostnameVerifier() throws Exception {
		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
	}
}