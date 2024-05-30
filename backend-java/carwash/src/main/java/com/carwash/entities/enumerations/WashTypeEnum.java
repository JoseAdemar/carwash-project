package com.carwash.entities.enumerations;

public enum WashTypeEnum {
    BASIC("Basic"),
    COMPLETE("Complete");

    private String type;

    WashTypeEnum(String type) {
        this.type = type;
    }
}
