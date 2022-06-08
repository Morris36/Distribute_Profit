package App;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicReference;

import Backpack.Agent;
import Backpack.Project;
import DataBank.*;
import FileReader.ReaderTXT;
import Simplex.SimplexMethods;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import lombok.Getter;
import org.apache.commons.math.optimization.OptimizationException;

public class ControllerRes {

    @Getter
    private ArrayList<TMPSimplex> tmpSimplexes = new ArrayList<>();
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button save;

    @FXML
    private TableView<UnionString> tableResOne;

    @FXML
    private TableView<TMPSimplex> tableResTwo;
    @FXML
    private Button exit;

    @FXML
    void initialize() {
        assert save != null : "fx:id=\"save\" was not injected: check your FXML file 'Res.fxml'.";
        assert tableResOne != null : "fx:id=\"tableResOneOne\" was not injected: check your FXML file 'Res.fxml'.";
        assert tableResOne != null : "fx:id=\"tableResOneTwo\" was not injected: check your FXML file 'Res.fxml'.";
        initTable();
        addItemsTableRes();
        addItemsResTwo();
        save.setOnAction(actionEvent -> {
            AtomicReference<File> dir = new AtomicReference<>();
            Platform.runLater(() -> {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                configuringDirectoryChooser(directoryChooser);
                dir.set(directoryChooser.showDialog(new Stage()));
                System.out.println(dir.get().getAbsolutePath());
                File newFile = new File(dir.get().getAbsolutePath(), "result.txt");
                try {
                    ReaderTXT.read(makeResultString(), newFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });
        exit.setOnAction(actionEvent -> {
            Stage stage = (Stage) save.getScene().getWindow();
            stage.close();
            System.exit(0);
        });

    }

    private void configuringDirectoryChooser(DirectoryChooser directoryChooser) {
        // Set title for DirectoryChooser
        directoryChooser.setTitle("Select Some Directories");

        // Set Initial Directory
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    }

    private SimplexMethods simplex() {
        SimplexMethods simplexMethods = new SimplexMethods(Bank.getResultBank(), Bank.getAgents().size(), Bank.getAgents());
        for (ActivePOJO pojo : Bank.getActivePOJOS()) {
            simplexMethods.addActive(pojo.getActive());
            simplexMethods.addPremium(pojo.getLowBound());
            simplexMethods.addPremium(pojo.getHighBound());
        }
        return simplexMethods;
    }

    private void initTable() {
        tableResOne.getColumns().clear();
        makeTableResOne(tableResOne);
        tableResTwo.getColumns().clear();
        TableColumn<TMPSimplex, String> tableColumnName = new TableColumn<>("Агент");
        TableColumn<TMPSimplex, Double> tableColumnMoney = new TableColumn<>("Прибыль");
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnMoney.setCellValueFactory(new PropertyValueFactory<>("money"));
        tableColumnName.prefWidthProperty().bind(tableResTwo.widthProperty().multiply(0.75));
        tableColumnMoney.prefWidthProperty().bind(tableResTwo.widthProperty().multiply(0.25));
        tableResTwo.getColumns().addAll(tableColumnName, tableColumnMoney);

    }

    static void makeTableResOne(TableView<UnionString> tableResOne) {
        TableColumn<UnionString, String> tableColumnName = new TableColumn<>("Агенты");
        TableColumn<UnionString, String> tableColumnProject = new TableColumn<>("Проекты");
        TableColumn<UnionString, Double> tableColumnProfit = new TableColumn<>("Прибыль");
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("nameAgents"));
        tableColumnProject.setCellValueFactory(new PropertyValueFactory<>("nameProjects"));
        tableColumnProfit.setCellValueFactory(new PropertyValueFactory<>("profit"));
        tableColumnName.prefWidthProperty().bind(tableResOne.widthProperty().multiply(0.4));
        tableColumnProject.prefWidthProperty().bind(tableResOne.widthProperty().multiply(0.35));
        tableColumnProfit.prefWidthProperty().bind(tableResOne.widthProperty().multiply(0.25));
        tableResOne.getColumns().addAll(tableColumnName, tableColumnProject, tableColumnProfit);
    }

    private void addItemsResTwo() {
        int count = 0;
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String rs;
        double[] result = new double[0];
        try {
            result = simplex().exMethod();
        } catch (OptimizationException e) {
            throw new RuntimeException(e);
        }
        for (Agent agent :
                Bank.getAgents()) {
            rs = decimalFormat.format(result[count]);
            tmpSimplexes.add(new TMPSimplex(agent.getName(), rs));
            tableResTwo.getItems().add(tmpSimplexes.get(count));
            count++;
        }

    }

    private void addItemsTableRes() {
        String tmpA = "";
        String tmpP = "";
        for (UnionAgents unionAgents : Bank.getResultBank().getUnions()) {
            tmpA = makeStringAgent(unionAgents);
            tmpP = makeStringProject(unionAgents);
            tableResOne.getItems().add(new UnionString(tmpA.substring(0, tmpA.length() - 2), tmpP.substring(0, tmpP.length() - 2), unionAgents.getProfit()));
        }


    }

    private String makeStringAgent(UnionAgents unionAgents) {
        StringBuilder res = new StringBuilder();
        for (Agent agent :
                unionAgents.getAgents()) {
            res.append(agent.getName()).append(",").append(" ");
        }
        return res.toString();
    }

    private String makeStringProject(UnionAgents unionAgents) {
        StringBuilder res = new StringBuilder();
        for (Project project :
                unionAgents.getProjects()) {
            res.append(project.getName()).append(",").append(" ");
        }
        return res.toString();
    }

    private String makeResultString() {
        String sp = System.getProperty("line.separator");
        StringBuilder text = new StringBuilder("Наименование агента - Средства - Показатель активности:" + sp);
        int count = 0;
        for(Agent agent : Bank.getAgents()) {
            text.append(agent.getName()).append(" - ").append(agent.getBudget()).append(" - ")
                    .append(Bank.getActivePOJOS().get(count).getActive()).append(sp);
            count++;
        }
        text.append("Наименование проекта - Агент - Затраты - Прибыль:").append(sp);
        for (Agent agent : Bank.getAgents())
            for (Project pr:
                    agent.getProjects()) {
                text.append(pr.getName()).append(" - ").append(pr.getAgent()).append(" - ").append(pr.getExpenses()).append(" - ").
                        append(pr.getProfit()).append(sp);
            }
        text.append("Результат:").append(sp);
        text.append("Имя агента - Оптимальная прибыль:").append(sp);
        for (TMPSimplex tmp:
             tmpSimplexes) {
            text.append(tmp.getName()).append(" - ").append(tmp.getMoney()).append(sp);
        }
        return text.toString();
    }
}

