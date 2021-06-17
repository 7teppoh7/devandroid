package com.example.devandroid.entities;

public class Dog {
    int id;
    StateAnimal state;
    Aviary aviary;
    String name;
    String photo;
    int age;
    String dateIn;
    String dateOut;

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

    public Dog(int id, StateAnimal state, Aviary aviary, String name, String photo, int age, String dateIn, String dateOut) {
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
                ", aviary=" + aviary +
                ", name='" + name + '\'' +
                ", photo='" + photo + '\'' +
                ", age=" + age +
                ", dateIn=" + dateIn +
                ", dateOut=" + dateOut +
                '}';
    }
}
