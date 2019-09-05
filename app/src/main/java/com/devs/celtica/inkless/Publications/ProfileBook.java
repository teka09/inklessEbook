package com.devs.celtica.inkless.Publications;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.devs.celtica.inkless.Activities.Accueil;
import com.devs.celtica.inkless.Activities.Login;
import com.devs.celtica.inkless.PostServerRequest5;
import com.devs.celtica.inkless.R;
import com.devs.celtica.inkless.Users.Narrator;
import com.devs.celtica.inkless.Users.Writer;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileBook extends AppCompatActivity {

    public static Book book;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_book);
        final RatingBar ratin2 = findViewById(R.id.profileBook_rating2);
        final RatingBar ratin = findViewById(R.id.profileBook_rating);
        book.setRat(new PostServerRequest5.doBeforAndAfterGettingData() {
            @Override
            public void before() {

            }

            @Override
            public void echec(Exception e) {

            }

            @Override
            public void After(String result) {
                JSONArray res = null;
                try {
                    res = new JSONArray(result);
                    JSONObject obj=res.getJSONObject(0);
                    if(obj.getInt("n")>0){
                    ratin.setRating(obj.getInt("S")/obj.getInt("n"));
                    ratin2.setRating(obj.getInt("S")/obj.getInt("n"));}
                    else{
                        ratin.setRating(0);
                        ratin2.setRating(0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });





        if (savedInstanceState != null) {
            //region Revenir a au Login ..
            Intent intent = new Intent(getApplicationContext(), Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //endregion
        }else {



            //region remplssage des infos de book ..
            ((TextView)findViewById(R.id.profileBook_writer)).setText(book.auteur.nom);
            ((TextView)findViewById(R.id.profileBook_nom1)).setText(book.nom1);
            ((TextView)findViewById(R.id.profileBook_nom2)).setText(book.nom2);
            ((TextView)findViewById(R.id.profileBook_maisonEdition)).setText(book.maison_edition);
            ((TextView)findViewById(R.id.profileBook_date)).setText(book.date_pub);
            ((TextView)findViewById(R.id.profileBook_category)).setText(book.categorie);
            Glide.with(ProfileBook.this)
                    .load(Login.ajax.url+"/"+book.photo)
                    .thumbnail(Glide.with(ProfileBook.this).load(R.drawable.wait))
                    .error(Glide.with(ProfileBook.this).load(R.drawable.bg_butt_bleu_fonce))
                    .apply(new RequestOptions().override(400, 600))
                    .into(((ImageView)findViewById(R.id.profileBook_photo)));
            //endregion

            //region read book ..
            ((LinearLayout)findViewById(R.id.profileBook_readButt)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ProfileBook.this,OpenPdf.class));

                }
            });
            //endregion

            //region listen to audio ..
            ((LinearLayout)findViewById(R.id.profileBook_audioButt)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ProfileBook.this,AfficherAudio.class));

                }
            });
            //endregion

            //region Upload audio for book
            if(Login.reader.id_user == book.auteur.id_user || Login.reader instanceof Narrator){
                ((LinearLayout)findViewById(R.id.profile_uploadAudioButt)).setVisibility(View.VISIBLE);
                ((LinearLayout)findViewById(R.id.profile_uploadAudioButt)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UploadAudio.book=book;
                        startActivity(new Intent(ProfileBook.this,UploadAudio.class));
                    }
                });
            }
            //endregion


        }
    }


}
