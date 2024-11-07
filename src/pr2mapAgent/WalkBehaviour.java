package pr2mapAgent;

import jade.core.behaviours.Behaviour;

public class WalkBehaviour extends Behaviour {

    boolean reachedTarget = false;
    Scout scout;

    public WalkBehaviour(Scout scout) {
        this.scout = scout;
    }

    @Override
    public void action() {
        if(!reachedTarget) {

        }
    }

    @Override
    public boolean done() {
        return reachedTarget;
    }
}
