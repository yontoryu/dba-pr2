package pr2mapAgent;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

public class Scout extends Agent {

    Environment env;
    int energy;

    @Override
    protected void setup() {
        energy = 0;

        env = new Environment();
        addBehaviour(new LoadMapBehavior(env));
    }

}

