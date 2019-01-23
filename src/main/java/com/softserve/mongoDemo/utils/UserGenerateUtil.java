package com.softserve.mongoDemo.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.softserve.mongoDemo.models.Comment;
import com.softserve.mongoDemo.models.Contact;
import com.softserve.mongoDemo.models.User;

public class UserGenerateUtil {

    private static final List<String> listNames = Arrays.asList("Adela", "Cliff", "Christopher","Cecilia", "Ethel", "Eleanor", "Homer", "Jacob", "Lance",
        "Leon", "Marian", "Martin", "Philip", "Rayner", "Seward", "Thomasina", "Una", "Wilbert", "Whitney", "William", "Wanda", "Wally");

    private static final List<String> listLastName = Arrays.asList("Andrews", "Little", "Mackenzie","Campbell", "Charlson", "Croftoon", "Fitzgerald", "Harrison",
        "Higgins", "Wood", "Young", "Hardman", "Gilson", "Galbraith", "Foster", "Forster", "Forman", "Ford", "Wainwright",
        "William", "Forman", "Gate");

    private static final List<String> listCities = Arrays.asList("Ipswich", "Crosby", "London","Norwich", "Chester", "Sunderland", "Manchester");


    public static List<User> prepareTestUsers(int countUsers) {
        List<User> listUser = new ArrayList<>();
        for (int x = 0; x < countUsers; x++) {
            User user = new User();
            user.setName(listNames.get(generateRandomInt(0, listNames.size() - 1)));
            user.setLastName(listLastName.get(generateRandomInt(0, listNames.size() - 1)));
            user.setAge(generateRandomInt(10, 80));
            user.setActive(true);
            user.setContact(prepareContact(listCities.get(generateRandomInt(0, listCities.size() - 1)),
                new Integer(generateRandomInt(2, 99999999)).toString()));
            user.setCommentsList(generateListComment(generateRandomInt(1, 10)));
            listUser.add(user);
        }
        return listUser;
    }

    private static Contact prepareContact(String city, String phone) {
        Contact contact = new Contact();
        contact.setAddress(city);
        contact.setPhone(phone);
        return contact;
    }

    private static int generateRandomInt(int min, int max){
        Random r = new Random();
        return r.nextInt(max-min) + min;
    }

    private static List<Comment> generateListComment(int commentCount) {
        List<Comment> list = new ArrayList<>();
        for(int x = 0; x < commentCount; x++) {
            Comment comment = new Comment();
            comment.setDescription(generateDescription(15));
            list.add(comment);
        }
        return list;
    }

    private static String generateDescription(int descriptionLength) {
        Random r = new Random();
        String description = "Default Description: ";
        if(generateRandomInt(0, 3) == 1) {
            description = "bad word ";
        }

        for (int x = 0; x < descriptionLength; x++) {
            description = description + "a" +
                (char) (r.nextInt(26) + 'A') +
                (char) (r.nextInt(26) + 'a') +
                (char) (r.nextInt(26) + 'a') + " ";
        }

        return description;
    }

    public static void printJson(List list) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        list.forEach(e -> System.out.println(gson.toJson(e)));
    }

}
