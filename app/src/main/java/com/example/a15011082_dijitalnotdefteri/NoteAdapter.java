package com.example.a15011082_dijitalnotdefteri;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> implements Filterable {

    private Context mCtx;
    private List<Note> NoteList;
    private List<Note> NoteListFull;
    private SQLiteHelper db;

    public NoteAdapter(Context mCtx, List<Note> NoteList) {

        this.mCtx = mCtx;

        this.NoteList = NoteList;
        NoteListFull = new ArrayList<Note>(NoteList);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_layout, null);
        return new NoteViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder NoteViewHolder, int i) {
        final Note Note = NoteList.get(i);

        NoteViewHolder.baslik.setText(Note.getBaslik());
        NoteViewHolder.icerik.setText(Note.getIcerik());
        NoteViewHolder.tarih.setText(Note.getTarih());
        NoteViewHolder.cardView.setBackgroundColor(Integer.valueOf(Note.getRenk()));
        if(Integer.valueOf(Note.getOncelik())==0)
            NoteViewHolder.star.setVisibility(View.VISIBLE);
        NoteViewHolder.listlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNote = new Intent(mCtx, NoteDisplay.class);
                intentNote.putExtra("Note_id", Note.getId());
                ((Activity) mCtx).startActivityForResult(intentNote, 1);
            }
        });
    /*    NoteViewHolder.listlayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                NoteViewHolder.cardView.setCardBackgroundColor(Color.YELLOW);
                return false;
            }
        }); */
    }


    @Override
    public int getItemCount() {
        return NoteList.size();
    }

    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Note> filteredList = new ArrayList<Note>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(NoteListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Note item : NoteListFull) {
                    if (item.getTarih().toLowerCase().contains(filterPattern) || item.getAdres().toLowerCase().contains(filterPattern)  || item.getBaslik().toLowerCase().contains(filterPattern) ) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            NoteList.clear();
            NoteList.addAll((Collection<? extends Note>) results.values);
            notifyDataSetChanged();
        }
    };


    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView icerik;
        TextView baslik;
        TextView tarih;
        LinearLayout listlayout;
        ImageView star;
        CardView cardView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            baslik = itemView.findViewById(R.id.baslik2);
            listlayout = itemView.findViewById(R.id.card);
            icerik = itemView.findViewById(R.id.icerik2);
            tarih = itemView.findViewById(R.id.tarih2);
            cardView = itemView.findViewById(R.id.cardview);
            star = itemView.findViewById(R.id.imageView3);
        }


    }

}
