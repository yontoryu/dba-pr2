package pr2mapAgent;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.core.Agent;

import java.util.ArrayList;
import java.util.List;

public class Scout extends Agent {

    Environment env;
    private WalkBehaviour walkBehaviour;
    int energy;
    int[] currentPos;
    int[] targetPos;

    public Scout() {
        super();
    }

    @Override
    protected void setup() {
        // Retrieve startup arguments
        Object[] args = getArguments();

        if (args != null && args.length > 0) {
            currentPos = ((int[]) args[0]).clone();
            targetPos = ((int[]) args[1]).clone();
            env = (Environment) args[2];
            System.out.println("Agent started with argument: Start - " + currentPos[0] + ", " + currentPos[1] + ", End - " + targetPos[0] + ", " + targetPos[1]);
        } else {
            System.out.println("No arguments provided.");
        }
        walkBehaviour = new WalkBehaviour(this, env);
        //start walking
        addBehaviour(new WalkBehaviour(this, env));
    }
    boolean isDone() {
        if (walkBehaviour != null) {
            return walkBehaviour.done();
        }
        return false;
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

    int[] getTargetPos() {
        return targetPos;
    }

    void startAgent(Object[] args) {
        // Step 1: Get the JADE runtime instance
        Runtime jadeRuntime = Runtime.instance();

        // Step 2: Create the main container (platform)
        Profile profile = new ProfileImpl();
//        profile.setParameter(Profile.GUI, "true"); // Enable the JADE GUI

        ContainerController mainContainer = jadeRuntime.createMainContainer(profile);

        try {
            // Step 3: Start your agent(s)
            String agentName = "raccoonie"; // Name of your agent
            String agentClass = "pr2mapAgent.Scout"; // Fully qualified name of your agent class

            AgentController agent = mainContainer.createNewAgent(agentName, agentClass, args);
            agent.start(); // Start the agent
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private List<int[]> visitedPath = new ArrayList<>();

    void setVisitedPath(List<int[]> path) {
        this.visitedPath = path;
    }

    public List<int[]> getVisitedPath() {
        return visitedPath;
    }


}