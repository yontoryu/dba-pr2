package pr2mapAgent;

import jade.core.behaviours.Behaviour;

public class MovementBehaviour extends Behaviour {
    private SAgent agent;

    public MovementBehaviour(SAgent agent) {
        this.agent = agent;
    }

    @Override
    public void action() {
        int[] currentPosition = agent.getPosition();
        int[] goal = agent.getGoal();
        Environment environment = agent.getEnvironment();


        int[] neighbors = environment.see(currentPosition[0], currentPosition[1]);


        int nextX = currentPosition[0];
        int nextY = currentPosition[1];

        //neighbors[0] = cell above agent
        //neighbors[1] = cell below agent
        //neighbors[2] = cell to the left of the agent
        //neighbors[3] = cell to the right of the agent



        // choose the move that will reduce the distance from the target
        if (currentPosition[0] < goal[0] && neighbors[1] == 0) {
            nextX++; // down
        } else if (currentPosition[0] > goal[0] && neighbors[0] == 0) {
            nextX--; // up
        } else if (currentPosition[1] < goal[1] && neighbors[3] == 0) {
            nextY++; // right
        } else if (currentPosition[1] > goal[1] && neighbors[2] == 0) {
            nextY--; // left
        } else {
            // all surrounding cells are either obstacles or off-goal
            System.out.println("Agent is blocked by obstacles.");
        }

        // if he can move he updates the position and the energy
        if (nextX != currentPosition[0] || nextY != currentPosition[1]) {
            agent.setPosition(nextX, nextY);
            agent.incrementEnergy();
            System.out.println("Agent moved to (" + nextX + ", " + nextY + ")");
        }
    }

    @Override
    public boolean done() {
        // checking if he is in the target
        return agent.getPosition()[0] == agent.getGoal()[0] && agent.getPosition()[1] == agent.getGoal()[1];
    }

    @Override
    public int onEnd() {
        System.out.println("Agent reached the goal!");
        System.out.println("Path taken: " + agent.getPath());
        System.out.println("Energy consumed: " + agent.getEnergyConsumed());
        return super.onEnd();
    }
}
