package com.carwash.entities.enumerations;

public enum CategoryEnum {
    CAR("Car"),
    MOTOCYCLE("Motocycle"),
    TRUCK("Truck"),
    OTHER("Other");

    private String category;
    CategoryEnum(String category){
        this.category = category;
    }
}
