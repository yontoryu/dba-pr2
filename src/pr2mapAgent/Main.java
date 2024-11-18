package pr2mapAgent;

import javax.swing.*;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        // List of the maps
        String[] mapFiles = {
                "maps/mapWithDiagonalWall.txt",
                "maps/mapWithHorizontalWall.txt",
                "maps/mapWithVerticalWall.txt",
                "maps/mapWithoutObstacle.txt",
                "maps/mapWithComplexObstacle1.txt",
                "maps/mapWithComplexObstacle2.txt",
                "maps/mapWithComplexObstacle3.txt",
                "maps/newMap.txt"
        };

        //show the list of map choices
        String selectedFile = (String) JOptionPane.showInputDialog(
                null,
                "Choose the map file:",
                "Choose map",
                JOptionPane.PLAIN_MESSAGE,
                null,
                mapFiles,
                mapFiles[0]
        );


        if (selectedFile != null && !selectedFile.isEmpty()) {
            //if a map is chosen
            Environment env = new Environment(new Map(selectedFile));

            GridLayoutManager glm = new GridLayoutManager(env);

            while (!glm.positionsSet()) {
                try {
                    Thread.sleep(1); // Wait for 1 millisecond
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Handle interrupt
                    break;
                }
            }

            System.out.println("Start [" + glm.getStartPos()[0] + ", " + glm.getStartPos()[1] + "]");
            System.out.println("Target [" + glm.getEndPos()[0] + ", " + glm.getEndPos()[1] + "]");

//            Object[] args = {""};
            Scout raccoon = new Scout(glm.getStartPos(), glm.getEndPos());

            raccoon.startAgent();


        } else {
            // if the user chancel the choice the program is finished
            System.out.println("No file has been selected.");
            System.exit(0);
        }
    }
}
