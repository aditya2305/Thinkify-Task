package com.example.demo.model;

import java.util.HashSet;
import java.util.Set;

public class User {
    private String id;
    private String name;

    private Set<String> following;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        this.following = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<String> getFollowing() {
        return following;
    }

    public void follow(String followeeId) {
        following.add(followeeId);
    }

    public void unfollow(String followeeId) {
        following.remove(followeeId);
    }
    
}
