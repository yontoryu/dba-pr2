package pr2mapAgent;

import jade.core.behaviours.OneShotBehaviour;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoadMapBehavior extends OneShotBehaviour {

    Environment env;

    public LoadMapBehavior(Environment env) {
        this.env = env;
    }

    @Override
    public void action() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Entra el mapa:\t");
        String filename;
        try {
            filename = br.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        env.setup(new Map(filename));
        env.printEnvironment();
    }
}
