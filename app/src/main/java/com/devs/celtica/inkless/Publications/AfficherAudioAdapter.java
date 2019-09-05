package com.devs.celtica.inkless.Publications;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


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

import java.util.ArrayList;

public class AfficherAudioAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    AppCompatActivity c;
    public  ArrayList<Audio> audios=new  ArrayList<>();
    public static int ItemSelected;

    public AfficherAudioAdapter(AppCompatActivity c) {
        this.c = c;

    }

    public static class AudioView extends RecyclerView.ViewHolder  {

        TextView narrator;
        TextView date;
        LinearLayout body;

        public AudioView(View v) {
            super(v);
            narrator=(TextView) v.findViewById(R.id.divAudio_narrator);
            date=(TextView) v.findViewById(R.id.divAudio_date);
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
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.div_audio_accueil,parent,false);
            AudioView vh = new AudioView(v);
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


        if (position !=audios.size()) {
             //afficher le div audio avec configuration ..
            ((AudioView)holder).narrator.setText(((audios.get(position) instanceof AudioNarrator == true)? ((AudioNarrator)audios.get(position)).narrator.nom :((AudioWriter)audios.get(position)).writer.nom));
            ((AudioView)holder).date.setText(audios.get(position).date_pub+"");
            ((AudioView)holder).body.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AfficherTracks.audioSlected=audios.get(position);
                    c.startActivity(new Intent(c,AfficherTracks.class));
                }
            });
        }else {
            if(audios.size() % 60 == 0 && audios.size() != 0){
                ((AddPlusView)holder).addPlusButt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ProfileBook.book.getAllAudios(audios.size(), new PostServerRequest5.doBeforAndAfterGettingData() {
                            @Override
                            public void before() {

                            }

                            @Override
                            public void echec(Exception e) {

                            }

                            @Override
                            public void After(String result) {
                                addAudios(result);
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
        if(position==audios.size()){
            return 2;
        }else {
            return 1;
        }

    }

    @Override
    public int getItemCount() {
        return audios.size()+1;
    }

    public void addAudios(String JSONResult){
        try {
            JSONArray r=new JSONArray(JSONResult);
            for (int i=0;i<r.length();i++){
                JSONObject obj=r.getJSONObject(i);
                if (!obj.getString("id_writter").equals("null")){
                    audios.add(new AudioWriter("",obj.getInt("id_pub"),ProfileBook.book.id_pub,obj.getString("date"),new Writer(obj.getInt("id_writter"),obj.getString("nom"))));
                }else if(!obj.getString("id_narrator").equals("null")) {
                    audios.add(new AudioNarrator("",obj.getInt("id_pub"),ProfileBook.book.id_pub,obj.getString("date"),new Narrator(obj.getInt("id_narrator"),obj.getString("nom"))));

                }
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
}
