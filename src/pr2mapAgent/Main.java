package pr2mapAgent;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        String filename = "maps/mapWithVerticalWall.txt";
        Map map = new Map(filename);
        map.printMap();
        System.out.println();
        System.out.println("------------------------------------------------");

        Environment env = new Environment(map);
        env.printEnvironment();

    }
}
