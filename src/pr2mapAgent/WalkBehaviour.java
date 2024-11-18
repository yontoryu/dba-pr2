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
        int c = 0;
        if(!reachedTarget) {
            if (c == 10) {
                System.out.println("c " + c);
                reachedTarget = true;
            }
        }
    }

    @Override
    public boolean done() {
        return reachedTarget;
    }
}
