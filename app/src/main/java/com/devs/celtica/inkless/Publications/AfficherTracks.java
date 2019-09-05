package com.devs.celtica.inkless.Publications;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.devs.celtica.inkless.Activities.Login;
import com.devs.celtica.inkless.PostServerRequest5;
import com.devs.celtica.inkless.R;

public class AfficherTracks extends AppCompatActivity {

    ProgressDialog progress;
    AfficherTrackAdapter mAdapter;
    public static Audio audioSlected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_tracks);
        if (savedInstanceState != null) {
            //region Revenir a au Login ..
            Intent intent = new Intent(getApplicationContext(), Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //endregion
        }else {

            //region Configuration de recyclervew ..
            progress=new ProgressDialog(this);
            final RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.affTrack_divAffich);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            final LinearLayoutManager mLayoutManager = new LinearLayoutManager(AfficherTracks.this);
            mRecyclerView.setLayoutManager(mLayoutManager);


            // specify an adapter (see also next example)
            mAdapter= new AfficherTrackAdapter(AfficherTracks.this);

            mRecyclerView.setAdapter(mAdapter);

            Login.ajax.setUrlWrite("/read.php");
            audioSlected.getTracks(0,new PostServerRequest5.doBeforAndAfterGettingData() {
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
                    Log.e("trr",result+"");
                    progress.dismiss();
                    mAdapter.addTracks(result);
                }
            });
            //endregion

        }
    }
}
