/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.devs.celtica.inkless.Publications;


import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.devs.celtica.inkless.Activities.Login;
import com.devs.celtica.inkless.PostServerRequest5;
import com.devs.celtica.inkless.R;
import com.devs.celtica.inkless.Users.ReaderFull;

import java.util.ArrayList;
import java.util.HashMap;

public class Audio extends Publication {
 public int  id_book;
 public String nom;

    public Audio(int id_book) {
        this.id_book = id_book;
    }

    public Audio(String nom, int id_pub, int id_book, String date_pub){
     super(id_pub,date_pub, "audio", "");
     this.id_book=id_book;
     this.nom=nom;


 }

    public Audio(int id_pub, int id_book,String date_pub) {
        super(id_pub,date_pub, "audio", "");

        this.id_book=id_book;
    }

    public Audio(String nom, int id_book, String date_pub, String type, String lien){
        this.id_book=id_book;

    }

    public void getTracks(int offset ,PostServerRequest5.doBeforAndAfterGettingData callback){
        HashMap<String,String> datas=new HashMap<>();
        datas.put("1",id_pub+"");
        Login.ajax.read("select * from track where id_audio=? order by idtrack desc limit 60 offset "+offset,datas,callback);
    }


    public void uploadAudio(final AppCompatActivity c,HashMap<String,String> datas ,ArrayList<TrackForUpload> tracks){
        Login.ajax.setUrlWrite("/upload_files.php");

        ArrayList<Uri> files=new ArrayList<>();
        datas.put("upload_type","audio");
        datas.put("id_book",id_book+"");
        int i=1;
        for (TrackForUpload t:tracks) {
            if(t.isValide()){
                datas.put("titre"+(i++),t.titre);
                files.add(t.audioFile);
            }
        }
        Login.ajax.sendWithFiles(datas, files, c, new PostServerRequest5.doBeforAndAfterUpload() {
            @Override
            public void onProgress(long numBytes, long totalBytes, float percent, float speed) {
                //region MAJ   progressbar ..
                ProgressBar progressBar=(ProgressBar)((UploadAudio)c).progressView.findViewById(R.id.div_progressbar_bar);
                progressBar.setProgress((int) (percent*100));
                TextView perrcent=(TextView)((UploadAudio)c).progressView.findViewById(R.id.div_progressbar_pourcent);
                perrcent.setText(String.format("%.2f",Double.parseDouble(percent*100+""))+" %");

            }

            @Override
            public void before() {

                //region affichage le div de progressbar ..
                UploadAudio cc =(UploadAudio)c;
                ((TextView)cc.progressView.findViewById(R.id.div_progressbar_msg)).setText(cc.getResources().getString(R.string.uploadBook_uploadWait));
                cc.ad.show();
                cc.ad.setCanceledOnTouchOutside(false); //ne pas fermer on click en dehors ..

            }

            @Override
            public void echec(Exception e) {
                e.printStackTrace();
                c.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(c,c.getResources().getString(R.string.uploadBook_err),Toast.LENGTH_SHORT).show();
                        UploadAudio.isSended=false;
                        ((UploadAudio)c).ad.dismiss();
                    }
                });
            }

            @Override
            public void After(String result) {
                Log.e("rrr","upload msg: "+result+" / "+result.trim().length());
                ((UploadAudio)c).ad.dismiss();
                UploadAudio.isSended=false;
                if (result.replaceAll("\n","").trim().equals("good")){
                    //c.finish();
                    c.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(c,c.getResources().getString(R.string.uploadBook_succ),Toast.LENGTH_SHORT).show();
                        }
                    });

                }else {
                    c.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(c,c.getResources().getString(R.string.uploadBook_err),Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });
    }
 
    
}
