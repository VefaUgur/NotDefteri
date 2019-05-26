package com.example.a15011082_dijitalnotdefteri;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

import static android.widget.Toast.LENGTH_SHORT;

public class Hatirlatici extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private ImageButton ayarla,cancel,datepicker;
    private TextView txt,date,kontrol;
    private Note not;
    private SQLiteHelper db;
    private Context context = this;
    public String msg;
    private Calendar cal_date = Calendar.getInstance();
    private long tekrar;
    private boolean tekrarlama=false;
    private Spinner spiner;

    String[] sureler = new String[] {
            "Tekrarlama ",
            "5 dk",
            "10 dk",
            "15 dk",
            "30 dk",
            "360 dk"

    };


    public void Find(){
        txt= findViewById(R.id.noAlarm);
        date = findViewById(R.id.date);
        datepicker = findViewById(R.id.datepicker);
        cancel=findViewById(R.id.cancelAlarm);
        ayarla = findViewById(R.id.selAlarm);
        kontrol = findViewById(R.id.kontrol);
        spiner = findViewById(R.id.spinner);


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hatirlatici);

        Find();
        db = new SQLiteHelper(context);

        Intent intent = getIntent();
        int id =intent.getIntExtra("reminder_id",-1);
        not = db.NoteOku(id);

        if (not.getSes() != null){ // Ses hatırlatıcı bilgilerini tutuyor
            String tmp[]=not.getSes().split("-");
            txt.setText(tmp[0]);
            date.setText(tmp[1]);
            kontrol.setVisibility(View.VISIBLE);
        }

        if (not.getBaslik() == null){   //Notifikasyonun başlığı için kontrol
            msg=" Notlarınıza bakın." ;
        }else{
            msg=not.getBaslik()+" adlı notunuza bakın.";
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, sureler);
        spiner.setAdapter(adapter);
        AdapterView.OnItemSelectedListener timeSelect = new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> spinner, View container,
                                       int position, long id) {

                switch (sureler[position]){
                    case "5 dk":
                        tekrarlama=true;
                        tekrar = 5*60*1000;
                        Toast.makeText(Hatirlatici.this,sureler[position]+" seçildi.",Toast.LENGTH_SHORT).show();
                        break;
                    case "10 dk":
                        tekrarlama=true;
                        tekrar = 10*60*1000;
                        Toast.makeText(Hatirlatici.this,sureler[position]+" seçildi.",Toast.LENGTH_SHORT).show();
                        break;
                    case "15 dk":
                        tekrarlama=true;
                        tekrar = 15*60*1000;
                        Toast.makeText(Hatirlatici.this,sureler[position]+" seçildi.",Toast.LENGTH_SHORT).show();
                        break;
                    case "30 dk":
                        tekrarlama=true;
                        tekrar = 30*60*1000;
                        Toast.makeText(Hatirlatici.this,sureler[position]+" seçildi.",Toast.LENGTH_SHORT).show();
                        break;
                    case "60 dk":
                        tekrarlama=true;
                        tekrar =60*60*1000;
                        Toast.makeText(Hatirlatici.this,sureler[position]+" seçildi.",Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                tekrarlama=false;
            }
        };

        spiner.setOnItemSelectedListener(timeSelect);



        //Hatırlatıcı saat ayarlama
        ayarla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timepicker = new TimePickerFragment();
                timepicker.show(getSupportFragmentManager(),"time picker");
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });

        // Hatırlatıcı tarih ayarlama
        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"Date Picker");
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
        c.set(Calendar.MINUTE,minute);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.YEAR,cal_date.get(Calendar.YEAR));
        c.set(Calendar.MONTH,cal_date.get(Calendar.MONTH));
        c.set(Calendar.DAY_OF_MONTH,cal_date.get(Calendar.DAY_OF_MONTH));

        updateTimeText(c);

        startAlarm(c);
    }

    private void updateTimeText(Calendar c){
        String timetext = " için ayarlandı. ";
        timetext = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime()) + timetext;
        txt.setText(timetext);
    }

    private void startAlarm(Calendar c_time){
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent in = new Intent(this, AlarmReceiver.class);

        in.putExtra("Message",msg);
   //     Toast.makeText(this,msg, Toast.LENGTH_LONG).show();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,not.getId(),in,0);


        if(c_time.before(Calendar.getInstance())){
            Toast.makeText(this,"Lütfen geçerli bir zaman dilimi seçiniz.", LENGTH_SHORT).show();
            cancelAlarm();
        }else if(tekrarlama==true){
            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, c_time.getTimeInMillis(),tekrar,pendingIntent);
            not.setSes(txt.getText().toString()+ "-" +date.getText().toString());
            db.NoteGuncelle(not);
        }else{
            alarm.setExact(AlarmManager.RTC_WAKEUP, c_time.getTimeInMillis(),pendingIntent);
            not.setSes(txt.getText().toString()+ "-" +date.getText().toString());
            db.NoteGuncelle(not);
        }


    }

    private void cancelAlarm(){
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent in = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,not.getId(),in,0);

        alarm.cancel(pendingIntent);
        date.setText("İptal edildi.");
        txt.setText("İptal edildi.");
        if (not.getSes() != null){
            not.setSes(null);
            db.NoteGuncelle(not);
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        cal_date.set(Calendar.YEAR,year);
        cal_date.set(Calendar.MONTH,month);
        cal_date.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        String currendate= DateFormat.getDateInstance(DateFormat.FULL).format(cal_date.getTime());
        date.setText(currendate);
    }
}
