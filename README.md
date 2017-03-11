# JCraftAR
CraftAR Java library

## Description

The Catchoom's CraftAR Service for [Augmented Reality and Image Recognition](http://craftar.com/product/craftar/augmented-reality-and-image-recognition/) is a  service
that allows you to build a wide range of __Image Recognition__ and __Augmented Reality__ applications
and services.

This client library provides access to CraftAR API:

- [CraftAR Management API]
(http://craftar.com/documentation/api/management/)
  allows upload and management of _collections_ of _reference images_, and associated meta-data such as _Augmented Reality experiences_ and their content.
  All requests must be authenticated using your _Management API key_.
- [CraftAR Image Recognition API]
(https://catchoom.com/documentation/image-recognition-api/)
  allows image recognition against one of your collections of reference images specified using the collection token.

## Dependencies

JCraftAR uses the following libraries:

- [JSON-java](https://github.com/stleary/JSON-java)
- [Apache HttpComponents](https://hc.apache.org/)
- [JUnit](http://junit.org/junit4/)

Minimum required Java version is 1.7

## Build

JCraftAR is build using [Apache Maven](http://maven.apache.org)

Running the following command will build two jars of JCraftAR, one including dependencies and one without.

    mvn clean install

## Quick Start

Follow these steps to create a collection with one item and perform an image recognition request against that collection.
A collection is a set of items representing entities that you want to recognize. Examples of items are logos, physical objects,
or a drawings, among others.

1. Get your [management api_key](https://my.craftar.net/accounts/login/?next=/api_access/). This is needed to authenticate your requests to the Management API.

    ```java
    //Use your own API key
    String apiKey = "your_key";
    ```

2. Create a _collection_

    ```java
    String collectionName = "My collection";
    JSONObject collection = Management.createCollection(apiKey, collectionName, true);
    ```

3. Store the newly created _collection_'s _token_, since it will be needed later for recognition

    ```java
    String collectionUuid = collection.getString("uuid");
    JSONObject filter = new JSONObject();
    filter.put("collection__uuid", collectionUuid);
	JSONArray tokensList = Management.getTokenList(apiKey, -1, -1, filter);
    String collectionToken = tokensList.getJSONObject(0).getString("token");
    ```

4. Create an _item_ in your _collection_

    ```java
    String name = "My item";
    String url = "http://example.com";
    String custom = "This is my custom data";
    JSONObject item = Management.createItem(apiKey, collectionUuid, name, url, custom, null, null, null);
    ```

5. Upload an _image_ representing your _item:

    - Every item can be represented by one or more reference images. This is useful for items that have
    different faces, e.g. cereal boxes.

    - Before performing a successful recognition, the corresponding reference image needs to be fully indexed
    by the server. Normally it takes less than one second after uploading.

    ```java
    String itemUuid = item.getString("uuid");
    String imagePath = "My image";
    JSONObject image = Management.createImage(apiKey, itemUuid, imagePath);
    ```

6. Now you can perform the image recognition request against your collection, which you reference with the token stored in step 3

    ```java
    String queryImage = "My query image";
    JSONObject result = Recognition.search(collectionToken, queryImage);
    System.out.println(result.toString());
    ```
