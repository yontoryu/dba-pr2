package pr2mapAgent;

import jade.core.behaviours.OneShotBehaviour;

import java.util.ArrayList;
import java.util.List;

public class StepBehaviour extends OneShotBehaviour {

    private WalkBehaviour aStarData;

    public StepBehaviour(WalkBehaviour wb) {
        this.aStarData = wb;
    }

    @Override
    public void action() {
        discoverArea(aStarData.current);

//        printHeuristic(aStarData.current);
        printKnownGrid(aStarData.current);
        System.out.println("------------------------------------------");

        // Get the best neighbor for the next move
        Node nextMove = getBestStep(aStarData.current);

        Node last = aStarData.current;
        aStarData.current = nextMove;
        aStarData.hh.penalizeDynamically(aStarData.current, last, aStarData.lastVisitiedNodes);
        updateRecentVisits(last);
        aStarData.notifyStepComplete();
    }

    //adds currentNode as well as all of his Neighbors to the graph and sets the Neighbors of currentNode
    private void discoverArea(Node currentNode) {
        aStarData.env.setNode(currentNode.x, currentNode.y, currentNode);

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] dir : directions) {
            int nx = currentNode.x + dir[0];
            int ny = currentNode.y + dir[1];

            if (aStarData.env.isInBounds(nx, ny)) {
                Node neighborNode = null;
                if (!aStarData.env.yetDiscovered(nx, ny)) {
                    boolean isObstacle = aStarData.env.see(aStarData.current, nx, ny) == -1;
                    neighborNode = new Node(nx, ny, isObstacle);
                    aStarData.env.setNode(nx, ny, neighborNode);
                }
                neighborNode = aStarData.env.getNode(nx, ny);
                neighborNode.setHCost(aStarData.hh.manhattan(neighborNode));
                neighborNode.setGCost(1);

                currentNode.addNeighbor(neighborNode);
                aStarData.env.setNode(nx, ny, neighborNode);
            }
        }
    }

    private Node getBestStep(Node current) {
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
                bestNeighbor = aStarData.hh.euclidean(neighbor) < aStarData.hh.euclidean(bestNeighbor) ? neighbor : bestNeighbor;
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

    private void updateRecentVisits(Node current) {
        aStarData.lastVisitiedNodes.addFirst(current); // Add the current node to recent visits
        if (aStarData.lastVisitiedNodes.size() > aStarData.hh.getMaxRecentVisits()) {
            aStarData.lastVisitiedNodes.removeLast(); // Remove the oldest node if we exceed the limit
        }
    }


    private void printKnownGrid(Node currentNode) {
        for (int ny = 0; ny < aStarData.env.getHeight(); ny++) {
            for (int nx = 0; nx < aStarData.env.getWidth(); nx++) {
                if (ny == currentNode.y && nx == currentNode.x) {
                    System.out.print("A  ");
                }
                else if (ny == aStarData.target.y && nx == aStarData.target.x) {
                    System.out.print("T  ");
                }
                else if (!aStarData.env.yetDiscovered(nx, ny) || !aStarData.env.getNode(nx, ny).isObstacle) {
                    System.out.print(".  ");
                } else if (aStarData.env.yetDiscovered(nx, ny)) {
                    System.out.print("#  ");
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private void printHeuristic(Node currentNode) {
        for (int ny = 0; ny < aStarData.env.getHeight(); ny++) {
            for (int nx = 0; nx < aStarData.env.getWidth(); nx++) {
                if (ny == aStarData.target.y && nx == aStarData.target.x) {
                    System.out.print("T    ");
                }
                else if (ny == currentNode.y && nx == currentNode.x) {
                    System.out.printf("%c%-2d%c ", '[', currentNode.fCost(), ']');
                }
                else if (!aStarData.env.yetDiscovered(nx, ny)) {
                    System.out.print(".    ");
                }
                else if (aStarData.env.getNode(nx, ny).isObstacle) {
                    System.out.print("#    ");
                }
                else {
                    System.out.printf("%-5d", aStarData.env.getNode(nx, ny).fCost());
                }
            }
            System.out.println();
        }
        System.out.println();
    }


}
