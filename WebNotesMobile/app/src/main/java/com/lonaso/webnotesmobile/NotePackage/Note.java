package com.lonaso.webnotesmobile.NotePackage;

import android.text.TextUtils;

/**
 * Created by nicolas on 29.11.16.
 */

public class Note {

    private int id;
    private String title;
    private String content;
    private String description;

    public Note(int id, String title, String description, String content){
        this.id = id;
        this.title = title;
        this.description = description;
        this.content = content;
    }

    public int getId(){return id;}

    public String getTitle(){
        return title;
    }

    public String getContent(){
        return content;
    }

    public String getDescription(){
        return description;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setContent(String content){
        this.content = content;
    }

    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }
        if(!(obj instanceof Note)){
            return false;
        }
        Note other = (Note) obj;
        return TextUtils.equals(getTitle(), other.getTitle());
    }
}
