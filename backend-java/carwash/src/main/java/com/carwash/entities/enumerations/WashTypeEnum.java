package com.carwash.entities.enumerations;

import com.fasterxml.jackson.annotation.JsonValue;

public enum WashTypeEnum {
    BASIC("BASICO"),
    NORMAL("REGULAR"),
    COMPLETE("COMPLETO");

    private String type;

    WashTypeEnum(String type) {
        this.type = type;
    }

    @JsonValue
    public String getStatus() {
        return type;
    }

    public static WashTypeEnum fromStatus(String type) {
        for (WashTypeEnum washStatus : values()) {
            if (washStatus.getStatus().equalsIgnoreCase(type)) {
                return washStatus;
            }
        }
        throw new IllegalArgumentException("Status não encontrado: " + type);
    }
}
