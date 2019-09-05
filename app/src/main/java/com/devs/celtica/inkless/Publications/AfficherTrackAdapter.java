package com.devs.celtica.inkless.Publications;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.devs.celtica.inkless.Activities.Login;
import com.devs.celtica.inkless.PostServerRequest5;
import com.devs.celtica.inkless.R;
import com.devs.celtica.inkless.Users.Narrator;
import com.devs.celtica.inkless.Users.Writer;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AfficherTrackAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    AppCompatActivity c;
    public  ArrayList<Track> tracks=new  ArrayList<>();
    public static int ItemSelected;
    private  MediaPlayer mediaPlayer;

    public AfficherTrackAdapter(AppCompatActivity c) {
        this.c = c;

    }

    public static class TrackView extends RecyclerView.ViewHolder  {

        TextView titre;
        FrameLayout playButt;
        LinearLayout body;

        public TrackView(View v) {
            super(v);
            titre=(TextView) v.findViewById(R.id.divTrack_titre);
            playButt=(FrameLayout)v.findViewById(R.id.divTrack_playButt);
            body=(LinearLayout)v.findViewById(R.id.body);

        }
    }

    public static class AddPlusView extends RecyclerView.ViewHolder  {

        LinearLayout addPlusButt;
        public AddPlusView(View v) {
            super(v);
            addPlusButt=(LinearLayout) v.findViewById(R.id.addPlusButt);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if(viewType==1){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.div_track,parent,false);
            TrackView vh = new TrackView(v);
            return vh;
        }else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.div_add_plus2_butt,parent,false);
            AddPlusView vh = new AddPlusView(v);
            return vh;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        if (position !=tracks.size()) {
            //afficher le div audio avec configuration ..
            ((TrackView)holder).titre.setText(tracks.get(position).titre+"");
            ((TrackView)holder).playButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    final ProgressDialog progress = new ProgressDialog(c); // activité non context ..

                    progress.setTitle("Uploading");
                    progress.setMessage("Please wait...");
                    progress.show();

                    //region setup media player ..
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        public boolean onError(MediaPlayer mp, int what, int extra) {
                            mp.reset();
                            return false;
                        }
                    });

                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        public void onPrepared(MediaPlayer mp) {




                            AlertDialog.Builder mb = new AlertDialog.Builder(c); //c est l activity non le context ..

                            View v= c.getLayoutInflater().inflate(R.layout.div_play_audio,null);
                            TextView titre=(TextView) v.findViewById(R.id.divPlayAudio_titre);
                            TextView currentTime=(TextView) v.findViewById(R.id.divPlayAudio_currentTime);
                            TextView finalTime=(TextView) v.findViewById(R.id.divPlayAudio_finalTime);
                            ImageView play=(ImageView) v.findViewById(R.id.divPlayAudio_play);
                            ImageView prev=(ImageView) v.findViewById(R.id.divPlayAudio_prev);
                            ImageView next=(ImageView) v.findViewById(R.id.divPlayAudio_next);

                            SeekBar progressBar=(SeekBar)v.findViewById(R.id.divPlayAudio_progress);
                            progressBar.setMax(mediaPlayer.getDuration());
                            DateFormat df = new SimpleDateFormat("HH:mm:ss");
                            String time = df.format(mediaPlayer.getDuration());


                            finalTime.setText(time+"");
                            mp.start();
                            progress.dismiss();

                            changeAudioState(progressBar,currentTime);

                            progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                    if (b){
                                        mediaPlayer.seekTo(i);
                                    }
                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {

                                }

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {

                                }
                            });

                            titre.setText(tracks.get(position).titre+"");

                            mb.setView(v);
                            final AlertDialog ad=mb.create();
                            ad.show();
                            ad.setCanceledOnTouchOutside(false); //ne pas fermer on click en dehors ..
                            ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialogInterface) {
                                    mediaPlayer.stop();
                                }
                            });


                        }
                    });

                    try {
                        Uri uri = Uri.parse("https://www.android-examples.com/wp-content/uploads/2016/04/Thunder-rumble.mp3");
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setDataSource(c, uri);
                        mediaPlayer.prepare();

                    } catch (IllegalArgumentException e) {
                    } catch (IllegalStateException e) {
                    } catch (IOException e) {
                    }
                    //endregion


                }
            });
        }else {
            if(tracks.size() % 60 == 0 && tracks.size() != 0){
                ((AddPlusView)holder).addPlusButt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ProfileBook.book.getAllAudios(tracks.size(), new PostServerRequest5.doBeforAndAfterGettingData() {
                            @Override
                            public void before() {

                            }

                            @Override
                            public void echec(Exception e) {

                            }

                            @Override
                            public void After(String result) {
                                addTracks(result);
                            }
                        });
                    }
                });
            }else {
                ((AddPlusView)holder).addPlusButt.setVisibility(View.GONE);
            }
        }







    }

    @Override
    public int getItemViewType(int position) {
        if(position==tracks.size()){
            return 2;
        }else {
            return 1;
        }

    }

    @Override
    public int getItemCount() {
        return tracks.size()+1;
    }

    public void addTracks(String JSONResult){
        try {
            JSONArray r=new JSONArray(JSONResult);
            for (int i=0;i<r.length();i++){
                JSONObject obj=r.getJSONObject(i);
               tracks.add(new Track(obj.getInt("idtrack"),obj.getString("titre"),obj.getString("lien")));
            }
            c.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void changeAudioState(final SeekBar progressBar, final TextView currentTime){
        progressBar.setProgress(mediaPlayer.getCurrentPosition());

        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        String time = df.format(mediaPlayer.getCurrentPosition());
        currentTime.setText(time+"");

        if (mediaPlayer.isPlaying()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    changeAudioState(progressBar,currentTime);
                }
            }, 1000);
        }

    }
}
