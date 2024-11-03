package pr2mapAgent;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;

public class Main1 {
    public static void main(String[] args) {
        // we start JADE runtime
        Runtime runtime = Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN, "true");
        jade.wrapper.AgentContainer container = runtime.createAgentContainer(profile);

        // we create the agent
        try {
            SAgent agent = new SAgent();
            container.createNewAgent("SAgent", "pr2mapAgent.SAgent", null).start();

            // later we can print the path
            agent.getEnvironment().displayMap();
            System.out.println("Path taken by the agent:");
            for (int[] pos : agent.getPath()) {
                System.out.println("Position: (" + pos[0] + ", " + pos[1] + ")");
            }
            System.out.println("Total Energy Consumed: " + agent.getEnergyConsumed());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
