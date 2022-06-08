package Simplex;

import Backpack.Agent;
import DataBank.Bank;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.linear.LinearConstraint;
import org.apache.commons.math.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math.optimization.linear.Relationship;
import org.apache.commons.math.optimization.linear.SimplexSolver;

import java.util.ArrayList;
import java.util.Collection;

public class SimplexMethods {
    private final Collection<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
    private LinearObjectiveFunction function;
    private final ArrayList<Double> premium = new ArrayList<>();
    private final ArrayList<Double> activeStatus = new ArrayList<>();
    private final ArrayList<Agent> agents;
    private final Bank bank;
    private final int countAgent;

    public SimplexMethods(Bank bank, int countAgent, ArrayList<Agent> agents1) {
        this.bank = bank;
        this.countAgent = countAgent;
        agents = agents1;
    }

    public void addPremium(double premium) {
        this.premium.add(premium);
    }

    public void addActive(double active) {
        this.activeStatus.add(active);
    }

    public ArrayList<Double> getPremium() {
        return premium;
    }

    public ArrayList<Double> getActiveStatus() {
        return activeStatus;
    }

    public double[] exMethod() throws OptimizationException {
        double[] tmp = new double[countAgent*2];
        for (int i = 0; i < bank.getUnions().size(); i++) {
            if (bank.getUnions().get(i).getAgents().size() == 1) {
                tmp[i] = -1;
                tmp[i + countAgent] = 1;
                constraints.add(new LinearConstraint(tmp, Relationship.GEQ, bank.getUnions().get(i).getProfit()));
            } else {
                if (bank.getUnions().get(i).getAgents().size() == countAgent) {
                    for (int j = countAgent; j < countAgent * 2; j++) {
                        tmp[j] = 1;
                    }
                    constraints.add(new LinearConstraint(tmp, Relationship.EQ, bank.getUnions().get(i).getProfit()));
                } else {
                    for (int j = 0; j < bank.getUnions().get(i).getAgents().size(); j++) {
                        tmp[countAgent + countIndexAgent(bank.getUnions().get(i).getAgents().get(j))] = 1;
                    }
                    constraints.add(new LinearConstraint(tmp, Relationship.GEQ, bank.getUnions().get(i).getProfit()));
                }
            }
            tmp = new double[countAgent*2];
        }
        tmp = new double[countAgent * 2];
        int n = 0;
        for (int i = 0; i < countAgent; i++) {
            tmp[i] = 1;
            constraints.add(new LinearConstraint(tmp, Relationship.GEQ, premium.get(n)));
            constraints.add(new LinearConstraint(tmp, Relationship.LEQ, premium.get(n + 1)));
            n += 2;
            tmp = new double[countAgent * 2];
        }

        makeFunction();
        return makeResult(countAgent);
    }

    public Collection<LinearConstraint> getConstraints() {
        return constraints;
    }

    private int countIndexAgent(Agent agent) {
        for (int i = 0; i < agents.size(); i++) {
            if (agents.get(i) == agent)
                return i;
        }
        return 0;
    }

    private double[] makeResult(int countAgent) throws OptimizationException {
        RealPointValuePair solution = new SimplexSolver().optimize(function, constraints, GoalType.MAXIMIZE, false);
        double[] result = new double[countAgent];
        int count = 0;
        for (int i = countAgent; i < countAgent * 2; i++) {
            result[count] = solution.getPoint()[i];
            count++;
        }
        return result;
    }

    private void makeFunction() {
        double[] func = new double[countAgent * 2];
        int n = 0;
        for (int i = 0; i < countAgent; i++) {
            func[i] = activeStatus.get(i) * (premium.get(n + 1) - premium.get(n));
            n += 2;
        }
        n = 0;
        for (int i = countAgent; i < countAgent * 2; i++) {
            func[i] = 0;
        }
        double agc = 0;
        for (int i = 0; i < countAgent; i++) {
            agc += func[i] * premium.get(n) * -1;
        }
        function = new LinearObjectiveFunction(func, agc);
    }
}
