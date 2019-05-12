/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.projektni062017;

import net.etfbl.atrakcija.TuristickaAtrakcija;
import net.etfbl.turista.TuristaKretanje;
import net.etfbl.turista.Turista;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import static net.etfbl.projektni062017.FXMLKorisnickiDioController.grad;
import static net.etfbl.projektni062017.FXMLKorisnickiDioController.oos;
import static net.etfbl.projektni062017.FXMLKorisnickiDioController.oos1;
import static net.etfbl.projektni062017.FXMLKorisnickiDioController.turisti;

/**
 * FXML Controller class
 *
 * @author Office
 */
public class FXMLSimulacijaController implements Initializable {

    @FXML
    private TextArea ispisText;
    
    public static String dopuna="";
    public static File slika;
    String matrica="";
    public static ArrayList<TuristaKretanje> turistiKretanje = new ArrayList<>();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for(int i=0;i<grad.length;i++)
            for(int j=0;j<grad[0].length;j++){  
                if (grad[i][j] instanceof Turista)
                    grad[i][j]=null;
            }
        for(Turista tur:turisti){
              grad[tur.getX()][tur.getY()]=tur;
              System.out.println(tur.getIme()+" "+tur.getKretanje());
          }

          for(int i=0;i<grad.length;i++){
            for(int j=0;j<grad[0].length;j++){
                if(grad[i][j] instanceof Turista)
                    matrica+="T  ";
                else if(grad[i][j] instanceof TuristickaAtrakcija)
                    matrica+="TA ";
                else if(grad[i][j]==null)
                    matrica+="*  ";
            }
            matrica+="\n";
          }
        matrica+="\n"; 
        dopuna=matrica;
         
        ispisText.setText(matrica);
        
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                for(Turista tur:turisti){
                    turistiKretanje.add(new TuristaKretanje(tur,grad));
                    
                }
                for(TuristaKretanje tur:turistiKretanje){
                    tur.setIspisText(ispisText);
                    tur.start();
                    
                }
                for(TuristaKretanje tur:turistiKretanje){
                      try {
                          tur.join();
                      } catch (InterruptedException ex) {
                          Logger.getLogger(FXMLSimulacijaController.class.getName()).log(Level.SEVERE, null, ex);
                      }
                }
                for(Turista tur:turisti){
                    tur.izracunatiPosijeceno(turisti.size());
                }
                
                ObjectOutputStream upisObjekta = null;
                    try {
                        FileOutputStream pisac = new FileOutputStream("./turisti.ser");
                        upisObjekta = new ObjectOutputStream(pisac);
                        upisObjekta.writeObject(turisti);
                        upisObjekta.close();
                        pisac.close();
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        try {
                            upisObjekta.close();

                        } catch (IOException ex) {
                            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                try {
                    FileOutputStream fos = new FileOutputStream("./turisti_folderi.zip");
                    ZipOutputStream zos = new ZipOutputStream(fos);
                    for(Turista tur:turisti){
                            addFileToZip(".",tur.getFolder().getName(), zos); 
                    }
                    zos.close();
                    fos.close();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(FXMLSimulacijaController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(FXMLSimulacijaController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(FXMLSimulacijaController.class.getName()).log(Level.SEVERE, null, ex);
                }
                turisti.clear();
                turistiKretanje.clear();
                
                ispisText.appendText("KRAJ");
                System.out.println("zavrseno");
                try {
                    File turistiFajl=new File("./turisti.ser");
                    oos1.writeObject("turisti");
                    oos1.flush();
                    oos1.writeObject(turistiFajl.getAbsolutePath());
                    oos1.flush();
                    File zipFajl=new File("./turisti_folderi.zip");
                    oos.writeObject("zip");
                    oos.flush();
                    oos.writeObject(zipFajl.getAbsolutePath());
                    oos.flush();
                } catch (IOException ex) {
                    Logger.getLogger(FXMLSimulacijaController.class.getName()).log(Level.SEVERE, null, ex);
                }



            }};
        new Timer().schedule(task, 0);
        
    }

static private void addFileToZip(String path, String srcFile, ZipOutputStream zip)
      throws Exception {

    File folder = new File(srcFile);
    if (folder.isDirectory() && folder.list().length>0) {
      addFolderToZip(path, srcFile, zip);
    }
    else if(folder.isFile()) {
      byte[] buf = new byte[1024];
      int len;
      FileInputStream in = new FileInputStream(srcFile);
      zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
      while ((len = in.read(buf)) > 0) {
        zip.write(buf, 0, len);
      }
    }
    else{
         zip.putNextEntry(new ZipEntry(folder.getName()+"/"));
        
    }
  }

  static private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip)
      throws Exception {
    File folder = new File(srcFolder);

    for (String fileName : folder.list()) {
      if (path.equals(".")) {
        addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
      } else {
        addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip);
      }
    }
  }    

    public TextArea getIspisText() {
        return ispisText;
    }
    
}
