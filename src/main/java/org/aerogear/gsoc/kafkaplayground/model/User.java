package org.aerogear.gsoc.kafkaplayground.model;


public class User {
    private String username;
    private int age;

    public User() {
    }

    public User(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public String toString() {
        return "{ \"username\":\"" +
                getUsername() +
                "\", \"age\":" +
                getAge() + " }";

    }

    public String getUsername() {
        return username;
    }

    public int getAge() {
        return age;
    }
}