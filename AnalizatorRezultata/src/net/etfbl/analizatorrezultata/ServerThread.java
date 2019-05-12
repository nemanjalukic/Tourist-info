/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.analizatorrezultata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.etfbl.turista.Turista;

/**
 *
 * @author Office
 */
public class ServerThread extends Thread {

    private Socket sock;
    private int value;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public ServerThread(Socket sock, int value) {
        this.sock = sock;
        this.value = value;
        try {
            oos = new ObjectOutputStream(sock.getOutputStream());
            ois = new ObjectInputStream(sock.getInputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        start();
    }

    @Override
    public void run() {

        try {
            ArrayList<Turista> turisti = new ArrayList<>();
            String s="";
            while(!s.equals("END")){
            s=(String) ois.readObject();
            if(s.equals("turisti")){
            String fajl = (String) ois.readObject();
            File f = new File(fajl);
            if (f.exists()) {
                FileInputStream citac = new FileInputStream(f);
                ObjectInputStream citanjeObjekta = new ObjectInputStream(citac);
                turisti = (ArrayList<Turista>) citanjeObjekta.readObject();
                Collections.sort(turisti);
                ArrayList<Turista> turisti5 = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    turisti5.add(turisti.get(i));
                }
                ObjectOutputStream upisObjekta = null;
                FileOutputStream pisac = new FileOutputStream("./top5.ser");
                upisObjekta = new ObjectOutputStream(pisac);
                upisObjekta.writeObject(turisti);
                upisObjekta.close();
                pisac.close();
                upisObjekta.close();
                File turistiFajl = new File("./top5.ser");
                oos.writeObject(turistiFajl.getAbsolutePath());
                oos.flush();
            }
            }
            }
            System.out.println("Klijent "+ value+" se odjavio");
            oos.close();
            ois.close();
            sock.close();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
