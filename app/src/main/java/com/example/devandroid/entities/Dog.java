package com.example.devandroid.entities;

import java.sql.Date;

public class Dog {
    int id;
    StateAnimal state;
    Aviary aviary;
    String name;
    String photo;
    int age;
    Date dateIn;
    Date dateOut;

    public Dog(){}

    public Dog(int id, StateAnimal state, Aviary aviary, String name, String photo, int age, Date dateIn, Date dateOut) {
        this.id = id;
        this.state = state;
        this.aviary = aviary;
        this.name = name;
        this.photo = photo;
        this.age = age;
        this.dateIn = dateIn;
        this.dateOut = dateOut;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StateAnimal getState() {
        return state;
    }

    public void setState(StateAnimal state) {
        this.state = state;
    }

    public Aviary getAviary() {
        return aviary;
    }

    public void setAviary(Aviary aviary) {
        this.aviary = aviary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getDateIn() {
        return dateIn;
    }

    public void setDateIn(Date dateIn) {
        this.dateIn = dateIn;
    }

    public Date getDateOut() {
        return dateOut;
    }

    public void setDateOut(Date dateOut) {
        this.dateOut = dateOut;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "id=" + id +
                ", state=" + state +
                ", aviary=" + aviary +
                ", name='" + name + '\'' +
                ", photo='" + photo + '\'' +
                ", age=" + age +
                ", dateIn=" + dateIn +
                ", dateOut=" + dateOut +
                '}';
    }
}
