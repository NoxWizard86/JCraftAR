package com.noxwizard.jcraftar;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Stefano Zanini
 */
class Commons {

	//Forces the usage of TLS version 1.2, because Java 7 defaults to version 1 but the server won't accept that
	private static SSLConnectionSocketFactory getSSLContext() throws NoSuchAlgorithmException {
		return new SSLConnectionSocketFactory(
				SSLContext.getDefault(),
				new String[]{"TLSv1.2"},
				null,
				new NoopHostnameVerifier());
	}

	private static void validate(String objectType, JSONObject data, String uuid) throws CatchoomException {
		boolean valid = true;
		//Check object type validity
		if (objectType != null) {
			boolean validObjectType = false;
			for (String allowedObjectType : Settings.ALLOWED_OBJECT_TYPES) {
				if (allowedObjectType.equals(objectType)) {
					validObjectType = true;
				}
			}
			valid = valid && validObjectType;
		}
		if (!valid) {
			CatchoomException ex = new CatchoomException(String.format("Wrong object_type: %s", objectType));
			throw ex;
		}
		//Check data validity
		if (data != null) {
			valid = valid && data.keySet().size() > 0;
		}
		if (!valid) {
			CatchoomException ex = new CatchoomException("Empty data");
			throw ex;
		}
		//Check uuid validity
		if (uuid != null) {
			if (objectType != null && objectType.equals("token")) {
				valid = valid && uuid.matches("[0-9a-f]{16}");
			} else {
				valid = valid && uuid.matches("[0-9a-f]{32}");
			}
		}
		if (!valid) {
			CatchoomException ex = new CatchoomException(String.format("Wrong token: %s", uuid));
			throw ex;
		}
	}

	private static void validateResponse(String response) throws CatchoomException {
		try {
			JSONObject jResponse = new JSONObject(response);
			if (jResponse.has("error")) {
				String msg = jResponse.getJSONObject("error").getString("message");
				CatchoomException ex = new CatchoomException(msg);
				throw ex;
			}
		} catch (JSONException e) {
			CatchoomException ex = new CatchoomException(e.getMessage());
			throw ex;
		}
	}

	private static JSONObject parseObject(JSONObject object) {
		//Remove resource_uri
		if (object.has("resource_uri")) {
			object.remove("resource_uri");
		}
		//Parse collection, item, app and version: set uuid instead of api uri
		String[] keysToParse1 = {"collection", "item", "version", "app", "tag"};
		for (String key : keysToParse1) {
			if (object.has(key)) {
				String[] splitVal = object.getString(key).split("/");
				object.put(key, splitVal[splitVal.length - 2]);
			}
		}
		//Parse collections and tags
		String[] keysToParse2 = {"collections", "tags"};
		for (String key : keysToParse2) {
			if (object.has(key)) {
				JSONArray array = object.getJSONArray(key);
				for (int i = 0; i < array.length(); i++) {
					String[] splitVal = array.getString(i).split("/");
					array.put(i, splitVal[splitVal.length - 2]);
				}
			}
		}
		return object;
	}

	private static String getUrl(String apiKey, String objectType, String uuid, int limit, int offset, JSONObject filter)
			throws CatchoomException {
		validate(objectType, null, uuid);
		String resourceName = objectType;
		if (uuid != null) {
			resourceName += String.format("/%s", uuid);
		}

		String url = String.format("%s/api/%s/%s/?api_key=%s",
				Settings.MANAGEMENT_HOSTNAME,
				Settings.MANAGEMENT_API_VERSION,
				resourceName,
				apiKey);

		if (limit > 0) {
			url += String.format("&limit=%s", limit);
		}
		if (offset > 0) {
			url += String.format("&offset=%s", offset);
		}
		if (filter != null) {
			for (String key : filter.keySet()) {
				url += String.format("&%s=%s", key, filter.getString(key));
			}
		}
		return url;
	}

	static JSONArray getObjectList(String apiKey, String objectType, int limit, int offset, JSONObject filter, Proxy proxy)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		JSONArray objectList = new JSONArray();
		validate(objectType, null, null);
		String url = getUrl(apiKey, objectType, null, limit, offset, filter);

		HttpClientBuilder clientBuilder = HttpClients.custom().setSSLSocketFactory(getSSLContext());
		if (proxy != null) {
			clientBuilder.setProxy(proxy.getHttpProxy());
			if (proxy.isAuthenticated()) {
				clientBuilder.setDefaultCredentialsProvider(proxy.getCredentialsProvider());
			}
		}

		try (CloseableHttpClient client = clientBuilder.build()) {
			HttpGet request = new HttpGet(url);

			HttpResponse response = client.execute(request);
			String output;
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
				output = "";
				String line;
				while ((line = reader.readLine()) != null) {
					output += line;
				}
			}

			validateResponse(output);
			JSONObject jResponse = new JSONObject(output);
			for (int i = 0; i < jResponse.getJSONArray("objects").length(); i++) {
				JSONObject object = jResponse.getJSONArray("objects").getJSONObject(i);
				objectList.put(parseObject(object));
			}
		}
		return objectList;
	}

	static int countObjectsInList(String apiKey, String objectType, JSONObject filter, Proxy proxy)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		int count;

		validate(objectType, null, null);
		String url = getUrl(apiKey, objectType, null, 1, 0, filter);

		HttpClientBuilder clientBuilder = HttpClients.custom().setSSLSocketFactory(getSSLContext());
		if (proxy != null) {
			clientBuilder.setProxy(proxy.getHttpProxy());
			if (proxy.isAuthenticated()) {
				clientBuilder.setDefaultCredentialsProvider(proxy.getCredentialsProvider());
			}
		}

		try (CloseableHttpClient client = clientBuilder.build()) {
			HttpGet request = new HttpGet(url);

			HttpResponse response = client.execute(request);
			String output;
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
				output = "";
				String line;
				while ((line = reader.readLine()) != null) {
					output += line;
				}
			}

			validateResponse(output);
			JSONObject jResponse = new JSONObject(output);
			count = jResponse.getJSONObject("meta").getInt("total_count");
		}
		return count;
	}

	static JSONObject getObject(String apiKey, String objectType, String uuid, Proxy proxy)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		JSONObject jResponse;
		validate(objectType, null, uuid);
		String url = getUrl(apiKey, objectType, uuid, -1, -1, null);

		HttpClientBuilder clientBuilder = HttpClients.custom().setSSLSocketFactory(getSSLContext());
		if (proxy != null) {
			clientBuilder.setProxy(proxy.getHttpProxy());
			if (proxy.isAuthenticated()) {
				clientBuilder.setDefaultCredentialsProvider(proxy.getCredentialsProvider());
			}
		}

		try (CloseableHttpClient client = clientBuilder.build()) {
			HttpGet request = new HttpGet(url);

			HttpResponse response = client.execute(request);
			String output;
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
				output = "";
				String line;
				while ((line = reader.readLine()) != null) {
					output += line;
				}
			}

			validateResponse(output);
			jResponse = parseObject(new JSONObject(output));
		}
		return jResponse;
	}

	static JSONObject createObject(String apiKey, String objectType, JSONObject data, Proxy proxy)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		JSONObject jResponse;
		validate(objectType, data, null);
		String url = getUrl(apiKey, objectType, null, -1, -1, null);

		HttpClientBuilder clientBuilder = HttpClients.custom().setSSLSocketFactory(getSSLContext());
		if (proxy != null) {
			clientBuilder.setProxy(proxy.getHttpProxy());
			if (proxy.isAuthenticated()) {
				clientBuilder.setDefaultCredentialsProvider(proxy.getCredentialsProvider());
			}
		}

		try (CloseableHttpClient client = clientBuilder.build()) {
			HttpPost request = new HttpPost(url);
			Header[] headers = {
				new BasicHeader("User-Agent", Settings.USER_AGENT),
				new BasicHeader("content-type", "application/json")};
			request.setHeaders(headers);
			request.setEntity(new StringEntity(data.toString()));

			HttpResponse response = client.execute(request);
			String output;
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
				output = "";
				String line;
				while ((line = reader.readLine()) != null) {
					output += line;
				}
			}

			validateResponse(output);
			jResponse = parseObject(new JSONObject(output));
		}
		return jResponse;
	}

	static JSONObject createObjectMultipart(String apiKey, String objectType, File file, JSONObject data, Proxy proxy)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		JSONObject jResponse;
		validate(objectType, data, null);
		String url = getUrl(apiKey, objectType, null, -1, -1, null);

		HttpClientBuilder clientBuilder = HttpClients.custom().setSSLSocketFactory(getSSLContext());
		if (proxy != null) {
			clientBuilder.setProxy(proxy.getHttpProxy());
			if (proxy.isAuthenticated()) {
				clientBuilder.setDefaultCredentialsProvider(proxy.getCredentialsProvider());
			}
		}

		try (CloseableHttpClient client = clientBuilder.build()) {
			HttpPost request = new HttpPost(url);
			Header[] headers = {new BasicHeader("User-Agent", Settings.USER_AGENT)};
			request.setHeaders(headers);
			MultipartEntityBuilder mpeb = MultipartEntityBuilder.create().addBinaryBody("file", file);
			if (data != null) {
				for (String k : data.keySet()) {
					mpeb = mpeb.addTextBody(k, data.getString(k));
				}
			}
			HttpEntity entity = mpeb.build();
			request.setEntity(entity);

			HttpResponse response = client.execute(request);
			String output;
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
				output = "";
				String line;
				while ((line = reader.readLine()) != null) {
					output += line;
				}
			}

			validateResponse(output);
			jResponse = parseObject(new JSONObject(output));

		}
		return jResponse;
	}

	static JSONObject updateObject(String apiKey, String objectType, String uuid, JSONObject data, Proxy proxy)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		JSONObject jResponse = new JSONObject();
		validate(objectType, data, uuid);
		String url = getUrl(apiKey, objectType, uuid, -1, -1, null);

		HttpClientBuilder clientBuilder = HttpClients.custom().setSSLSocketFactory(getSSLContext());
		if (proxy != null) {
			clientBuilder.setProxy(proxy.getHttpProxy());
			if (proxy.isAuthenticated()) {
				clientBuilder.setDefaultCredentialsProvider(proxy.getCredentialsProvider());
			}
		}

		try (CloseableHttpClient client = clientBuilder.build()) {
			HttpPost request = new HttpPost(url);
			Header[] headers = {
				new BasicHeader("User-Agent", Settings.USER_AGENT),
				new BasicHeader("content-type", "application/json")};
			request.setHeaders(headers);
			request.setEntity(new StringEntity(data.toString()));

			HttpResponse response = client.execute(request);
			jResponse.put("statusCode", response.getStatusLine().getStatusCode());
			jResponse.put("message", response.getStatusLine().getReasonPhrase());
		}
		return jResponse;
	}

	static JSONObject updateObjectMultipart(String apiKey, String objectType, String uuid, File file, JSONObject data, Proxy proxy)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		JSONObject jResponse;
		validate(objectType, data, uuid);
		String url = getUrl(apiKey, objectType, uuid, -1, -1, null);

		HttpClientBuilder clientBuilder = HttpClients.custom().setSSLSocketFactory(getSSLContext());
		if (proxy != null) {
			clientBuilder.setProxy(proxy.getHttpProxy());
			if (proxy.isAuthenticated()) {
				clientBuilder.setDefaultCredentialsProvider(proxy.getCredentialsProvider());
			}
		}

		try (CloseableHttpClient client = clientBuilder.build()) {
			HttpPost request = new HttpPost(url);
			Header[] headers = {new BasicHeader("User-Agent", Settings.USER_AGENT)};
			request.setHeaders(headers);
			HttpEntity entity = MultipartEntityBuilder
					.create()
					.addBinaryBody("files", file)
					.addTextBody("data", data.toString())
					.build();
			request.setEntity(entity);

			HttpResponse response = client.execute(request);
			String output;
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
				output = "";
				String line;
				while ((line = reader.readLine()) != null) {
					output += line;
				}
			}

			validateResponse(output);
			jResponse = parseObject(new JSONObject(output));
		}
		return jResponse;
	}

	static JSONObject deleteObject(String apiKey, String objectType, String uuid, Proxy proxy)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		JSONObject jResponse = new JSONObject();
		validate(objectType, null, uuid);
		String url = getUrl(apiKey, objectType, uuid, -1, -1, null);

		HttpClientBuilder clientBuilder = HttpClients.custom().setSSLSocketFactory(getSSLContext());
		if (proxy != null) {
			clientBuilder.setProxy(proxy.getHttpProxy());
			if (proxy.isAuthenticated()) {
				clientBuilder.setDefaultCredentialsProvider(proxy.getCredentialsProvider());
			}
		}

		try (CloseableHttpClient client = clientBuilder.build()) {
			HttpDelete request = new HttpDelete(url);
			HttpResponse response = client.execute(request);

			jResponse.put("statusCode", response.getStatusLine().getStatusCode());
			jResponse.put("message", response.getStatusLine().getReasonPhrase());
		}
		return jResponse;
	}
}
