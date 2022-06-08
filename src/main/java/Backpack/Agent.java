package Backpack;


import java.util.ArrayList;

public class Agent {
    private final ArrayList<Project> projects = new ArrayList<Project>();
    public final String name;
    public final double budget;

    public Agent(String name, double budget) {
        this.name = name;
        this.budget = budget;
    }

    public ArrayList<Project> getProjects() {
        return projects;
    }

    public String getName() {
        return name;
    }

    public double getBudget() {
        return budget;
    }

    public void addProject(Project project) {
        boolean flag = false;
        for (Project value : projects) {
            flag = value.equals(project);
        }
        if (!flag)
            projects.add(project);
    }

    @Override
    public String toString() {
        return "Agent{" +
                "projects=" + projects +
                ", name='" + name + '\'' +
                ", budget=" + budget +
                '}';
    }
}
