package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.ocpsoft.prettytime.PrettyTime;

import com.example.demo.model.Post;
import com.example.demo.model.User;

import java.util.Comparator;
import java.util.Date;

import org.springframework.stereotype.Service;


@Service
public class SocialMediaService {

    private Map<String, User> userMap = new HashMap<>();
    private Map<String, Post> postMap = new HashMap<>();

    private List<Post> postList = new ArrayList<>();

    private AtomicInteger postCounter = new AtomicInteger(1);

    private PrettyTime prettyTime = new PrettyTime();

    public String registerUser(String userId, String userName) {
        if (userMap.containsKey(userId)) {
            return "User ID already exists!";
        }
        User newUser = new User(userId, userName);
        userMap.put(userId, newUser);
        return userName + " Registered!!";
    }

    public String uploadPost(String userId, String content) {
        if (!userMap.containsKey(userId)) {
            return "User not found!";
        }
        String postId = String.format("%03d", postCounter.getAndIncrement());
        Post post = new Post(postId, userId, content, LocalDateTime.now());
        postMap.put(postId, post);
        postList.add(post);
        return "Upload Successful with post id: " + postId;
    }

    public String interactWithUser(String interactionType, String userId1, String userId2) {
        User user1 = userMap.get(userId1);
        User user2 = userMap.get(userId2);
        if (user1 == null || user2 == null) {
            return "User(s) not found!";
        }
        if ("FOLLOW".equalsIgnoreCase(interactionType)) {
            user1.follow(userId2);
            return "Followed " + user2.getName() + "!!";
        } else if ("UNFOLLOW".equalsIgnoreCase(interactionType)) {
            user1.unfollow(userId2);
            return "Unfollowed " + user2.getName() + "!!";
        } else {
            return "Invalid interaction type!";
        }
    }

    public String showFeed(String userId) {
        if (!userMap.containsKey(userId)) {
            return "User not found!";
        }
        User currentUser = userMap.get(userId);
        Set<String> following = currentUser.getFollowing();

        List<Post> followedPosts = new ArrayList<>();
        List<Post> nonFollowedPosts = new ArrayList<>();

        for (Post post : postList) {
            if (following.contains(post.getUserId())) {
                followedPosts.add(post);
            } else {
                nonFollowedPosts.add(post);
            }
        }

        Comparator<Post> comparator = Comparator.comparing(Post::getTimestamp).reversed();
        followedPosts.sort(comparator);
        nonFollowedPosts.sort(comparator);

        List<Post> feedPosts = new ArrayList<>();
        feedPosts.addAll(followedPosts);
        feedPosts.addAll(nonFollowedPosts);

        StringBuilder feed = new StringBuilder();
        for (Post post : feedPosts) {
            User postUser = userMap.get(post.getUserId());
            feed.append("UserName - ").append(postUser.getName()).append("\n")
                .append("# of Likes - ").append(post.getLikes()).append("\n")
                .append("# of Dislikes - ").append(post.getDislikes()).append("\n")
                .append("Post - ").append(post.getContent()).append("\n")
                .append("Post time - ").append(getRelativeTime(post.getTimestamp())).append("\n")
                .append("-------------------------------------\n");
        }
        return feed.toString();
    }

    public String interactWithPost(String interactionType, String userId, String postId) {
        if (!userMap.containsKey(userId)) {
            return "User not found!";
        }
        Post post = postMap.get(postId);
        if (post == null) {
            return "Post not found!";
        }
        if ("LIKE".equalsIgnoreCase(interactionType)) {
            boolean success = post.addLike(userId);
            if (success) {
                return "Post Liked!!";
            } else {
                return "User has already liked the post!";
            }
        } else if ("DISLIKE".equalsIgnoreCase(interactionType)) {
            boolean success = post.addDislike(userId);
            if (success) {
                return "Post Disliked!!";
            } else {
                return "User has already disliked the post!";
            }
        } else {
            return "Invalid post interaction type!";
        }
    }

    private String getRelativeTime(LocalDateTime postTime) {
        Date relativeDate = java.sql.Timestamp.valueOf(postTime);
        return prettyTime.format(relativeDate);
    }
    
}
