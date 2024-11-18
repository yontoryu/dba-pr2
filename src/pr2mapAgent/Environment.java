package pr2mapAgent;

import java.util.Random;

public class Environment {

    Map map;
    int[][] path;
    int[] startPos;
    int[] targetPos;

    public Environment(Map map) {
        startPos = new int[]{0, 0};
        targetPos = new int[]{0, 0};
        loadMap(map);
        setStartAndTargetRandom();
    }

    private void loadMap(Map map) {
        this.map = map;
    }

    private void setStartAndTargetRandom() {
        Random rand = new Random();
        do {
            startPos[0] = rand.nextInt(map.getHeight());
            startPos[1] = rand.nextInt(map.getWidth());
        }
        while (map.getMatrix()[startPos[0]][startPos[1]] == -1);

        do {
            targetPos[0] = rand.nextInt(map.getHeight());
            targetPos[1] = rand.nextInt(map.getWidth());
        }
        while (map.getMatrix()[targetPos[0]][targetPos[1]] == -1);
    }

    public void setStartAndTarget(int[] start, int[] target) {
        startPos[0] = start[0];
        startPos[1] = start[1];
        targetPos[0] = target[0];
        targetPos[1] = target[1];
    }

    Map getMap() {
        return map;
    }

    void printEnvironment() {
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                if (i == startPos[0] && j == startPos[1]) {
                    System.out.print("[S]");
                    continue;
                }
                if (i == targetPos[0] && j == targetPos[1]) {
                    System.out.print("[T]");
                    continue;
                }
                if (map.getMatrix()[i][j] == 0) {
                    System.out.print(" ");
                }
                System.out.print(map.getMatrix()[i][j] + " ");
            }
            System.out.println();
        }
    }

    int[] getTargetPos() {
        return targetPos;
    }

    int[] getStartPos() {
        return startPos;
    }

}
