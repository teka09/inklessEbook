package com.devs.celtica.inkless.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.devs.celtica.inkless.PostServerRequest5;
import com.devs.celtica.inkless.Publications.AccueilBooksAdapter;
import com.devs.celtica.inkless.Publications.Book;
import com.devs.celtica.inkless.R;
import com.devs.celtica.inkless.Users.Writer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Accueil extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        if (savedInstanceState != null) {
            //region Revenir a au Login ..
            Intent intent = new Intent(getApplicationContext(), Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //endregion
        }else {


            //region recomanded affichage ..
            final RecyclerView recomendedDivAffich = (RecyclerView) findViewById(R.id.affichBook_recomended);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            recomendedDivAffich.setHasFixedSize(true);

            // use a linear layout manager
            final LinearLayoutManager mLayoutManager = new LinearLayoutManager(Accueil.this);
            mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recomendedDivAffich.setLayoutManager(mLayoutManager);
            final AccueilBooksAdapter recomandedAdapter=new AccueilBooksAdapter(this);
            recomandedAdapter.type_affich= AccueilBooksAdapter.TYPE_AFFICH.BEST_CATEGORY;
            recomendedDivAffich.setAdapter(recomandedAdapter);

            Login.reader.getBookOfBestCategory(0,new PostServerRequest5.doBeforAndAfterGettingData() {
                @Override
                public void before() {

                }

                @Override
                public void echec(Exception e) {
                    e.printStackTrace();
                }

                @Override
                public void After(String result) {
                    recomandedAdapter.addBooks(result);

                }
            });
            //endregion

            //region best sels affichage ..
            final RecyclerView bestSelsDivAffich = (RecyclerView) findViewById(R.id.affichBook_bestSels);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            bestSelsDivAffich.setHasFixedSize(true);

            // use a linear layout manager
            final LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(Accueil.this);
            mLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
            bestSelsDivAffich.setLayoutManager(mLayoutManager2);

            final AccueilBooksAdapter bestSelsAdapter  =new AccueilBooksAdapter(this);
            bestSelsAdapter.type_affich= AccueilBooksAdapter.TYPE_AFFICH.BEST_SELS;
            bestSelsDivAffich.setAdapter(bestSelsAdapter);
            Login.reader.getBookOfBestSels(0,new PostServerRequest5.doBeforAndAfterGettingData() {
                @Override
                public void before() {

                }

                @Override
                public void echec(Exception e) {
                    e.printStackTrace();
                }

                @Override
                public void After(String result) {
                    Log.e("kkk",result);
                    bestSelsAdapter.addBooks(result);

                }
            });
            //endregion
        }

    }
}
