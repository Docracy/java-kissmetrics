package com.jeraff.kissmetrics.client.el.testobjects;

import java.util.Collection;
import java.util.HashMap;

public class User {
    private String username;
    private int age;
    private Collection<String> tags;
    private HashMap<String, String> attrs;

    public User(String username, int age, Collection<String> tags, HashMap<String, String> attrs) {
        this.username = username;
        this.age = age;
        this.tags = tags;
        this.attrs = attrs;
    }

    public User(String username) {
        this.username = username;
    }

    public User(int age) {
        this.age = age;
    }

    public User(Collection<String> tags) {
        this.tags = tags;
    }

    public User(HashMap<String, String> attrs) {
        this.attrs = attrs;
    }

    public User() {
    }

    public HashMap<String, String> getAttrs() {
        return attrs;
    }

    public void setAttrs(HashMap<String, String> attrs) {
        this.attrs = attrs;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Collection<String> getTags() {
        return tags;
    }

    public void setTags(Collection<String> tags) {
        this.tags = tags;
    }
}
