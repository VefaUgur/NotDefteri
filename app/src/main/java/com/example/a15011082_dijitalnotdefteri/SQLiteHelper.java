package com.example.a15011082_dijitalnotdefteri;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final int database_Version =3;
    private static final String database_NAME = "NoteDB";
    private static final String table_NAME = "Notlar";
    private static final String note_id = "id";
    private static final String note_baslik = "Baslik";
    private static final String note_icerik = "Icerik";
    private static final String note_tarih = "Tarih";
    private static final String note_adres = "Adres";
    private static final String note_foto = "Foto";
    private static final String note_video = "Video";
    private static final String note_ses = "Ses";
    private static final String note_Dosya = "Dosya";
    private static final String note_Renk = "Renk";
    private static final String note_oncelik = "Oncelik";

    private static final String DATABASE_ALTER_TEAM_1 = "ALTER TABLE "
            + table_NAME + " ADD COLUMN " + note_adres + " TEXT";

    private static final String DATABASE_ALTER_TEAM_2 = "ALTER TABLE "
            + table_NAME + " ADD COLUMN " + note_foto + " TEXT";

    private static final String DATABASE_ALTER_TEAM_3 = "ALTER TABLE "
            + table_NAME + " ADD COLUMN " + note_video + " TEXT";

    private static final String DATABASE_ALTER_TEAM_4 = "ALTER TABLE "
            + table_NAME + " ADD COLUMN " + note_ses + " TEXT";

    private static final String DATABASE_ALTER_TEAM_5 = "ALTER TABLE "
            + table_NAME + " ADD COLUMN " + note_Dosya + " TEXT";



    private static final String[] COLUMNS = {note_id,note_baslik,note_icerik,note_tarih,note_adres,note_foto,note_video,note_ses,note_Dosya,note_Renk,note_oncelik};

    private static final String CREATE_BOOK_TABLE ="CREATE TABLE "
            + table_NAME+" ("
            + note_id+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + note_baslik +" TEXT, "
            + note_icerik+" TEXT, "
            + note_tarih+" TEXT, "
            +note_adres+" TEXT, "
            +note_foto+" TEXT, "
            +note_video+" TEXT, "
            +note_ses+" TEXT, "
            +note_Dosya+" TEXT, "
            +note_Renk+" TEXT, "
            +note_oncelik+" TEXT )";

    public SQLiteHelper( Context context) {

        super(context,database_NAME , null, database_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ table_NAME);
        this.onCreate(db);

    }

    public void NotEkle(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues degerler =  new ContentValues();
        degerler.put(note_baslik,note.getBaslik());
        degerler.put(note_icerik,note.getIcerik());
        degerler.put(note_tarih,note.getTarih());
        degerler.put(note_adres,note.getAdres());
        degerler.put(note_foto,note.getFoto());
        degerler.put(note_video,note.getVideo());
        degerler.put(note_ses,note.getSes());
 //     degerler.put(note_Dosya,note.getDosya());
        degerler.put(note_Renk,note.getRenk());
        degerler.put(note_oncelik,note.getOncelik());
        db.insert(table_NAME,null,degerler);
        db.close();


    }
    public List<Note> NotlariGetir(){
        List<Note> notlar = new ArrayList<>();
        String query =" SELECT * FROM "+table_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        Note note = null;
        if(cursor.moveToFirst()){
            do{
               note = new Note();
               note.setId(Integer.parseInt(cursor.getString(0)));
               note.setBaslik(cursor.getString(1));
               note.setIcerik(cursor.getString(2));
               note.setTarih(cursor.getString(3));
               note.setAdres(cursor.getString(4));
               note.setFoto(cursor.getString(5));
               note.setVideo(cursor.getString(6));
               note.setSes(cursor.getString(7));
     //        note.setDosya(cursor.getString(8));
                note.setRenk(cursor.getString(9));
                note.setOncelik(cursor.getString(10));
               notlar.add(note);
            }while(cursor.moveToNext());
        }
        return notlar;
    }
    public Note NoteOku(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(table_NAME,COLUMNS," id = ?",new String[]{String.valueOf(id)},null,null,null);
        if (cursor != null)
            cursor.moveToFirst();

        Note note = new Note ();
        note.setId(Integer.parseInt(cursor.getString(0)));
        note.setBaslik(cursor.getString(1));
        note.setIcerik(cursor.getString(2));
        note.setTarih(cursor.getString(3));
        note.setAdres(cursor.getString(4));
        note.setFoto(cursor.getString(5));
        note.setVideo(cursor.getString(6));
        note.setSes(cursor.getString(7));
  //      note.setDosya(cursor.getString(8));
        note.setRenk(cursor.getString(9));
        note.setOncelik(cursor.getString(10));
        return note;

    }

    public void NotSil(Note not){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table_NAME,note_id + " = ?",new String[]{String.valueOf(not.getId())});
        db.close();

    }
    public int NoteGuncelle(Note not){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues degerler =  new ContentValues();
        degerler.put("baslik",not.getBaslik());
        degerler.put("icerik",not.getIcerik());
        degerler.put(note_tarih,not.getTarih());
        degerler.put("adres",not.getAdres());
        degerler.put(note_foto,not.getFoto());
        degerler.put(note_video,not.getVideo());
        degerler.put(note_ses,not.getSes());
//      degerler.put(note_Dosya,not.getDosya());
        degerler.put(note_Renk,not.getRenk());
        degerler.put(note_oncelik,not.getOncelik());
        int i = db.update(table_NAME,degerler,note_id + " = ?",new String[]{String.valueOf(not.getId())});
        db.close();
        return i;
    }
}
