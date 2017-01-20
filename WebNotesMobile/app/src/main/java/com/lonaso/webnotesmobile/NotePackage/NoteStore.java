package com.lonaso.webnotesmobile.NotePackage;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lonaso.webnotesmobile.IWebNoteAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nicolas on 29.11.16.
 */

public class NoteStore {

    public static List<Note> NOTES = new ArrayList<Note>();
    private static int userID = 1;
    public static Note NOTE;

    public void addNote(Note note){
        NOTES.add(note);
    }

    public static Note findNoteByTitle(String title){
        Note res = null;

        for(Note note : NOTES){
            if(note.getTitle().equals(title)){
                res = note;
            }
        }
        return res;
    }
    public List<Note> getList(){
        return NOTES;
    }

    public static void loadNotes(){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IWebNoteAPI.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        IWebNoteAPI webNoteAPI = retrofit.create(IWebNoteAPI.class);
        Call<List<Note>> call = webNoteAPI.getNotes();
        try{
            NOTES = call.execute().body();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void updateNote(RequestBody title, RequestBody description, RequestBody content){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IWebNoteAPI.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        IWebNoteAPI webNoteAPI = retrofit.create(IWebNoteAPI.class);
        Call<ResponseBody> call = webNoteAPI.uploadNote(NOTE.getId(), title, description, content);
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
