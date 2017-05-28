package com.android.doctalktask.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.doctalktask.R;
import com.android.doctalktask.models.Note;
import com.android.doctalktask.views.AddNoteActivity;
import com.android.doctalktask.views.NotesListActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by audyf on 5/27/2017.
 */

public class NotesListViewModel extends Observable {
    private Context context;
    public ObservableInt notesRecyclerView;
    public ObservableInt notesTextView;
    public ObservableField<String> messageLabel;

    public NotesListViewModel(@NonNull Context context){
        this.context=context;
        notesRecyclerView = new ObservableInt(View.GONE);
        notesTextView = new ObservableInt(View.VISIBLE);
        messageLabel = new ObservableField<>("No Notes");
    }
    public void setNumberOfItems(int count){
        if (count==0){
            notesRecyclerView = new ObservableInt(View.GONE);
            notesTextView = new ObservableInt(View.VISIBLE);
        }else {
            notesRecyclerView = new ObservableInt(View.VISIBLE);
            notesTextView = new ObservableInt(View.GONE);
        }
    }
    public void setNotesTextView(int visibility){
        notesTextView=new ObservableInt(visibility);
    }
    public void onClickFabLoad(View view){
        Intent intent=new Intent(context, AddNoteActivity.class);
        context.startActivity(intent);
    }


    private void unSubscribeFromObservable() {

    }

    public void reset() {
        unSubscribeFromObservable();
        context = null;
    }
}
