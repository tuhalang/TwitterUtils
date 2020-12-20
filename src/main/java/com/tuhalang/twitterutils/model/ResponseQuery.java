package com.tuhalang.twitterutils.model;

import java.util.Map;

public class ResponseQuery {

    private Tweet[] data;
    private Map<String, String> meta;

    public Tweet[] getData() {
        return data;
    }

    public void setData(Tweet[] data) {
        this.data = data;
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, String> meta) {
        this.meta = meta;
    }
}
