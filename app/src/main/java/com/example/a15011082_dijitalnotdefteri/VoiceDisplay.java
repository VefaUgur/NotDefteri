package com.example.a15011082_dijitalnotdefteri;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import static android.widget.Toast.LENGTH_SHORT;


public class VoiceDisplay extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;

    private MediaRecorder recorder = null;

    private MediaPlayer   player = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    private Button record;
    private Button play;
    private ImageButton delete;
    boolean mStartPlaying = true;
    boolean mStartRecording = true;
    private Note not;
    private SQLiteHelper db;
    private Context context = this;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        db = new SQLiteHelper(context);
        Intent intent = getIntent();
        int id =intent.getIntExtra("ses_id",-1);
        not = db.NoteOku(id);
        fileName =  getExternalCacheDir().getAbsolutePath()+"/audiorecordtest"+not.getId()+".3gp";

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        setContentView(R.layout.activity_voice_display);
        record = findViewById(R.id.record);
        play = findViewById(R.id.Play);
        delete = findViewById(R.id.deleteRecord);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File myFile = new File(fileName);
                if(myFile.exists()) {
                    onPlay(mStartPlaying);
                    if (mStartPlaying) {
                        play.setText("Durdur");
                    } else {
                        play.setText("Oynat");
                    }
                    mStartPlaying = !mStartPlaying;
                }else{
                    Toast.makeText(VoiceDisplay.this,"Kayıt Bulunmamaktadır", LENGTH_SHORT).show();
                }
            }
        });
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        onRecord(mStartRecording);
                        if (mStartRecording) {
                            record.setText("Kaydı Durdur");
                        } else {
                            record.setText("Kaydı Başlat");
                        }
                        mStartRecording = !mStartRecording;

                }

        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File myFile = new File(fileName);
                if(myFile.exists()){
                    myFile.delete();
                    Toast.makeText(VoiceDisplay.this,"Kayıt Silindi", LENGTH_SHORT).show();
                }else Toast.makeText(VoiceDisplay.this,"Kayıt Bulunamadı", LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }
}

