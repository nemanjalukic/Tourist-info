/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.turista;

import net.etfbl.turista.Turista;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.etfbl.turista.Turista.nacinKretanja;
import net.etfbl.atrakcija.Crkva;
import net.etfbl.atrakcija.IstorijskiSpomenik;
import net.etfbl.atrakcija.Muzej;
import net.etfbl.atrakcija.ZabavniPark;

/**
 *
 * @author Office
 */
public class TuristaKretanje extends Thread{


    private Turista turista;
    private TextArea ispisText;
    private Object[][] grad;
    public Random rand = new Random();

    public TuristaKretanje(Turista turista,Object[][] grad) {
        this.grad=grad;
        this.turista=turista;
    }

    @Override
    public void run() {
        if (turista.getKretanje().equals(nacinKretanja.SAMO_U_JENDOM_REDU)) {
            for (int i = turista.getX(); i < grad.length; i++) {
                if (grad[i][turista.getY()] instanceof Muzej) {
                    Muzej muzej = (Muzej) grad[i][turista.getY()];
                    if (turista.getNovac() > muzej.placa()) {
                        turista.smanjiNovac(muzej.placa());
                        File fajl = new File(turista.getFolder().getPath() + "/" + muzej.getLetak().getName());
                        try {
                            Files.copy(muzej.getLetak().toPath(), fajl.toPath(), REPLACE_EXISTING);
                        } catch (IOException ex) {
                            Logger.getLogger(TuristaKretanje.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        turista.posijetaDodaj();
                        dodaj(turista.getIme() + " je posijetio " + muzej.getNaziv());
                    } else {
                        break;
                    }
                    if (turista.getNovac() == 0) {
                        break;
                    }
                } else if (grad[i][turista.getY()] instanceof ZabavniPark) {
                    {
                        ZabavniPark park = (ZabavniPark) grad[i][turista.getY()];
                        if (turista.getNovac() > park.placa()) {
                            turista.smanjiNovac(park.placa());
                            turista.posijetaDodaj();
                        dodaj(turista.getIme() + " je posijetio park " + park.getNaziv());
                        } else {
                            break;
                        }
                        if (turista.getNovac() == 0) {
                            break;
                        }
                    }
                } else if (grad[i][turista.getY()] instanceof Crkva) {

                    Crkva crkva = (Crkva) grad[i][turista.getY()];
                    int prilog = rand.nextInt(turista.getNovac());
                    crkva.dodajPrilog(prilog);
                    turista.smanjiNovac(prilog);
                    turista.posijetaDodaj();
                    dodaj(turista.getIme() + " je posijetio crkvu " + crkva.getNaziv());

                    if (turista.getNovac() == 0) {
                        break;
                    }

                } else if (grad[i][turista.getY()] instanceof IstorijskiSpomenik) {
                    IstorijskiSpomenik muz = (IstorijskiSpomenik) grad[i][turista.getY()];
                    turista.posijetaDodaj();
                    prikaziSliku(muz.getSlika());
                    dodaj(turista.getIme() + " je posijetio spomenik " + muz.getNaziv());
                }
                try {
                    sleep(turista.getVrijemeKretanja());
                } catch (InterruptedException ex) {
                    Logger.getLogger(TuristaKretanje.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
        if (turista.getKretanje().equals(nacinKretanja.DIJAGONALNO)) {
            int i = turista.getX();
            for (int j = turista.getY(); j < grad[0].length && i < grad.length; j++) {
                if (grad[i][j] instanceof Muzej) {
                    Muzej muzej = (Muzej) grad[i][j];
                    if (turista.getNovac() > muzej.placa()) {
                        turista.smanjiNovac(muzej.placa());
                        File fajl = new File(turista.getFolder().getPath() + "/" + muzej.getLetak().getName());
                        try {
                            Files.copy(muzej.getLetak().toPath(), fajl.toPath(), REPLACE_EXISTING);
                        } catch (IOException ex) {
                            Logger.getLogger(TuristaKretanje.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        turista.posijetaDodaj();
                        dodaj(turista.getIme() + " je posijetio muzej " + muzej.getNaziv());
                    } else {
                        break;
                    }
                    if (turista.getNovac() == 0) {
                        break;
                    }
                } else if (grad[i][j] instanceof ZabavniPark) {
                    {
                        ZabavniPark park = (ZabavniPark) grad[i][j];
                        if (turista.getNovac() > park.placa()) {
                            turista.smanjiNovac(park.placa());
                            turista.posijetaDodaj();
                        dodaj(turista.getIme() + " je posijetio zabavni park " + park.getNaziv());
                        } else {
                            break;
                        }
                        if (turista.getNovac() == 0) {
                            break;
                        }
                    }
                } else if (grad[i][j] instanceof Crkva) {

                    Crkva crkva = (Crkva) grad[i][j];
                    int prilog = rand.nextInt(turista.getNovac());
                    crkva.dodajPrilog(prilog);
                    turista.smanjiNovac(prilog);
                    turista.posijetaDodaj();
                    dodaj(turista.getIme() + " je posijetio crkvu " + crkva.getNaziv());

                    if (turista.getNovac() == 0) {
                        break;
                    }

                } else if (grad[i][j] instanceof IstorijskiSpomenik) {
                    IstorijskiSpomenik muz = (IstorijskiSpomenik) grad[i][j];
                    turista.posijetaDodaj();
                    prikaziSliku(muz.getSlika());
                    dodaj(turista.getIme() + " je posijetio spomenik " + muz.getNaziv());
                }
                i++;
                try {
                    sleep(turista.getVrijemeKretanja());
                } catch (InterruptedException ex) {
                    Logger.getLogger(TuristaKretanje.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
        if (turista.getKretanje().equals(nacinKretanja.KROZ_CIJELU_MATRICU)) {
            for (int i = turista.getX(); i < grad.length; i++) {
                for (int j = turista.getY(); j < grad[0].length; j++) {
                    if (grad[i][j] instanceof Muzej) {
                    Muzej muzej = (Muzej) grad[i][j];
                    if (turista.getNovac() > muzej.placa()) {
                        turista.smanjiNovac(muzej.placa());
                        File fajl = new File(turista.getFolder().getPath() + "/" + muzej.getLetak().getName());
                        try {
                            Files.copy(muzej.getLetak().toPath(), fajl.toPath(), REPLACE_EXISTING);
                        } catch (IOException ex) {
                            Logger.getLogger(TuristaKretanje.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        turista.posijetaDodaj();
                        dodaj(turista.getIme() + " je posijetio muzej " + muzej.getNaziv());
                    } else {
                        break;
                    }
                    if (turista.getNovac() == 0) {
                        break;
                    }
                } else if (grad[i][j] instanceof ZabavniPark) {
                    {
                        ZabavniPark park = (ZabavniPark) grad[i][j];
                        if (turista.getNovac() > park.placa()) {
                            turista.smanjiNovac(park.placa());
                            turista.posijetaDodaj();
                        dodaj(turista.getIme() + " je posijetio zabavni park " + park.getNaziv());
                        } else {
                            break;
                        }
                        if (turista.getNovac() == 0) {
                            break;
                        }
                    }
                } else if (grad[i][j] instanceof Crkva) {

                    Crkva crkva = (Crkva) grad[i][j];
                    int prilog = rand.nextInt(turista.getNovac());
                    crkva.dodajPrilog(prilog);
                    turista.smanjiNovac(prilog);
                    turista.posijetaDodaj();
                    dodaj(turista.getIme() + " je posijetio crkvu " + crkva.getNaziv());

                    if (turista.getNovac() == 0) {
                        break;
                    }

                } else if (grad[i][j] instanceof IstorijskiSpomenik) {
                    IstorijskiSpomenik muz = (IstorijskiSpomenik) grad[i][j];
                    turista.posijetaDodaj();
                    prikaziSliku(muz.getSlika());
                    dodaj(turista.getIme() + " je posijetio spomenik " + muz.getNaziv());
                }
                try {
                    sleep(turista.getVrijemeKretanja());
                } catch (InterruptedException ex) {
                    Logger.getLogger(TuristaKretanje.class.getName()).log(Level.SEVERE, null, ex);
                }

                }
            }
        }
        dodaj(turista.getIme() + " je zavrsio kretanje");
    }

    public void setIspisText(TextArea ispisText) {
        this.ispisText = ispisText;
    }
    public synchronized void dodaj(String text) {                          
        
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        ispisText.appendText(text+"\n");
                    }
                });
            
        
    }

    public void prikaziSliku(File file) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Stage stage = new Stage();
                StackPane root = new StackPane();
                ImageView iv1 = new ImageView();
                try {
                    InputStream in = new FileInputStream(file.getAbsolutePath());
                    iv1.setImage(new Image(in));
                    in.close();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(TuristaKretanje.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(TuristaKretanje.class.getName()).log(Level.SEVERE, null, ex);
                }
                iv1.setFitWidth(300);
                iv1.setFitHeight(300);
                iv1.setPreserveRatio(true);
                iv1.setSmooth(true);
                iv1.setCache(true);
                root.getChildren().add(iv1);
                Scene scene = new Scene(root, 400, 400);
                stage.setTitle("Slika");
                stage.setScene(scene);
                stage.show();
                TimerTask task1 = new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                stage.close();
                            }
                        });
                    }
                };
                new Timer().schedule(task1, 3000);
            }

        });
    }
    

}
