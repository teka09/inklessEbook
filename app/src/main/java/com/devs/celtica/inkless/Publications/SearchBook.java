package com.devs.celtica.inkless.Publications;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.devs.celtica.inkless.Activities.Login;
import com.devs.celtica.inkless.PostServerRequest5;
import com.devs.celtica.inkless.R;
import com.devs.celtica.inkless.Users.Writer;

public class SearchBook extends AppCompatActivity {
    AfficherBookSearchADapter mAdapter;
    public ProgressDialog progress;
    public LinearLayout divSearch;
    public EditText searchInp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);
        if (savedInstanceState != null) {
            //region Revenir a au Login ..
            Intent intent = new Intent(getApplicationContext(), Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //endregion

        }else {

            divSearch=(LinearLayout)findViewById(R.id.searchBook_divSearch);
            searchInp=(EditText)findViewById(R.id.searchBook_searchInp);

            //region Configuration de recyclervew ..
            progress=new ProgressDialog(this);
            final RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.searchBook_divAffich);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            final LinearLayoutManager mLayoutManager = new LinearLayoutManager(SearchBook.this);
            mRecyclerView.setLayoutManager(mLayoutManager);


            // specify an adapter (see also next example)
            mAdapter = new AfficherBookSearchADapter(SearchBook.this);

            mRecyclerView.setAdapter(mAdapter);

            search(searchInp.getText().toString()+"");

             //endregion

            ((LinearLayout)findViewById(R.id.searchBook_openDivSearchButt)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    divSearch.setVisibility(View.VISIBLE);
                    searchInp.requestFocus();
                    Login.reader.openClavier(SearchBook.this);
                }
            });

            ((ImageView)findViewById(R.id.searchBook_serachButt)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    divSearch.setVisibility(View.GONE);
                    search(searchInp.getText().toString()+"");
                    searchInp.setText("");
                    Login.reader.fermerClavier(SearchBook.this);
                }
            });


        }
    }

    public void search(String hint){
        mAdapter.books.clear();
        Login.ajax.setUrlWrite("/read.php");
        Login.reader.getSearchBook(0,hint,new PostServerRequest5.doBeforAndAfterGettingData() {
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
            public void After(String result) {
                Log.e("sssearch",result+"");
                progress.dismiss();
                mAdapter.addBooks(result);
            }
        });
    }

    @Override
    public void onBackPressed() {


        if (divSearch.getVisibility() == View.VISIBLE) divSearch.setVisibility(View.GONE);
        else  super.onBackPressed();
    }
}
