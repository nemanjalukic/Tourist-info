/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.projektni062017;

import net.etfbl.atrakcija.Crkva;
import net.etfbl.atrakcija.TuristickaAtrakcija;
import net.etfbl.atrakcija.ZabavniPark;
import net.etfbl.atrakcija.IstorijskiSpomenik;
import net.etfbl.atrakcija.Muzej;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import static net.etfbl.projektni062017.FXMLKorisnickiDioController.ois;
import static net.etfbl.projektni062017.FXMLKorisnickiDioController.ois1;
import static net.etfbl.projektni062017.FXMLKorisnickiDioController.oos;
import static net.etfbl.projektni062017.FXMLKorisnickiDioController.oos1;
import static net.etfbl.projektni062017.FXMLKorisnickiDioController.sock;
import static net.etfbl.projektni062017.FXMLKorisnickiDioController.sock1;
import net.etfbl.turista.TuristaKretanje;

/**
 *
 * @author Office
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Label cijenaLab;

    @FXML
    private ImageView slika;

    @FXML
    private Button dodajButton;

    @FXML
    private TextField nazivText;

    @FXML
    private Button korisnikButton;

    @FXML
    private ChoiceBox<String> tipChoice;

    @FXML
    private Button izmijenaButton;

    @FXML
    private Button izmijenaKonacnoButton;

    @FXML
    private Label opisLab;

    @FXML
    private TableView<TuristickaAtrakcija> tabela;

    @FXML
    private TableColumn<TuristickaAtrakcija, String> colLokacija;

    @FXML
    private TableColumn<TuristickaAtrakcija, String> colNaziv;

    @FXML
    private Button brisanjeButton;

    @FXML
    private Label fajlLab;

    @FXML
    private Button fajlButton;

    @FXML
    private TextField cijenaText;

    @FXML
    private TextField lokacijaText;

    @FXML
    private TextField opisText;

    public static ArrayList<TuristickaAtrakcija> atrakcije = new ArrayList<>();
    private FileChooser fileChooser = new FileChooser();
    private File dodaniFajl = null;
    ExtensionFilter text = new ExtensionFilter("Text Files", "*.txt");
    ExtensionFilter slike = new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif");
    ExtensionFilter sve = new ExtensionFilter("All Files", "*.*");
    Random rand = new Random();
    TuristickaAtrakcija pom;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fileChooser.getExtensionFilters().addAll(text, slike, sve);

        izmijenaKonacnoButton.setVisible(false);

        tipChoice.setItems(FXCollections.observableArrayList("Muzej", "Zabavni park", "Istorijski spomenik", "Crkva"));
        colNaziv.setCellValueFactory(new PropertyValueFactory<TuristickaAtrakcija, String>("naziv"));
        colLokacija.setCellValueFactory(new PropertyValueFactory<TuristickaAtrakcija, String>("lokacija"));

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
            tabela.getItems().clear();
            for (TuristickaAtrakcija k : atrakcije) {
                tabela.getItems().add(k);
            }

        }

        dodajButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                if (!tipChoice.getSelectionModel().isEmpty()) {
                    if (tipChoice.getSelectionModel().getSelectedItem().equals("Muzej") && !nazivText.getText().isEmpty() && !lokacijaText.getText().isEmpty() && dodaniFajl != null) {
                        if (!cijenaText.getText().isEmpty()) {
                            if (provjeraCijene(cijenaText.getText())) {
                                atrakcije.add(new Muzej(nazivText.getText(), lokacijaText.getText(), dodaniFajl, Integer.parseInt(cijenaText.getText())));
                                nazivText.clear();
                                lokacijaText.clear();
                                cijenaText.clear();
                                dodaniFajl = null;
                                fajlLab.setText("");

                            } else {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        Stage stage = new Stage();
                                        StackPane root = new StackPane();
                                        Label l = new Label("Unos cijene nije dobar");
                                        root.getChildren().add(l);
                                        Scene scene = new Scene(root, 300, 250);
                                        stage.setTitle("Obavještenje!");
                                        stage.setScene(scene);
                                        stage.show();
                                    }
                                });

                            }
                        } else {
                            atrakcije.add(new Muzej(nazivText.getText(), lokacijaText.getText(), dodaniFajl, rand.nextInt(70) + 10));
                            nazivText.clear();
                            lokacijaText.clear();
                            dodaniFajl = null;
                            fajlLab.setText("");
                        }

                    } else if (tipChoice.getSelectionModel().getSelectedItem().equals("Zabavni park") && !nazivText.getText().isEmpty() && !lokacijaText.getText().isEmpty()) {
                        if (!cijenaText.getText().isEmpty()) {
                            if (provjeraCijene(cijenaText.getText())) {
                                atrakcije.add(new ZabavniPark(nazivText.getText(), lokacijaText.getText(), Integer.parseInt(cijenaText.getText())));
                                nazivText.clear();
                                lokacijaText.clear();
                                cijenaText.clear();
                            } else {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        Stage stage = new Stage();
                                        StackPane root = new StackPane();
                                        Label l = new Label("Unos cijene nije dobar");
                                        root.getChildren().add(l);
                                        Scene scene = new Scene(root, 300, 250);
                                        stage.setTitle("Obavještenje!");
                                        stage.setScene(scene);
                                        stage.show();
                                    }
                                });

                            }
                        } else {
                            atrakcije.add(new ZabavniPark(nazivText.getText(), lokacijaText.getText(), rand.nextInt(70) + 10));
                            nazivText.clear();
                            lokacijaText.clear();
                            cijenaText.clear();
                        }

                    } else if (tipChoice.getSelectionModel().getSelectedItem().equals("Istorijski spomenik") && !nazivText.getText().isEmpty() && !lokacijaText.getText().isEmpty() && !opisText.getText().isEmpty() && dodaniFajl != null) {
                        atrakcije.add(new IstorijskiSpomenik(nazivText.getText(), lokacijaText.getText(), opisText.getText(), dodaniFajl));
                        nazivText.clear();
                        lokacijaText.clear();
                        opisText.clear();
                        dodaniFajl = null;
                        slika.setImage(null);

                    } else if (tipChoice.getSelectionModel().getSelectedItem().equals("Crkva") && !nazivText.getText().isEmpty() && !lokacijaText.getText().isEmpty()) {
                        atrakcije.add(new Crkva(nazivText.getText(), lokacijaText.getText()));
                        nazivText.clear();
                        lokacijaText.clear();

                    } else {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Stage stage = new Stage();
                                StackPane root = new StackPane();
                                Label l = new Label("Podaci nisu dobro unijeti");
                                root.getChildren().add(l);
                                Scene scene = new Scene(root, 300, 250);
                                stage.setTitle("Obavještenje!");
                                stage.setScene(scene);
                                stage.show();
                            }
                        });

                    }

                    tabela.getItems().clear();
                    for (TuristickaAtrakcija k : atrakcije) {
                        tabela.getItems().add(k);
                    }

                    izmijenaKonacnoButton.setVisible(false);
                    izmijenaButton.setVisible(true);
                } else {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Stage stage = new Stage();
                            StackPane root = new StackPane();
                            Label l = new Label("Izaberite tip atrakcije");
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

        brisanjeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (tabela.getSelectionModel().isEmpty()) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Stage stage = new Stage();
                            StackPane root = new StackPane();
                            Label l = new Label("Izaberite jednu atrakciju");
                            root.getChildren().add(l);
                            Scene scene = new Scene(root, 300, 250);
                            stage.setTitle("Obavještenje!");
                            stage.setScene(scene);
                            stage.show();
                        }
                    });
                } else {
                    TuristickaAtrakcija pom1 = (TuristickaAtrakcija) tabela.getSelectionModel().getSelectedItem();
                    atrakcije.remove(pom1);
                    tabela.getItems().clear();
                    for (TuristickaAtrakcija k : atrakcije) {
                        tabela.getItems().add(k);
                    }

                }
                izmijenaKonacnoButton.setVisible(false);
                izmijenaButton.setVisible(true);
            }
        });
        fajlButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                Stage s = new Stage();
                File file = fileChooser.showOpenDialog(s);
                if (file != null) {
                    dodaniFajl = file;
                    if (tipChoice.getSelectionModel().getSelectedItem().equals("Istorijski spomenik")) {
                        try {
                            InputStream in = new FileInputStream(file.getAbsolutePath());
                            slika.setImage(new Image(in));
                            in.close();
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(TuristaKretanje.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(TuristaKretanje.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (tipChoice.getSelectionModel().getSelectedItem().equals("Muzej")) {
                        fajlLab.setText(file.getName());

                    }

                }
            }
        });

        tipChoice.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                if (tipChoice.getSelectionModel().getSelectedItem().equals("Muzej")) {
                    opisLab.setVisible(false);
                    opisText.setVisible(false);
                    cijenaLab.setVisible(true);
                    cijenaText.setVisible(true);
                    fajlButton.setVisible(true);
                    fileChooser.setSelectedExtensionFilter(text);

                } else if (tipChoice.getSelectionModel().getSelectedItem().equals("Zabavni park")) {
                    opisLab.setVisible(false);
                    opisText.setVisible(false);
                    cijenaLab.setVisible(true);
                    cijenaText.setVisible(true);
                    fajlButton.setVisible(false);
                } else if (tipChoice.getSelectionModel().getSelectedItem().equals("Istorijski spomenik")) {
                    opisLab.setVisible(true);
                    opisText.setVisible(true);
                    cijenaLab.setVisible(false);
                    cijenaText.setVisible(false);
                    fajlButton.setVisible(true);
                    fileChooser.setSelectedExtensionFilter(slike);

                } else if (tipChoice.getSelectionModel().getSelectedItem().equals("Crkva")) {
                    opisLab.setVisible(false);
                    opisText.setVisible(false);
                    cijenaLab.setVisible(false);
                    cijenaText.setVisible(false);
                    fajlButton.setVisible(false);

                }
                izmijenaKonacnoButton.setVisible(false);
                izmijenaButton.setVisible(true);
                 dodajButton.setDisable(false);
                ciscenje();
            }
        });
        izmijenaButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                if (tabela.getSelectionModel().isEmpty()) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Stage stage = new Stage();
                            StackPane root = new StackPane();
                            Label l = new Label("Izaberite jednu atrakciju");
                            root.getChildren().add(l);
                            Scene scene = new Scene(root, 300, 250);
                            stage.setTitle("Obavještenje!");
                            stage.setScene(scene);
                            stage.show();
                        }
                    });
                } else {
                    pom = (TuristickaAtrakcija) tabela.getSelectionModel().getSelectedItem();
                    if (pom instanceof Muzej) {
                        Muzej muz = (Muzej) pom;
                        tipChoice.getSelectionModel().select("Muzej");
                        nazivText.setText(muz.getNaziv());
                        lokacijaText.setText(muz.getLokacija());
                        cijenaText.setText(Integer.toString(muz.getCijena()));
                        dodaniFajl = muz.getLetak();
                    }
                    if (pom instanceof ZabavniPark) {
                        ZabavniPark park = (ZabavniPark) pom;
                        tipChoice.getSelectionModel().select("Zabavni park");
                        nazivText.setText(park.getNaziv());
                        lokacijaText.setText(park.getLokacija());
                        cijenaText.setText(Integer.toString(park.getCijena()));
                    }
                    if (pom instanceof IstorijskiSpomenik) {
                        IstorijskiSpomenik spom = (IstorijskiSpomenik) pom;
                        tipChoice.getSelectionModel().select("Istorijski spomenik");
                        nazivText.setText(spom.getNaziv());
                        lokacijaText.setText(spom.getLokacija());
                        opisText.setText(spom.getOpis());
                        dodaniFajl = spom.getSlika();
                    }
                    if (pom instanceof Crkva) {
                        Crkva crkva = (Crkva) pom;
                        tipChoice.getSelectionModel().select("Crkva");
                        nazivText.setText(crkva.getNaziv());
                        lokacijaText.setText(crkva.getLokacija());
                    }
                    izmijenaKonacnoButton.setVisible(true);
                    izmijenaButton.setVisible(false);
                    dodajButton.setDisable(true);

                }

            }
        });

        izmijenaKonacnoButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                atrakcije.remove(pom);

                if (tipChoice.getSelectionModel().getSelectedItem().equals("Muzej") && !nazivText.getText().isEmpty() && !lokacijaText.getText().isEmpty() && dodaniFajl != null) {
                    if (!cijenaText.getText().isEmpty()) {
                        if (provjeraCijene(cijenaText.getText())) {
                            atrakcije.add(new Muzej(nazivText.getText(), lokacijaText.getText(), dodaniFajl, Integer.parseInt(cijenaText.getText())));
                            nazivText.clear();
                            lokacijaText.clear();
                            cijenaText.clear();
                            dodaniFajl = null;
                            izmijenaKonacnoButton.setVisible(false);
                            izmijenaButton.setVisible(true);
                        } else {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    Stage stage = new Stage();
                                    StackPane root = new StackPane();
                                    Label l = new Label("Unos cijene nije dobar");
                                    root.getChildren().add(l);
                                    Scene scene = new Scene(root, 300, 250);
                                    stage.setTitle("Obavještenje!");
                                    stage.setScene(scene);
                                    stage.show();
                                }
                            });

                        }
                    } else {
                        atrakcije.add(new Muzej(nazivText.getText(), lokacijaText.getText(), dodaniFajl, rand.nextInt(70) + 10));
                        nazivText.clear();
                        lokacijaText.clear();
                        cijenaText.clear();
                        dodaniFajl = null;
                        izmijenaKonacnoButton.setVisible(false);
                        izmijenaButton.setVisible(true);
                    }

                } else if (tipChoice.getSelectionModel().getSelectedItem().equals("Zabavni park") && !nazivText.getText().isEmpty() && !lokacijaText.getText().isEmpty()) {
                    if (!cijenaText.getText().isEmpty()) {
                        if (provjeraCijene(cijenaText.getText())) {
                            atrakcije.add(new ZabavniPark(nazivText.getText(), lokacijaText.getText(), Integer.parseInt(cijenaText.getText())));
                            nazivText.clear();
                            lokacijaText.clear();
                            cijenaText.clear();
                            izmijenaKonacnoButton.setVisible(false);
                            izmijenaButton.setVisible(true);
                        } else {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    Stage stage = new Stage();
                                    StackPane root = new StackPane();
                                    Label l = new Label("Unos cijene nije dobar");
                                    root.getChildren().add(l);
                                    Scene scene = new Scene(root, 300, 250);
                                    stage.setTitle("Obavještenje!");
                                    stage.setScene(scene);
                                    stage.show();
                                }
                            });

                        }
                    } else {
                        atrakcije.add(new ZabavniPark(nazivText.getText(), lokacijaText.getText(), rand.nextInt(70) + 10));
                        nazivText.clear();
                        lokacijaText.clear();
                        cijenaText.clear();
                        izmijenaKonacnoButton.setVisible(false);
                        izmijenaButton.setVisible(true);
                    }

                } else if (tipChoice.getSelectionModel().getSelectedItem().equals("Istorijski spomenik") && !nazivText.getText().isEmpty() && !lokacijaText.getText().isEmpty() && !opisText.getText().isEmpty() && dodaniFajl != null) {
                    atrakcije.add(new IstorijskiSpomenik(nazivText.getText(), lokacijaText.getText(), opisText.getText(), dodaniFajl));
                    nazivText.clear();
                    lokacijaText.clear();
                    opisText.clear();
                    dodaniFajl = null;
                    izmijenaKonacnoButton.setVisible(false);
                    izmijenaButton.setVisible(true);

                } else if (tipChoice.getSelectionModel().getSelectedItem().equals("Crkva") && !nazivText.getText().isEmpty() && !lokacijaText.getText().isEmpty()) {
                    atrakcije.add(new Crkva(nazivText.getText(), lokacijaText.getText()));
                    nazivText.clear();
                    lokacijaText.clear();
                    izmijenaKonacnoButton.setVisible(false);
                    izmijenaButton.setVisible(true);

                } else {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Stage stage = new Stage();
                            StackPane root = new StackPane();
                            Label l = new Label("Podaci nisu dobro unijeti");
                            root.getChildren().add(l);
                            Scene scene = new Scene(root, 300, 250);
                            stage.setTitle("Obavještenje!");
                            stage.setScene(scene);
                            stage.show();
                        }
                    });

                }

                tabela.getItems().clear();
                for (TuristickaAtrakcija k : atrakcije) {
                    tabela.getItems().add(k);
                }
                dodajButton.setDisable(false);

            }
        });
        korisnikButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                if (atrakcije.size() > 0) {
                    ObjectOutputStream upisObjekta = null;
                    try {
                        FileOutputStream pisac = new FileOutputStream("./turisticka-mapa.ser");
                        upisObjekta = new ObjectOutputStream(pisac);
                        upisObjekta.writeObject(atrakcije);
                        upisObjekta.close();
                        pisac.close();
                        Stage s = new Stage();
                        Pane myPane = (Pane) FXMLLoader.load(getClass().getResource("/net/etfbl/projektni062017/FXMLKorisnickiDio.fxml"));
                        Scene myScene = new Scene(myPane);
                        s.setScene(myScene);
                        s.show();
                        s.setOnCloseRequest(new EventHandler<WindowEvent>() {
                                @Override
                                public void handle(WindowEvent event) {
                                    try {
                                        oos.writeObject("END");
                                        oos.flush();
                                        oos.close();
                                        ois.close();
                                        sock.close();
                                        oos1.writeObject("END");
                                        oos1.flush();
                                        oos1.close();
                                        ois1.close();
                                        sock1.close();
                                        System.exit(0);
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            });
                        Node source = (Node) e.getSource();
                        Stage stage = (Stage) source.getScene().getWindow();
                        stage.close();

                    } catch (IOException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        try {
                            upisObjekta.close();
                        } catch (IOException ex) {
                            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Stage stage = new Stage();
                            StackPane root = new StackPane();
                            Label l = new Label("Ne postoje atrakcije, dodajte bar jednu");
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

    public void ciscenje() {
        nazivText.clear();
        lokacijaText.clear();
        opisText.clear();
        cijenaText.clear();
        dodaniFajl = null;
        fajlLab.setText("");
        slika.setImage(null);

    }

    public boolean provjeraCijene(String cijena) {
        return cijena.matches("[0-9]+");

    }

}
