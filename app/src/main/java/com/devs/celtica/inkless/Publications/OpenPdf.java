package com.devs.celtica.inkless.Publications;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.devs.celtica.inkless.Activities.Login;
import com.devs.celtica.inkless.PostServerRequest5;
import com.devs.celtica.inkless.R;
import com.devs.celtica.inkless.Users.Writer;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

import static android.os.SystemClock.sleep;
import static com.devs.celtica.inkless.Publications.ProfileBook.book;



public class OpenPdf extends AppCompatActivity {

    public int ISRATING=0;
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
    @Override
    public void onBackPressed() {


        book.isRating(Login.reader.id_user, new PostServerRequest5.doBeforAndAfterGettingData() {
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
                    ISRATING=obj.getInt("n");

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });
        sleep(500);
        if(ISRATING==0) ShowRat();

        else OpenPdf.super.onBackPressed();

 }



    public void ShowRat(){

        final Dialog rankDialog = new Dialog(OpenPdf.this);
        rankDialog.setContentView(R.layout.div_rating);
        rankDialog.setCancelable(true);
        final RatingBar ratingBar = (RatingBar) rankDialog.findViewById(R.id.dialog_ratingbar);
        ratingBar.setRating(2);
        Button BackRat = (Button) rankDialog.findViewById(R.id.backRat);
        Button Rat = (Button) rankDialog.findViewById(R.id.BtnRat);




        BackRat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            rankDialog.dismiss();
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            OpenPdf.super.onBackPressed();
            }
        });

        Rat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 final ProgressDialog progresS=new ProgressDialog(OpenPdf.this);
                  book.rating(Login.reader.id_user,ratingBar.getProgress(), new PostServerRequest5.doBeforAndAfterGettingData() {
                    @Override
                    public void before() {
                        rankDialog.dismiss();
                        progresS.setTitle("جاري التقييم ..");
                        progresS.setMessage("انتظر قليلا ..");
                        progresS.show();


                    }

                    @Override
                    public void echec(Exception e) {
                        progresS.dismiss();
                        OpenPdf.super.onBackPressed();
                        Toast.makeText(OpenPdf.this,"تأكد من اتصالك بالانترنت ..",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void After(String result) {
                        progresS.dismiss();
                        finish();
                    }
                });

            }
        });







        rankDialog.show();
    }


}
