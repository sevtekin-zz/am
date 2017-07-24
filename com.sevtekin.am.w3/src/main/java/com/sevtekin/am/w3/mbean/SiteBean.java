package com.sevtekin.am.w3.mbean;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.sevtekin.am.client.AMClient;

public class SiteBean implements Serializable {

	private static final long serialVersionUID = 1L;
	AMClient client = new AMClient();
	String user = "Unknown";

	public SiteBean() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext extContext = context.getExternalContext();
		user = extContext.getRemoteUser();
	}

	@PostConstruct
	public void initialize() {
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
	    if(session != null){
	    	System.out.println("[AM W3][INFO] Session is still active");
	    	session.invalidate();
	    }else{
	    	System.out.println("[AM W3][INFO] Session is null");
	    }
	   
		try {
			extContext.redirect(extContext.getRequestContextPath()
					+ "/buildInfo.html?faces-redirect=true");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
