package com.devs.celtica.inkless.Publications;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devs.celtica.inkless.Activities.Login;
import com.devs.celtica.inkless.R;

import java.util.ArrayList;

/**
 * Created by celtica on 21/05/19.
 */

public class TrackUploadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    AppCompatActivity c;
    public static ArrayList<TrackForUpload> audios=new  ArrayList<TrackForUpload>();
    public static int itemSelected;

    public TrackUploadAdapter(AppCompatActivity c) {
        this.c = c;

    }

    public static class AudioUploadView extends RecyclerView.ViewHolder  {
        EditText titre;
        TextView uploadButt;
        LinearLayout body;
        public AudioUploadView(View v) {
            super(v);
              titre=(EditText) v.findViewById(R.id.div_addAudio_titre);
              uploadButt=(TextView)v.findViewById(R.id.div_addAudio_uploadButt);
              body=(LinearLayout)v.findViewById(R.id.body);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.div_add_audio,parent,false);

        AudioUploadView vh = new AudioUploadView(v);
        return vh;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        //region configuration titre de track ..
        if(audios.get(position).titre == null){
            ((AudioUploadView)holder).titre.setHint(c.getResources().getString(R.string.uploadAudio_titre)+" "+(position+1));
        }else {
            ((AudioUploadView)holder).titre.setText(audios.get(position).titre);
        }
        ((AudioUploadView)holder).titre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.toString().equals("")){
                    audios.get(position).titre=null;
                }else {
                    audios.get(position).titre=s.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //endregion

        //region configuration file de track
        if(audios.get(position).audioFile == null){
            ((AudioUploadView)holder).uploadButt.setText(c.getResources().getString(R.string.uploadBook_upload));
        }else {
            ((AudioUploadView)holder).uploadButt.setText(Login.reader.getFilePath(c,audios.get(position).audioFile));
        }
        //endregion

        //region open selection file ..
        ((AudioUploadView)holder).uploadButt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                itemSelected=position;
                Login.reader.openSelectFile(c,TypeFiles.AUDIO);
            }
        });
        //endregion

    }

    public boolean isPanierVide(){
        for (TrackForUpload t:audios){
            if (t.isValide()) return false;
        }
        return true;
    }

    @Override
    public int getItemCount() {
        return audios.size();
    }
}
