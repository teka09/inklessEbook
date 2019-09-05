package com.devs.celtica.inkless.Publications;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.devs.celtica.inkless.Activities.Login;
import com.devs.celtica.inkless.PostServerRequest5;
import com.devs.celtica.inkless.R;
import com.devs.celtica.inkless.Users.Writer;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;

import java.io.InputStream;



public class OpenPdf extends AppCompatActivity {

    PDFView pdfView;
    public ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_pdf);
        if (savedInstanceState != null) {
            //region Revenir a au Login ..
            Intent intent = new Intent(getApplicationContext(), Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //endregion
        }else {

            pdfView=(PDFView)findViewById(R.id.pdfView);
            progress=new ProgressDialog(this);




            String lien;
            if (Login.reader.contrat_reader_valide ||  (Login.reader instanceof Writer && ((Writer)Login.reader).contrat_writer_valide) ){
                lien=ProfileBook.book.lien;
            }else {
                lien=ProfileBook.book.lien_resume;
            }

            Login.ajax.setUrlRead("/"+lien);
            Login.ajax.readPdfFile(this, new PostServerRequest5.doBeforAndAfterGettingFile() {
                @Override
                public void before() {
                    progress.show();
                }

                @Override
                public void echec(Exception e) {
                    e.printStackTrace();
                    progress.dismiss();
                }

                @Override
                public void After(InputStream result) {
                    pdfView.fromStream(result).onLoad(new OnLoadCompleteListener() {
                        @Override
                        public void loadComplete(int nbPages) {
                            progress.dismiss();
                        }
                    }).load();//from server ..

                }
            });
        }
    }
}
