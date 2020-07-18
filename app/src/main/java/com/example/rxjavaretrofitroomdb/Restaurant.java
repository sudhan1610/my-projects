package com.example.rxjavaretrofitroomdb;

public class Restaurant {

    String name;
    String place;
    public Restaurant(){
        name = "Royal dine";
        place = "Bangalore";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
