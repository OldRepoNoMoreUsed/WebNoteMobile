package com.lonaso.webnotesmobile.users;

import java.util.ArrayList;
import java.util.List;

public class UserStore {

    public static final List<User> USERS = new ArrayList<>();
    public static final List<User> USERS1 = new ArrayList<>();

    static {
        for(int i = 0; i < 20; i++) {
            USERS.add(new User(i, "User name " + i, "user" + i + "@example.com", "password" + i, "avatar" + 1 + ".png"));
        }
        for(int i = 10; i < 20; i++) {
            USERS1.add(new User(i, "User name " + i, "user" + i + "@example.com", "password" + i, "avatar" + 1 + ".png"));
        }
    }

    public static User findUserById(int id) {
        User result = null;

        for(User user : USERS) {
            if(user.getId() == id) {
                result = user;
            }
        }

        return result;
    }

    public static User findUserByName(int name) {
        User result = null;

        for(User user : USERS) {
            if(user.getName().equals(name)) {
                result = user;
            }
        }

        return result;
    }

    public static User findUserByEmail(String email) {
        User result = null;

        for(User user : USERS) {
            if(user.getEmail().equals(email)) {
                result = user;
            }
        }

        return result;
    }
}
