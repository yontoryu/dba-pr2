package pr2mapAgent;

import jade.core.behaviours.OneShotBehaviour;

public class DecisionMakingBehaviour extends OneShotBehaviour {
    private SAgent agent;

    public DecisionMakingBehaviour(SAgent agent) {
        this.agent = agent;
    }

    @Override
    public void action() {
        int[] position = agent.getPosition();
        int[] goal = agent.getGoal();

        // just an example of the movement that the agent should make to the target
        if (position[0] < goal[0] && agent.getEnvironment().getCell(position[0] + 1, position[1]) == 0) {
            agent.setPosition(position[0] + 1, position[1]); // Move down
            agent.incrementEnergy();
        } else if (position[1] < goal[1] && agent.getEnvironment().getCell(position[0], position[1] + 1) == 0) {
            agent.setPosition(position[0], position[1] + 1); // Move right
            agent.incrementEnergy();
        } else if (position[0] > goal[0] && agent.getEnvironment().getCell(position[0] - 1, position[1]) == 0) {
            agent.setPosition(position[0] - 1, position[1]); // Move up
            agent.incrementEnergy();
        } else if (position[1] > goal[1] && agent.getEnvironment().getCell(position[0], position[1] - 1) == 0) {
            agent.setPosition(position[0], position[1] - 1); // Move left
            agent.incrementEnergy();
        } else {
            System.out.println("Cannot move towards goal, obstacle encountered.");
        }
    }
}
