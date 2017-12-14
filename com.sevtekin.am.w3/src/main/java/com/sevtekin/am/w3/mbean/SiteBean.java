package com.sevtekin.am.w3.mbean;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.sevtekin.am.client.AMClient;

public class SiteBean implements Serializable {

	private static final long serialVersionUID = 1L;
	AMClient client = new AMClient();
	String user = "";
	String puser = "";
	String pass = "";


	@PostConstruct
	public void initialize() {
		System.out.println("[AM W3][INFO] Initializing the site bean");
		Map<String, String> requestParams = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap();
		if ((requestParams.get("j_username") != null) && (requestParams.get("j_password") != null)) {
			puser = requestParams.get("j_username");
			pass = requestParams.get("j_password");
			System.out.println("u " + puser + " p " + pass);
			this.user = client.auth(puser, pass);
			System.out.println("au " + user);
			if (!user.equalsIgnoreCase("none")) {	
				try {
					ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
					Map<String, Object> sessionMap = externalContext.getSessionMap();
					sessionMap.put("authuser", user);
					externalContext.redirect(externalContext.getRequestContextPath() + "/home.jsf");
					return;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else {
				logout();
			}
		} else {
			System.out.println("[AM W3][INFO] No parameters");
		}
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void logout() {
		System.out.println("[AM W3][INFO] Logging out");
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext extContext = context.getExternalContext();
		extContext.invalidateSession();

		HttpSession session = (HttpSession) extContext.getSession(false);
		if (session != null) {
			System.out.println("[AM W3][INFO] Session is still active");
			session.invalidate();
		} else {
			System.out.println("[AM W3][INFO] Session is null");
		}

		try {
			extContext.redirect(extContext.getRequestContextPath() + "/home.jsf");
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void checkCreds() {
		System.out.println("[AM W3][INFO] Checking the creds");
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext extContext = context.getExternalContext();
       
		Map<String, Object> sessionMap = extContext.getSessionMap();
		String user = (String) sessionMap.get("authuser");
		if (user!=null) {
			System.out.println("[AM W3][INFO] user creds OK for " + user);
		    
		} else {
			System.out.println("[AM W3][INFO] user creds not found");
			try {
				extContext.redirect(extContext.getRequestContextPath() + "/login.html?faces-redirect=true");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
