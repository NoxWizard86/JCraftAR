package com.noxwizard.jcraftar;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;

/**
 *
 * @author Stefano Zanini
 */
public class Proxy {

	private final String host;
	private final int port;
	private final String user;
	private final String password;

	public Proxy(String host, int port, String user, String password) {
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
	}

	public Proxy(String host, int port) {
		this.host = host;
		this.port = port;
		this.user = null;
		this.password = null;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public HttpHost getHttpProxy() {
		HttpHost proxy = new HttpHost(host, port);
		return proxy;
	}

	public boolean isAuthenticated() {
		return user != null || password != null;
	}

	public CredentialsProvider getCredentialsProvider() {
		CredentialsProvider creds;
		if (isAuthenticated()) {
			creds = new BasicCredentialsProvider();
			creds.setCredentials(new AuthScope(host, port), new UsernamePasswordCredentials(user, password));
		} else {
			creds = null;
		}
		return creds;
	}
}