/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.devs.celtica.inkless.Publications;

import android.support.v7.app.AppCompatActivity;

import com.devs.celtica.inkless.Users.Writer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author INFO
 */
public class AudioWriter extends Audio{

    public Writer writer;

    //pour l affichages des audios ..
    public AudioWriter(String nom, int id_pub, int id_book, String date_pub,Writer w) {
        super(nom, id_pub, id_book,date_pub);
        this.writer=w;
    }

    public AudioWriter(int id_book , Writer writer) {
        super(id_book);
        this.writer = writer;
    }

    public void uploadAudio(AppCompatActivity c, ArrayList<TrackForUpload> tracks){
        HashMap<String,String> datas=new HashMap<>();
        datas.put("audio_for","writter");
        datas.put("id_writter",""+writer.id_user);
        super.uploadAudio(c,datas,tracks);

    }
}
