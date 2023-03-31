package com.jayaram.mvvmpractice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends ListAdapter<Note,NotesAdapter.ViewHolder> {
OnNoteClickListener onNoteClickListener;


    protected NotesAdapter(OnNoteClickListener onNoteClickListener) {
        super(DIFF_CALLBACK);
        this.onNoteClickListener=onNoteClickListener;
    }
private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
    @Override
    public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
        return oldItem.getId()==newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {

        return oldItem.getTitle().equals(newItem.getTitle())&&
                oldItem.getDescription().equals(newItem.getDescription())&&
                oldItem.getPriority()==newItem.getPriority();
    }
};


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new ViewHolder(view,onNoteClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         holder.getTitle().setText(getItem(position).getTitle());
         holder.getDescription().setText(getItem(position).getDescription());
         holder.getPriority().setText(String.valueOf(getItem(position).getPriority()));
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
       private final TextView title;
       private  final  TextView description;
       private final  TextView priority;
       private final OnNoteClickListener onNoteClickListener;
        public ViewHolder(@NonNull View itemView, OnNoteClickListener onNoteClickListener) {
            super(itemView);
            this.onNoteClickListener=onNoteClickListener;
            itemView.setOnClickListener(this);
            title=itemView.findViewById(R.id.txt_title);
            description=itemView.findViewById(R.id.txt_description);
            priority=itemView.findViewById(R.id.text_view_priority);
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getDescription() {
            return description;
        }

        public TextView getPriority() {
            return priority;
        }

        @Override
        public void onClick(View view) {
            onNoteClickListener.onClick(getAdapterPosition());
        }
    }

    public interface OnNoteClickListener
    {
        void onClick(int position);
    }
}
