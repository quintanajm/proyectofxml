package rg.quintana.proyectofxml;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class PrimaryController implements Initializable {

    private EntityManager entityManager;
    @FXML
    private TableView<Jugadores> tableViewJugadores;
    @FXML
    private TableColumn<Jugadores, String> columnNombre;
    @FXML
    private TableColumn<Jugadores, String> columnApellidos;
    @FXML
    private TableColumn<Jugadores, String> columnClub;
    @FXML
    private TextField textFieldNombre;
    @FXML
    private TextField textFieldApellidos;

    private Jugadores jugadorSeleccionado;
    @FXML
    private AnchorPane rootJugadoresView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        columnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        columnClub.setCellValueFactory(
                cellData -> {
                    SimpleStringProperty property = new SimpleStringProperty();
                    if (cellData.getValue().getClub() != null) {
                        property.setValue(cellData.getValue().getClub().getNombre());
                    }
                    return property;
                });

        tableViewJugadores.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    jugadorSeleccionado = newValue;
                    if (jugadorSeleccionado != null) {
                        textFieldNombre.setText(jugadorSeleccionado.getNombre());
                        textFieldApellidos.setText(jugadorSeleccionado.getApellidos());
                    } else {
                        textFieldNombre.setText("");
                        textFieldApellidos.setText("");
                    }
                });
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void cargarTodosJugadores() {
        Query queryJugadoresFindAll = entityManager.createNamedQuery("Jugadores.findAll");
        List<Jugadores> listJugador = queryJugadoresFindAll.getResultList();
        tableViewJugadores.setItems(FXCollections.observableArrayList(listJugador));
    }

    @FXML
    private void onActionButtonGuardar(ActionEvent event) {
        if (jugadorSeleccionado != null) {
            jugadorSeleccionado.setNombre(textFieldNombre.getText());
            jugadorSeleccionado.setApellidos(textFieldApellidos.getText());

            entityManager.getTransaction().begin();
            entityManager.merge(jugadorSeleccionado);
            entityManager.getTransaction().commit();

            int numFilaSeleccionada = tableViewJugadores.getSelectionModel().getSelectedIndex();
            tableViewJugadores.getItems().set(numFilaSeleccionada, jugadorSeleccionado);
            TablePosition pos = new TablePosition(tableViewJugadores, numFilaSeleccionada, null);
            tableViewJugadores.getFocusModel().focus(pos);
            tableViewJugadores.requestFocus();

        }
    }

    @FXML
    private void onActionButtonNuevo(ActionEvent event) {

        try {
            // Cargar la vista de detalle
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("secondary.fxml"));
            Parent rootDetalleView = fxmlLoader.load();
            // Ocultar la vista de la lista
            rootJugadoresView.setVisible(false);

            SecondaryController secondaryController = (SecondaryController) fxmlLoader.getController();
            secondaryController.setrootJugadoresView(rootJugadoresView);
            secondaryController.setTableViewPrevio(tableViewJugadores);

            jugadorSeleccionado = new Jugadores();
            secondaryController.setJugador(entityManager, jugadorSeleccionado, true);
            secondaryController.mostrarDatos();

            // Añadir la vista de detalle al StackPane principal para que se muestre
            StackPane rootMain = (StackPane) rootJugadoresView.getScene().getRoot();
            rootMain.getChildren().add(rootDetalleView);
        } catch (IOException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void onActionButtonEditar(ActionEvent event) {
        if (jugadorSeleccionado != null) {
            try {
                // Cargar la vista de detalle
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("secondary.fxml"));
                Parent rootDetalleView = fxmlLoader.load();
                // Ocultar la vista de la lista
                rootJugadoresView.setVisible(false);

                SecondaryController secondaryController = (SecondaryController) fxmlLoader.getController();
                secondaryController.setrootJugadoresView(rootJugadoresView);
                secondaryController.setTableViewPrevio(tableViewJugadores);
                secondaryController.setJugador(entityManager, jugadorSeleccionado, false);
                secondaryController.mostrarDatos();

                // Añadir la vista de detalle al StackPane principal para que se muestre
                StackPane rootMain = (StackPane) rootJugadoresView.getScene().getRoot();
                rootMain.getChildren().add(rootDetalleView);
            } catch (IOException ex) {
                Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("¡Cuidado!");
            alert.setContentText("Tienes que seleccionar un registro para editarlo.");

            alert.showAndWait();
        }
    }

    @FXML
    private void onActionButtonSuprimir(ActionEvent event) {
        if (jugadorSeleccionado != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmar");
            alert.setHeaderText("¿Desea suprimir el siguiente registro?");
            alert.setContentText(jugadorSeleccionado.getNombre() + " "
                    + jugadorSeleccionado.getApellidos());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                entityManager.getTransaction().begin();
                jugadorSeleccionado = entityManager.merge(jugadorSeleccionado);
                entityManager.remove(jugadorSeleccionado);
                entityManager.getTransaction().commit();

                tableViewJugadores.getItems().remove(jugadorSeleccionado);

                tableViewJugadores.getFocusModel().focus(null);
                tableViewJugadores.requestFocus();

            } else {
                int numFilaSeleccionada = tableViewJugadores.getSelectionModel().getSelectedIndex();
                tableViewJugadores.getItems().set(numFilaSeleccionada, jugadorSeleccionado);
                TablePosition pos = new TablePosition(tableViewJugadores, numFilaSeleccionada, null);
                tableViewJugadores.getFocusModel().focus(pos);
                tableViewJugadores.requestFocus();
            }
        }else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("¡Cuidado!");
            alert.setContentText("Tienes que seleccionar un registro para suprimirlo.");

            alert.showAndWait();
        }
    }
}
    