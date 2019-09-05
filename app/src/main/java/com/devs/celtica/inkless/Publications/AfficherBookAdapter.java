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
import com.devs.celtica.inkless.Users.Writer;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AfficherBookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    AppCompatActivity c;
    public static ArrayList<Book> books=new  ArrayList<Book>();
    public static int ItemSelected;

    public AfficherBookAdapter(AppCompatActivity c) {
        this.c = c;

    }

    public static class BookView extends RecyclerView.ViewHolder  {

        TextView nom1;
        TextView nom2;
        ImageView photo;
        LinearLayout body;
        public BookView(View v) {
            super(v);
            nom1=(TextView) v.findViewById(R.id.div_book_nom1);
            nom2=(TextView)v.findViewById(R.id.div_book_nom2);
            photo=(ImageView)v.findViewById(R.id.div_book_photo);
            //body=(LinearLayout)v.findViewById(R.id.body);

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
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.div_book,parent,false);
            BookView vh = new BookView(v);
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


        if (position !=books.size()) {
            ((BookView)holder).nom1.setText(books.get(position).nom1+"");
            ((BookView)holder).nom2.setText(books.get(position).nom2+"");

            Glide.with(c)
                    .load(Login.ajax.url+"/"+books.get(position).photo)
                    .thumbnail(Glide.with(c).load(R.drawable.wait))
                    .apply(new RequestOptions().override(400, 600))
                    .into(((BookView)holder).photo);

            //region open book profile ..
            ((BookView)holder).photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ItemSelected=position;
                    ProfileBook.book =books.get(position);
                    c.startActivity(new Intent(c,ProfileBook.class));
                }
            });
            //endregion



        }else {
            if(books.size() % 60 == 0 && books.size() != 0){
                ((AddPlusView)holder).addPlusButt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ((Writer)Login.reader).getBooks(books.size(), new PostServerRequest5.doBeforAndAfterGettingData() {
                            @Override
                            public void before() {

                            }

                            @Override
                            public void echec(Exception e) {

                            }

                            @Override
                            public void After(String result) {
                                addBooks(result);
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
        if(position==books.size()){
            return 2;
        }else {
            return 1;
        }

    }

    @Override
    public int getItemCount() {
        return books.size()+1;
    }

    public void addBooks(String JSONResult){
        Writer auteur=new Writer(Login.reader.id_user,Login.reader.nom);
        try {
            JSONArray r=new JSONArray(JSONResult);
            for (int i=0;i<r.length();i++){
                JSONObject obj=r.getJSONObject(i);
                AfficherBookAdapter.books.add(new Book(obj.getInt("id_pub"),obj.getString("lien_resume"),obj.getString("lien"),obj.getString("photo"),obj.getString("maison_edition"),obj.getString("nom1"),obj.getString("nom2"),obj.getString("category"),obj.getString("date"),auteur,true,0,0));
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
