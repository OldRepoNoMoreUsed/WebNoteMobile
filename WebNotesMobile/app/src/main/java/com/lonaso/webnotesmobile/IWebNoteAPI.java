package com.lonaso.webnotesmobile;

import com.lonaso.webnotesmobile.groups.Group;
import com.lonaso.webnotesmobile.users.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IWebNoteAPI {
//    String ENDPOINT = "http://157.26.108.189/api/";
    String ENDPOINT = "http://157.26.109.109/api/";

    @GET("group/")
    Call<List<Group>> getGroups();

    @GET("group/{group}")
    Call<Group> getGroup (@Path("group") int groupID);

    @GET("group/{group}/users")
    Call<List<User>> getUsersFromGroup(@Path("group") int groupID);
}
