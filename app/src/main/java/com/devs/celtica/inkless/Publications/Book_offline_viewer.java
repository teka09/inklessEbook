package com.devs.celtica.inkless.Publications;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import com.devs.celtica.inkless.PostServerRequest5;
import com.devs.celtica.inkless.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;


public class Book_offline_viewer extends AppCompatActivity {
    PDFView pdfView;
    File pdfbook;
    PostServerRequest5 forbook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_offline_viewer);

        pdfView=(PDFView)findViewById(R.id.pdfView);

        Log.d("#######################", "MOHAMED COSANOSTRA HADJERSI");
        Log.d("MOHAMED COSANOSTRA : ", "MON PDF =============>"+afficher_book_offline.nom_pdf);



        // pdfbook = new File(getFilesDir().getPath()+"/"+afficher_book_offline.nom_pdf);
        pdfbook =new File(getFilesDir(),afficher_book_offline.nom_pdf);
        pdfView.fromFile(pdfbook).load();



    }
}
