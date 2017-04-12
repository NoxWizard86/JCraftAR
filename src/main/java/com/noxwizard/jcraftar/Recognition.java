package com.noxwizard.jcraftar;

import com.noxwizard.jcraftar.datapassing.SearchParameters;
import com.noxwizard.jcraftar.datapassing.SyncParameters;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Stefano Zanini
 */
public class Recognition {

	private static Proxy PROXY;

	public static void setProxy(Proxy p) {
		PROXY = p;
	}

	public static void unsetProxy() {
		PROXY = null;
	}

	/**
	 * Performs the search request and returns the JSON Catchoom answers with
	 *
	 * @param token Catchoom collection token
	 * @param fileName the path to the image to search
	 * @return a JSON object containing the results of the search
	 * @throws IOException if the file can't be found
	 */
	public static JSONObject search(String token, String fileName) throws IOException {
		return search(token, fileName, new SearchParameters());
	}

	/**
	 * Performs the search request and returns the JSON Catchoom answers with
	 *
	 * @param token Catchoom collection token
	 * @param fileName the path to the image to search
	 * @param optionals optional parameters for Catchoom search
	 * @return a JSON object containing the results of the search
	 * @throws IOException if the file can't be found
	 */
	public static JSONObject search(String token, String fileName, SearchParameters optionals) throws IOException {
		//Since the server won't accept an InputStream, the original content of the file has to be stored so that changes 
		//can be reverted later on
		byte[] originalData = saveFileContent(fileName);

		File image = prepareImage(fileName, optionals.isColor(), optionals.getMinSize());
		JSONObject searchResult = new JSONObject();
		try {
			//Create the httpClient
			HttpClientBuilder clientBuilder = HttpClientBuilder.create();
			if (PROXY != null) {
				clientBuilder.setProxy(PROXY.getHttpProxy());
				if (PROXY.isAuthenticated()) {
					clientBuilder.setDefaultCredentialsProvider(PROXY.getCredentialsProvider());
				}
			}
			HttpClient httpClient = clientBuilder.build();

			//Create the request and set the headers
			String url = String.format("%s/%s/search", Settings.RECOGNITION_HOSTNAME, Settings.RECOGNITION_API_VERSION);
			HttpPost request = new HttpPost(url);
			request.setHeader("User-Agent", Settings.USER_AGENT);

			//Set request data
			MultipartEntityBuilder entityBuilder = MultipartEntityBuilder
					.create()
					.addBinaryBody("image", image)
					.addTextBody("token", token);
			JSONObject jOptionals = optionals.getOptionalsAsJSON();
			for (String k : jOptionals.keySet()) {
				entityBuilder.addTextBody(k, jOptionals.getString(k));
			}
			request.setEntity(entityBuilder.build());

			//Perform the request and read the response
			HttpResponse response = httpClient.execute(request);
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String output = "";
			String line;
			while ((line = reader.readLine()) != null) {
				output += line;
			}

			//Parse the response into a JSON object
			searchResult = new JSONObject(output);
		} catch (JSONException | IOException e) {
			System.err.println(e.getMessage());
		} finally {
			//Restore the orginal data on the file
			try (OutputStream os = new BufferedOutputStream(new FileOutputStream(fileName))) {
				os.write(originalData);
			}
		}
		return searchResult;
	}

	/**
	 * Synchronizes the on-device bundle and returns the JSON Catchoom answers with
	 *
	 * @param token Catchoom collection token
	 * @param appID application ID
	 * @param version target SDK version
	 * @return a JSON object containing the results of the synchronization
	 */
	public static JSONObject sync(String token, int appID, String version) {
		return sync(token, appID, version, new SyncParameters());
	}

	/**
	 * Synchronizes the on-device bundle and returns the JSON Catchoom answers with
	 *
	 * @param token Catchoom collection token
	 * @param appID application ID
	 * @param version target SDK version
	 * @param optionals optional parameters for Catchoom synchronization
	 * @return a JSON object containing the results of the synchronization
	 */
	public static JSONObject sync(String token, int appID, String version, SyncParameters optionals) {
		JSONObject syncResult = new JSONObject();
		try {
			//Create the httpClient
			HttpClient httpClient = HttpClientBuilder.create().build();

			//Create the request and set the headers
			String url = String.format("%s/%s/sync", Settings.RECOGNITION_HOSTNAME, Settings.RECOGNITION_API_VERSION);
			HttpPost request = new HttpPost(url);
			request.setHeader("User-Agent", Settings.USER_AGENT);

			//Set request data
			ArrayList<NameValuePair> postParameters = new ArrayList<>();
			postParameters.add(new BasicNameValuePair("token", token));
			postParameters.add(new BasicNameValuePair("app_id", appID + ""));
			postParameters.add(new BasicNameValuePair("version", version));
			JSONObject jOptionals = optionals.getOptionalsAsJSON();
			for (String k : jOptionals.keySet()) {
				postParameters.add(new BasicNameValuePair(k, jOptionals.getString(k)));
			}
			request.setEntity(new UrlEncodedFormEntity(postParameters));

			//Perform the request and read the response
			HttpResponse response = httpClient.execute(request);
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String output = "";
			String line;
			while ((line = reader.readLine()) != null) {
				output += line;
			}

			//Parse the response into a JSON object
			syncResult = new JSONObject(output);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return syncResult;
	}

	private static byte[] saveFileContent(String fileName) throws IOException {
		File originalFile = new File(fileName);
		byte[] originalData = new byte[(int) originalFile.length()];
		int totalBytesRead = 0;
		try (InputStream is = new BufferedInputStream(new FileInputStream(originalFile))) {
			while (totalBytesRead < originalData.length) {
				int bytesRemaining = originalData.length - totalBytesRead;
				//input.read() returns -1, 0, or more :
				int bytesRead = is.read(originalData, totalBytesRead, bytesRemaining);
				if (bytesRead > 0) {
					totalBytesRead = totalBytesRead + bytesRead;
				}
			}
		}
		return originalData;
	}

	private static File prepareImage(String imagePath, boolean color, int minSize) throws IOException {
		BufferedImage input = ImageIO.read(new File(imagePath));
		//Convert to b/w if required
		if (!color) {
			int width = input.getWidth();
			int height = input.getHeight();
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					int p = input.getRGB(x, y);
					int a = (p >> 24) & 0xff;
					int r = (p >> 16) & 0xff;
					int g = (p >> 8) & 0xff;
					int b = p & 0xff;
					int avg = (r + g + b) / 3;
					p = (a << 24) | (avg << 16) | (avg << 8) | avg;
					input.setRGB(x, y, p);
				}
			}
		}
		//Resize if required
		if (minSize >= 0) {
			int width = input.getWidth();
			int height = input.getHeight();
			int smallestImageSize = Math.min(width, height);
			float scaleFactor = (float) minSize / smallestImageSize;
			int newWidth = Math.round(width * scaleFactor);
			int newHeight = Math.round(height * scaleFactor);

			Image tmp = input.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
			input = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = input.createGraphics();
			g2d.drawImage(tmp, 0, 0, null);
			g2d.dispose();
		}
		//Write final result to output
		File output = new File(imagePath);
		ImageIO.write(input, "jpg", output);
		return output;
	}
}
