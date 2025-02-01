package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SocialMediaServiceTest {

    private SocialMediaService service;

    @BeforeEach
    public void setUp() {
        service = new SocialMediaService();
    }

    @Test
    public void testUserRegistration() {
        String result1 = service.registerUser("1", "Akash");
        assertEquals("Akash Registered!!", result1);

        // Trying to register with an existing ID should fail.
        String result2 = service.registerUser("1", "Akash");
        assertEquals("User ID already exists!", result2);
    }

    @Test
    public void testUploadPost() {
        service.registerUser("1", "Akash");
        String uploadResult = service.uploadPost("1", "Hello World!");
        // Verify that the returned message contains the expected phrase.
        assertTrue(uploadResult.contains("Upload Successful with post id: "));
    }

    @Test
    public void testFollowUnfollow() {
        service.registerUser("1", "Akash");
        service.registerUser("2", "Hemant");
        String followResult = service.interactWithUser("FOLLOW", "2", "1");
        assertEquals("Followed Akash!!", followResult);
        
        String unfollowResult = service.interactWithUser("UNFOLLOW", "2", "1");
        assertEquals("Unfollowed Akash!!", unfollowResult);
    }

    @Test
    public void testLikeDislikePost() {
        service.registerUser("1", "Akash");
        service.registerUser("2", "Hemant");

        String uploadResult = service.uploadPost("1", "Hello World!");
        String postId = uploadResult.substring(uploadResult.lastIndexOf(":") + 2).trim();

        String likeResult = service.interactWithPost("LIKE", "2", postId);
        assertEquals("Post Liked!!", likeResult);

        // Liking the same post twice should fail.
        String duplicateLike = service.interactWithPost("LIKE", "2", postId);
        assertEquals("User has already liked the post!", duplicateLike);

        // Dislike the same post
        String dislikeResult = service.interactWithPost("DISLIKE", "2", postId);
        assertEquals("Post Disliked!!", dislikeResult);
    }

    @Test
    public void testShowFeed() {
        service.registerUser("1", "Akash");
        service.registerUser("2", "Hemant");
        service.registerUser("3", "Priya");

        service.uploadPost("1", "Post from Akash");
        service.uploadPost("2", "Post from Hemant");
        service.interactWithUser("FOLLOW", "3", "1");

        // Priya's feed should list Akash's post first (since she follows him)
        String feed = service.showFeed("3");
        assertTrue(feed.contains("UserName - Akash"));
    }
    
}
