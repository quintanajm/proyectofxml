package rg.quintana.proyectofxml;

import java.io.IOException;
import javafx.fxml.FXML;
import javax.persistence.EntityManager;

public class PrimaryController {

    private EntityManager entityManager;

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}