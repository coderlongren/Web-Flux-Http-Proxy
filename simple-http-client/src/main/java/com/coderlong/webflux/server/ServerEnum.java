package com.coderlong.webflux.server;

public enum ServerEnum {
    USER_SERVICE("http://localhost:8081", "webflux-server");

    String domain;
    String microName;

    ServerEnum(String domain, String microName) {
        this.domain = domain;
        this.microName = microName;
    }

    ServerEnum() {
    }

    public String getDomain() {
        return domain;
    }


    public String getMicroName() {
        return microName;
    }
}
