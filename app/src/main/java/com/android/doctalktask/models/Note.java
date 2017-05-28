package com.android.doctalktask.models;

import java.io.Serializable;

/**
 * Created by audyf on 5/27/2017.
 */

public class Note implements Serializable{
    private String title;
    private String noteMessage;
    private String uid;
    private String key;
    public Note(){

    }
    public Note(String title,String noteMessage,String uid){
        this.title=title;
        this.noteMessage=noteMessage;
        this.uid=uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNoteMessage() {
        return noteMessage;
    }

    public void setNoteMessage(String noteMessage) {
        this.noteMessage = noteMessage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
