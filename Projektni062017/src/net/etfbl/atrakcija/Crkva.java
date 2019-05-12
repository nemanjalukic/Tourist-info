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
public class Crkva extends TuristickaAtrakcija implements Dobrovoljno,Serializable{
    
    private double prilozi;
    public Crkva(String naziv, String lokacija) {
        super(naziv, lokacija);
        prilozi=0;
    }

    @Override
    public boolean dobrovoljno() {
        return true;
        
    }
    
    public void dodajPrilog(int prilog){
        prilozi+=prilog;
    }
    
}
