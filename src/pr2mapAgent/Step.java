package pr2mapAgent;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;

public class Step extends OneShotBehaviour {

    int direction;

    public Step(int direction) {
        this.direction = direction;
    }

    @Override
    public void action() {

    }
}
