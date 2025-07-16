package com.grupo6.buscapets.model;

public class Usuario {
    private String name;
    private String email;
    private String age;
    private String phone;
    private String address;

    public Usuario() {
        // Constructor vacío que necesita Firebase
    }

    public Usuario(String name, String email, String age, String phone, String address) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.phone = phone;
        this.address = address;
    }

    // Getters y Setters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() { return phone; }

    public String getAddress() { return address; }

    public String getAge() {
        return age;
    }

    public void setEmail(String email) { this.email = email; }

    public void setName(String name) { this.name = name; }

    public void setAge(String age) { this.age = age; }

    public void setPhone(String phone) { this.phone = phone; }

    public void setAddress(String address) { this.address = address; }
}

