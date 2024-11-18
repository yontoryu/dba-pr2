package pr2mapAgent;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.core.Agent;

public class Scout extends Agent {

    Environment env;
    int energy;
    int[] currentPos;
    int[] targetPos;

    public Scout(int[] start, int[] target) {
        currentPos = start.clone();
        targetPos = target.clone();
        energy = 0;
    }

    @Override
    protected void setup() {
        // Retrieve startup arguments
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            System.out.println("Agent started with argument: " + args[0]);
        } else {
            System.out.println("No arguments provided.");
        }

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

    void startAgent() {
        // Step 1: Get the JADE runtime instance
        Runtime jadeRuntime = Runtime.instance();

        // Step 2: Create the main container (platform)
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.GUI, "true"); // Enable the JADE GUI

        ContainerController mainContainer = jadeRuntime.createMainContainer(profile);

        try {
            // Step 3: Start your agent(s)
            String agentName = "badi"; // Name of your agent
            String agentClass = "pr2mapAgent.Scout"; // Fully qualified name of your agent class

            AgentController agent = mainContainer.createNewAgent(agentName, agentClass, null);
            agent.start(); // Start the agent
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}