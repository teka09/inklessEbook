/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.devs.celtica.inkless.Users;

/**
 *
 * @author INFO
 */
public class Contrat {
   
    private int id_contrat;
    private String montants,type;

    public Contrat(int id_contrat, String montants, String type) {
        this.id_contrat = id_contrat;
        this.montants = montants;
        this.type = type;
    }

    public int getId_contrat() {
        return id_contrat;
    }

    public void setId_contrat(int id_contrat) {
        this.id_contrat = id_contrat;
    }

    public String getMontants() {
        return montants;
    }

    public void setMontants(String montants) {
        this.montants = montants;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
 
    
}
