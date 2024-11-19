package pr2mapAgent;

import java.util.Random;

public class Environment {

    int[][] path;
    int width;
    int height;
    Node[][] exploredArea;

    public Environment(int width, int height) {
        this.width = width;
        this.height = height;
        exploredArea = new Node[height][width];
    }

    public void setNode(int x, int y, Node n) {
        exploredArea[x][y] = n;
    }

    public Node getNode(int x, int y) {
        return exploredArea[x][y];
    }

    public boolean isInBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public boolean yetDiscovered(int x, int y) {
        return exploredArea[x][y] != null;
    }

    public
}
