/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.projektni062017;

import net.etfbl.atrakcija.TuristickaAtrakcija;
import net.etfbl.turista.Turista;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Office
 */
public class FXMLKorisnickiDioController implements Initializable {

    @FXML
    private TableView<Turista> tabela;

    @FXML
    private TableColumn<Turista, String> imeCol;

    @FXML
    private TextField yText;

    @FXML
    private TextField xText;

    @FXML
    private Button pokreniButton;

    @FXML
    private TableColumn<Turista, Double> posijecenoCol;

    @FXML
    private TextField turistiText;

    @FXML
    private Button preuzmiButton;

    @FXML
    private Button prikaziButton;

    @FXML
    private TextField atrakcijaText;

    public static Object grad[][];
    public static ArrayList<Turista> turisti = new ArrayList<>();
    public ArrayList<TuristickaAtrakcija> atrakcije = new ArrayList<>();
    private ArrayList<Turista> turisti5 = new ArrayList<>();
    Random rand = new Random();
    public static final int TCP_PORT = 9000;
    public static ObjectOutputStream oos;
    public static ObjectInputStream ois;
    public static ObjectOutputStream oos1;
    public static ObjectInputStream ois1;
    public static Socket sock;
    public static Socket sock1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        preuzmiButton.setVisible(false);
        prikaziButton.setVisible(false);

        File f = new File("./turisticka-mapa.ser");
        if (f.exists()) {
            try {
                FileInputStream citac = new FileInputStream(f);
                ObjectInputStream citanjeObjekta = new ObjectInputStream(citac);
                atrakcije = (ArrayList<TuristickaAtrakcija>) citanjeObjekta.readObject();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        imeCol.setCellValueFactory(new PropertyValueFactory<Turista, String>("ime"));
        posijecenoCol.setCellValueFactory(new PropertyValueFactory<Turista, Double>("posijeceno"));
        try {
            InetAddress addr = InetAddress.getByName("127.0.0.1");
            sock = new Socket(addr, TCP_PORT);
            sock1 = new Socket(addr, TCP_PORT + 1);
            oos = new ObjectOutputStream(sock.getOutputStream());
            ois = new ObjectInputStream(sock.getInputStream());
            oos1 = new ObjectOutputStream(sock1.getOutputStream());
            ois1 = new ObjectInputStream(sock1.getInputStream());
        } catch (UnknownHostException ex) {
            Logger.getLogger(FXMLKorisnickiDioController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLKorisnickiDioController.class.getName()).log(Level.SEVERE, null, ex);
        }

        pokreniButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                if (provjeraBroj(xText.getText()) && !xText.getText().isEmpty() && provjeraBroj(yText.getText()) && !yText.getText().isEmpty() && provjeraBroj(turistiText.getText()) && !turistiText.getText().isEmpty() && provjeraBroj(atrakcijaText.getText()) && !atrakcijaText.getText().isEmpty() && atrakcije.size() > 0) {
                    int X = Integer.parseInt(xText.getText());
                    int Y = Integer.parseInt(yText.getText());
                    int atrakcija = Integer.parseInt(atrakcijaText.getText());
                    int turista = Integer.parseInt(turistiText.getText());
                    if (Y * X >= atrakcija + turista) {
                        grad = new Object[X][Y];
                        if (atrakcije.size() < atrakcija) {
                            for (int i = 0; i < atrakcije.size();) {
                                int x = rand.nextInt(X);
                                int y = rand.nextInt(Y);
                                if (grad[x][y] == null) {
                                    grad[x][y] = atrakcije.get(i);
                                    i++;
                                }
                            }
                            for (int i = 0; i < (atrakcija - atrakcije.size());) {
                                int x = rand.nextInt(X);
                                int y = rand.nextInt(Y);
                                if (grad[x][y] == null) {
                                    grad[x][y] = atrakcije.get(rand.nextInt(atrakcije.size()));
                                    i++;
                                }
                            }

                        } else {
                            for (int i = 0; i < atrakcija;) {
                                int x = rand.nextInt(X);
                                int y = rand.nextInt(Y);
                                if (grad[x][y] == null) {
                                    grad[x][y] = atrakcije.get(i);
                                    i++;
                                }
                            }
                        }
                        for (int i = 0; i < turista;) {
                            int x = rand.nextInt(X);
                            int y = rand.nextInt(Y);
                            if (grad[x][y] == null && provjeraListe(x, y)) {
                                Turista tur = new Turista(x, y);
                                turisti.add(tur);
                                i++;
                            }
                        }
                        try {
                            Stage s = new Stage();
                            Pane myPane = (Pane) FXMLLoader.load(getClass().getResource("/net/etfbl/projektni062017/FXMLSimulacija.fxml"));
                            Scene myScene = new Scene(myPane);
                            s.setScene(myScene);
                            s.show();
                        } catch (IOException ex) {
                            Logger.getLogger(FXMLKorisnickiDioController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        pokreniButton.setDisable(true);

                        TimerTask task1 = new TimerTask() {
                            String poruka;

                            @Override
                            public void run() {
                                try {
                                    poruka = (String) ois.readObject();
                                    if(poruka.equals("")){
                                        poruka="Nema fajlova za analizu";
                                    }
                                } catch (IOException ex) {
                                    Logger.getLogger(FXMLKorisnickiDioController.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (ClassNotFoundException ex) {
                                    Logger.getLogger(FXMLKorisnickiDioController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        Stage stage = new Stage();
                                        StackPane root = new StackPane();
                                        Label l = new Label(poruka);
                                        root.getChildren().add(l);
                                        Scene scene = new Scene(root, 300, 250);
                                        stage.setTitle("Obavještenje!");
                                        stage.setScene(scene);
                                        stage.show();
                                    }
                                });
                            }
                        };
                        TimerTask task2 = new TimerTask() {

                            @Override
                            public void run() {
                                try {
                                    String fajl = (String) ois1.readObject();
                                    File f = new File(fajl);
                                    if (f.exists()) {
                                        FileInputStream citac = new FileInputStream(f);
                                        ObjectInputStream citanjeObjekta = new ObjectInputStream(citac);
                                        turisti5 = (ArrayList<Turista>) citanjeObjekta.readObject();
                                    }

                                } catch (IOException | ClassNotFoundException ex) {
                                    Logger.getLogger(FXMLKorisnickiDioController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        tabela.getItems().clear();
                                        for (Turista k : turisti5) {
                                            tabela.getItems().add(k);
                                        }
                                        preuzmiButton.setVisible(true);
                                        prikaziButton.setVisible(true);
                                        pokreniButton.setDisable(false);
                                    }

                                });

                            }
                        };
                        new Timer().schedule(task1, 0);
                        new Timer().schedule(task2, 0);

                    } else {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Stage stage = new Stage();
                                StackPane root = new StackPane();
                                Label l = new Label("Dimenzije matrice su premale");
                                root.getChildren().add(l);
                                Scene scene = new Scene(root, 300, 250);
                                stage.setTitle("Obavještenje!");
                                stage.setScene(scene);
                                stage.show();
                            }
                        });

                    }
                } else {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Stage stage = new Stage();
                            StackPane root = new StackPane();
                            Label l = new Label("Svi podaci nisu uneseni ili nisu ispravni");
                            root.getChildren().add(l);
                            Scene scene = new Scene(root, 300, 250);
                            stage.setTitle("Obavještenje!");
                            stage.setScene(scene);
                            stage.show();
                        }
                    });
                }
            }
        });
        preuzmiButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                PrintWriter writer = null;
                try {

                    writer = new PrintWriter("./RezultatiTop5.csv", "UTF-8");
                    for (Turista k1 : turisti5) {
                        writer.println(k1.getIme() + "," + k1.getPosijeceno());
                    }
                } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                } finally {
                    writer.close();
                }
            }
        });
        prikaziButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                Random rand = new Random();
                if (!tabela.getSelectionModel().isEmpty()) {
                    Turista pom = tabela.getSelectionModel().getSelectedItem();
                    File folder = new File("./" + pom.getIme());
                    File fajlovi[] = folder.listFiles();
                    if (fajlovi.length > 0) {
                        File citanje = fajlovi[rand.nextInt(fajlovi.length)];
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Stage stage = new Stage();
                                stage.initModality(Modality.APPLICATION_MODAL);
                                StackPane root = new StackPane();
                                TextArea ispisText = new TextArea();
                                ArrayList<String> text = new ArrayList<>();
                                try {
                                    text = (ArrayList<String>) Files.readAllLines(citanje.toPath());
                                } catch (IOException ex) {
                                    Logger.getLogger(FXMLKorisnickiDioController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                for (String t : text) {
                                    ispisText.appendText(t + "\n");
                                }
                                root.getChildren().add(ispisText);
                                Scene scene = new Scene(root, 700, 650);
                                stage.setTitle("Letak");
                                stage.setScene(scene);
                                stage.show();
                            }
                        });
                    } else {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Stage stage = new Stage();
                                StackPane root = new StackPane();
                                Label l = new Label("Ovaj turista nema letaka");
                                root.getChildren().add(l);
                                Scene scene = new Scene(root, 300, 250);
                                stage.setTitle("Obavještenje!");
                                stage.setScene(scene);
                                stage.show();
                            }
                        });
                    }
                }
                else {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Stage stage = new Stage();
                                StackPane root = new StackPane();
                                Label l = new Label("Izaberite turistu");
                                root.getChildren().add(l);
                                Scene scene = new Scene(root, 300, 250);
                                stage.setTitle("Obavještenje!");
                                stage.setScene(scene);
                                stage.show();
                            }
                        });
                    }
            }
        });
    }

    public boolean provjeraBroj(String cijena) {
        return cijena.matches("[0-9]+");

    }

    public boolean provjeraListe(int x, int y) {
        boolean pom = true;
        for (Turista tur : turisti) {
            if (tur.getX() == x && tur.getY() == y) {
                pom = false;
            }
        }
        return pom;
    }

}
