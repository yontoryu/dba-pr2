package pr2mapAgent;
import jade.core.behaviours.Behaviour;

import java.util.LinkedList;

public class WalkBehaviour extends Behaviour {

    boolean reachedTarget = false;
    Scout scout;
    int c;
    private boolean stepTurn = true; // Flag to alternate turns
    private int basePenalty = 10;
    private int maxRecentVisits = 50;
    private LinkedList<Node> lastVisitiedNodes = new LinkedList<>();
    private Environment env;
    private Node current;
    private Node target;
    private HeuristicHandler hh;


    public WalkBehaviour(Scout scout, Environment env) {
        this.scout = scout;
        this.env = env;
        this.hh = new HeuristicHandler(target, basePenalty, maxRecentVisits);

        this.current = new Node(scout.getCurrentPos()[0], scout.getCurrentPos()[1]);
        current.gCost = 0;
        current.hCost = hh.manhattan(current);

        this.target = new Node(scout.getTargetPos()[0], scout.getTargetPos()[1]);
    }

    @Override
    public void action() {
        if (!current.equals(target)) {
            scout.addBehaviour(new StepBehaviour(env));
        }
    }

    @Override
    public boolean done() {
        return current.equals(target);
    }
}
