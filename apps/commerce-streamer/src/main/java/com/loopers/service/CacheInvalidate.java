package com.loopers.service;

import lombok.Getter;

@Getter
public class CacheInvalidate {

    private final String type;
    private final String payload;

    public CacheInvalidate(String type, String payload) {
        this.type = type;
        this.payload = payload;
    }

}
