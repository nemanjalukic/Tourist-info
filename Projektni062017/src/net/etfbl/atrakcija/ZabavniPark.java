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
public class ZabavniPark extends TuristickaAtrakcija implements Placa,Serializable{
    
    private int cijena;
    public ZabavniPark(String naziv, String lokacija, int cijena) {
        super(naziv, lokacija);
        this.cijena=cijena;
    }

    @Override
    public int placa() {
        return cijena;
    }

    public int getCijena() {
        return cijena;
    }
    

 
    
}
