package com.tut.DestinationHashGenerator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class DestinationHashGenerator {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java -jar DestinationHashGenerator.jar <PRN Number> <path to json file>");
            System.exit(1);
        }

        String prnNumber = args[0];
        String jsonFilePath = args[1];

        // Read and parse JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String destinationValue = null;
        try {
            JsonNode rootNode = objectMapper.readTree(new File(jsonFilePath));
            destinationValue = findDestinationValue(rootNode);
            if (destinationValue == null) {
                System.err.println("No 'destination' key found in JSON.");
                System.exit(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Generate random string
        String randomString = generateRandomString(8);

        // Concatenate PRN number, destination value, and random string
        String concatenated = prnNumber + destinationValue + randomString;

        // Generate MD5 hash
        String md5Hash = DigestUtils.md5Hex(concatenated);

        // Output result
        System.out.println(md5Hash + ";" + randomString);
    }

    private static String findDestinationValue(JsonNode node) {
        if (node.isObject()) {
            for (JsonNode child : node) {
                if (child.has("destination")) {
                    return child.get("destination").asText();
                }
                String result = findDestinationValue(child);
                if (result != null) {
                    return result;
                }
            }
        } else if (node.isArray()) {
            for (JsonNode element : node) {
                String result = findDestinationValue(element);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }
}