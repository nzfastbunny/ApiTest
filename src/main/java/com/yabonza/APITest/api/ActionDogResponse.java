package com.yabonza.APITest.api;

public class ActionDogResponse {
    private final String id;
    private final String content;

    public ActionDogResponse(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
