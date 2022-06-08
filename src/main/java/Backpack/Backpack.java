package Backpack;

import DataBank.Bank;
import DataBank.UnionAgents;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Backpack {
    private double tmpSum = 0;
    private double tmpExpenses;
    private double tmpProfit;
    private ArrayList<Agent> agents;


    public Backpack(ArrayList<Agent> agents) {
        this.agents = agents;
    }

    private ArrayList<String> backpack(ArrayList<Project> projects, double budget) {

        double[] tmpEE = new double[projects.size()];
        double[] tmpPP = new double[projects.size()];
        String[] names = new String[projects.size()];
        for (int i = 0; i < projects.size(); i++) {
            tmpEE[i] = projects.get(i).getExpenses();
            tmpPP[i] = projects.get(i).getProfit();
            names[i] = projects.get(i).getName();
        }
        final int size = (int) (Math.round(budget) + 1);
        double[] Sum = new double[size];
        double[] Sum_old = new double[size];
        ArrayList<String> arL = new ArrayList<>(size);
        ArrayList<String> arL_old = new ArrayList<>(size);
        for (int v = 0; v < (int) Math.round(budget) + 1; v++) {
            Sum[v] = 0;
            arL.add("");
            arL_old.add("");
        }


        for (int i = 0; i < projects.size(); i++) {
            for (int w = 0; w < (int) Math.round(budget) + 1; w++) {
                Sum_old[w] = Sum[w];
                arL_old.set(w, arL.get(w));
            }
            tmpExpenses = tmpEE[i];
            tmpProfit = tmpPP[i];

            for (double v = 1; v < budget + 1; v++) {
                if (tmpExpenses <= v) {
                    tmpSum = Sum_old[(int) ((int) v - Math.round(tmpExpenses))] + tmpProfit;
                    if (Sum_old[(int) v] < tmpSum) {
                        Sum[(int) v] = tmpSum;
                        arL.set((int) v, arL_old.get((int) ((int) v - Math.round(tmpExpenses))) + names[i] + ",");
                    } else {
                        Sum[(int) v] = Sum_old[(int) v];
                        arL.set((int) v, arL_old.get((int) v));
                    }
                } else {
                    Sum[(int) v] = Sum_old[(int) v];
                    arL.set((int) v, arL_old.get((int) v));
                }
            }
        }

        arL.add(String.valueOf(Sum[size - 1]));

        return arL;
    }

    private UnionAgents makeUnion(@NotNull ArrayList<String> arL) {
        String[] tmpProjects = arL.get(arL.size() - 2).split(",");
        double profit = Double.parseDouble(arL.get(arL.size() - 1));
        ArrayList<Agent> agents = new ArrayList<>();
        ArrayList<Project> projects = new ArrayList<Project>();
        for (String tmpProject : tmpProjects) {
            for (Agent agent : this.agents) {
                for (int k = 0; k < agent.getProjects().size(); k++) {
                    if (Objects.equals(agent.getProjects().get(k).getName(), tmpProject)) {
                        if (checkAgent(agents, agent)) {
                            agents.add(agent);
                        }
                        projects.add(agent.getProjects().get(k));
                        break;
                    }
                }
            }
        }
        return new UnionAgents(profit, agents, projects);
    }

    private boolean checkAgent(ArrayList<Agent> agents, Agent agent) {
        boolean flag = false;
        for (int i = 0; i < agents.size(); i++) {
            if (agents.get(i) == agent) {
                flag = true;
                break;
            }
        }
        return !flag;
    }

    private int[] generateCombinations(int[] arr, int M) {
        int N = this.agents.size();

        if (arr == null) {
            arr = new int[M];
            for (int i = 0; i < M; i++)
                arr[i] = i + 1;
            return arr;
        }
        for (int i = M - 1; i >= 0; i--)
            if (arr[i] < N - M + i + 1) {
                arr[i]++;
                for (int j = i; j < M - 1; j++)
                    arr[j + 1] = arr[j] + 1;
                return arr;
            }
        return null;
    }

    public Bank getResultBackpack() {
        Bank bank = new Bank();
        ArrayList<int[]> combinations = new ArrayList<>();
        int[] arr = null;
        for (int i = 1; i < agents.size() + 1; i++) {
            while ((arr = generateCombinations(arr, i)) != null)
                combinations.add(makeArray(arr));
        }
        ArrayList<Project> projects1 = new ArrayList<>();
        double budget;
        for (int[] combination : combinations) {
            projects1 = makeProjects(combination);
            budget = makeBudget(makeAgents(combination));
            bank.addUnion(makeUnion(backpack(projects1, budget)));
        }
        return bank;
    }

    private double makeBudget(ArrayList<Agent> agents1) {
        double res = 0;
        for (Agent agent : agents1) {
            res += agent.getBudget();
        }
        return res;
    }

    private ArrayList<Project> makeProjects(int[] index) {
        ArrayList<Agent> agents1 = makeAgents(index);
        ArrayList<Project> projects1 = new ArrayList<>();
        for (Agent agent : agents1) {
            projects1.addAll(agent.getProjects());
        }
        return projects1;
    }

    private ArrayList<Agent> makeAgents(int[] index) {
        ArrayList<Agent> agents1 = new ArrayList<>();
        for (int j : index) {
            agents1.add(this.agents.get(j - 1));
        }
        return agents1;
    }

    private int[] makeArray(int[] arr) {
        return Arrays.copyOf(arr, arr.length);
    }
}

