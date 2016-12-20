package com.lonaso.webnotesmobile.NotePackage;

/**
 * Created by nicolas on 29.11.16.
 */

public class Note {

    private String title;
    private String content;
    private String description;

    public Note(String title, String description, String content){
        this.title = title;
        this.description = description;
        this.content = content;
    }

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
}
