/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.devs.celtica.inkless.Publications;

/**
 *
 * @author INFO
 */
public class Track {
    
public int id_track;
public String lien,titre;


    public Track(String titre) {
        this.titre = titre;
    }

    public Track( ) {

    }

    public Track(int id_track, String titre, String lien) {
        this.id_track = id_track;
        this.titre=titre;
        this.lien = lien;
    }


}
