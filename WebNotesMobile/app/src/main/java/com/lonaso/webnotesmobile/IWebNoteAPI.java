package com.lonaso.webnotesmobile;

import com.lonaso.webnotesmobile.NotePackage.Note;
import com.lonaso.webnotesmobile.groups.Group;
import com.lonaso.webnotesmobile.users.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IWebNoteAPI {
//    String SERVER = "http://157.26.109.109/
    String SERVER = "http://webnotes/";
    String STORAGE = SERVER + "storage/";
    String ENDPOINT = SERVER + "api/";

    @GET("user/")
    Call<List<User>> getUsers();

    @GET("group/")
    Call<List<Group>> getGroups();

    @GET("group/{group}")
    Call<Group> getGroup (@Path("group") int groupID);

    @GET("group/{group}/users")
    Call<List<User>> getUsersFromGroup(@Path("group") int groupID);

    @GET("note/{user}/notes")
    Call<List<Note>> getNotesFromUser(@Path("user") int userID);
}
