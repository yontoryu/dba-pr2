package astar2;
import java.util.*;
public class Astar2 {
    static int[][] grid = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, -1},
            {0, 0, 0, 0, 0, 0, 0, 0, -1, 0},
            {0, 0, 0, 0, 0, 0, 0, -1, 0, 0},
            {0, 0, 0, 0, 0, 0, -1, 0, 0, 0},
            {0, 0, 0, 0, 0, -1, 0, 0, 0, 0},
            {0, 0, 0, 0, -1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };

    static Node start = new Node(0, 0);
    static Node goal = new Node(9, 9);

    static int width = grid[0].length;
    static int height = grid.length;
    static int basePenalty = 10;
    static int maxRecentVisits = 50;
    static LinkedList<Node> lastVisitiedNodes = new LinkedList<>();

    static Node[][] graph = new Node[height][width];

    public static void main(String[] args) {
        moveAgentStepByStep();
    }

    public static void moveAgentStepByStep() {
        Node current = start;
        current.gCost = 0;
        current.hCost = heuristicManhattan(current);

        printHeuristic(current);
//        printKnownGrid(current);
        System.out.println("------------------------------------------");

        while (!current.equals(goal)) {
            discoverArea(current);

//            printHeuristic(current);
            printKnownGrid(current);
            System.out.println("------------------------------------------");

            // Get the best neighbor for the next move
            Node nextMove = getBestStep(current);

            Node last = current;
            current = nextMove;
            penalizeDynamically(current, last);
            updateRecentVisits(last);
        }

        System.out.println("Goal reached!");
        printKnownGrid(current);
    }

    //adds currentNode as well as all of his Neighbors to the graph and sets the Neighbors of currentNode
    private static void discoverArea(Node currentNode) {
        graph[currentNode.y][currentNode.x] = currentNode;

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] dir : directions) {
            int nx = currentNode.x + dir[0];
            int ny = currentNode.y + dir[1];

            if (isInBounds(nx, ny)) {
                Node neighborNode = null;
                if (graph[ny][nx] == null) {
                    boolean isObstacle = grid[ny][nx] == -1;
                    neighborNode = new Node(nx, ny, isObstacle);
                    graph[ny][nx] = neighborNode;
                }
                neighborNode = graph[ny][nx];
                neighborNode.hCost = heuristicManhattan(neighborNode);
                neighborNode.gCost = 1;

                currentNode.addNeighbor(neighborNode);
                graph[ny][nx] = neighborNode;
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

    private static boolean isInBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    private static int heuristicManhattan(Node a) {
        return Math.abs(a.x - goal.x) + Math.abs(a.y - goal.y);
    }

    private static int heuristicEuclidean(Node a) {
        return (int) (Math.pow(Math.abs(a.x - goal.x), 2) + Math.pow(Math.abs(a.y - goal.y), 2));
    }

    private static void penalizeDynamically(Node current, Node last) {
        int penalty = basePenalty;
        int position = 0;

        // Calculate penalty based on the position in recentVisits
        for (Node recentNode : lastVisitiedNodes) {
            if (recentNode.equals(current)) {
                penalty = basePenalty * (maxRecentVisits - position); // Dynamic penalty
                break;
            }
            position++;
        }
        last.penalize(penalty);
    }

    private static void updateRecentVisits(Node current) {
        lastVisitiedNodes.addFirst(current); // Add the current node to recent visits
        if (lastVisitiedNodes.size() > maxRecentVisits) {
            lastVisitiedNodes.removeLast(); // Remove the oldest node if we exceed the limit
        }
    }

    private static void printKnownGrid(Node currentNode) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == currentNode.y && j == currentNode.x) {
                    System.out.print("A  ");
                }
                else if (i == goal.y && j == goal.x) {
                    System.out.print("T  ");
                }
                else if (graph[i][j] == null || !graph[i][j].isObstacle) {
                    System.out.print(".  ");
                } else if (graph[i][j] != null) {
                    System.out.print("#  ");
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void printHeuristic(Node currentNode) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == goal.y && j == goal.x) {
                    System.out.print("T    ");
                }
                else if (i == currentNode.y && j == currentNode.x) {
                    System.out.printf("%c%-2d%c ", '[', currentNode.fCost(), ']');
                }
                else if (graph[i][j] == null) {
                    System.out.print(".    ");
                }
                else if (graph[i][j].isObstacle) {
                    System.out.print("#    ");
                }
                else {
                    System.out.printf("%-5d", graph[i][j].fCost());
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}