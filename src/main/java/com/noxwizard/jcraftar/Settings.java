package com.noxwizard.jcraftar;

/**
 *
 * @author Stefano Zanini
 */
public class Settings {

    static String RECOGNITION_HOSTNAME = "https://search.craftar.net";
    static String MANAGEMENT_HOSTNAME = "https://my.craftar.net";

    static String RECOGNITION_API_VERSION = "v2";
    static String MANAGEMENT_API_VERSION = "v0";

    static String USER_AGENT = String.format("CraftAR/1.3.3 (Java %s)", System.getProperty("java.version"));

    public static int DEFAULT_QUERY_MIN_SIZE = 240; //default image transformation parameters

    static String[] ALLOWED_IMG_EXTENSIONS = {".jpg", ".jpeg", ".png", ".JPG", ".JPEG", ".PNG"};
    static String[] ALLOWED_OBJECT_TYPES = {"collection", "item", "image", "token", "media", "tag", "version",
        "collectionbundle", "app"};
}
