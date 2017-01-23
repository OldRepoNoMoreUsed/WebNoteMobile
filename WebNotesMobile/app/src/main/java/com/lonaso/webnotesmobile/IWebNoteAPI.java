package com.lonaso.webnotesmobile;

import com.lonaso.webnotesmobile.notes.Note;
import com.lonaso.webnotesmobile.groups.Group;
import com.lonaso.webnotesmobile.users.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Interface with requests to API
 */
public interface IWebNoteAPI {
    String SERVER = "http://157.26.109.219/";       // Server Address or IP
    String STORAGE = SERVER + "storage/";           // Storage URL (for images)
    String ENDPOINT = SERVER + "api/";              // API URL

    @GET("user/")
    Call<List<User>> getUsers();

    @GET("user/{user}")
    Call<User> getUser(@Path("user") int userID);

    @GET("user/{user}/groups")
    Call<List<Group>> getGroupsOfUser(@Path("user") int userID);

    @GET("group/{group}")
    Call<Group> getGroup (@Path("group") int groupID);

    @GET("group/{group}/users")
    Call<List<User>> getUsersFromGroup(@Path("group") int groupID);

    @GET("note")
    Call<List<Note>> getNotes();

    @GET("user/{user}/notes")
    Call<List<Note>> getNotesOfUser(@Path("user") int userID);

    @GET("note/{note}")
    Call<Note> getNote(@Path("note") int noteID);

    @Multipart
    @POST("note/{note}")
    Call<ResponseBody> uploadNote(@Path("note") int noteID,
                                  @Part("content") RequestBody content,
                                  @Part("member") Integer member,
                                  @Part("auteur") RequestBody author);

    @Multipart
    @POST("note")
    Call<ResponseBody> createNote(@Part("title") RequestBody title,
                                  @Part("description") RequestBody description,
                                  @Part("content") RequestBody content,
                                  @Part("member") Integer member,
                                  @Part("auteur") RequestBody author);

    @Multipart
    @POST("group/{group}")
    Call<ResponseBody> uploadGroup(@Path("group") int groupID,
                                @Part("name") RequestBody name,
                                @Part("description") RequestBody description,
                                @Part("members[]") List<Integer> members,
                                @Part MultipartBody.Part icon);

    @Multipart
    @POST("group/{group}/addUser")
    Call<ResponseBody> addUserGroup( @Path("group") int groupID,
                                     @Part("member") Integer member);

    @Multipart
    @POST("user/authUser")
    Call<User> authUser(   @Part("email") RequestBody email,
                           @Part("name") RequestBody name);

    @Multipart
    @POST("group")
    Call<ResponseBody> createGroup(@Part("name") RequestBody name,
                                   @Part("description") RequestBody description,
                                   @Part("member") Integer member);

    @Multipart
    @POST("user/{user}")
    Call<ResponseBody> uploadUser(@Path("user") int userID,
                                   @Part("name") RequestBody name,
                                   @Part("email") RequestBody email,
                                   @Part MultipartBody.Part avatar);
}
