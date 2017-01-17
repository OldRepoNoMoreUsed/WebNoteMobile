package com.lonaso.webnotesmobile;

import com.lonaso.webnotesmobile.groups.Group;
import com.lonaso.webnotesmobile.users.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IWebNoteAPI {
//    String SERVER = "http://157.26.109.109/
    String SERVER = "http://192.168.1.115/";
    String STORAGE = SERVER + "storage/";
    String ENDPOINT = SERVER + "api/";

    @GET("user/")
    Call<List<User>> getUsers();

    @GET("user/{user}")
    Call<User> getUser(@Path("user") int userID);

    @GET("group/")
    Call<List<Group>> getGroups();

    @GET("group/{group}")
    Call<Group> getGroup (@Path("group") int groupID);

    @GET("group/{group}/users")
    Call<List<User>> getUsersFromGroup(@Path("group") int groupID);
}
