package rg.quintana.proyectofxml;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/**
 * JavaFX App
 */
public class App extends Application {

    private EntityManagerFactory emf;
    private EntityManager em;

    private static Scene scene;

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("primary.fxml"));
        Parent root = fxmlLoader.load();
        PrimaryController primaryController = (PrimaryController) fxmlLoader.getController();

        emf = Persistence.createEntityManagerFactory("AgendaJugadoresFutbolPU");
        em = emf.createEntityManager();
        primaryController.setEntityManager(em);
        
        primaryController.cargarTodosJugadores();

        Scene scene = new Scene(root, 600, 400);

        primaryStage.setTitle("proyectoFXML");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
        

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    
    @Override
    public void stop() throws Exception {
        em.close();
        emf.close();
        try {
            DriverManager.getConnection("jdbc:derby:BDAgendaJugadores;shutdown=true");
        } catch (SQLException ex) {
        }
    }

}
