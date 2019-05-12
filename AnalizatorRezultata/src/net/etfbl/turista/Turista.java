/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.turista;

import java.io.File;
import java.io.Serializable;
import java.util.Random;
import javafx.scene.control.TextArea;

/**
 *
 * @author Office
 */
public class Turista implements Serializable, Comparable {

    public static enum nacinKretanja {
        SAMO_U_JENDOM_REDU, DIJAGONALNO, KROZ_CIJELU_MATRICU;
    }
    private int x;
    private int y;
    private String ime;
    private int novac;
    private nacinKretanja kretanje;
    private int vrijemeKretanja;
    private Random rand = new Random();
    private File folder = null;
    private int broj;
    private double posijeceno;

    public Turista(int x, int y) {
        Random rand = new Random();
        this.x = x;
        this.y = y;
        ime = Integer.toString(x) +"-"+ Integer.toString(y);
        novac = rand.nextInt(1000);
        vrijemeKretanja = rand.nextInt(6000) + 1000;
        folder = new File(ime);
        brisanje(folder);
        folder.mkdir();
        kretanje = nacinKretanja.values()[rand.nextInt(3)];
        broj = 0;

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getIme() {
        return ime;
    }

    public nacinKretanja getKretanje() {
        return kretanje;
    }

    public int getVrijemeKretanja() {
        return vrijemeKretanja;
    }

    public File getFolder() {
        return folder;
    }

    public double getPosijeceno() {
        return posijeceno;
    }

    public int getNovac() {
        return novac;
    }

    public void smanjiNovac(int n) {
        novac -= n;
    }

    public void posijetaDodaj() {
        broj++;
    }

    public void izracunatiPosijeceno(int i) {
        posijeceno = 100 * (double) broj / i;
    }

    @Override
    public int compareTo(Object o) {
        Turista tur = (Turista) o;
        if (this.broj < tur.broj) {
            return -1;
        } else if (this.broj > tur.broj) {
            return 1;
        } else {
            return 0;
        }
    }

    public void brisanje(File f) {
        if (f.isDirectory()) {
            for (File f1 : f.listFiles()) {
                brisanje(f1);
            }
        }
        f.delete();
    }

}
