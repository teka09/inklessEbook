package com.devs.celtica.inkless.Publications;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.devs.celtica.inkless.Activities.Login;
import com.devs.celtica.inkless.R;
import com.devs.celtica.inkless.Users.Narrator;
import com.devs.celtica.inkless.Users.Writer;


public class UploadAudio extends AppCompatActivity {

    TrackUploadAdapter mAdapter;
    public static Book book;
    public  static boolean isSended=false;
    AlertDialog ad;
    public ProgressDialog progress;
    View progressView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_audio);
        if (savedInstanceState != null) {
            //region Revenir a au Login ..
            Intent intent = new Intent(getApplicationContext(), Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //endregion
        }else {

            progress=new ProgressDialog(this);

            progressView= getLayoutInflater().inflate(R.layout.div_progressbar,null);
            AlertDialog.Builder mb = new AlertDialog.Builder(this); //c est l activity non le context ..
            mb.setView(progressView);
            ad=mb.create();

            //region Configuration de recyclervew ..
            final RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.div_upload_audio);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            final LinearLayoutManager mLayoutManager = new LinearLayoutManager(UploadAudio.this);
            mRecyclerView.setLayoutManager(mLayoutManager);


            // specify an adapter (see also next example)
            mAdapter = new TrackUploadAdapter(UploadAudio.this);

            mRecyclerView.setAdapter(mAdapter);
            ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    int position = viewHolder.getAdapterPosition();
                    TrackUploadAdapter.audios.remove(position);
                    mAdapter.notifyDataSetChanged();
                }
            });
            itemTouchHelper.attachToRecyclerView(mRecyclerView);

            int i=0;
            while (i != 6){
                TrackUploadAdapter.audios.add(new TrackForUpload());
                i++;

            }
            mAdapter.notifyDataSetChanged();

            //region ajouter un champ de nouveau track..
            ((TextView)findViewById(R.id.uploadAudio_addPlus)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TrackUploadAdapter.audios.add(new TrackForUpload());
                    mAdapter.notifyItemInserted(mAdapter.getItemCount());
                    mRecyclerView.scrollToPosition(TrackUploadAdapter.audios.size()-1);

                }
            });
            //endregion

            //endregion

            //region valider upload l audio ..
            ((TextView)findViewById(R.id.uploadAudio_valider)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mAdapter.isPanierVide()){
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.uploadAudio_noAudio),Toast.LENGTH_SHORT).show();
                    }else {
                        if (!isSended) {
                            isSended=true;
                            if(Login.reader instanceof Writer) {
                                AudioWriter audio = new AudioWriter(book.id_pub, (Writer) Login.reader);
                                audio.uploadAudio(UploadAudio.this, TrackUploadAdapter.audios);
                            }else {
                                AudioNarrator audio = new AudioNarrator(book.id_pub, (Narrator) Login.reader);
                                audio.uploadAudio(UploadAudio.this, TrackUploadAdapter.audios);
                            }
                        }else {
                            ad.show();
                        }


                    }
                }
            });
            //endregion

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Uri file=data.getData();
            TrackUploadAdapter.audios.get(TrackUploadAdapter.itemSelected).audioFile=file;
            mAdapter.notifyItemChanged(TrackUploadAdapter.itemSelected);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TrackUploadAdapter.audios.clear();
    }
}
