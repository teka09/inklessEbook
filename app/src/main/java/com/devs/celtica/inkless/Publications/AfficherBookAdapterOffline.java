package com.devs.celtica.inkless.Publications;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devs.celtica.inkless.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AfficherBookAdapterOffline extends RecyclerView.Adapter <AfficherBookAdapterOffline.Recyclerviewholder>{
    Context ctx;

    //String url ="https://testingebook.000webhostapp.com/";

    ArrayList<Book> arrayList;

    // pour onclicklistener

    private  OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick( int position);
        void onPdfClick( int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        mListener = clickListener;
    }
    //fin OnclickListener

    public AfficherBookAdapterOffline(ArrayList<Book> arrayList,Context ctx) {
        this.arrayList = arrayList;
        this.ctx=ctx;

    }

    @Override
    public Recyclerviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.div_book_offline,parent,false);
        Recyclerviewholder recyclerviewholder= new Recyclerviewholder(view,mListener);

        return recyclerviewholder;
    }



    @Override
    public void onBindViewHolder(Recyclerviewholder holder, int position) {

        Book infos = arrayList.get(position);


        holder.nombook1.setText(infos.nom1);
        holder.nombook2.setText(infos.nom2);

        // pour récupérer les images je vais faire quelques modification




    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }


    public static class Recyclerviewholder extends RecyclerView.ViewHolder {



        TextView nombook1,nombook2;
        LinearLayout readButt_offline;
        ImageView imgBook;
        //likaynin fe Recycle dyali div_book_offline

        public Recyclerviewholder(View itemView, final OnItemClickListener Listener) {
            super(itemView);

            //Pour Book
            nombook1=(TextView)itemView.findViewById(R.id.div_book_offline_nom1);
            nombook2=(TextView)itemView.findViewById(R.id.div_book_offline_nom2);
            imgBook = (ImageView)itemView.findViewById(R.id.ImageBook);
            readButt_offline =(LinearLayout)itemView.findViewById(R.id.profileBook_readButt_offline);

            //test for item click
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Listener.onItemClick(position);
                        }
                    }
                }
            });

            //test pour read offline bouton

            readButt_offline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Listener.onPdfClick(position);
                        }
                    }
                }
            });




        }
    }
}

