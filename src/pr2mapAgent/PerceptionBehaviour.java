package pr2mapAgent;

import jade.core.behaviours.OneShotBehaviour;

public class PerceptionBehaviour extends OneShotBehaviour {
    private SAgent agent;

    public PerceptionBehaviour(SAgent agent) {
        this.agent = agent;
    }

    @Override
    public void action() {
        int[] position = agent.getPosition();
        int[] view = agent.getEnvironment().see(position[0], position[1]);
        System.out.println("Current View: " + view[0] + ", " + view[1] + ", " + view[2] + ", " + view[3]);
    }
}

