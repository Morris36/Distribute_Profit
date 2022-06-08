package App;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;


import Backpack.Agent;
import Backpack.Project;
import DataBank.Bank;
import FileScanner.FileXLS;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;

public class ControllerStart {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button Data;

    @FXML
    private Button NewData;

    @FXML
    void initialize() {
        assert Data != null : "fx:id=\"Data\" was not injected: check your FXML file 'Start.fxml'.";
        assert NewData != null : "fx:id=\"NewData\" was not injected: check your FXML file 'Start.fxml'.";
        NewData.setOnAction(actionEvent -> {
        openWindow();
        });
        AtomicReference<File> dir = new AtomicReference<>();
        Data.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Укажите путь до файла с данными");
            alert.showAndWait();
            FileChooser directoryChooser = new FileChooser();
            configuringDirectoryChooser(directoryChooser);
            dir.set(directoryChooser.showOpenDialog(new Stage()));
            FileXLS fileXLS = new FileXLS();
            try {
                Bank.setPatch(dir.get().getAbsolutePath());
                Bank.setAgents(fileXLS.readAgent(Bank.getPatch()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            openWindow();
        });

    }
    private void configuringDirectoryChooser(FileChooser directoryChooser) {
        directoryChooser.setTitle("Select Some Files");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    }

    private void openWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Objects.requireNonNull(Application.class.getResource("AddWindowOne.fxml")));
            Scene scene = new Scene(fxmlLoader.load(), -1, -1);
            Stage stage = new Stage();
            stage.getIcons().add(new Image(Objects.requireNonNull(Application.class.getResourceAsStream("DPIcon.png"))));
            stage.setTitle("Distribute Profit");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
        Stage stage = (Stage) NewData.getScene().getWindow();
        stage.close();
    }

}
