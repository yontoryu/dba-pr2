package pr2mapAgent;

import java.util.Random;

public class Environment {

    Map map;
    int[][] path;
    int[] startPos;
    int[] targetPos;
    int[] currentPos;

    public Environment(Map map) {
        this.map = map;
        startPos = new int[2];
        targetPos = new int[2];
    }

    void setStartAndTarget() {
        Random rand = new Random();
//        while ()
        startPos[0] = rand.nextInt(map.getWidth());
        startPos[1] = rand.nextInt(map.getHeight());


    }

}
