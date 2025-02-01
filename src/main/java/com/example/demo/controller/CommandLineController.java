package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Scanner;

import com.example.demo.service.SocialMediaService;


@Component
public class CommandLineController implements CommandLineRunner{

    @Autowired
    private SocialMediaService socialMediaService;

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Social Media Console Application Started...");
        System.out.println("Enter commands (or type 'exit' to quit):");

        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            try {
                processCommand(input);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
        System.out.println("Exiting application. Goodbye!");
    }

    private void processCommand(String input) {

        String[] tokens = input.split(" ", 3);
        String command = tokens[0];

        switch (command) {
            case "RegisterUser":
                if (tokens.length < 3) {
                    System.out.println("Invalid command. Usage: RegisterUser <user_id> <user_name>");
                    break;
                }
                String userId = tokens[1];
                String userName = tokens[2];
                String regResult = socialMediaService.registerUser(userId, userName);
                System.out.println(regResult);
                break;

            case "UploadPost":
                if (tokens.length < 3) {
                    System.out.println("Invalid command. Usage: UploadPost <user_id> \"<post>\"");
                    break;
                }
                userId = tokens[1];
                String postContent = tokens[2].trim();
                if (postContent.startsWith("\"") && postContent.endsWith("\"")) {
                    postContent = postContent.substring(1, postContent.length()-1);
                }
                String uploadResult = socialMediaService.uploadPost(userId, postContent);
                System.out.println(uploadResult);
                break;

            case "InteractionWithUser":
                String[] userTokens = input.split(" ");
                if (userTokens.length < 4) {
                    System.out.println("Invalid command. Usage: InteractionWithUser <FOLLOW/UNFOLLOW> <user_id1> <user_id2>");
                    break;
                }
                String interactionType = userTokens[1];
                String followerId = userTokens[2];
                String followeeId = userTokens[3];
                String interactionResult = socialMediaService.interactWithUser(interactionType, followerId, followeeId);
                System.out.println(interactionResult);
                break;

            case "ShowFeed":
                if (tokens.length < 2) {
                    System.out.println("Invalid command. Usage: ShowFeed <user_id>");
                    break;
                }
                userId = tokens[1];
                String feedResult = socialMediaService.showFeed(userId);
                System.out.println(feedResult);
                break;

            case "InteractWithPost":
                String[] postTokens = input.split(" ");
                if (postTokens.length < 4) {
                    System.out.println("Invalid command. Usage: InteractWithPost <LIKE/DISLIKE> <user_id> <post_id>");
                    break;
                }
                String postInteractionType = postTokens[1];
                userId = postTokens[2];
                String postId = postTokens[3];
                String postInteractionResult = socialMediaService.interactWithPost(postInteractionType, userId, postId);
                System.out.println(postInteractionResult);
                break;

            default:
                System.out.println("Unknown command.");
                break;
        }
    }
}
    

