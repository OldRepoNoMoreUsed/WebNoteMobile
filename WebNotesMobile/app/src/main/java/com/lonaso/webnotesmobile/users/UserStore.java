package com.lonaso.webnotesmobile.users;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lonaso.webnotesmobile.IWebNoteAPI;
import com.lonaso.webnotesmobile.groups.Group;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;

public class UserStore {

    public static List<User> USERS = new ArrayList<>();
    public static User USER;

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

    /**
     * Load members of a group
     * @param groupID
     */
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

    /**
     * Load list of users from DB
     */
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
        try {
            USERS = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void loadUser(int userID) {
//        Gson gson = new GsonBuilder()
//                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
//                .create();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(IWebNoteAPI.ENDPOINT)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//
//        IWebNoteAPI webNoteAPI = retrofit.create(IWebNoteAPI.class);
//        Call<User> call = webNoteAPI.getUser(userID);
//        try {
//            USER = call.execute().body();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void removeUser(int position) {
        USERS.remove(position);
    }

    /**
     * Check if user exists in DB
     * @param email
     * @param name
     */
    public static void authUser(RequestBody email, RequestBody name) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IWebNoteAPI.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        IWebNoteAPI webNoteAPI = retrofit.create(IWebNoteAPI.class);
        Call<User> call = webNoteAPI.authUser(email, name);
        try {
            USER = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateUser(RequestBody name, RequestBody email, MultipartBody.Part avatar) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IWebNoteAPI.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        IWebNoteAPI webNoteAPI = retrofit.create(IWebNoteAPI.class);
        Call<User> call = webNoteAPI.uploadUser(USER.getId(), name, email, avatar);

        try {
            USER = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
