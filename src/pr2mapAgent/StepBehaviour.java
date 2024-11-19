package pr2mapAgent;

import jade.core.behaviours.OneShotBehaviour;

import java.util.ArrayList;
import java.util.List;

public class StepBehaviour extends OneShotBehaviour {

    private Environment env;

    public StepBehaviour(Environment env) {
        this.env = env;
    }

    @Override
    public void action() {
        System.out.println("STEP");
    }

    //adds currentNode as well as all of his Neighbors to the graph and sets the Neighbors of currentNode
    private void discoverArea(Node currentNode) {
        env.setNode(currentNode.x, currentNode.y, currentNode);

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] dir : directions) {
            int nx = currentNode.x + dir[0];
            int ny = currentNode.y + dir[1];

            if (env.isInBounds(nx, ny)) {
                Node neighborNode = null;
                if (!env.yetDiscovered(nx, ny)) {
                    boolean isObstacle = grid[ny][nx] == -1;
                    neighborNode = new Node(nx, ny, isObstacle);
                    exploredArea[ny][nx] = neighborNode;
                }
                neighborNode = exploredArea[ny][nx];
                neighborNode.hCost = heuristicManhattan(neighborNode);
                neighborNode.gCost = 1;

                currentNode.addNeighbor(neighborNode);
                exploredArea[ny][nx] = neighborNode;
            }
        }
    }

    private static Node getBestStep(Node current) {
        List<Node> validNeighbors = getValidNeighbors(current);

        if (validNeighbors.isEmpty()) {
            return null;
        }

        Node bestNeighbor = null;
        int lowestFCost = Integer.MAX_VALUE;

        //prioritize non-visited neighbors
        for (Node neighbor : validNeighbors) {
            int fCost = neighbor.fCost(); //if fCost of current neighbor is lower than lowest known fCost
            if (fCost < lowestFCost) {
                lowestFCost = fCost;
                bestNeighbor = neighbor;
            }
            else if(fCost == lowestFCost) {
                bestNeighbor = heuristicEuclidean(neighbor) < heuristicEuclidean(bestNeighbor) ? neighbor : bestNeighbor;
            }
        }

        return bestNeighbor;
    }

    private static List<Node> getValidNeighbors(Node currentNode) {
        List<Node> validNeighbors = new ArrayList<>();
        List<Node> allNeighbors = currentNode.getNeighbors();

        for(Node neighbor : allNeighbors) {
            if (!neighbor.isObstacle) {
                validNeighbors.add(neighbor);
            }
        }

        return validNeighbors;
    }

}
