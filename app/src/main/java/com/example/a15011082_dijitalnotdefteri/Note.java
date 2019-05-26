package com.example.a15011082_dijitalnotdefteri;

import android.graphics.Color;

public class Note {
    private  int id;
    private String baslik;
    private String icerik;
    private String tarih;
    private String adres;
    private String ses;
    private String foto;
    private String video;
    private String dosya;
    private String renk;
    private String oncelik="1";

    public Note() {
    }



    public Note(String baslik, String icerik, String tarih,String adres,String foto,String renk,String oncelik,String video) {
        this.baslik = baslik;
        this.icerik = icerik;
        this.tarih = tarih;
        this.adres = adres ;
        this.foto = foto ;
        this.renk=renk;
        this.oncelik=oncelik;
        this.video = video ;
    }

    public String getRenk() {
        return renk;
    }

    public void setRenk(String renk) {
        this.renk = renk;
    }

    public String getOncelik() {
        return oncelik;
    }

    public void setOncelik(String oncelik) {
        this.oncelik = oncelik;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getIcerik() {
        return icerik;
    }

    public void setIcerik(String icerik) {
        this.icerik = icerik;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getSes() {
        return ses;
    }

    public void setSes(String ses) {
        this.ses = ses;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getDosya() {
        return dosya;
    }

    public void setDosya(String dosya) {
        this.dosya = dosya;
    }
}