package com.grupo6.buscapets.model;

import java.io.Serializable;

public class Protectora implements Serializable {
    private String name;
    private String address;
    private String phone;
    private String imageName;

    public Protectora(){
        // Constructor vacío que necesita Firebase
    }

    public Protectora(String name, String address, String phone, String imageName) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.imageName = imageName;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getImageName() {
        return imageName;
    }
}
