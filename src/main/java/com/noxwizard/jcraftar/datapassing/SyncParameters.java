package com.noxwizard.jcraftar.datapassing;

import org.json.JSONObject;

/**
 *
 * @author Stefano Zanini
 */
public class SyncParameters implements Parameters {

    private boolean bundled = true;
    private String tag = null;

    public boolean isBundled() {
        return bundled;
    }

    public SyncParameters setBundled(boolean bundled) {
        this.bundled = bundled;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public SyncParameters setTag(String tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public JSONObject getOptionalsAsJSON() {
        JSONObject optionals = new JSONObject();
        optionals.put("bundled", bundled);
        optionals.put("tag", tag != null ? tag : "");
        return optionals;
    }
}
