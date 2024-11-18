package pr2mapAgent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.border.LineBorder;
import java.util.Scanner;

public class GridLayoutManager extends JFrame {

    private JPanel gridPanel;
    private CellPanel[][] squares;
    private int[][] matrix;
    private int height = 10;
    private int width = 10;

    private BufferedImage grassImage;
    private BufferedImage raccoonImage;
    private BufferedImage obstacleImage; // Image of obstacles (walls)
    private BufferedImage endImage; // Image of the target

    private boolean startSet = false;
    private boolean endSet = false;
    private int[] startPos = new int[2];
    private int[] endPos = new int[2];  // Target position

    public GridLayoutManager(String filename) {
        super("GUI GridLayout Manager");

        // Load images for cells
        try {
            grassImage = ImageIO.read(new File("grass.png"));
            raccoonImage = ImageIO.read(new File("raccoon2.png"));
            obstacleImage = ImageIO.read(new File("wall.png"));
            endImage = ImageIO.read(new File("stink.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        gridPanel = new JPanel(new GridLayout(height, width));
        squares = new CellPanel[height][width];
        matrix = new int[height][width];

        // Load the map from file
        try {
            readMapFromFile(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        createCells();
        add(gridPanel, BorderLayout.CENTER);

        // Add Reset button
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetPositions());
        add(resetButton, BorderLayout.SOUTH);

        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void createCells() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                squares[i][j] = new CellPanel(i, j);
                squares[i][j].setBorder(new LineBorder(Color.BLACK, 1));
                setCellStyle(i, j); // Set initial cell style
                gridPanel.add(squares[i][j]);
            }
        }
    }

    private void setCellStyle(int i, int j) {
        // Set obstacle or free cell based on matrix value
        if (matrix[i][j] == -1) {
            squares[i][j].setBackground(Color.RED);  // Mark obstacle
        } else {
            squares[i][j].setBackground(Color.WHITE);  // Mark free cell
        }
    }

    private void readMapFromFile(String filename) throws FileNotFoundException {
        File file = new File(filename);
        Scanner scanner = new Scanner(file);
        height = scanner.nextInt();
        width = scanner.nextInt();
        matrix = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                matrix[i][j] = scanner.nextInt();
            }
        }
        scanner.close();
    }

    private void resetPositions() {
        startSet = false;
        endSet = false;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                squares[i][j].setRaccoon(false);
                squares[i][j].setTarget(false);
            }
        }
        gridPanel.repaint();
    }

    private class CellPanel extends JPanel {
        private int row, col;
        private boolean hasRaccoon = false;
        private boolean hasTarget = false;

        public CellPanel(int row, int col) {
            this.row = row;
            this.col = col;

            // Mouse listener to set start or target position
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (matrix[row][col] == -1) {
                        JOptionPane.showMessageDialog(null, "Obstacle cell. Cannot place raccoon or target here.");
                        return;
                    }
                    // Set start position if not set
                    if (!startSet) {
                        startPos[0] = row;
                        startPos[1] = col;
                        hasRaccoon = true;
                        startSet = true;
                    }
                    // Set target position if not set and it's not the start position
                    else if (!endSet) {
                        if (row == startPos[0] && col == startPos[1]) {
                            JOptionPane.showMessageDialog(null, "The target position cannot be the same as the start position.");
                        } else {
                            endPos[0] = row;
                            endPos[1] = col;
                            hasTarget = true;
                            endSet = true;
                        }
                    }
                    gridPanel.repaint();
                }
            });
        }

        public void setRaccoon(boolean hasRaccoon) {
            this.hasRaccoon = hasRaccoon;
        }

        public void setTarget(boolean hasTarget) {
            this.hasTarget = hasTarget;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw appropriate image based on cell content
            if (matrix[row][col] == -1) {
                g.drawImage(obstacleImage, 0, 0, getWidth(), getHeight(), null);
            } else {
                g.drawImage(grassImage, 0, 0, getWidth(), getHeight(), null);
            }
            if (hasRaccoon) {
                g.drawImage(raccoonImage, 0, 0, getWidth(), getHeight(), null);
            }
            if (hasTarget) {
                g.drawImage(endImage, 0, 0, getWidth(), getHeight(), null);
            }
        }
    }

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
            new GridLayoutManager(selectedFile);
        } else {
            // if the user chancel the choice the program is finished
            System.out.println("No file has been selected.");
            System.exit(0);
        }
    }
}
