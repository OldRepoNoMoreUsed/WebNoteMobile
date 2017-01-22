package com.lonaso.webnotesmobile.notes;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lonaso.webnotesmobile.IWebNoteAPI;
import com.lonaso.webnotesmobile.users.UserStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NoteStore {

    public static List<Note> NOTES = new ArrayList<>();
    public static Note NOTE;

    public static Note findNoteByTitle(String title){
        Note res = null;

        for(Note note : NOTES){
            if(note.getTitle().equals(title)){
                res = note;
            }
        }
        return res;
    }

    public static void loadNote(int noteID){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IWebNoteAPI.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        IWebNoteAPI webNoteAPI = retrofit.create(IWebNoteAPI.class);
        Call<Note> call = webNoteAPI.getNote(noteID);
        try{
            NOTE = call.execute().body();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void loadNotesOfUser(int userID){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IWebNoteAPI.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        IWebNoteAPI webNoteAPI = retrofit.create(IWebNoteAPI.class);
        Call<List<Note>> call = webNoteAPI.getNotesOfUser(userID);
        try{
            NOTES = call.execute().body();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void updateNote(RequestBody content){
        RequestBody author =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), UserStore.USER.getName());
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IWebNoteAPI.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        IWebNoteAPI webNoteAPI = retrofit.create(IWebNoteAPI.class);
        Call<ResponseBody> call = webNoteAPI.uploadNote(NOTE.getId(), content, UserStore.USER.getId(), author);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("Upload", "Success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload failed:", t.getMessage());
            }
        });
    }

    public static void createNote(RequestBody title, RequestBody description, RequestBody content) {
        RequestBody author =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), UserStore.USER.getName());
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IWebNoteAPI.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        IWebNoteAPI webNoteAPI = retrofit.create(IWebNoteAPI.class);
        Call<ResponseBody> call = webNoteAPI.createNote(title, description, content, UserStore.USER.getId(), author);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("Upload", "Success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload failed:", t.getMessage());
            }
        });
    }
}
