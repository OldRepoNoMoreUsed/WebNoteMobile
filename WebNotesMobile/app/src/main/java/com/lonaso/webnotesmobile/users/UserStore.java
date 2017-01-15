package com.lonaso.webnotesmobile.users;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lonaso.webnotesmobile.IWebNoteAPI;
import com.lonaso.webnotesmobile.groups.Group;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserStore {

    public static List<User> USERS = new ArrayList<>();

    static {
        for(int i = 0; i < 20; i++) {
            USERS.add(new User(i, "User name " + i, "user" + i + "@example.com", "password" + i, "avatar" + 1 + ".png"));
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

    public static void loadUsersFromGroup(int groupID) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IWebNoteAPI.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        IWebNoteAPI webNoteAPI = retrofit.create(IWebNoteAPI.class);
        Call<List<User>> call = webNoteAPI.getUsersFromGroup(groupID);
        try {
            USERS = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadUsers() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IWebNoteAPI.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        IWebNoteAPI webNoteAPI = retrofit.create(IWebNoteAPI.class);
        Call<List<User>> call = webNoteAPI.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                int statusCode = response.code();
                USERS = response.body();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.err.println("API ERROR : " + t.getMessage());
            }
        });
    }

    public static void removeUser(int position) {
        USERS.remove(position);
    }
}
