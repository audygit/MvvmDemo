package com.android.doctalktask.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.doctalktask.R;
import com.android.doctalktask.databinding.NotesListActivityBinding;
import com.android.doctalktask.models.Note;
import com.android.doctalktask.viewmodels.NotesListViewModel;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;


public class NotesListActivity extends AppCompatActivity implements Observer {

    private NotesListActivityBinding notesListActivityBinding;
    private NotesListViewModel listViewModel;

    private FirebaseRecyclerAdapter<Note, NoteViewHolder> mAdapter;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private DatabaseReference mChatRef;
    private RecyclerView listNotes;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        mAuth = FirebaseAuth.getInstance();
        if(!isUserLogin()){
            signOut();
        }
        else {
            initFireBase();
            listNotes = notesListActivityBinding.listNotes;
            setListView(listNotes);
            startObserving(listViewModel);
        }
    }

    private void initDataBinding() {
       notesListActivityBinding = DataBindingUtil.setContentView(this, R.layout.notes_list_activity);
        listViewModel = new NotesListViewModel(this);
        notesListActivityBinding.setListViewModel(listViewModel);
    }
    private void  initFireBase(){
        mAuth=FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();
        mChatRef = mRef.child("notes").child(mAuth.getCurrentUser().getUid());
    }
    private void setListView(RecyclerView listNotes){
        listNotes.setHasFixedSize(true);
        listNotes.setLayoutManager(new LinearLayoutManager(NotesListActivity.this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter = new FirebaseRecyclerAdapter<Note, NoteViewHolder>(Note.class, R.layout.notes_item, NoteViewHolder.class,mChatRef) {
            @Override
            protected void populateViewHolder(NoteViewHolder viewHolder, Note model, int position) {
                final String key=getRef(position).getKey();
                viewHolder.bindNote(model,NotesListActivity.this,key);
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        promptDelete(key);
                        return false;
                    }
                });
            }
            @Override
            protected void onDataChanged() {
                if (mAdapter.getItemCount()==0){
                    listNotes.setVisibility(View.GONE);
                    notesListActivityBinding.noNotes.setVisibility(View.VISIBLE);
                }else {
                    listNotes.setVisibility(View.VISIBLE);
                    notesListActivityBinding.noNotes.setVisibility(View.GONE);
                }
//                    listViewModel.setNumberOfItems(mAdapter.getItemCount());
            }

        };
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

            }
        });

        listNotes.setAdapter(mAdapter);
    }
    private void promptDelete(final String key){
        AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(NotesListActivity.this);
        builder.setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mChatRef.child(key).removeValue();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void startObserving(Observable observable) {
        observable.addObserver(this);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        listViewModel.reset();
    }
    private boolean isUserLogin(){
        if(mAuth.getCurrentUser() != null){
            return true;
        }
        return false;
    }
    private void signOut(){
        Intent signOutIntent = new Intent(NotesListActivity.this, AuthUiActivity.class);
        startActivity(signOutIntent);
        finish();
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}

