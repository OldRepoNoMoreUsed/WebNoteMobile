package com.lonaso.webnotesmobile.NotePackage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicolas on 29.11.16.
 */

public class NoteStore {

    private List<Note> NOTES = new ArrayList<Note>();

    public void addNote(Note note){
        NOTES.add(note);
    }

    public List<Note> getList(){
        return NOTES;
    }
}
