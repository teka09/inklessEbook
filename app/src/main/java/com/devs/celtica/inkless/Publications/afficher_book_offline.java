package com.devs.celtica.inkless.Publications;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.devs.celtica.inkless.R;
import com.devs.celtica.inkless.Users.Profile;

import java.util.ArrayList;

public class afficher_book_offline extends AppCompatActivity {
    Context ctx;
    Activity activity;
    RecyclerView recyclerView;
    AfficherBookAdapterOffline adapter;
    RecyclerView.LayoutManager layoutManager;

    //pour le lien du Book
    static String nom_pdf="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_book_offline);

        // pour mon recycler view
        recyclerView =(RecyclerView)findViewById(R.id.affichBook_divAffich_offline);
        layoutManager=new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //Pour DataBase
        adapter=new AfficherBookAdapterOffline(ProfileBook.arrayList_book,ctx);

        recyclerView.setAdapter(adapter);


        // Pour ItemClick

        //test click to open resume

        adapter.setOnItemClickListener(new AfficherBookAdapterOffline.OnItemClickListener() {





            @Override
            public void onPdfClick(int position) {


                Log.d("#######################", String.valueOf(position));
                Log.d("lien du book est  : ", (ProfileBook.book.lien));

                Log.d("le Nom livre  est  : ",(ProfileBook.book.nom1));
                Log.d("le 2éme Nomlivre  est :",(ProfileBook.book.nom2));
                Log.d("lien Image : ",(ProfileBook.book.photo));

                Log.d("######################","#######################");

                nom_pdf=ProfileBook.arrayList_book.get(position).nom1+".pdf";
                startActivity(new Intent(afficher_book_offline.this,Book_offline_viewer.class));



            }

            @Override
            public void onItemClick(int position)  {

                Log.d("#######################", String.valueOf(position));
                Log.d("lien du book est  : ", (ProfileBook.book.lien));
                Log.d("lien Image : ",(ProfileBook.book.photo));
                Log.d("le Nom livre  est  : ",(ProfileBook.book.nom1));
                Log.d("le 2éme Nomlivre  est :",(ProfileBook.book.nom2));
                Log.d("######################","#######################");


                Toast.makeText(afficher_book_offline.this,"إسم الكتاب "+ ProfileBook.arrayList_book.get(position).nom1+".pdf",Toast.LENGTH_LONG).show();


            }



        });

    }
}
