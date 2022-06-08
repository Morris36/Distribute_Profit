package DataBank;


import Backpack.Agent;
import Backpack.Project;
import javafx.scene.control.TableView;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedList;

public class Bank {
    @Getter
    @Setter
    private static String patch = "None Patch";
    @Getter
    @Setter
    private static TableView<Agent> agentTableView;
    @Getter
    @Setter
    private static TableView<Project> projectTableView;
    @Getter
    @Setter
    private static ArrayList<Agent> agents = new ArrayList<>();
    private final ArrayList<UnionAgents> unions = new ArrayList<>();
    @Setter
    @Getter
    private static Bank resultBank;
    @Getter
    @Setter
    private static ArrayList<ActivePOJO> activePOJOS;
    public void addUnion(UnionAgents unionAgents) {
        unions.add(unionAgents);
    }

    public void removeUnion(UnionAgents unionAgents) {
        unions.remove(unionAgents);
    }

    public ArrayList<UnionAgents> getUnions() {
        return unions;
    }

}
