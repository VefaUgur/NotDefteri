package com.example.colorsplit;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {

    private Context context = this;
    private ImageAdapter adapter;
    private List<Image> imageList = new ArrayList<>();
    private RecyclerView recyclerView;
    private int STORAGE_PERMISSION_CODE = 2;
    private static final int IMAGE_PICK = 1;
    private Uri selectedImage;
    private String picturePath ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        adapter =new ImageAdapter(this,imageList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            //               resim.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            Toast.makeText(this, "Seçilen Fotoğraf Yüklendi", LENGTH_SHORT).show();

            if (picturePath != null){
                try {
                    final InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                    final Bitmap selected = BitmapFactory.decodeStream(imageStream);
                    Bitmap resized = Bitmap.createScaledBitmap(selected, 200, 200, true);
                    Image image = new Image(resized);
                    imageList.add(image);
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }
            }

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            adapter = new ImageAdapter(this, imageList);
            recyclerView.setAdapter(adapter);
        }
    }


    public void analyzePictures(View view) {
        DominantColorFinder colorFinder  = new DominantColorFinder();
        if (imageList.size() > 0){
            for(Image img : imageList){
                colorFinder.findDominant(img);
            }
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ImageAdapter(this, imageList);
        recyclerView.setAdapter(adapter);
    }

    public void addImage(View v){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            Intent intent1 = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent1.setType("image/*");
            startActivityForResult(Intent.createChooser(intent1, "Resim Yükle"), IMAGE_PICK);
        } else {
            requestStoragePermission();
        }
    }

    private void requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){

            new android.app.AlertDialog.Builder(this).setTitle("İzin gerekli").setMessage("Fotoğraf yüklenmesi için bu izin gerekli").setPositiveButton("onayla", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);

                }
            }).setNegativeButton("iptal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();

        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"İzin onaylandı",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,"İzin reddedildi",Toast.LENGTH_LONG).show();

            }
        }
    }


}
