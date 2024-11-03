package pr2mapAgent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Environment {
    private int[][] grid; // represents the map
    private int rows;     // num of lines
    private int cols;     // num of columns

    public Environment(String mapFile) {
        loadMap(mapFile);
    }

    // loads the map from a file
    private void loadMap(String mapFile) {
        try (Scanner scanner = new Scanner(new File(mapFile))) {
            rows = Integer.parseInt(scanner.nextLine()); // reads the number of lines
            cols = Integer.parseInt(scanner.nextLine()); // reads the number of columns
            grid = new int[rows][cols];

            for (int i = 0; i < rows; i++) {
                String[] line = scanner.nextLine().split("\t");
                for (int j = 0; j < cols; j++) {
                    grid[i][j] = Integer.parseInt(line[j]); // loads the cells of the map
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int getCell(int x, int y) {
        return grid[x][y];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    // return the values of the neighbor cells
    public int[] see(int x, int y) {
        int[] view = new int[4]; // [up, down, left, right]

        view[0] = (x > 0) ? grid[x - 1][y] : -1; // up
        view[1] = (x < rows - 1) ? grid[x + 1][y] : -1; // down
        view[2] = (y > 0) ? grid[x][y - 1] : -1; // left
        view[3] = (y < cols - 1) ? grid[x][y + 1] : -1; // right

        return view;
    }
    public void displayMap() {
        System.out.println("Current Map State:");
        for (int[] row : grid) {
            for (int cell : row) {
                System.out.print(cell + "\t");
            }
            System.out.println();
        }
    }
}
