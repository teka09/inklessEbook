package com.devs.celtica.inkless.Publications;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.devs.celtica.inkless.Activities.Accueil;
import com.devs.celtica.inkless.Activities.Login;
import com.devs.celtica.inkless.PostServerRequest5;
import com.devs.celtica.inkless.R;
import com.devs.celtica.inkless.Users.Profile;
import com.devs.celtica.inkless.Users.Writer;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



public class AccueilBooksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    AppCompatActivity c;
    public  ArrayList<Book> books=new  ArrayList<Book>();
    public TYPE_AFFICH type_affich;
    public static int ItemSelected;

    public AccueilBooksAdapter(AppCompatActivity c) {
        this.c = c;

    }

    public static class BookView extends RecyclerView.ViewHolder  {

        ImageView photo;
        LinearLayout body;
        public BookView(View v) {
            super(v);
            photo=(ImageView) v.findViewById(R.id.accueil_divBook_img);
            body=(LinearLayout)v.findViewById(R.id.accueil_divBook_body);
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
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.accueil_div_book,parent,false);
            BookView vh = new BookView(v);
            return vh;
        }else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_plus_butt,parent,false);
            AddPlusView vh = new AddPlusView(v);
            return vh;
        }




    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (position !=books.size()) {
            Glide.with(c)
                    .load(Login.ajax.url + "/" + books.get(position).photo)
                    .thumbnail(Glide.with(c).load(R.drawable.wait))
                    .apply(new RequestOptions().override(400, 600))
                    .error(Glide.with(c).load(R.drawable.bg_butt_bleu_fonce))
                    .into(((BookView) holder).photo);

            ((BookView) holder).photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i=new Intent(c,ProfileBook.class);
                    i.putExtra("request","accueil");
                    ProfileBook.book =books.get(position);
                    c.startActivity(i);
                }
            });
        }else {
            if(books.size() % 60 == 0 && books.size() != 0){
                ((AddPlusView)holder).addPlusButt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(type_affich == TYPE_AFFICH.BEST_CATEGORY){
                            Login.reader.getBookOfBestCategory(books.size(), new PostServerRequest5.doBeforAndAfterGettingData() {
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
                        }else {
                            Login.reader.getBookOfBestSels(books.size(), new PostServerRequest5.doBeforAndAfterGettingData() {
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
                    }
                });
            }else {
                ((AddPlusView)holder).addPlusButt.setVisibility(View.GONE);
            }
        }





    }

    @Override
    public int getItemCount() {
        if (books.size() != 0)
        return books.size()+1;
        else return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==books.size()){
            return 2;
        }else {
            return 1;
        }

    }

    public void addBooks(String JsonResult){
        Log.e("ddd",JsonResult);
        try {
            JSONArray r=new JSONArray(JsonResult);
            for (int i=0;i<r.length();i++){
                JSONObject obj=r.getJSONObject(i);
                books.add(new Book(obj.getInt("id_pub"),obj.getString("lien_resume"),obj.getString("lien"),obj.getString("photo"),obj.getString("maison_edition"),obj.getString("nom1"),obj.getString("nom2"),obj.getString("category"),obj.getString("date"),new Writer(obj.getInt("id_writter"),"Ouss"),(obj.getInt("has_papier_version") == 1 ? true : false),(obj.getInt("has_papier_version") == 1 ? obj.getDouble("prix_book") : 0),obj.getDouble("prix_pdf")));
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

    public enum TYPE_AFFICH{
        BEST_SELS,BEST_CATEGORY
    }
}
