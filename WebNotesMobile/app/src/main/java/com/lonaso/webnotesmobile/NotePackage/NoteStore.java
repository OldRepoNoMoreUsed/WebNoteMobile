package com.lonaso.webnotesmobile.NotePackage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lonaso.webnotesmobile.IWebNoteAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nicolas on 29.11.16.
 */

public class NoteStore {

    private static List<Note> NOTES = new ArrayList<Note>();
    private static int userID = 1;

    public void addNote(Note note){
        NOTES.add(note);
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
        Call<List<Note>> call = webNoteAPI.getNotesFromUser(userID);
        try{
            NOTES = call.execute().body();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
