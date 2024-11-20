package pr2mapAgent;
import jade.core.behaviours.Behaviour;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class WalkBehaviour extends Behaviour {

    Scout scout;
    private boolean waitingForStep = false; // Flag to alternate turns
    int basePenalty = 10;
    int maxRecentVisits = 50;
    LinkedList<Node> lastVisitiedNodes;
    Environment env;
    Node current;
    Node target;
    HeuristicHandler hh;


    public WalkBehaviour(Scout scout, Environment env) {
        this.scout = scout;
        this.env = env;

        this.target = new Node(scout.getTargetPos()[0], scout.getTargetPos()[1]);
        this.hh = new HeuristicHandler(target, basePenalty, maxRecentVisits);

        this.current = new Node(scout.getCurrentPos()[0], scout.getCurrentPos()[1]);
        current.setGCost(0);
        current.setHCost(hh.manhattan(current));

        lastVisitiedNodes = new LinkedList<>();

    }
    public List<int[]> getVisitedPath() {
        List<int[]> visitedPath = new ArrayList<>();
        for (Node node : lastVisitiedNodes) {
            visitedPath.add(new int[]{node.x, node.y});
        }
        return visitedPath;
    }


    @Override
    public void action() {
        if (!current.equals(target) && !waitingForStep) {
            scout.addBehaviour(new StepBehaviour(this/*, current, target, lastVisitiedNodes, env, hh*/));
            waitingForStep = true;
        }
    }

    @Override
    public boolean done() {
        return current.equals(target);
    }

    public void notifyStepComplete() {
        this.waitingForStep = false;
    }
}
