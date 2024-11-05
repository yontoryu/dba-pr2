package pr2mapAgent;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class Main1 {
    public static void main(String[] args) {
        // we start JADE runtime
        Runtime runtime = Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN, "true");
        ContainerController container = runtime.createAgentContainer(profile);

        // we create the agent
        try {

            container.createNewAgent("SAgent", "pr2mapAgent.SAgent", null).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
