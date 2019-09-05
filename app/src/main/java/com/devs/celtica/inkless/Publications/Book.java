
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
import com.devs.celtica.inkless.Users.Writer;

import java.util.ArrayList;
import java.util.HashMap;

public class Book extends Publication{

    public String lien_resume,photo,maison_edition,nom1,nom2,isbn,description,categorie;
    public boolean hasPaperVersion;
    public double prixPaper,prixPdf;
    public Writer auteur;

    public Book(int id_pub, String lien_resume, String lien, String photo, String maison_edition_auteur, String nom1, String nom2, String categorie,String date_pub,Writer auteur,boolean hasPaperVersion, double prixPaper, double prixPdf) {
        super(id_pub, date_pub, "book", lien);
        this.lien_resume = lien_resume;
        this.photo = photo;
        this.maison_edition = maison_edition_auteur;
        this.nom1 = nom1;
        this.nom2 = nom2;
        this.categorie=categorie;
        this.auteur=auteur;
    }

    //this contruct for uploading to BD ..
    public Book(String maison_edition_auteur, String nom1, String nom2, String isbn, String description,String categorie, boolean hasPaperVersion, double prixPaper, double prixPdf) {
        this.maison_edition = maison_edition_auteur;
        this.nom1 = nom1;
        this.nom2 = nom2;
        this.isbn = isbn;
        this.description = description;
        this.hasPaperVersion = hasPaperVersion;
        this.prixPaper = prixPaper;
        this.prixPdf = prixPdf;
        this.categorie=categorie;
    }

    public void uploadBook(final AppCompatActivity c, ArrayList<Uri> files){
        Login.ajax.setUrlWrite("/upload_files.php");
        HashMap<String,String> data=new HashMap<String,String>();

        data.put("upload_type","book");
        data.put("writer",Login.reader.id_user+"");
        data.put("categorie",categorie);
        data.put("titre1",nom1);
        data.put("titre2",nom2);
        data.put("prix_paper",prixPaper+"");
        data.put("prix_pdf",prixPdf+"");
        data.put("maison_edition",maison_edition);
        data.put("isbn",isbn+"");
        data.put("book_desc",description+"");
        if (hasPaperVersion)
            data.put("has_paper_version","1");
        else
            data.put("has_paper_version","0");

        Login.ajax.sendWithFiles(data, files, c, new PostServerRequest5.doBeforAndAfterUpload() {
            @Override
            public void onProgress(long numBytes, long totalBytes, float percent, float speed) {
                ProgressBar progressBar=(ProgressBar)((UploadPdf)c).progressView.findViewById(R.id.div_progressbar_bar);
                progressBar.setProgress((int) (percent*100));
                TextView perrcent=(TextView)((UploadPdf)c).progressView.findViewById(R.id.div_progressbar_pourcent);
                perrcent.setText(String.format("%.2f",Double.parseDouble(percent*100+""))+" %");
            }

            @Override
            public void before() {
                UploadPdf cc=(UploadPdf)c;
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
                        UploadPdf.isSended=false;
                        ((UploadPdf)c).ad.dismiss();
                    }
                });

            }

            @Override
            public void After(final String result) {

                c.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("rrr","upload msg: "+result+" / "+result.trim().length());
                        ((UploadPdf)c).ad.dismiss();
                        UploadPdf.isSended=false;

                        if (result.replaceAll("\n","").trim().equals("good")){
                            Toast.makeText(c,c.getResources().getString(R.string.uploadBook_succ),Toast.LENGTH_SHORT).show();
                        }else {
                           Toast.makeText(c,c.getResources().getString(R.string.uploadBook_err),Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });
    }

    public void getAllAudios(int offset ,PostServerRequest5.doBeforAndAfterGettingData callback){
        HashMap<String,String> datas=new HashMap<>();
        datas.put("1",id_pub+"");
        Login.ajax.read("SELECT p.id_pub, \n" +
                "       p.date, \n" +
                "       p.approuver, \n" +
                "       audio.NAME, \n" +
                "       audio_writter.id_writter, \n" +
                "       audio_narrator.id_narrator, \n" +
                "       user.nom, \n" +
                "       user.email \n" +
                "FROM   audio \n" +
                "       INNER JOIN publication p \n" +
                "               ON audio.id_audio = p.id_pub \n" +
                "       LEFT JOIN audio_writter \n" +
                "              ON audio_writter.id_audio = audio.id_audio \n" +
                "       LEFT JOIN audio_narrator \n" +
                "              ON audio_narrator.id_audio = audio.id_audio \n" +
                "       INNER JOIN user \n" +
                "               ON ( audio_writter.id_writter = user.id_user \n" +
                "                     OR audio_narrator.id_narrator = user.id_user ) \n" +
                "WHERE  audio.id_book = ? order by id_pub desc limit 60 OFFSET "+offset,datas,callback);
    }

    public void getAudiosByWriter(String id_writer,AppCompatActivity c,PostServerRequest5.doBeforAndAfterGettingData callback){
        HashMap<String,String> datas=new HashMap<>();
        datas.put("1",id_pub+"");
        datas.put("2",id_writer+"");
        Login.ajax.read("SELECT * from publication inner JOIN audio on audio.id_audio=publication.id_pub inner join audio_writter on audio.id_audio=audio_writter.id_audio where audio.id_book=? ORDER BY publication.date desc",datas,callback);
    }
    public void rating(int id_user,int rat,PostServerRequest5.doBeforAndAfterGettingData callback){
        HashMap<String,String> data=new HashMap<>();
        Login.ajax.setUrlWrite("/write.php");
        data.put("id_user",""+id_user);
        data.put("id_pub",""+id_pub);
        data.put("rat",""+rat);
        data.put("request","rating");
        Login.ajax.send(data,callback);



    }

    public void isRating(int idUSer,PostServerRequest5.doBeforAndAfterGettingData callback){
        HashMap<String,String> datas=new HashMap<>();

        datas.put("1",idUSer+"");
        datas.put("2",id_pub+"");
        Login.ajax.setUrlRead( "/read.php" );
        Login.ajax.read("SELECT count(note) as n ,sum(note) as S FROM `rating` where id_user=? and id_pub=?",datas,callback);



    }
    public void setRat(PostServerRequest5.doBeforAndAfterGettingData callback){
        HashMap<String,String> datas=new HashMap<>();

        datas.put("1",id_pub+"");
        Login.ajax.setUrlRead( "/read.php" );
        Login.ajax.read("SELECT count(note) as n ,sum(note) as S FROM `rating` where  id_pub=?",datas,callback);




    }

}
