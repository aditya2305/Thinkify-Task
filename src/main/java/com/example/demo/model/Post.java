package com.example.demo.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class Post {

    private String postId;
    private String userId;
    private String content;
    private LocalDateTime timestamp;
    private int likes;
    private int dislikes;

    private Set<String> likedUsers;
    private Set<String> dislikedUsers;

    public Post(String postId, String userId, String content, LocalDateTime timestamp) {
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.timestamp = timestamp;
        this.likes = 0;
        this.dislikes = 0;
        this.likedUsers = new HashSet<>();
        this.dislikedUsers = new HashSet<>();
    }

    public String getPostId() {
        return postId;
    }

    public String getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getLikes() {
        return likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public boolean addLike(String userId) {

        if (likedUsers.contains(userId)) {
            return false; 
        }
        likedUsers.add(userId);
        if(dislikedUsers.remove(userId)) {
            dislikes--;
        }
        likes++;
        return true;
    }

    public boolean addDislike(String userId) {
        if (dislikedUsers.contains(userId)) {
            return false;
        }
        dislikedUsers.add(userId);
        if(likedUsers.remove(userId)) {
            likes--;
        }
        dislikes++;
        return true;
    }

    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
        return timestamp.format(formatter);
    }
    
}
