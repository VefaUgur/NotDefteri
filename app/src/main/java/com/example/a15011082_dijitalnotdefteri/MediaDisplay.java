package com.example.a15011082_dijitalnotdefteri;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_SHORT;

public class MediaDisplay extends AppCompatActivity {

    private Note not;
    private SQLiteHelper db;
    private Context context = this;
    private ImageView resim;
    private TextView uyari;
    private ImageButton fotoSil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_display);

        db = new SQLiteHelper(context);
        resim=findViewById(R.id.fotograf);
        uyari=findViewById(R.id.textView2);
        fotoSil = findViewById(R.id.FotoSil);

        Intent intent = getIntent();
        int id =intent.getIntExtra("media_id",-1);
        not = db.NoteOku(id);
        if (not.getFoto() != null){
            resim.setImageBitmap(BitmapFactory.decodeFile(not.getFoto()));
            resim.setVisibility(View.VISIBLE);
            fotoSil.setVisibility(View.VISIBLE);
        }else uyari.setVisibility(View.VISIBLE);

    }

    public void Fotosil(View v){
        not.setFoto(null);
        db.NoteGuncelle(not);
        Toast.makeText(MediaDisplay.this,"Fotoğraf silindi.", LENGTH_SHORT).show();
        finish();

    }
}
