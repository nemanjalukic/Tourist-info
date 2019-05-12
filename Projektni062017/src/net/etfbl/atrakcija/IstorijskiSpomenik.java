/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.atrakcija;

import java.io.File;
import java.io.Serializable;

/**
 *
 * @author Office
 */
public class IstorijskiSpomenik extends TuristickaAtrakcija implements Besplatno,Serializable{
  
    private String opis;
    private File slika;

    public IstorijskiSpomenik(String naziv, String lokacija,String opis,File slika) {
        super(naziv, lokacija);
        this.opis=opis;
        this.slika=slika;
    }
 

 
 @Override
  public boolean besplatno(){
    return true;
  }

    public String getOpis() {
        return opis;
    }

    public File getSlika() {
        return slika;
    }
  
  
  
}
