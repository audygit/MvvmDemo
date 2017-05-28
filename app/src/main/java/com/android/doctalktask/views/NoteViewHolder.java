package com.android.doctalktask.views;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.doctalktask.R;
import com.android.doctalktask.models.Note;




/**
 * Created by audyf on 5/28/2017.
 */

public class NoteViewHolder extends RecyclerView.ViewHolder {
    private TextView titleView;
    private TextView messageView;
    private View view;

    public NoteViewHolder(View view) {
        super(view);
        this.view=view;

    }
        void bindNote(final Note note, final Context context, final String key) {
            titleView= (TextView) view.findViewById(R.id.item_title);
            titleView.setText(note.getTitle());
            messageView= (TextView) view.findViewById(R.id.item_message);
            messageView.setText(note.getNoteMessage());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, AddNoteActivity.class);
                    intent.putExtra("note",note);
                    intent.putExtra("isNew",false);
                    intent.putExtra("key",key);
                    context.startActivity(intent);
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return false;
                }
            });
        }


}
