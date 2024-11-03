package pr2mapAgent;

import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;

import java.util.ArrayList;
import java.util.List;

public class SAgent extends Agent {
    private Environment environment;
    private int[] position; // agents current position
    private int[] goal; // target's position
    private int energyConsumed;
    private List<int[]> path; //  agent's path

    @Override
    protected void setup() {
        // initialize the environment ad the positions
        String mapFile = "maps/mapWithVerticalWall.txt";
        this.environment = new Environment(mapFile);
        this.position = new int[] {0, 0}; // initial position
        this.goal = new int[] {4, 4}; // target's position
        this.energyConsumed = 0;
        this.path = new ArrayList<>();

        SequentialBehaviour seq = new SequentialBehaviour();
        seq.addSubBehaviour(new PerceptionBehaviour(this));
        seq.addSubBehaviour(new DecisionMakingBehaviour(this));
       // seq.addSubBehaviour(new MovementBehaviour(this));
        addBehaviour(seq);
    }

    public int getEnergyConsumed() {
        return energyConsumed;
    }

    public void incrementEnergy() {
        energyConsumed++;
    }

    public int[] getPosition() {
        return position;
    }

    public void setPosition(int x, int y) {
        position[0] = x;
        position[1] = y;
        path.add(new int[]{x, y}); // we add the current position to the path
    }

    public Environment getEnvironment() {
        return environment;
    }

    public int[] getGoal() {
        return goal;
    }

    public List<int[]> getPath() {
        return path; // we return the agent's path
    }
}
