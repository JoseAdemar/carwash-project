package com.carwash.entities.enumerations;

public enum WashStatusEnum {
    WASHING("Lavando"),
    FINISHED("Finalizado"),
    CANCELED("Cancelado");

    private String status;

    WashStatusEnum(String status){
        this.status = status;
    }

}
