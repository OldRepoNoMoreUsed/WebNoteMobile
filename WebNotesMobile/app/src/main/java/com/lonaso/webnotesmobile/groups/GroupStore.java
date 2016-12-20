package com.lonaso.webnotesmobile.groups;

import com.lonaso.webnotesmobile.IWebNoteAPI;
import com.lonaso.webnotesmobile.MainActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GroupStore {

    public static List<Group> GROUPS = new ArrayList<>();
    private static List groups;

    static {
//        for(int i = 0; i < 10; i++) {
//            GROUPS.add(new Group(i, "Group name " + i, "Description " + i, "icon" + 1 + ".png"));
//        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IWebNoteAPI.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IWebNoteAPI webNoteAPI = retrofit.create(IWebNoteAPI.class);
        Call<List<Group>> call = webNoteAPI.getGroupList();
        call.enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                int statusCode = response.code();
                GROUPS = response.body();
                System.out.println("-->" + response.body());
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                System.err.println("API ERROR : " + t.getMessage());
            }
        });
    }

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

    public static List<Group> getGroupsFromAPI() {



        return groups;
    }
}
