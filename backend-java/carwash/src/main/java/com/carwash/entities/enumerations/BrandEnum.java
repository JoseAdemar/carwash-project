package com.carwash.entities.enumerations;

import java.util.Locale;

public enum BrandEnum {
    VOLKSWAGEN("Volkswagen"),
    CHEVROLET("Chevrolet"),
    FORD("Ford"),
    FIAT("Fiat"),
    TOYOTA("Toyota"),
    HONDA("Honda"),
    HYUNDAI("Hyundai"),
    RENAULT("Renault"),
    NISSAN("Nissan"),
    JEEP("Jeep"),
    PEUGEOT("Peugeot"),
    CITROËN("Citroen"),
    MERCEDESBENZ("Mercedes Benz"),
    BMW("bmw"),
    AUDI("Audi"),
    KIA("Kia"),
    MITSUBISHI("Mitsubishi"),
    SUBARU("Subaru"),
    LANDROVER("Landrover"),
    JAGUAR("Jaguar"),
    YAMAHASUZUKI("Yamahasuzuki"),
    KAWASAKI("Kawasaki"),
    HARLEYDAVIDSON("Harleydavidson"),
    DUCATI("Ducati"),
    TRIUMPH("Triumph"),
    KTM("Ktm"),
    ROYALENFIELD("Royalenfield"),
    OTHER("Other");
    private String brand;
    BrandEnum(String brand) {
        this.brand = brand;
    }
}
