package App;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import Backpack.Agent;
import DataBank.Bank;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerAddAgent {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button add;

    @FXML
    private TextField budget;

    @FXML
    private TextField name;

    @FXML
    void initialize() {
        assert add != null : "fx:id=\"add\" was not injected: check your FXML file 'AddAgent.fxml'.";
        assert budget != null : "fx:id=\"budget\" was not injected: check your FXML file 'AddAgent.fxml'.";
        assert name != null : "fx:id=\"name\" was not injected: check your FXML file 'AddAgent.fxml'.";
        add.setOnAction(actionEvent -> {
            if (name.getText() != null && !Objects.equals(name.getText(), "") && checkAgent()) {
                if (budget.getText() != null && isNumeric(budget.getText()) && !Objects.equals(budget.getText(), "") && checkNumber()) {
                    Bank.getAgents().add(new Agent(name.getText(), Double.parseDouble(budget.getText())));
                    Bank.getAgentTableView().getItems().add(new Agent(name.getText(), Double.parseDouble(budget.getText())));
                    Stage stage = (Stage) add.getScene().getWindow();
                    stage.close();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Введено некоректное значение");
                    alert.showAndWait();
                }
            } else {
                if (!checkAgent()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Такое имя агента уже есть или оно не вписано");
                    alert.showAndWait();

                }
            }

        });
    }

    private static boolean isNumeric(String string) {
        double value;
        if (string == null || string.equals("")) {

            return false;
        }
        try {
            value = Double.parseDouble(string);
            return true;
        } catch (NumberFormatException ignored) {

        }
        return false;
    }

    private boolean checkAgent() {
        for (Agent agent :
                Bank.getAgents()) {
            if (Objects.equals(agent.getName(), name.getText()) || name.getText() == null || Objects.equals(name.getText(), ""))
                return false;
        }
        return true;
    }

    private boolean checkNumber() {
        if (isNumeric(budget.getText()))
            return !(Double.parseDouble(budget.getText()) < 0);
        return false;
    }

}
