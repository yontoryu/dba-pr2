package pr2mapAgent;

import jade.core.Agent;

public class Scout extends Agent {

    Environment env;
    int energy;
    int[] currentPos;

    @Override
    protected void setup() {
        energy = 0;
        env = new Environment();

        //load map
        addBehaviour(new LoadMapBehaviour(env));

        //start walking
        addBehaviour(new WalkBehaviour(this));
    }

    int getEnergy() {
        return energy;
    }

    void incrementEnergy(int energy) {
        this.energy++;
    }

    int[] getCurrentPos() {
        return currentPos;
    }

    void setCurrentPos(int[] currentPos) {
        this.currentPos = currentPos;
    }

}