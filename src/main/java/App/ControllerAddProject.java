package App;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import Backpack.Agent;
import Backpack.Project;
import DataBank.Bank;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerAddProject {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button add;

    @FXML
    private ComboBox<String> agents;

    @FXML
    private TextField expense;

    @FXML
    private TextField name;

    @FXML
    private TextField profit;

    @FXML
    void initialize() {
        assert add != null : "fx:id=\"add\" was not injected: check your FXML file 'AddAgent.fxml'.";
        assert agents != null : "fx:id=\"agents\" was not injected: check your FXML file 'AddAgent.fxml'.";
        assert expense != null : "fx:id=\"expense\" was not injected: check your FXML file 'AddAgent.fxml'.";
        assert name != null : "fx:id=\"name\" was not injected: check your FXML file 'AddAgent.fxml'.";
        assert profit != null : "fx:id=\"profit\" was not injected: check your FXML file 'AddAgent.fxml'.";
        if (Bank.getAgents().size() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Отсутствуют агенты!!!!!");
            alert.showAndWait();
            Stage stage = (Stage) agents.getScene().getWindow();
            stage.close();

        }
        for (Agent agent : Bank.getAgents())
            agents.getItems().add(agent.getName());
        add.setOnAction(actionEvent -> {
            if (agents.getValue() != null) {
                if (name.getText() != null && !Objects.equals(name.getText(), "") && checkProject()) {
                    if (expense.getText() != null && isNumeric(expense.getText()) && !Objects.equals(expense.getText(), "") && checkNumber(expense)
                            && profit.getText() != null && isNumeric(profit.getText()) && !Objects.equals(profit.getText(), "") && checkNumber(profit)) {
                        for (Agent agent :
                                Bank.getAgents()) {
                            if (Objects.equals(agent.getName(), agents.getValue())) {
                                agent.addProject(new Project(name.getText(), Double.parseDouble(expense.getText()), Double.parseDouble(profit.getText()), agent.getName()));
                                Bank.getProjectTableView().getItems().add(new Project(name.getText(), Double.parseDouble(expense.getText()), Double.parseDouble(profit.getText()), agent.getName()));
                            }
                        }
                        Stage stage = (Stage) add.getScene().getWindow();
                        stage.close();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Введено некорректное значение");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Такое имя уже существует или оно не вписано");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Выберите агента");
                alert.showAndWait();
            }
        });


    }

    private static boolean isNumeric(String string) {
        if (string == null || string.equals("")) {

            return false;
        }
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException ignored) {

        }
        return false;
    }

    private boolean checkProject() {
        for (Agent agent :
                Bank.getAgents()) {
            for (Project project :
                    agent.getProjects()) {
                if (Objects.equals(project.getName(), name.getText()))
                    return false;
            }

        }
        return true;
    }

    private boolean checkNumber(TextField textField) {
        if (isNumeric(textField.getText()))
            return !(Double.parseDouble(textField.getText()) < 0);
        return false;
    }

}

