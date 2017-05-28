package com.android.doctalktask.views;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;

import com.android.doctalktask.R;
import com.android.doctalktask.databinding.AddNoteActivityBinding;
import com.android.doctalktask.models.Note;
import com.android.doctalktask.viewmodels.NoteDetailsViewModel;
import com.android.doctalktask.viewmodels.NotesListViewModel;

import java.util.Observable;
import java.util.Observer;

public class AddNoteActivity extends AppCompatActivity  implements Observer,NoteDetailsViewModel.HandleBackPress {

    private AddNoteActivityBinding addNoteActivityBinding;
    private Note note;
    private boolean isNew;
    private String key;
    private NoteDetailsViewModel noteDetailsViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isNew=  getIntent().getBooleanExtra("isNew",true);
        if (!isNew) {
            note = (Note) getIntent().getSerializableExtra("note");
        }
        key=getIntent().getStringExtra("key");
        initDataBinding();
        initUI();
        startObserving(noteDetailsViewModel);
    }
    private void initDataBinding(){
        addNoteActivityBinding = DataBindingUtil.setContentView(this, R.layout.add_note_activity);
        noteDetailsViewModel = new NoteDetailsViewModel(this,note,isNew,key);
        noteDetailsViewModel.setHandleBackPress(AddNoteActivity.this);
        addNoteActivityBinding.setNoteViewModel(noteDetailsViewModel);
    }
    private void initUI(){
        addNoteActivityBinding.titleFld.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    noteDetailsViewModel.setNoteTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        addNoteActivityBinding.noteFld.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                noteDetailsViewModel.setNoteMessage(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public void startObserving(Observable observable) {
        observable.addObserver(this);
    }
    @Override
    public void update(Observable o, Object arg) {

    }


    @Override
    public void pressBack() {
        onBackPressed();
    }
}
