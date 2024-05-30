package com.carwash.entities.enumerations;

public enum ColorEnum {

    WHITE("White"),
    RED("Red"),
    GRAY("Gray"),
    BLACK("Black"),
    BLUE("Blue"),
    YELLOW("Yellow"),
    PURPLE("Purple"),
    PINK("Pink"),
    OTHER("Other");

    private String color;
    ColorEnum(String color){
        this.color = color;
    }
}
