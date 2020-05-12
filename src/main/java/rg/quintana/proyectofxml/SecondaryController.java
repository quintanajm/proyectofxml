/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rg.quintana.proyectofxml;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javax.persistence.EntityManager;

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
    private ComboBox<?> comboBoxClub;
    @FXML
    private AnchorPane rootJugadorDetalleView;

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

        if (nuevoJugador) {
            entityManager.persist(jugador);
        } else {
            entityManager.merge(jugador);
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
//    entityManager.getTransaction().begin();
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

    }

}
