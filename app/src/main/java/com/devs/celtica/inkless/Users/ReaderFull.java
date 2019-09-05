/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.devs.celtica.inkless.Users;

import android.support.v7.app.AppCompatActivity;

import com.devs.celtica.inkless.Activities.Login;
import com.devs.celtica.inkless.PostServerRequest5;

import java.util.HashMap;

/**
 *
 * @author INFO
 */
public class ReaderFull extends User{

    public boolean contrat_reader_valide=false;
    
    public ReaderFull(int id_user, String nom, String num_tel, String email, String mdp, String nation,String photo) {
        super(id_user, nom, num_tel, email, mdp, nation,photo);
    }

    public ReaderFull(int id_reader, String nom) {
        super(id_reader,nom);
    }

    public void signUp(AppCompatActivity c){
        HashMap<String,String> data= getHashmapUserInfos ();
        data.put("request","inscription");
        data.put("type_user","reader");

        sendUserINfosToServer(c,data);
    }

    //Récupération des books ordonné par la category la plus lu par c Reader ..
    public void getBookOfBestCategory(int offset ,PostServerRequest5.doBeforAndAfterGettingData callback){

        HashMap<String,String> data=new HashMap<>();
        data.put("user",id_user+"");
        Login.ajax.setUrlRead("/read.php");
        Login.ajax.read("SELECT *\n" +
                "FROM book b\n" +
                "INNER JOIN publication p ON b.id_book=p.id_pub\n" +
                "WHERE category =\n" +
                "    (SELECT category AS best_read\n" +
                "     FROM\n" +
                "       (SELECT category,\n" +
                "               COUNT(category) AS nbr_read\n" +
                "        FROM reading r\n" +
                "        INNER JOIN publication p ON p.id_pub=r.id_pub\n" +
                "        INNER JOIN book b ON r.id_pub=b.id_book\n" +
                "        WHERE r.id_reader=? \n" +
                "        GROUP BY b.category\n" +
                "        ORDER BY nbr_read DESC\n" +
                "        LIMIT 1) AS tmp)\n" +
                "UNION\n" +
                "  (SELECT *\n" +
                "   FROM book b\n" +
                "   INNER JOIN publication p ON b.id_book=p.id_pub) order by date desc limit 60 OFFSET "+offset+"\n",data,callback);
    }

    public void getBookOfBestSels(int offset ,PostServerRequest5.doBeforAndAfterGettingData callback){

        HashMap<String,String> data=new HashMap<>();
        Login.ajax.setUrlRead("/read.php");
        Login.ajax.read("SELECT p.*, \n" +
                "       b.*, \n" +
                "       Count(sb.id_book) AS nb_vente \n" +
                "FROM   book b \n" +
                "       INNER JOIN publication p \n" +
                "               ON p.id_pub = b.id_book \n" +
                "       LEFT JOIN sel_book sb \n" +
                "              ON sb.id_book = b.id_book \n" +
                "GROUP  BY b.id_book \n" +
                "ORDER  BY nb_vente DESC limit 60 OFFSET "+offset+"\n",data,callback);
    }

    
}
