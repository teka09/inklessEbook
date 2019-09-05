
package com.devs.celtica.inkless.Publications;

public class Publication {
    //Publication(id_pub,date_pub,type,lien)
    public int id_pub;
    public String date_pub;
    public String type,lien;

    public Publication(int id_pub, String date_pub, String type, String lien) {
        this.id_pub = id_pub;
        this.date_pub = date_pub;
        this.type = type;
        this.lien = lien;
    }

    public Publication(){

    }

}
