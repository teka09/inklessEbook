/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.devs.celtica.inkless.Users;

import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;

/**
 *
 * @author INFO
 */
public class Narrator extends ReaderFull{
    public String ccp;


    //pour affecter a un objet audioNarator ..
    public Narrator(int id_reader, String nom) {
        super(id_reader, nom);
    }

    public Narrator(int id_user, String nom, String num_tel, String email, String mdp, String nation, String photo, String ccp) {
        super(id_user, nom, num_tel, email, mdp,nation,photo);
        this.ccp=ccp;
    }

    public void signUp(AppCompatActivity c){
        HashMap<String,String> data= getHashmapUserInfos ();
        data.put("request","inscription");
        data.put("type_user","narrator");
        data.put("ccp",ccp);

        sendUserINfosToServer(c,data);
    }
    
}
