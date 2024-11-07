package pr2mapAgent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.border.LineBorder;
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

    private int[] startPos = new int[2];  // start position
    private int[] endPos = new int[2];  // target position
    private boolean startSet = false;
    private boolean endSet = false;

    public GridLayoutManager(String filename) {
        super("GUI GridLayout Manager - (click a valid square to set the start and the target )");

        // initialize the environment
        contents = getContentPane();
        contents.setLayout(new BorderLayout()); // use BorderLayout to put the reset button inn the bottom

        // initialize the grid panel
        gridPanel = new JPanel(new GridLayout(height, width));
        contents.add(gridPanel, BorderLayout.CENTER); // add grid in the center

        //creation of the matrix for the buttons
        squares = new JButton[height][width];
        matrix = new int[height][width];

        // read the file
        try {
            readMapFromFile(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // create and add buttons in the grid
        createButtons();

        // add action listener in the buttons
        ButtonHandler buttonHandler = new ButtonHandler();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                squares[i][j].addActionListener(buttonHandler);
            }
        }


        robot = new ImageIcon(new ImageIcon("raccoon.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        endIcon = new ImageIcon(new ImageIcon("stink.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

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
                setButtonStyle(i, j); //define the button color
                squares[i][j].setBorder(new LineBorder(Color.BLACK, 2));
                gridPanel.add(squares[i][j]);
            }
        }
    }

    // id the value is -1 the button is red , if the value is 0 the button is white
    private void setButtonStyle(int i, int j) {
        if (matrix[i][j] == -1) {
            squares[i][j].setBackground(colorRed); // Εμπόδιο
        } else if (matrix[i][j] == 0) {
            squares[i][j].setBackground(colorWhite); // Κενό κελί
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
                        // if the initial position hasn't been set
                        if (!startSet) {
                            startPos[0] = i;
                            startPos[1] = j;
                            squares[i][j].setIcon(robot);
                            startSet = true;

                        }
                        // if the final position hasn't been set
                        else if (!endSet) {
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

    // reset positions
    private void resetPositions() {
        startSet = false;
        endSet = false;

        // Επαναφορά κουμπιών και εικόνων
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                setButtonStyle(i, j);
                squares[i][j].setIcon(null); // Αφαίρεση εικόνας
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
