package App;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.commons.math.optimization.OptimizationException;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent fxmlLoader = FXMLLoader.load(Objects.requireNonNull(Application.class.getResource("Start.fxml")));
        stage.setTitle("Distribute profit");
        stage.getIcons().add(new Image(Objects.requireNonNull(Application.class.getResourceAsStream("DPIcon.png"))));
        stage.setScene(new Scene(fxmlLoader));
        stage.show();
    }

    public static void main(String[] args) throws OptimizationException, IOException {
        launch();
    }

}
