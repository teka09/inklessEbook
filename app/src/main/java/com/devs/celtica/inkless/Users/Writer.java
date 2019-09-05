package com.devs.celtica.inkless.Users;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import com.devs.celtica.inkless.Activities.Login;
import com.devs.celtica.inkless.PostServerRequest5;
import com.devs.celtica.inkless.Publications.Book;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

public class Writer extends ReaderFull{

    public String ccp;
    public boolean contrat_writer_valide=false;
    public ArrayList<Book> books=new ArrayList<Book>();

    public Writer(int id_user, String nom, String num_tel, String email, String mdp,String nation, String photo,String ccp) {
        super(id_user, nom, num_tel, email, mdp,nation, photo);
        this.ccp = ccp;
    }

    public Writer(int id_writer, String nom) {
        super(id_writer,nom);
    }

    @Override
    public void signUp(AppCompatActivity c){
        HashMap<String,String> data= getHashmapUserInfos ();
        data.put("request","inscription");
        data.put("type_user","writer");
        data.put("ccp",ccp);

        sendUserINfosToServer(c,data);
    }

    public void getBooks(int offset ,PostServerRequest5.doBeforAndAfterGettingData callback){
        HashMap<String,String> data=new HashMap<>();
        data.put("user",id_user+"");
        Login.ajax.setUrlRead("/read.php");
        Login.ajax.read("select * from publication pub inner join book on book.id_book=pub.id_pub and book.id_writter=? order by id_pub desc limit 60 OFFSET "+offset, data,callback);
    }


    
}
