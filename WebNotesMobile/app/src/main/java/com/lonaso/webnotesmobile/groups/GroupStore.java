package com.lonaso.webnotesmobile.groups;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lonaso.webnotesmobile.IWebNoteAPI;
import com.lonaso.webnotesmobile.MainActivity;
import com.lonaso.webnotesmobile.users.User;
import com.lonaso.webnotesmobile.users.UserAdapter;
import com.lonaso.webnotesmobile.users.UserStore;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GroupStore {

    public static List<Group> GROUPS = new ArrayList<>();
    public static Group GROUP;


    public static Group findGroupById(int id) {
        Group result = null;

        for(Group group : GROUPS) {
            if(group.getId() == id) {
                result = group;
            }
        }

        return result;
    }

    public static Group findGroupByName(int name) {
        Group result = null;

        for(Group group : GROUPS) {
            if(group.getName().equals(name)) {
                result = group;
            }
        }

        return result;
    }

    public static void loadGroupsOfUser(int userID) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IWebNoteAPI.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        IWebNoteAPI webNoteAPI = retrofit.create(IWebNoteAPI.class);
        Call<List<Group>> call = webNoteAPI.getGroupsOfUser(userID);
        try {
            GROUPS = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadGroup(int groupID) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IWebNoteAPI.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        IWebNoteAPI webNoteAPI = retrofit.create(IWebNoteAPI.class);
        Call<Group> call = webNoteAPI.getGroup(groupID);
        try {
            GROUP = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateGroup(RequestBody name, RequestBody description, MultipartBody.Part icon, List<User> members) {

        List<Integer> membersID = new ArrayList<>();

        for(int i = 0 ; i<members.size(); i++) {
            membersID.add(members.get(i).getId());
        }

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IWebNoteAPI.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        IWebNoteAPI webNoteAPI = retrofit.create(IWebNoteAPI.class);
        Call<ResponseBody> call = webNoteAPI.uploadGroup(GROUP.getId(), name, description, membersID, icon);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.e("Upload error:", t.getMessage());

            }
        });
    }

    public static void addUser(Integer member) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IWebNoteAPI.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        IWebNoteAPI webNoteAPI = retrofit.create(IWebNoteAPI.class);
        Call<ResponseBody> call = webNoteAPI.addUserGroup(GROUP.getId(), member);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.e("Upload error:", t.getMessage());

            }
        });
    }

    public static void createGroup(RequestBody name, RequestBody description, User user) {
        List<Integer> membersID = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IWebNoteAPI.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        IWebNoteAPI webNoteAPI = retrofit.create(IWebNoteAPI.class);
        Call<ResponseBody> call = webNoteAPI.createGroup(name, description, user.getId());

        try {
            ResponseBody responseBody = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
