package com.example.a15011082_dijitalnotdefteri;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import static android.widget.Toast.LENGTH_SHORT;

public class VideoDisplay extends AppCompatActivity {

    private Button baslat;
    private ImageButton delete;
    private VideoView video;
    private MediaController mediaController;
    private Note not;
    private SQLiteHelper db;
    private Context context = this;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_display);

        delete = findViewById(R.id.deleteVideo);
        baslat = findViewById(R.id.VideoBaslat);
        video=findViewById(R.id.videoView);
        text = findViewById(R.id.textView3);
        db = new SQLiteHelper(context);
        mediaController = new MediaController(this);

        Intent intent = getIntent();
        int id =intent.getIntExtra("video_id",-1);
        not = db.NoteOku(id);

        if (not.getVideo() != null){
            try{
                video.setVideoPath((not.getVideo()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            baslat.setVisibility(View.INVISIBLE);
            video.setVisibility(View.INVISIBLE);
            text.setVisibility(View.VISIBLE);
            delete.setVisibility(View.INVISIBLE);
        }
        baslat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video.start();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                not.setVideo(null);
                db.NoteGuncelle(not);
                Toast.makeText(VideoDisplay.this,"Video silindi.", LENGTH_SHORT).show();
                finish();
            }
        });

    }


}
