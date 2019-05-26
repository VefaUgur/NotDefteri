package com.example.a15011082_dijitalnotdefteri;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.widget.Toast.LENGTH_SHORT;

public class NoteDisplay extends AppCompatActivity {

    private final static int videoResultCode=4;
    private final static int voiceResultCode=6;
    private EditText baslik,icerik;
    private TextView adres,saat;
    private SQLiteHelper db;
    private ImageButton ekle,media_show,sil, reminder;
    private Context context = this;
    private Note not;
    private Uri selectedImage,selectedVideo;
    private String picturePath ;
    private CheckBox checkBox;
    private RadioGroup radioGroup;
    private RadioButton secili_renk_button;
    private  String secili_renk = String.valueOf(Color.YELLOW);



    private static  final  int REQUEST_LOCATION = 1 ;
    private int STORAGE_PERMISSION_CODE = 2;
    private static final int IMAGE_PICK = 1;

    private String lattitude,longitude;
    LocationManager locationManager;
    double latti;
    double longi;

    public void FindAll(){
        baslik = findViewById(R.id.baslik);
        icerik = findViewById(R.id.icerik);
        adres = findViewById(R.id.adres);
        sil = findViewById(R.id.sil);
        ekle = findViewById(R.id.yukle);
        media_show= findViewById(R.id.media);
        saat = findViewById(R.id.saat);
        checkBox=findViewById(R.id.checkBox);
        radioGroup=findViewById(R.id.renkpaleti);
        reminder = findViewById(R.id.reminder);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_display);

        FindAll();
        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);



        db = new SQLiteHelper(context);


        Intent intent = getIntent();
        int id =intent.getIntExtra("Note_id",-1);
        int ekle_kontrol=intent.getIntExtra("ekle_kontrol",-1);
        if (ekle_kontrol == 1){
            not=null;
        }else {
            not = db.NoteOku(id);
            baslik.setText(not.getBaslik());
            icerik.setText(not.getIcerik());
            adres.setText(not.getAdres());
            saat.setText(not.getTarih());
            if (not.getOncelik().equals("0"))
                checkBox.setChecked(true);
            if(!not.getRenk().equals(String.valueOf(Color.YELLOW))){
                secili_renk=not.getRenk();
            }

            this.registerForContextMenu(ekle);
            this.registerForContextMenu(media_show);

        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_display_note_reminder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.reminder:
                if (not != null){
                    Intent rem_int= new Intent(NoteDisplay.this,Hatirlatici.class);
                    rem_int.putExtra("reminder_id",not.getId());
                    startActivityForResult(rem_int,10);
                } else {
                    Toast.makeText(NoteDisplay.this,"Önce notu kaydediniz", LENGTH_SHORT).show();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void mediaShow(View v){
        PopupMenu popupMenu = new PopupMenu(NoteDisplay.this,ekle);
        popupMenu.getMenuInflater().inflate(R.menu.gosterme_menu,popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.fotoGoster:
                        Intent intentNote = new Intent(NoteDisplay.this,MediaDisplay.class);
                        intentNote.putExtra("media_id",not.getId());
                        startActivityForResult(intentNote,3);
                        break;
                    case R.id.videoGoster:
                        Intent i = new Intent(NoteDisplay.this,VideoDisplay.class);
                        i.putExtra("video_id",not.getId());
                        startActivityForResult(i,5);
                        break;
                    case R.id.sesKayitGoster:
                        if (not!=null){
                            Intent in = new Intent(NoteDisplay.this, VoiceDisplay.class);
                            in.putExtra("ses_id",not.getId());
                            startActivityForResult(in,voiceResultCode);
                        }else{
                            Toast.makeText(NoteDisplay.this,"Önce notu kaydediniz", LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.tDosyaGoster:
                        break;
                }
                return false;
            }
        });
        popupMenu.show();

    }

    public void renkSec(View v){
        int radioId=radioGroup.getCheckedRadioButtonId();
       secili_renk_button=findViewById(radioId);

        if(secili_renk_button.getText().toString().equals("Yeşil")){
            secili_renk = (String.valueOf(0xFF99CC00));
      //      Toast.makeText(getBaseContext(),secili_renk_button.getText().toString()+"secildi",Toast.LENGTH_SHORT).show();
        }
        else if(secili_renk_button.getText().toString().equals("Mavi")){
            secili_renk = (String.valueOf(0xFF33B5E5));
        }
        else if(secili_renk_button.getText().toString().equals("Mor")){
            secili_renk = (String.valueOf(0xFFAA66CC));
        }
        else if(secili_renk_button.getText().toString().equals("Kırmızı")){
            secili_renk = (String.valueOf(0xFFCC0000));
        }
        else if(secili_renk_button.getText().toString().equals("Turuncu")){
            secili_renk = (String.valueOf(0xFFFF8800));
        }

    }



    public void btn_sil(View v){
        db.NotSil(not);
        finish();
    }

    public void MediaEkle(View v){
        PopupMenu popupMenu = new PopupMenu(NoteDisplay.this,ekle);
        popupMenu.getMenuInflater().inflate(R.menu.ekleme_menu,popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.fotoEkle:
                        if (ContextCompat.checkSelfPermission(NoteDisplay.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                            Intent intent1 = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent1.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent1, "Resim Yükle"), IMAGE_PICK);
                        } else {
                            requestStoragePermission();

                        }
                        break;
                    case R.id.videoEkle:
                        if (not!=null){
                        Intent i = new Intent(Intent.ACTION_PICK,MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i,videoResultCode);
                        }else {
                            Toast.makeText(NoteDisplay.this,"Önce notu kaydediniz", LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.sesKayitEkle:
                        if (not!=null){
                            Intent in = new Intent(NoteDisplay.this, VoiceDisplay.class);
                            in.putExtra("ses_id",not.getId());
                            in.putExtra("ekle_kont",1);
                            startActivityForResult(in,voiceResultCode);
                        }else{
                            Toast.makeText(NoteDisplay.this,"Önce notu kaydediniz", LENGTH_SHORT).show();
                        }

                        break;
                    case R.id.tDosyaEkle:
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if (resultCode == Activity.RESULT_OK && data!=null) {
            switch (requestCode) {
                case IMAGE_PICK:
                    selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    cursor.close();

     //               resim.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    Toast.makeText(this,"Seçilen Fotoğraf Yüklendi", LENGTH_SHORT).show();
                    if (picturePath != null)
                        not.setFoto(picturePath);
                    db.NoteGuncelle(not);
                    break;
                case videoResultCode:
                    selectedVideo = data.getData();
                    String selectedVideoPath = getPath(selectedVideo);
                    Log.d("path",selectedVideoPath);
                    Toast.makeText(this,"Seçilen Video Yüklendi", LENGTH_SHORT).show();
                    //saveVideoToInternalStorage(selectedVideoPath);
                    not.setVideo(selectedVideoPath);
                    db.NoteGuncelle(not);
                    break;
                case voiceResultCode:
                    Toast.makeText(this,"Kaydedilen Ses Yüklendi", LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }

    public void DatabaseGuncelle(){
        if (not != null){
            not.setBaslik(baslik.getText().toString());
            not.setIcerik(icerik.getText().toString());
            not.setAdres(adres.getText().toString());
            not.setRenk(secili_renk);
            if (checkBox.isChecked()==true)
                not.setOncelik("0");
            else not.setOncelik("1");
            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
            String date = df.format(Calendar.getInstance().getTime())+" (Düzenlendi.)";
            not.setTarih(date);
;
            db.NoteGuncelle(not);
        }else{
            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
            String date = df.format(Calendar.getInstance().getTime());
            String baslik_ismi=baslik.getText().toString();
            String icerik_tmp= icerik.getText().toString();
            String adres_tmp= adres.getText().toString();
            String oncelik_tmp="1";
            if (checkBox.isChecked()==true)
                oncelik_tmp="0";
            String fotom=null;
            if ( picturePath != null) {
                fotom = picturePath;
            }
            String videom = null;
            if (selectedVideo!=null)
                videom=selectedVideo.toString();
            db.NotEkle(new Note(baslik_ismi,icerik_tmp,date,adres_tmp,fotom,secili_renk,oncelik_tmp,videom));
        }

    }
    public void guncelle(View v){
        DatabaseGuncelle();
        finish();
    }

    public void konumBul(View v){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            alertMessage();
        }else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
           try {
               getLocation();
               konumToadres();
           }catch (Exception e){
               e.printStackTrace();
               Toast.makeText(this," Konum bulunamadı.",Toast.LENGTH_LONG).show();
           }
        }

    }

    private  void getLocation(){
        if( ActivityCompat.checkSelfPermission(NoteDisplay.this,Manifest.permission.ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NoteDisplay.this,Manifest.permission.ACCESS_COARSE_LOCATION )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(NoteDisplay.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);

        }else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null){
                latti = location.getLatitude();
                longi =location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

           //     adres.setText(lattitude+" - "+longitude);
            }else{
                Toast.makeText(this,"Konuma ulaşılamadı.", LENGTH_SHORT).show();
            }
        }
    }

    public void konumToadres(){
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latti, longi, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        adres.setText(address+" - "+city);
    }


    protected void alertMessage(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Lütfen GPS ayarınızı açın.")
        .setCancelable(false)
                .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){

            new android.app.AlertDialog.Builder(this).setTitle("İzin gerekli").setMessage("Fotoğraf yüklenmesi için bu izin gerekli").setPositiveButton("onayla", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(NoteDisplay.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);

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




    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {

            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

}
