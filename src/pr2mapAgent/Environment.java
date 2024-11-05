package pr2mapAgent;

import java.util.Random;

public class Environment {

    Map map;
    int[][] path;
    int[] startPos;
    int[] targetPos;
    int[] currentPos;
    int height;
    int width;

    public Environment() {
        startPos = new int[]{0, 0};
        targetPos = new int[]{0, 0};
    }

    public void setup(Map map) {
        loadMap(map);
        setStartAndTarget();
        currentPos = startPos.clone();
    }

    private void loadMap(Map map) {
        this.map = map;
        this.height = map.getHeight();
        this.width = map.getWidth();
    }

    private void setStartAndTarget() {
        Random rand = new Random();
        do {
            startPos[0] = rand.nextInt(height);
            startPos[1] = rand.nextInt(width);
        }
        while (map.getMatrix()[startPos[0]][startPos[1]] == -1);

        do {
            targetPos[0] = rand.nextInt(height);
            targetPos[1] = rand.nextInt(width);
        }
        while (map.getMatrix()[targetPos[0]][targetPos[1]] == -1);
    }

    void printEnvironment() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
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

}
