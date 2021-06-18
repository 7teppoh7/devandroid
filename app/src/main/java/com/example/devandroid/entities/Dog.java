package com.example.devandroid.entities;

import java.util.Objects;

public class Dog {
    private int id;
    private StateAnimal state;
    private String name;
    private String photo;
    private int age;
    private String dateIn;
    private String dateOut;

    public Dog(){}

    public Dog(String name, String photo, int age, String dateIn, String dateOut) {
        this.name = name;
        this.photo = photo;
        this.age = age;
        this.dateIn = dateIn;
        this.dateOut = dateOut;
    }

    public Dog(int id, String name, String photo, int age){
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.age = age;
    }

    public Dog(int id, StateAnimal state, String name, String photo, int age, String dateIn, String dateOut) {
        this.id = id;
        this.state = state;
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

    public String getDateIn() {
        return dateIn;
    }

    public void setDateIn(String dateIn) {
        this.dateIn = dateIn;
    }

    public String getDateOut() {
        return dateOut;
    }

    public void setDateOut(String dateOut) {
        this.dateOut = dateOut;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "id=" + id +
                ", state=" + state +
                ", name='" + name + '\'' +
                ", photo='" + photo + '\'' +
                ", age=" + age +
                ", dateIn=" + dateIn +
                ", dateOut=" + dateOut +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dog dog = (Dog) o;
        return id == dog.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
