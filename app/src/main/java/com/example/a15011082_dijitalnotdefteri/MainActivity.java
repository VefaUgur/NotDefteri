package com.example.a15011082_dijitalnotdefteri;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context context=this;
    private NoteAdapter adapter;
    private List<Note> NoteList;
    private RecyclerView recycleView;
    private SQLiteHelper db = new SQLiteHelper(context);
    private Button ekle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NoteList =new ArrayList<>();
        recycleView = findViewById(R.id.recyclerView);
        ekle = findViewById(R.id.ekle);
        //       db.onUpgrade(db.getWritableDatabase(),1,2);
        db.getWritableDatabase();


        NoteList= db.NotlariGetir();
        Collections.sort(NoteList,new SortbyOncelik());
        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(new GridLayoutManager(this,2));
        adapter =new NoteAdapter(this,NoteList);
        recycleView.setAdapter(adapter);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        NoteList= db.NotlariGetir();
        Collections.sort(NoteList,new SortbyOncelik());
        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(new GridLayoutManager(this,2));
        adapter =new NoteAdapter(this,NoteList);
        recycleView.setAdapter(adapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ex_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    public void NotEkle(View v){
        Intent intentNote = new Intent(MainActivity.this,NoteDisplay.class);
        intentNote.putExtra("ekle_kontrol",1);
        startActivityForResult(intentNote,2);
    }

}
