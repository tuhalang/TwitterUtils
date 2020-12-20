package com.tuhalang.twitterutils.model;

import java.util.Map;

public class ResponseTag {

    private Tag[] data;
    private Map<String, Object> meta;

    public Tag[] getData() {
        return data;
    }

    public void setData(Tag[] data) {
        this.data = data;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }
}
