package com.android.doctalktask.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.android.doctalktask.databinding.AddNoteActivityBinding;
import com.android.doctalktask.models.Note;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

/**
 * Created by audyf on 5/28/2017.
 */

public class NoteDetailsViewModel extends Observable {
    private Note note;
    private Context context;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private DatabaseReference mNoteRef;
    private String key;
    private boolean isNew;
    private HandleBackPress handleBackPress;
    public NoteDetailsViewModel(@NonNull Context context,Note note,boolean isNew,String key){
        if (note==null){
            this.note= new Note();
        }else {
            this.note=note;
        }
        this.key=key;
        this.isNew=isNew;
        this.context=context;
        initFireBase();
    }

    public HandleBackPress getHandleBackPress() {
        return handleBackPress;
    }

    public void setHandleBackPress(HandleBackPress handleBackPress) {
        this.handleBackPress = handleBackPress;
    }

    public interface HandleBackPress{
        public void pressBack();
    }
    public void setNoteTitle(String s){
        note.setTitle(s);
    }
    public void setNoteMessage(String s){
        note.setNoteMessage(s);
    }
    private void  initFireBase(){
        mAuth= FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();
        mNoteRef = mRef.child("notes").child(mAuth.getCurrentUser().getUid());
    }
    private void addNote(){
        if (isValid()){
            note.setUid(mAuth.getCurrentUser().getUid());
            mNoteRef.push().setValue(note, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError==null){
                        Toast.makeText(context,"Added Successfully",Toast.LENGTH_SHORT).show();
                        handleBackPress.pressBack();
                    }else {
                        Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public String getTitle(){
     return    note.getTitle();
    }
    public String getMessage(){
        return    note.getNoteMessage();
    }
    public void  onSaveClicked(View view){
        if (isNew) {
            addNote();
        }else {
            updateNote();
        }
    }
    private void updateNote(){
            if (isValid()){
                mNoteRef.child(key).setValue(note, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError==null){
                            Toast.makeText(context,"Saved Successfully",Toast.LENGTH_SHORT).show();
                            handleBackPress.pressBack();
                        }else {
                            Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
    }
    public boolean isValid(){
        if (note.getTitle()!=null&&!note.getTitle().isEmpty()){
            if (note.getNoteMessage()!=null&&!note.getNoteMessage().isEmpty()){
                return  true;
            }else {
                Toast.makeText(context,"Note can not be empty",Toast.LENGTH_SHORT).show();
                return false;
            }
        }else {
            Toast.makeText(context,"Title can not be empty",Toast.LENGTH_SHORT).show();
            return false;
        }
    }


}
