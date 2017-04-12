package com.noxwizard.jcraftar;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Stefano Zanini
 */
public class Management {

	private static Proxy PROXY = null;

	public static void setProxy(Proxy p) {
    	PROXY = p;
	}

	public static void unsetProxy() {
		PROXY = null;
	}

	private static String getObjectUrl(String objectType, String uuid) {
		return String.format("/api/%s/%s/%s/", Settings.MANAGEMENT_API_VERSION, objectType, uuid);
	}

	//<editor-fold desc="Collections management" defaultstate="collapsed">
	/**
	 * Returns the number of collections matching the filtering criterias.
	 *
	 * @param apiKey your API key
	 * @param filter filtering criterias for reducing the result set; available criterias are <i>name</i> and
	 * <i>name__contains</i>
	 * @return the number of collections matching the filtering criterias
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static int getCollectionsCount(String apiKey, JSONObject filter)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.countObjectsInList(apiKey, "collection", filter, PROXY);
	}

	/**
	 * Return a list of collections matching the filtering criterias, paginated by limit and offset
	 *
	 * @param apiKey your API key
	 * @param limit the maximum number of items in the result, for pagination purpose
	 * @param offset the starting item, for pagination purpose
	 * @param filter filtering criterias for reducing the result set; available criterias are <i>name</i> and
	 * <i>name__contains</i>
	 * @return a JSON array with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONArray getCollectionList(String apiKey, int limit, int offset, JSONObject filter)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.getObjectList(apiKey, "collection", limit, offset, filter, PROXY);
	}

	/**
	 * Return a collection, identified by uuid
	 *
	 * @param apiKey your API key
	 * @param uuid the collection uuid
	 * @return a JSON object with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONObject getCollection(String apiKey, String uuid)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.getObject(apiKey, "collection", uuid, PROXY);
	}

	/**
	 * Create a collection with a given name (must be unique)
	 *
	 * @param apiKey your API key
	 * @param name the name of the collection that has to be created
	 * @param offline whether the collection will be available on-device or on the cloud
	 * @return a JSON object with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONObject createCollection(String apiKey, String name, boolean offline)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();
		data.put("name", name);
		data.put("offline", offline);
		return Commons.createObject(apiKey, "collection", data, PROXY);
	}

	/**
	 * Update the collection name, identified by uuid
	 *
	 * @param apiKey your API key
	 * @param uuid the collection uuid
	 * @param name the new name of the collection
	 * @return a JSON object with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONObject updateCollection(String apiKey, String uuid, String name)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		JSONObject data;
		if (name != null) {
			data = new JSONObject();
			data.put("name", name);
		} else {
			data = null;
		}
		return Commons.updateObject(apiKey, "collection", uuid, data, PROXY);
	}

	/**
	 * Delete a collection, identified by uuid
	 *
	 * @param apiKey your API key
	 * @param uuid the collection uuid
	 * @return a JSON object with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONObject deleteCollection(String apiKey, String uuid) throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.deleteObject(apiKey, "collection", uuid, PROXY);
	}
	//</editor-fold>

	//<editor-fold desc="Items management" defaultstate="collapsed">
	/**
	 * Returns the number of items matching the filtering criterias.
	 *
	 * @param apiKey your API key
	 * @param filter filtering criterias for reducing the result set; available criterias are <i>name</i>,
	 * <i>name__contains</i>, <i>collection__uuid</i>, <i>collection__name</i> and <i>collection__name__contains</i>
	 * @return the number of items matching the filtering criterias
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static int getItemsCount(String apiKey, JSONObject filter)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.countObjectsInList(apiKey, "item", filter, PROXY);
	}

	/**
	 * Return a list of items, paginated by limit and offset and filter by collection
	 *
	 * @param apiKey your API key
	 * @param limit the maximum number of items in the result, for pagination purpose
	 * @param offset the starting item, for pagination purpose
	 * @param filter filtering criterias for reducing the result set; available criterias are <i>name</i>,
	 * <i>name__contains</i>, <i>collection__uuid</i>, <i>collection__name</i> and <i>collection__name__contains</i>
	 * @return a JSON array with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONArray getItemList(String apiKey, int limit, int offset, JSONObject filter)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.getObjectList(apiKey, "item", limit, offset, filter, PROXY);
	}

	/**
	 * Return an item, identified by uuid
	 *
	 * @param apiKey your API key
	 * @param uuid the item uuid
	 * @return a JSON object with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONObject getItem(String apiKey, String uuid)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.getObject(apiKey, "item", uuid, PROXY);
	}

	/**
	 * Create a collection with a given name, belonging to @collection. The fields url, custom, trackable and content
	 * are optional.
	 *
	 * @param apiKey your API key
	 * @param collection the collection the new item should be added to
	 * @param name the new item name
	 * @param url the new item url
	 * @param custom new item custom data
	 * @param trackable new item trackable data
	 * @param content new item content
	 * @param tags new item tags
	 * @return a JSON object with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONObject createItem(
			String apiKey,
			String collection,
			String name,
			String url,
			String custom,
			String trackable,
			String content,
			JSONArray tags) throws CatchoomException, IOException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();
		data.put("collection", getObjectUrl("collection", collection));
		data.put("name", name);
		if (url != null) {
			data.put("url", url);
		}
		if (custom != null) {
			data.put("custom", custom);
		}
		if (trackable != null) {
			data.put("trackable", trackable);
		}
		if (content != null) {
			data.put("content", content);
		}
		if (tags != null) {
			JSONArray parsedTags = new JSONArray();
			for (Object tag : tags) {
				parsedTags.put(getObjectUrl("tag", tag.toString()));
			}
			data.put("tags", parsedTags);
		}
		return Commons.createObject(apiKey, "item", data, PROXY);
	}

	/**
	 * Update an item, identified by uuid"
	 *
	 * @param apiKey your API key
	 * @param uuid the item uuid
	 * @param name the item new name
	 * @param url the item new url
	 * @param custom item new custom data
	 * @param trackable item new trackable data
	 * @param content item new content
	 * @param tags item new tags
	 * @return a JSON object with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONObject updateItem(
			String apiKey,
			String uuid,
			String name,
			String url,
			String custom,
			String trackable,
			String content,
			JSONArray tags) throws CatchoomException, IOException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();
		if (name != null) {
			data.put("name", name);
		}
		if (custom != null) {
			data.put("custom", custom);
		}
		if (trackable != null) {
			data.put("trackable", trackable);
		}
		if (content != null) {
			data.put("content", content);
		}
		if (tags != null) {
			JSONArray parsedTags = new JSONArray();
			for (Object tag : tags) {
				parsedTags.put(getObjectUrl("tag", tag.toString()));
			}
			data.put("tags", parsedTags);
		}
		return Commons.updateObject(apiKey, "item", uuid, data, PROXY);
	}

	/**
	 * Delete an item, identified by uuid
	 *
	 * @param apiKey your API key
	 * @param uuid the item uuid
	 * @return a JSON object with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONObject deleteItem(String apiKey, String uuid)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.deleteObject(apiKey, "item", uuid, PROXY);
	}
	//</editor-fold>

	//<editor-fold desc="Images management" defaultstate="collapsed">
	/**
	 * Returns the number of images matching the filtering criterias.
	 *
	 * @param apiKey your API key
	 * @param filter filtering criterias for reducing the result set; available criterias are <i>name</i>,
	 * <i>name__contains</i>, <i>item__uuid</i>, <i>item__name</i>, <i>item__name__contains</i>,
	 * <i>item__collection__uuid</i>, <i>item__collection__name</i>, <i>item__collection__name__contains</i>
	 * and <i>status</i> ('ER' or 'OK')
	 * @return the number of images matching the filtering criterias
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static int getImagesCount(String apiKey, JSONObject filter)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.countObjectsInList(apiKey, "image", filter, PROXY);
	}

	/**
	 * Return a list of images, paginated by limit and offset and filtered by item"
	 *
	 * @param apiKey your API key
	 * @param limit the maximum number of items in the result, for pagination purpose
	 * @param offset the starting item, for pagination purpose
	 * @param filter filtering criterias for reducing the result set; available criterias are <i>name</i>,
	 * <i>name__contains</i>, <i>item__uuid</i>, <i>item__name</i>, <i>item__name__contains</i>,
	 * <i>item__collection__uuid</i>, <i>item__collection__name</i>, <i>item__collection__name__contains</i>
	 * and <i>status</i> ('ER' or 'OK')
	 * @return a JSON array with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONArray getImageList(String apiKey, int limit, int offset, JSONObject filter)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.getObjectList(apiKey, "image", limit, offset, filter, PROXY);
	}

	/**
	 * Return an image, identified by uuid
	 *
	 * @param apiKey your API key
	 * @param uuid the image uuid
	 * @return a JSON object with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONObject getImage(String apiKey, String uuid)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.getObject(apiKey, "image", uuid, PROXY);
	}

	/**
	 * Create an image from a filename, belongs to item
	 *
	 * @param apiKey your API key
	 * @param item the item the new image should be added to
	 * @param filename the path to the image file
	 * @return a JSON object with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONObject createImage(String apiKey, String item, String filename)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		File file = new File(filename);
		JSONObject data = new JSONObject();
		data.put("item", getObjectUrl("item", item));
		return Commons.createObjectMultipart(apiKey, "image", file, data, PROXY);
	}

	/**
	 * Update the image file, identified by uuid
	 *
	 * @param apiKey your API key
	 * @param uuid the image uuid
	 * @param filename the path to the new image file
	 * @return a JSON object with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONObject updateImage(String apiKey, String uuid, String filename)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		File file = new File(filename);
		return Commons.updateObjectMultipart(apiKey, "image", uuid, file, null, PROXY);
	}

	/**
	 * Delete an image, identified by uuid
	 *
	 * @param apiKey your API key
	 * @param uuid the image uuid
	 * @return a JSON object with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONObject deleteImage(String apiKey, String uuid)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.deleteObject(apiKey, "image", uuid, PROXY);
	}
	//</editor-fold>

	//<editor-fold desc="Tokens management" defaultstate="collapsed">
	/**
	 * Returns the number of tokens matching the filtering criterias.
	 *
	 * @param apiKey your API key
	 * @param filter filtering criterias for reducing the result set; available criterias are <i>collection__uuid</i>,
	 * <i>collection__name</i> and <i>collection__name__contains</i>
	 * @return the number of tokens matching the filtering criterias
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static int getTokensCount(String apiKey, JSONObject filter)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.countObjectsInList(apiKey, "token", filter, PROXY);
	}

	/**
	 * Return a list of tokens, paginated by limit and offset and filtered by collection
	 *
	 * @param apiKey your API key
	 * @param limit the maximum number of items in the result, for pagination purpose
	 * @param offset the starting item, for pagination purpose
	 * @param filter filtering criterias for reducing the result set; available criterias are <i>collection__uuid</i>,
	 * <i>collection__name</i> and <i>collection__name__contains</i>
	 * @return a JSON array with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONArray getTokenList(String apiKey, int limit, int offset, JSONObject filter)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.getObjectList(apiKey, "token", limit, offset, filter, PROXY);
	}

	/**
	 * Create a token, belongs to collection
	 *
	 * @param apiKey your API key
	 * @param collection the collection the token should be added to
	 * @param tags the token tags
	 * @return a JSON object with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONObject createToken(String apiKey, String collection, JSONArray tags)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();
		data.put("collection", getObjectUrl("collection", collection));
		if (tags != null) {
			JSONArray parsedTags = new JSONArray();
			for (Object tag : tags) {
				parsedTags.put(getObjectUrl("tag", tag.toString()));
			}
			data.put("tags", parsedTags);
		}
		return Commons.createObject(apiKey, "token", data, PROXY);
	}

	/**
	 * Update the tags for a given token
	 *
	 * @param apiKey your API key
	 * @param uuid the token uuid
	 * @param tags the token new tags
	 * @return a JSON object with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONObject updateToken(String apiKey, String uuid, JSONArray tags)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		JSONObject data;
		if (tags != null) {
			data = new JSONObject();
			JSONArray parsedTags = new JSONArray();
			for (Object tag : tags) {
				parsedTags.put(getObjectUrl("tag", tag.toString()));
			}
			data.put("tags", parsedTags);
		} else {
			data = null;
		}
		return Commons.updateObject(apiKey, "token", uuid, data, PROXY);
	}

	/**
	 * Delete a token, identified by @uuid
	 *
	 * @param apiKey your API key
	 * @param uuid the token uuid
	 * @return a JSON object with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONObject deleteToken(String apiKey, String uuid)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.deleteObject(apiKey, "token", uuid, PROXY);
	}
	//</editor-fold>

	//<editor-fold desc="Media objects management" defaultstate="collapsed">
	/**
	 * Returns the number of media matching the filtering criterias.
	 *
	 * @param apiKey your API key
	 * @param filter filtering criterias for reducing the result set; available criterias are <i>name</i>,
	 * <i>name__contains</i>, <i>mimetype</i> and <i>mimetype__contains</i>
	 * @return the number of media matching the filtering criterias
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static int getMediaCount(String apiKey, JSONObject filter)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.countObjectsInList(apiKey, "media", filter, PROXY);
	}

	/**
	 * Return a list of media objects, paginated by limit and offset
	 *
	 * @param apiKey your API key
	 * @param limit the maximum number of items in the result, for pagination purpose
	 * @param offset the starting item, for pagination purpose
	 * @param filter filtering criterias for reducing the result set; available criterias are <i>name</i>,
	 * <i>name__contains</i>, <i>mimetype</i> and <i>mimetype__contains</i>
	 * @return a JSON array with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONArray getMediaList(String apiKey, int limit, int offset, JSONObject filter)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.getObjectList(apiKey, "media", limit, offset, filter, PROXY);
	}

	/**
	 * Return a media object, identified by uuid
	 *
	 * @param apiKey your API key
	 * @param uuid the media uuid
	 * @return a JSON object with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONObject getMedia(String apiKey, String uuid)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.getObject(apiKey, "media", uuid, PROXY);
	}

	/**
	 * Create a media object from a filename, belongs to item
	 *
	 * @param apiKey your API key
	 * @param filename the path to the file
	 * @return a JSON object with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONObject createMedia(String apiKey, String filename)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		File file = new File(filename);
		return Commons.createObjectMultipart(apiKey, "media", file, null, PROXY);
	}

	/**
	 * Create a video media
	 *
	 * @param apiKey your API key
	 * @param url the video url
	 * @param name the video name
	 * @return a JSON object with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONObject createVideoMedia(String apiKey, String url, String name)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		String videoName;
		if (name != null) {
			videoName = name;
		} else {
			String[] splitUrl = url.split("/");
			videoName = splitUrl[splitUrl.length - 1];
		}
		JSONObject data = new JSONObject();
		data.put("mimetype", "video");
		data.put("name", videoName);
		JSONObject meta = new JSONObject();
		meta.put("video-url", url);
		data.put("meta", meta);
		return Commons.createObject(apiKey, "media", data, PROXY);
	}

	/**
	 * Delete an image, identified by uuid
	 *
	 * @param apiKey your API key
	 * @param uuid the media uuid
	 * @return a JSON object with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONObject deleteMedia(String apiKey, String uuid)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.deleteObject(apiKey, "media", uuid, PROXY);
	}
	//</editor-fold>

	//<editor-fold desc="Tags management" defaultstate="collapsed">
	/**
	 * Returns the number of tags matching the filtering criterias.
	 *
	 * @param apiKey your API key
	 * @param filter filtering criterias for reducing the result set; available criterias are <i>name</i> and
	 * <i>name__contains</i>
	 * @return the number of tags matching the filtering criterias
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static int getTagsCount(String apiKey, JSONObject filter)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.countObjectsInList(apiKey, "tag", filter, PROXY);
	}

	/**
	 * Return a list of tags, paginated by limit and offset and filtered by collection
	 *
	 * @param apiKey your API key
	 * @param limit the maximum number of items in the result, for pagination purpose
	 * @param offset the starting item, for pagination purpose
	 * @param filter filtering criterias for reducing the result set; available criterias are <i>name</i> and
	 * <i>name__contains</i>
	 * @return a JSON array with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONArray getTagList(String apiKey, int limit, int offset, JSONObject filter)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.getObjectList(apiKey, "tag", limit, offset, filter, PROXY);
	}

	/**
	 * Return an item, identified by uuid
	 *
	 * @param apiKey your API key
	 * @param uuid the tag uuid
	 * @return a JSON object with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONObject getTag(String apiKey, String uuid)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.getObject(apiKey, "tag", uuid, PROXY);
	}

	/**
	 * Create a tag with a given name, belonging to collection
	 *
	 * @param apiKey your API key
	 * @param collection the collection the new tag should be added to
	 * @param name the tag name
	 * @return a JSON object with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONObject createTag(String apiKey, String collection, String name)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();
		data.put("collection", getObjectUrl("collection", collection));
		data.put("name", name);
		return Commons.createObject(apiKey, "tag", data, PROXY);
	}

	/**
	 * Delete a tag, identified by uuid
	 *
	 * @param apiKey your API key
	 * @param uuid the tag uuid
	 * @return a JSON object with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONObject deleteTag(String apiKey, String uuid)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.deleteObject(apiKey, "tag", uuid, PROXY);
	}
	//</editor-fold>

	//<editor-fold desc="Applications management" defaultstate="collapsed">
	/**
	 * Returns the number of apps matching the filtering criterias.
	 *
	 * @param apiKey your API key
	 * @return the number of apps matching the filtering criterias
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static int getAppsCount(String apiKey)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.countObjectsInList(apiKey, "app", null, PROXY);
	}

	/**
	 * Return a list of applications, paginated by limit and offset and filtered by collection
	 *
	 * @param apiKey your API key
	 * @param limit the maximum number of items in the result, for pagination purpose
	 * @param offset the starting item, for pagination purpose
	 * @return a JSON array with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONArray getAppList(String apiKey, int limit, int offset)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.getObjectList(apiKey, "app", limit, offset, null, PROXY);
	}

	/**
	 * Return an application, identified by uuid
	 *
	 * @param apiKey your API key
	 * @param uuid the app uuid
	 * @return a JSON object with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONObject getApp(String apiKey, String uuid)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.getObject(apiKey, "app", uuid, PROXY);
	}

	/**
	 * Create an application with a given name, belonging to collection
	 *
	 * @param apiKey your API key
	 * @param collection the collection the new app should be added to
	 * @param name the new app name
	 * @return a JSON object with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONObject createApp(String apiKey, String collection, String name)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();
		data.put("name", name);
		data.put("collection", getObjectUrl("collection", collection));
		return Commons.createObject(apiKey, "app", data, PROXY);
	}

	/**
	 * Delete an application, identified by uuid
	 *
	 * @param apiKey your API key
	 * @param uuid the app uuid
	 * @return a JSON object with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONObject deleteApp(String apiKey, String uuid)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.deleteObject(apiKey, "app", uuid, PROXY);
	}
	//</editor-fold>

	//<editor-fold desc="SDK versions management" defaultstate="collapsed">
	/**
	 * Returns the number of versions matching the filtering criterias.
	 *
	 * @param apiKey your API key
	 * @param filter filtering criterias for reducing the result set; available criterias are <i>sdk_name</i> and
	 * <i>sdk_version</i>
	 * @return the number of versions matching the filtering criterias
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static int getVersionsCount(String apiKey, JSONObject filter)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.countObjectsInList(apiKey, "version", filter, PROXY);
	}

	/**
	 * Return a list of SDK versions, paginated by limit and offset
	 *
	 * @param apiKey your API key
	 * @param limit the maximum number of items in the result, for pagination purpose
	 * @param offset the starting item, for pagination purpose
	 * @param filter filtering criterias for reducing the result set; available criterias are <i>sdk_name</i> and
	 * <i>sdk_version</i>
	 * @return a JSON array with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONArray getVersionList(String apiKey, int limit, int offset, JSONObject filter)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.getObjectList(apiKey, "version", limit, offset, filter, PROXY);
	}

	/**
	 * Return an SDK Version, identified by uuid
	 *
	 * @param apiKey your API key
	 * @param uuid the version uuid
	 * @return a JSON object with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONObject getVersion(String apiKey, String uuid)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.getObject(apiKey, "version", uuid, PROXY);
	}
	//</editor-fold>

	//<editor-fold desc="Collection bundles management" defaultstate="collapsed">
	/**
	 * Returns the number of bundles matching the filtering criterias.
	 *
	 * @param apiKey your API key
	 * @param filter filtering criterias for reducing the result set; available criterias are <i>tag__name</i> and
	 * <i>app__name</i>
	 * @return the number of bundles matching the filtering criterias
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static int getBundlesCount(String apiKey, JSONObject filter)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.countObjectsInList(apiKey, "collectionbundle", filter, PROXY);
	}

	/**
	 * Return a list of bundles, paginated by limit and offset
	 *
	 * @param apiKey your API key
	 * @param limit the maximum number of items in the result, for pagination purpose
	 * @param offset the starting item, for pagination purpose
	 * @param filter filtering criterias for reducing the result set; available criterias are <i>tag__name</i> and
	 * <i>app__name</i>
	 * @return a JSON array with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONArray getBundleList(String apiKey, int limit, int offset, JSONObject filter)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.getObjectList(apiKey, "collectionbundle", limit, offset, filter, PROXY);
	}

	/**
	 * Return a bundle, identified by uuid
	 *
	 * @param apiKey your API key
	 * @param uuid the bundle uuid
	 * @return a JSON object with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONObject getBundle(String apiKey, String uuid)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.getObject(apiKey, "collectionbundle", uuid, PROXY);
	}

	/**
	 * Create a bundle
	 *
	 * @param apiKey your API key
	 * @param collection yet unknown
	 * @param app yet unknown
	 * @param version yet unknown
	 * @param tag yet unknown
	 * @return a JSON object with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONObject createBundle(String apiKey, String collection, String app, String version, String tag)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();
		data.put("collection", getObjectUrl("collection", collection));
		data.put("version", getObjectUrl("version", version));
		data.put("app", getObjectUrl("app", app));
		if (tag != null) {
			data.put("tag", getObjectUrl("tag", tag));
		}
		return Commons.createObject(apiKey, "collectionbundle", data, PROXY);
	}

	/**
	 * Delete a bundle, identified by uuid
	 *
	 * @param apiKey your API key
	 * @param uuid the bundle uuid
	 * @return a JSON object with the server response
	 * @throws CatchoomException if the parameters are incorrect or the server response is not valid
	 * @throws IOException if something goes wrong in the interaction with the server
	 * @throws java.security.NoSuchAlgorithmException if TLS 1.2 is not available
	 */
	public static JSONObject deleteBundle(String apiKey, String uuid)
			throws CatchoomException, IOException, NoSuchAlgorithmException {
		return Commons.deleteObject(apiKey, "collectionbundle", uuid, PROXY);
	}
	//</editor-fold>
}
