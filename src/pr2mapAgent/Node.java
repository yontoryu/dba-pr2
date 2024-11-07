package pr2mapAgent;

import java.util.Objects;

public class Node {
    int x, y;           // Node positions on the map
    int gCost, hCost;   // Movement cost and heuristic cost
    Node parent;        // Parent node to reconstruct the final path

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.gCost = Integer.MAX_VALUE; // Initializes with a high value
        this.hCost = 0;
        this.parent = null;
    }

    // Calculates the total cost for A*
    public int fCost() {
        return gCost + hCost;
    }

    // Check if two nodes are the same (same position)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node node = (Node) obj;
        return x == node.x && y == node.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}