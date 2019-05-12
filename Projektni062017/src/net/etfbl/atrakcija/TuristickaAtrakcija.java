/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.atrakcija;

import java.io.Serializable;

/**
 *
 * @author Office
 */
public class TuristickaAtrakcija implements Serializable{
    
    private String naziv;
    private String lokacija;

    public TuristickaAtrakcija(String naziv, String lokacija) {
        this.naziv = naziv;
        this.lokacija = lokacija;
    }

    public String getNaziv() {
        return naziv;
    }

    public String getLokacija() {
        return lokacija;
    }
    
    
    
}
