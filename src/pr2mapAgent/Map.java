package pr2mapAgent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Map {
    int height;
    int width;
    File file;
    int[][] map;

    Map (String filename) {
        file = new File(filename);
        try {
            setMap();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void setMap() throws FileNotFoundException {
        Scanner scan = new Scanner(file);

        height = scan.nextInt();
        width = scan.nextInt();

        map = new int[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                map[i][j] = scan.nextInt();
            }
        }
    }

    public int[][] getMap() {
        return map;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void printMap() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (map[i][j] == 0) {
                    System.out.print(" ");
                }
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }
}
