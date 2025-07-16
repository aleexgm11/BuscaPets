package com.grupo6.buscapets.model;

import java.io.Serializable;

public class Pet implements Serializable {
    private String name;
    private String type;
    private String breed;
    private int age;
    private String personality;
    private String imageName;
    private String weight;
    private String height;
    private String favoriteFood;
    private String protectora;

    public Pet(){
        // Constructor vacío que necesita Firebase
    }

    public Pet(String name, String type, String breed, int age, String personality, String imageName, String weight, String height, String favoriteFood
    ,String protectora) {
        this.name = name;
        this.type = type;
        this.breed = breed;
        this.age = age;
        this.personality = personality;
        this.imageName = imageName;
        this.weight = weight;
        this.height = height;
        this.favoriteFood = favoriteFood;
        this.protectora = protectora;
    }

    // Getters
    public String getName() { return name; }
    public String getType() { return type; }
    public String getBreed() { return breed; }
    public int getAge() { return age; }
    public String getPersonality() { return personality; }
    public String getImageName() { return imageName; }
    public String getWeight() { return weight; }
    public String getHeight() { return height; }
    public String getFavoriteFood() { return favoriteFood; }
    public String getProtectora() { return protectora; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setType(String type) { this.type = type; }
    public void setBreed(String breed) { this.breed = breed; }
    public void setAge(int age) { this.age = age; }
    public void setPersonality(String personality) { this.personality = personality; }
    public void setImageName(String imageName) { this.imageName = imageName; }
    public void setWeight(String weight) { this.weight = weight; }
    public void setHeight(String height) { this.height = height; }
    public void setFavoriteFood(String favoriteFood) { this.favoriteFood = favoriteFood; }
    public void setProtectora(String protectora) { this.protectora = protectora; }
}
