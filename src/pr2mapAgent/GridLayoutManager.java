package pr2mapAgent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.border.LineBorder;

import static javax.swing.text.StyleConstants.setIcon;

public class GridLayoutManager extends JFrame {

    private Container contents;
    private JPanel gridPanel;
    private JButton[][] squares;
    private int[][] matrix;
    private int height = 10;
    private int width = 10;

    private Color colorRed = Color.RED; // color for obstacles
    private Color colorWhite = Color.WHITE; // color for free cells

    private ImageIcon robot;  // image of the agent
    private ImageIcon endIcon; // image of the target
    private ImageIcon obstacle; // image of the obstacles
    private ImageIcon grass; //image for free cells

    private int[] startPos = new int[2];  // start position
    private int[] endPos = new int[2];  // target position
    private boolean startSet = false;
    private boolean endSet = false;

    public GridLayoutManager(String filename) {
        super("GUI GridLayout Manager - (click a valid square to set the start and the target )");

        endIcon = new ImageIcon(new ImageIcon("stink.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        grass = new ImageIcon(new ImageIcon("grass.png").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
        obstacle = new ImageIcon(new ImageIcon("wall.png").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
        //robot = new ImageIcon(new ImageIcon("raccoon2.png").getImage().getScaledInstance(30, 40, Image.SCALE_SMOOTH));
        Image img = Toolkit.getDefaultToolkit().getImage("raccoon2.png");
        robot = new ImageIcon(img.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        System.out.println("Image Load Status: " + robot.getImageLoadStatus());


        // initialize the environment
        contents = getContentPane();
        contents.setLayout(new BorderLayout()); // use BorderLayout to put the reset button inn the bottom


        //creation of the matrix for the buttons
        squares = new JButton[height][width];
        matrix = new int[height][width];

        // initialize the grid panel
        gridPanel = new JPanel(new GridLayout(height, width));


        // read the file
        try {
            readMapFromFile(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // create and add buttons in the grid
        createButtons();

        contents.add(gridPanel, BorderLayout.CENTER);
        contents.revalidate();
        contents.repaint();

        // add action listener in the buttons
        ButtonHandler buttonHandler = new ButtonHandler();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                squares[i][j].addActionListener(buttonHandler);
            }
        }



        // add the  "Reset" button
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetPositions();
            }
        });
        contents.add(resetButton, BorderLayout.SOUTH);
    }

    //read the map file
    private void readMapFromFile(String filename) throws FileNotFoundException {
        File file = new File(filename);
        Scanner scanner = new Scanner(file);

        // read the map's dimensions
        height = scanner.nextInt();
        width = scanner.nextInt();
        matrix = new int[height][width];

        // read the map
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                matrix[i][j] = scanner.nextInt();
            }
        }

        scanner.close();
    }

    // creation of the buttons
    private void createButtons() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                squares[i][j] = new JButton();
                setButtonStyle(i , j); //define the button color
                squares[i][j].setBorder(new LineBorder(Color.BLACK, 2));
                gridPanel.add(squares[i][j]);
            }
        }
        gridPanel.repaint();
    }

    // id the value is -1 the button is red , if the value is 0 the button is white
    private void setButtonStyle(int i, int j) {
        if (matrix[i][j] == -1) {
            squares[i][j].setBackground(colorRed);
            squares[i][j].setIcon(obstacle);
        } else if (matrix[i][j] == 0) {
            squares[i][j].setIcon(grass);
        }
        squares[i][j].setPreferredSize(new Dimension(50, 50));
    }


    // action for the buttons when they're pushed
    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (squares[i][j] == source) {
                        // check if it's an obstacle
                        if (matrix[i][j] == -1) {
                            JOptionPane.showMessageDialog(null, "You can't set the start position in an obstacle .");
                            return;
                        }
                        // Set start position if not set
                        if (!startSet) {
                            startPos[0] = i;
                            startPos[1] = j;
                            squares[i][j].setIcon(grass);
                            squares[i][j].setIcon(robot);
                            startSet = true;
                        }
                        // Set end position if not set and it's not the start position
                        else if (!endSet) {
                            if (i == startPos[0] && j == startPos[1]) {
                                JOptionPane.showMessageDialog(null, "The end position cannot be the same as the start position.");
                            }
                            else if (matrix[i][j] == -1) {
                                JOptionPane.showMessageDialog(null, "You can't set the end position in an obstacle .");
                                return;
                            } else {
                                endPos[0] = i;
                                endPos[1] = j;
                                squares[i][j].setIcon(endIcon);
                                endSet = true;
                            }
                        }
                    }
                }
            }
        }
    }

    // reset positions
    private void resetPositions() {
        startSet = false;
        endSet = false;

        // Επαναφορά των κουμπιών και των εικόνων
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // if matrix is empty we put the grass icon
                if (matrix[i][j] == -1) {
                    squares[i][j].setBackground(colorRed);
                    squares[i][j].setIcon(obstacle);
                } else if (matrix[i][j] == 0) {
                    squares[i][j].setBackground(colorWhite);
                    squares[i][j].setIcon(grass);
                } else {
                    squares[i][j].setIcon(null); //we clean the other cells
                }
            }
        }
    }

    // main method
    public static void main(String[] args) {
        String filename = "maps/mapWithDiagonalWall.txt";
        GridLayoutManager gui = new GridLayoutManager(filename);
        gui.setSize(600, 600);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setVisible(true);
    }
}
