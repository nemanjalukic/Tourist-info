/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.arhiverletaka;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
            String s="";
            while(!s.equals("END")){
            s=(String) ois.readObject();
             if(s.equals("zip")){
            File folderi = new File("./folderi");
            brisanje(folderi);
            File folderSlovo = new File("./slova");
            brisanje(folderSlovo);
            String fajl = (String) ois.readObject();
            File f = new File(fajl);
            folderi.mkdir();
            folderSlovo.mkdir();
            unzip(f, folderi.getAbsolutePath());
            sort(folderi);
            oos.writeObject(poruka(folderSlovo));
            oos.flush();
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

    public static void unzip(File source, String out) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(source))) {

            ZipEntry entry = zis.getNextEntry();

            while (entry != null) {

                File file = new File(out, entry.getName());

                if (entry.isDirectory()) {
                    file.mkdirs();
                } else {
                    File parent = file.getParentFile();

                    if (!parent.exists()) {
                        parent.mkdirs();
                    }

                    try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {

                        byte[] buffer = new byte[1024];

                        int location;

                        while ((location = zis.read(buffer)) != -1) {
                            bos.write(buffer, 0, location);
                        }
                    }
                }
                entry = zis.getNextEntry();
            }
        }
    }

    public void sort(File f) {
        if (f.isDirectory()) {
            for (File f1 : f.listFiles()) {
                sort(f1);
            }
        } else {

            File folderSlovo = new File("./slova/" + String.valueOf(f.getName().charAt(0)).toLowerCase());
            folderSlovo.mkdir();

            try {
                String ime[] = f.getName().split("[.]");
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Calendar cal = Calendar.getInstance();
                ime[0] += sdf.format(cal.getTime());
                //System.out.println(folderSlovo.getPath());
                File novi = new File(folderSlovo.getPath() + "/" + ime[0] + "." + ime[1]);
                novi.createNewFile();
                Files.copy(f.toPath(), novi.toPath(), REPLACE_EXISTING);
            } catch (IOException ex) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String poruka(File f) {
        String poruka = "";
        for (File f1 : f.listFiles()) {
            poruka += "Fajlova sa pocetnim slovom " + f1.getName() + " ima " + f1.listFiles().length + "\n";
        }
        return poruka;
    }
    public void brisanje(File f){
    if (f.isDirectory()) {
        for (File f1 : f.listFiles()) {
            brisanje(f1);
        }
    }
    f.delete();
}
}
