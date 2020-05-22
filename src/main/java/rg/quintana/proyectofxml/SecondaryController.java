/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rg.quintana.proyectofxml;

import java.io.File;
import java.io.IOException;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * FXML Controller class
 *
 * @author josem
 */
public class SecondaryController implements Initializable {

    private TableView tableViewPrevio;
    private Jugadores jugador;
    private EntityManager entityManager;
    private boolean nuevoJugador;

    private Pane rootJugadoresView;

    @FXML
    private TextField textFieldNombre;
    @FXML
    private TextField textFieldApellidos;
    @FXML
    private TextField textFieldNacionalidad;
    @FXML
    private TextField textFieldValor;
    @FXML
    private TextField textFieldDorsal;
    @FXML
    private RadioButton radioButtonDisponible;
    @FXML
    private RadioButton radioButtonNoDisponible;
    @FXML
    private ImageView imageViewFoto;
    @FXML
    private ComboBox<Club> comboBoxClub;
    @FXML
    private AnchorPane rootJugadorDetalleView;

    public static final String CARPETA_FOTOS = "Fotos";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void onActionButtonGuardar(ActionEvent event) {
        StackPane rootMain = (StackPane) rootJugadorDetalleView.getScene().getRoot();
        rootMain.getChildren().remove(rootJugadorDetalleView);

        rootJugadoresView.setVisible(true);

        jugador.setNombre(textFieldNombre.getText());
        jugador.setApellidos(textFieldApellidos.getText());
        jugador.setNacionalidad(textFieldNacionalidad.getText());

        entityManager.getTransaction().begin();

        if (comboBoxClub.getValue() != null) {
            jugador.setClub(comboBoxClub.getValue());
        } else {
            Alert alert = new Alert(AlertType.INFORMATION, "Debe indicar un club");
            alert.showAndWait();
            boolean errorFormato = true;
        }

        if (radioButtonDisponible.isSelected()) {
            jugador.setDisponible(TRUE);
        } else {
            jugador.setDisponible(FALSE);
        }

        if (nuevoJugador) {
//            para meter un nuevo objeto
            entityManager.persist(jugador);
        } else {
//            para actualizar el objeto
            entityManager.merge(jugador);
        }

        if (!textFieldDorsal.getText().isEmpty()) {
            try {
                jugador.setDorsal(Integer.valueOf(textFieldDorsal.getText()));
            } catch (NumberFormatException ex) {
                boolean errorFormato = true;
                Alert alert = new Alert(AlertType.INFORMATION, "Dorsal no válido");
                alert.showAndWait();
                textFieldDorsal.requestFocus();
            }
        }

        if (!textFieldValor.getText().isEmpty()) {
            try {
                jugador.setValor(BigDecimal.valueOf(Double.valueOf(textFieldValor.getText()).doubleValue()));
            } catch (NumberFormatException ex) {
                boolean errorFormato = true;
                Alert alert = new Alert(AlertType.INFORMATION, "Salario no válido");
                alert.showAndWait();
                textFieldValor.requestFocus();
            }
        }

        entityManager.getTransaction().commit();

        int numFilaSeleccionada;
        if (nuevoJugador) {
            tableViewPrevio.getItems().add(jugador);
            numFilaSeleccionada = tableViewPrevio.getItems().size() - 1;
            tableViewPrevio.getSelectionModel().select(numFilaSeleccionada);
            tableViewPrevio.scrollTo(numFilaSeleccionada);
        } else {
            numFilaSeleccionada = tableViewPrevio.getSelectionModel().getSelectedIndex();
            tableViewPrevio.getItems().set(numFilaSeleccionada, jugador);
        }
        TablePosition pos = new TablePosition(tableViewPrevio, numFilaSeleccionada, null);
        tableViewPrevio.getFocusModel().focus(pos);
        tableViewPrevio.requestFocus();

    }

    @FXML
    private void onActionButtonCancelar(ActionEvent event) {
        StackPane rootMain = (StackPane) rootJugadorDetalleView.getScene().getRoot();
        rootMain.getChildren().remove(rootJugadorDetalleView);

        rootJugadoresView.setVisible(true);

        int numFilaSeleccionada = tableViewPrevio.getSelectionModel().getSelectedIndex();
        TablePosition pos = new TablePosition(tableViewPrevio, numFilaSeleccionada, null);
        tableViewPrevio.getFocusModel().focus(pos);
        tableViewPrevio.requestFocus();

    }

    public void setrootJugadoresView(Pane rootJugadoresView) {
        this.rootJugadoresView = rootJugadoresView;
    }

    public void setTableViewPrevio(TableView tableViewPrevio) {
        this.tableViewPrevio = tableViewPrevio;
    }

    public void setJugador(EntityManager entityManager, Jugadores jugador, boolean nuevoJugador) {
        this.entityManager = entityManager;

        if (!nuevoJugador) {
            this.jugador = entityManager.find(Jugadores.class, jugador.getId());
        } else {
            this.jugador = jugador;
        }
        this.nuevoJugador = nuevoJugador;
    }

    public void mostrarDatos() {
        textFieldNombre.setText(jugador.getNombre());
        textFieldApellidos.setText(jugador.getApellidos());
        textFieldNacionalidad.setText(jugador.getNacionalidad());

        if (jugador.getValor() != null) {
            textFieldValor.setText(jugador.getValor().toString());
        }

        if (jugador.getDorsal() != null) {
            textFieldDorsal.setText(jugador.getDorsal().toString());
        }

        if (jugador.getFoto() != null) {
            String imageFileName = jugador.getFoto();
            File file = new File(CARPETA_FOTOS + "/" + imageFileName);
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                imageViewFoto.setImage(image);
            } else {
                Alert alert = new Alert(AlertType.INFORMATION, "No se encuentra la imagen");
                alert.showAndWait();
            }
        }

        if (jugador.getDisponible() != null) {
//            si esta disponible, que seleccione RadioButtonDisponible
            if (jugador.getDisponible() == TRUE) {
                radioButtonDisponible.setSelected(TRUE);
            } else {
                radioButtonNoDisponible.setSelected(TRUE);
            }
        }

        Query queryProvinciaFindAll = entityManager.createNamedQuery("Club.findAll");
        List listClub = queryProvinciaFindAll.getResultList();
        comboBoxClub.setItems(FXCollections.observableList(listClub));

        if (jugador.getClub() != null) {
            comboBoxClub.setValue(jugador.getClub());
        }
        comboBoxClub.setCellFactory((ListView<Club> l) -> new ListCell<Club>() {
            @Override
            protected void updateItem(Club club, boolean empty) {
                super.updateItem(club, empty);
                if (club == null || empty) {
                    setText("");
                } else {
                    setText(club.getNombre());
                }
            }
        });

        comboBoxClub.setConverter(new StringConverter<Club>() {
            @Override
            public String toString(Club club) {
                if (club == null) {
                    return null;
                } else {
                    return club.getNombre();
                }
            }

            @Override
            public Club fromString(String userId) {
                return null;
            }
        });
    }

    @FXML
    private void onActionButtonExaminar(ActionEvent event) {
        System.out.println("llega");
        File carpetaFotos = new File(CARPETA_FOTOS);

        if (!carpetaFotos.exists()) {
            carpetaFotos.mkdir();
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes (jpg, png)", "*.jpg", "*.png"),
                new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );
        File file = fileChooser.showOpenDialog(rootJugadorDetalleView.getScene().getWindow());
        if (file != null) {
            try {
                Files.copy(file.toPath(), new File(CARPETA_FOTOS + "/" + file.getName()).toPath());
                jugador.setFoto(file.getName());
                Image image = new Image(file.toURI().toString());
                imageViewFoto.setImage(image);
            } catch (FileAlreadyExistsException ex) {
                Alert alert = new Alert(AlertType.WARNING, "Nombre de archivo duplicado");
                alert.showAndWait();
            } catch (IOException ex) {
                Alert alert = new Alert(AlertType.WARNING, "No se ha podido guardar la imagen");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void onActionSuprimirFoto(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar supresión de imagen");
        alert.setHeaderText("¿Desea SUPRIMIR el archivo asociado a la imagen, \n"
                + "quitar la foto pero MANTENER el archivo, \no CANCELAR la operación?");
        alert.setContentText("Elija la opción deseada:");

        ButtonType buttonTypeEliminar = new ButtonType("Suprimir");
        ButtonType buttonTypeMantener = new ButtonType("Mantener");
        ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeEliminar, buttonTypeMantener, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeEliminar) {
            String imageFileName = jugador.getFoto();
            File file = new File(CARPETA_FOTOS + "/" + imageFileName);
            if (file.exists()) {
                file.delete();
            }
            jugador.setFoto(null);
            imageViewFoto.setImage(null);
        } else if (result.get() == buttonTypeMantener) {
            jugador.setFoto(null);
            imageViewFoto.setImage(null);
        }
    }

}
