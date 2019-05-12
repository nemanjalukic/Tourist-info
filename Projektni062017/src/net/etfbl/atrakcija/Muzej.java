/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.atrakcija;

import java.io.File;
import java.io.Serializable;
import java.util.Calendar;

/**
 *
 * @author Office
 */
public class Muzej extends TuristickaAtrakcija implements Placa,Besplatno,Serializable{
  
  
  private boolean dan;
  private File letak;
  private int cijena;
  
  
  public Muzej(String naziv, String lokacija,File letak, int cijena) { 
    super(naziv,lokacija);
    this.letak=letak;
    this.cijena=cijena;

  }

    @Override
    public int placa() {
      if(besplatno())
        return cijena;
      else
        return 0;
    }

    @Override
    public boolean besplatno() {
        if(Calendar.getInstance().DAY_OF_WEEK == 2 || Calendar.getInstance().DAY_OF_WEEK == 4 || Calendar.getInstance().DAY_OF_WEEK == 6 || Calendar.getInstance().DAY_OF_WEEK ==1) 
            dan=true;
        else
            dan=false;
        
        return dan;
    }

    public File getLetak() {
        return letak;
    }

    public int getCijena() {
        return cijena;
    }
  


    
}
