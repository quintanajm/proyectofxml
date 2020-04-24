package rg.quintana.proyectofxml;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
    }

}
