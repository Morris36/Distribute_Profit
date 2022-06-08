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
import Backpack.Backpack;
import Backpack.Project;
import DataBank.Bank;
import FileReader.ReaderTXT;
import FileScanner.FileXLS;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;

public class ControllerAddWindowOne {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addAgent;

    @FXML
    private Button addProject;

    @FXML
    private Button removeAgent;

    @FXML
    private Button removeProject;

    @FXML
    private Button result;

    @FXML
    private TableView<Agent> tableAgents;

    @FXML
    private TableView<Project> tableProjects;

    @FXML
    void initialize() {
        assert addAgent != null : "fx:id=\"addAgent\" was not injected: check your FXML file 'AddWindowOne.fxml'.";
        assert addProject != null : "fx:id=\"addProject\" was not injected: check your FXML file 'AddWindowOne.fxml'.";
        assert removeAgent != null : "fx:id=\"removeAgent\" was not injected: check your FXML file 'AddWindowOne.fxml'.";
        assert removeProject != null : "fx:id=\"removeProject\" was not injected: check your FXML file 'AddWindowOne.fxml'.";
        assert result != null : "fx:id=\"result\" was not injected: check your FXML file 'AddWindowOne.fxml'.";
        assert tableAgents != null : "fx:id=\"tableAgents\" was not injected: check your FXML file 'AddWindowOne.fxml'.";
        assert tableProjects != null : "fx:id=\"tableProjects\" was not injected: check your FXML file 'AddWindowOne.fxml'.";
        Bank.setAgentTableView(tableAgents);
        Bank.setProjectTableView(tableProjects);
        tableAgents.setEditable(true);
        initTable();
        fillTable();
        addAgent.setOnAction(actionEvent -> {
            openWindow("AddAgent.fxml");
        });

        addProject.setOnAction(actionEvent -> {
            openWindow("AddProject.fxml");
        });
        removeAgent.setOnAction(actionEvent -> {
            openWindow("RemoveAgent.fxml");
        });
        removeProject.setOnAction(actionEvent -> openWindow("RemoveProject.fxml"));
        result.setOnAction(actionEvent -> {
            if (Bank.getAgents().size() <= 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Ничего не введено");
                alert.showAndWait();
            } else {
                Backpack backpack = new Backpack(Bank.getAgents());
                Bank bank = backpack.getResultBackpack();
                if (bank.getUnions().get(0).getProjects().size() == 0){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Введены некоректные значения");
                    alert.showAndWait();
                }else {
                    AtomicReference<File> dir = new AtomicReference<>();
                    if (Objects.equals(Bank.getPatch(), "None Patch")) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Укажите путь до папки, куда будет сохранён файл с данными (Data.xlsx)");
                        alert.showAndWait();
                        DirectoryChooser directoryChooser = new DirectoryChooser();
                        configuringDirectoryChooser(directoryChooser);
                        dir.set(directoryChooser.showDialog(new Stage()));
                        Bank.setPatch(dir.get().getAbsolutePath());
                        String nameFile = "Data.xlsx";
                        try {
                            File tmp = ReaderTXT.createNewFile(new File(Bank.getPatch(), nameFile));
                            FileXLS fileXLS = new FileXLS();
                            fileXLS.saveNullFile(tmp.getAbsolutePath());
                            fileXLS.save(tmp.getAbsolutePath());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        FileXLS fileXLS = new FileXLS();
                        try {
                            fileXLS.save(Bank.getPatch());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    Bank.setResultBank(backpack.getResultBackpack());
                    openWindow("ResultOne.fxml");
                    Stage stage = (Stage) result.getScene().getWindow();
                    stage.close();
                }
            }
        });

    }

    private void configuringDirectoryChooser(DirectoryChooser directoryChooser) {
        directoryChooser.setTitle("Select Some Files");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    }

    private void openWindow(String name) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Objects.requireNonNull(Application.class.getResource(name)));
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
    }

    private void initTable() {
        tableAgents.getColumns().clear();
        TableColumn<Agent, String> tableColumnName = new TableColumn<>("Имя Агента");
        TableColumn<Agent, Double> tableColumnBudget = new TableColumn<>("Средства");
        tableColumnName.setCellValueFactory(new PropertyValueFactory("name"));
        tableColumnBudget.setCellValueFactory(new PropertyValueFactory("budget"));
        tableColumnName.prefWidthProperty().bind(tableAgents.widthProperty().multiply(0.6));
        tableColumnBudget.prefWidthProperty().bind(tableAgents.widthProperty().multiply(0.4));
        tableAgents.getColumns().addAll(tableColumnName, tableColumnBudget);
        TableColumn<Project, String> projectsName = new TableColumn<>("Имя проекта");
        TableColumn<Project, String> projectsAgent = new TableColumn<>("Имя Агента");
        TableColumn<Project, Double> projectsEx = new TableColumn<>("Затраты");
        TableColumn<Project, Double> projectsProfit = new TableColumn<>("Прибыль");
        projectsName.prefWidthProperty().bind(tableProjects.widthProperty().multiply(0.3));
        projectsAgent.prefWidthProperty().bind(tableProjects.widthProperty().multiply(0.3));
        projectsProfit.prefWidthProperty().bind(tableProjects.widthProperty().multiply(0.2));
        projectsEx.prefWidthProperty().bind(tableProjects.widthProperty().multiply(0.2));
        projectsName.setCellValueFactory(new PropertyValueFactory("name"));
        projectsEx.setCellValueFactory(new PropertyValueFactory("expenses"));
        projectsProfit.setCellValueFactory(new PropertyValueFactory("profit"));
        projectsAgent.setCellValueFactory(new PropertyValueFactory("agent"));
        tableProjects.getColumns().clear();
        tableProjects.getColumns().addAll(projectsName, projectsAgent, projectsEx, projectsProfit);
    }

    private void fillTable() {
        if (Bank.getAgents().size() > 0) {
            for (Agent agent :
                    Bank.getAgents()) {
                Bank.getAgentTableView().getItems().add(agent);
                for (Project project :
                        agent.getProjects()) {
                    Bank.getProjectTableView().getItems().add(project);
                }
            }
        }
    }

}

