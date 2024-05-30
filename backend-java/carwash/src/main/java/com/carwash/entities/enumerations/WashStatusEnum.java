package com.carwash.entities.enumerations;

public enum WashStatusEnum {
    WAITING("Waiting"),
    WASHING("Washing"),
    FINISHED("Finished");

    private String status;

    WashStatusEnum(String status){
        this.status = status;
    }

}
