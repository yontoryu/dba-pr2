package pr2mapAgent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.border.LineBorder;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;



public class GridLayoutManager extends JFrame {

    private JPanel gridPanel;
    private CellPanel[][] squares;
    private int height;
    private int width;
    private Environment env;
    private Map map;

    private BufferedImage grassImage;
    private BufferedImage raccoonImage;
    private BufferedImage obstacleImage; // Image of obstacles (walls)
    private BufferedImage endImage; // Image of the target
    private BufferedImage raccoonTarget;


    private boolean endSet = false;
    private boolean startSet = false;
    private boolean positionsSet = false;
    private int[] startPos = new int[2];
    private int[] endPos = new int[2];  // Target position


    public GridLayoutManager(Environment env , Map map) {
        super("GUI GridLayout Manager");

        this.env = env;
        this.map = map;

        // Load images for cells
        try {
            raccoonTarget= ImageIO.read(new File("png-clipart-pixel-art-fireworks-fireworks-purple-game-removebg-preview (1).png"));
            grassImage = ImageIO.read(new File("summer-grass.png"));
            raccoonImage = ImageIO.read(new File("raccoon5.png"));
            obstacleImage = ImageIO.read(new File("stone_wall_2_by_sarahstudiosart_d7l2g11-fullview.png"));
            endImage = ImageIO.read(new File("trashbin.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        width = map.getWidth();
        height = map.getHeight();

        gridPanel = new JPanel(new GridLayout(height, width));
        squares = new CellPanel[height][width];

        createCells();
        add(gridPanel, BorderLayout.CENTER);

        // Add Reset and Start buttons in the south
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Panel with horizontal layout

        // Add Reset button
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetPositions());
        resetButton.setPreferredSize(new Dimension(300, 40));
        buttonPanel.add(resetButton); // Add Reset button to the panel

        // Add Start button
        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> {
            Scout scout = new Scout();


            new Thread(() -> {
                scout.startAgent(new Object[]{getStartPos(), getEndPos(), env});


                List<int[]> path = scout.getVisitedPath();


                if (path != null && !path.isEmpty()) {

                    SwingUtilities.invokeLater(() -> {
                        startMoving(path);
                    });
                } else {

                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, "No path found.");
                    });
                }
            }).start();
        });

        startButton.setPreferredSize(new Dimension(300, 40));
        buttonPanel.add(startButton); // Add Start button to the panel

        // Add the button panel to the south
        add(buttonPanel, BorderLayout.SOUTH);

        // Finalize JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800); // Adjust size as needed
        setVisible(true);
    }

    private void startRunning() {
        positionsSet = true;
    }


    public boolean positionsSet() {
        return positionsSet;
    }

    public int[] getStartPos() {
        return startPos;
    }

    public int[] getEndPos() {
        return endPos;
    }
    public void startMoving(List<int[]> path) {
        Timer timer = new Timer(500, null); // 500ms για κάθε βήμα
        final int[] index = {0};

        timer.addActionListener(e -> {
            if (index[0] < path.size()) {
                int[] currentPos = path.get(index[0]);
                int[] prevPos = index[0] > 0 ? path.get(index[0] - 1) : null;


                if (prevPos != null) {
                    squares[prevPos[0]][prevPos[1]].setRaccoon(false);
                }
                squares[currentPos[0]][currentPos[1]].setRaccoon(true);
                gridPanel.repaint();
                if (Arrays.equals(currentPos, endPos)) {
                    squares[currentPos[0]][currentPos[1]].setTargetReached(true);
                    squares[currentPos[0]][currentPos[1]].setTarget(false);
                    squares[currentPos[0]][currentPos[1]].setRaccoon(false);
                    gridPanel.repaint();
                }
                index[0]++;

            }  else {
                ((Timer) e.getSource()).stop();
                JOptionPane.showMessageDialog(this, "Raccoon reached the target!");
            }
        });

        timer.start();
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
        if (map.getMatrix()[i][j] == -1) {
            squares[i][j].setBackground(Color.RED);  // Mark obstacle
        } else {
            squares[i][j].setBackground(Color.WHITE);  // Mark free cell
        }
    }

    private void resetPositions() {
        startSet = false;
        endSet = false;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                squares[i][j].setRaccoon(false);
                squares[i][j].setTarget(false);
                squares[i][j].setTargetReached(false);

            }
        }
        gridPanel.repaint();
    }

    private class CellPanel extends JPanel {
        private int row, col;
        private boolean hasRaccoon = false;
        private boolean hasTarget = false;
        private boolean hasTargetReached = false;

        public CellPanel(int row, int col) {
            this.row = row;
            this.col = col;

            // Mouse listener to set start or target position
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (map.getMatrix()[row][col] == -1) {
                        JOptionPane.showMessageDialog(null, "Obstacle cell. Cannot place raccoon or target here.");
                        return;
                    }
                    // Set start position if not set
                    if (!startSet) {
                        startPos[0] = col;
                        startPos[1] = row;
                        hasRaccoon = true;
                        startSet = true;
                    }
                    // Set target position if not set and it's not the start position
                    else if (!endSet) {
                        if (row == startPos[0] && col == startPos[1]) {
                            JOptionPane.showMessageDialog(null, "The target position cannot be the same as the start position.");
                        } else {
                            endPos[0] = col;
                            endPos[1] = row;
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
        public void setTargetReached(boolean hasTargetReached) {
            this.hasTargetReached = hasTargetReached;
        }

        public void setTarget(boolean hasTarget) {
            this.hasTarget = hasTarget;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw appropriate image based on cell content
            if (map.getMatrix()[row][col] == -1) {
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
            if (hasTargetReached) {
                g.drawImage(raccoonTarget, 0, 0, getWidth(), getHeight(), null);

            }
        }
    }


}
