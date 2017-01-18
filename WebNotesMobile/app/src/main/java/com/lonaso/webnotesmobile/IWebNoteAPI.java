package com.lonaso.webnotesmobile;

import com.lonaso.webnotesmobile.NotePackage.Note;
import com.lonaso.webnotesmobile.groups.Group;
import com.lonaso.webnotesmobile.users.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface IWebNoteAPI {
//    String SERVER = "http://157.26.109.109/
    String SERVER = "http://webnotes/";

    String STORAGE = SERVER + "storage/";
    String ENDPOINT = SERVER + "api/";

    @GET("user/")
    Call<List<User>> getUsers();

    @GET("user/{user}")
    Call<User> getUser(@Path("user") int userID);

    @GET("group")
    Call<List<Group>> getGroups();

    @GET("group/{group}")
    Call<Group> getGroup (@Path("group") int groupID);

    @GET("group/{group}/users")
    Call<List<User>> getUsersFromGroup(@Path("group") int groupID);

<<<<<<< HEAD
    @GET("note/{user}/notes")
    Call<List<Note>> getNotesFromUser(@Path("user") int userID);
=======
    @Multipart
    @POST("group/{group}")
    Call<ResponseBody> uploadGroup(  @Path("group") int groupID,
                                @Part("name") RequestBody name,
                                @Part("description") RequestBody description);
>>>>>>> d7ef719dc7ff19ef9a6d43515c9b410931183036
}
