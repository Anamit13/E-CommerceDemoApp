package com.example.e_commercedemoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {
    private Context context;
    private ArrayList<Note> arrayList;
    private static OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        NoteAdapter.listener = (OnItemClickListener) listener;
    }

    public NoteAdapter() {
    }

    public NoteAdapter(Context context, ArrayList<Note> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_act, parent, false);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note note = arrayList.get(position);
        holder.pname.setText(note.getPname());
        holder.pprice.setText(String.valueOf(note.getPprice()));

        Glide.with(context).load(note.getpImg()).into(holder.shoe1img);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class NoteHolder extends RecyclerView.ViewHolder{
        private AppCompatTextView pname, pprice;
        private AppCompatImageView shoe1img;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);

            pname = itemView.findViewById(R.id.sname1);
            pprice = itemView.findViewById(R.id.sprice1);
            shoe1img = itemView.findViewById(R.id.shoe1img);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
