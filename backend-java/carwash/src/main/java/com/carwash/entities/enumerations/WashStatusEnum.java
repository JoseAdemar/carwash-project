package com.carwash.entities.enumerations;

import com.fasterxml.jackson.annotation.JsonValue;

public enum WashStatusEnum {
    WASHING("LAVANDO"),
    FINISHED("FINALIZADO"),
    CANCELED("CANCELADO");

    private String status;

    WashStatusEnum(String status){
        this.status = status;
    }

    @JsonValue
    public String getStatus() {
        return status;
    }

    public static WashStatusEnum fromStatus(String status) {
        for (WashStatusEnum washStatus : values()) {
            if (washStatus.getStatus().equalsIgnoreCase(status)) {
                return washStatus;
            }
        }
        throw new IllegalArgumentException("Status não encontrado: " + status);
    }

}
