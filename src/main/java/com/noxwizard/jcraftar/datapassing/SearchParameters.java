package com.noxwizard.jcraftar.datapassing;

import com.noxwizard.jcraftar.Settings;
import org.json.JSONObject;

/**
 *
 * @author Stefano Zanini
 */
public class SearchParameters implements Parameters {

    private boolean color = false;
    private int minSize = Settings.DEFAULT_QUERY_MIN_SIZE;
    private boolean embedCustom = false;
    private boolean embedTracking = false;
    private boolean bbox = false;
    private String appID = null;
    private String strategy = Settings.DEFAULT_SEARCH_STRATEGY;
    private String version = Settings.RECOGNITION_API_VERSION;

    public boolean isColor() {
        return color;
    }

    public SearchParameters setColor(boolean color) {
        this.color = color;
        return this;
    }

    public int getMinSize() {
        return minSize;
    }

    public SearchParameters setMinSize(int minSize) {
        this.minSize = minSize;
        return this;
    }

    public boolean isEmbedCustom() {
        return embedCustom;
    }

    public SearchParameters setEmbedCustom(boolean embedCustom) {
        this.embedCustom = embedCustom;
        return this;
    }

    public boolean isEmbedTracking() {
        return embedTracking;
    }

    public SearchParameters setEmbedTracking(boolean embedTracking) {
        this.embedTracking = embedTracking;
        return this;
    }

    public boolean isBbox() {
        return bbox;
    }

    public SearchParameters setBbox(boolean bbox) {
        this.bbox = bbox;
        return this;
    }

    public String getAppID() {
        return appID;
    }

    public SearchParameters setAppID(String appID) {
        this.appID = appID;
        return this;
    }

    public String getStrategy() {
        return strategy;
    }

    public SearchParameters setStrategy(String strategy) {
        this.strategy = strategy;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public SearchParameters setVersion(String version) {
        this.version = version;
        return this;
    }

    @Override
    public JSONObject getOptionalsAsJSON() {
        JSONObject optionals = new JSONObject();
        optionals.put("embedCustom", String.valueOf(embedCustom));
        optionals.put("embedTracking", String.valueOf(embedTracking));
        optionals.put("bbox", String.valueOf(bbox));
        optionals.put("appID", appID != null ? appID : "");
        optionals.put("strategy", strategy);
        optionals.put("version", version);
        return optionals;
    }
}
