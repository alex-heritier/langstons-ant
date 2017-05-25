/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package langtons_ant;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Alex
 */
public class Langtons_Ant {

    public static final int FRAME_WIDTH = 800;
    public static final int FRAME_HEIGHT = 800;
    public static final int GRID_WIDTH = 400;
    public static final int GRID_HEIGHT = 400;

    private static final int BLACK = 0;
    private static final int WHITE = 1;

    private static final int LEFT = 0;
    private static final int UP = 1;
    private static final int RIGHT = 2;
    private static final int DOWN = 3;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int grid[][] = createGrid();
        int antState[] = {GRID_WIDTH / 2, GRID_HEIGHT / 2, LEFT}; // [x, y, dir]
        JFrame frame = new JFrame("Langton's Ant");
        JPanel panel = createCustomJPanel(grid, antState);

        panel.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        panel.setVisible(true);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

        while (true) {
            antState = step(grid, antState);
            panel.repaint();
            try {
                Thread.sleep(10);
            } catch (Exception e) {
            }
        }
    }

    private static int[] step(int grid[][], int[] antState) {
        int x = antState[0];
        int y = antState[1];
        int currentCell = grid[x][y];
        
        if (currentCell == WHITE) {
            grid[x][y] = BLACK;
            antState[2] = (antState[2] + 5) % 4;
        } else {
            grid[x][y] = WHITE;
            antState[2] = (antState[2] + 3) % 4;
        }

        switch (antState[2]) {
            case LEFT:
                antState[0]--;
                if (antState[0] < 0) {
                    antState[0] = GRID_WIDTH - 1;
                }
                break;
            case UP:
                antState[1]++;
                if (antState[1] >= GRID_HEIGHT) {
                    antState[1] = 0;
                }
                break;
            case RIGHT:
                antState[0]++;
                if (antState[0] >= GRID_WIDTH) {
                    antState[0] = 0;
                }
                break;
            case DOWN:
                antState[1]--;
                if (antState[1] < 0) {
                    antState[1] = GRID_HEIGHT - 1;
                }
                break;
        }

        return antState;
    }

    private static int[][] createGrid() {
        int grid[][] = new int[GRID_WIDTH][];
        for (int i = 0; i < grid.length; i++) {
            grid[i] = new int[GRID_HEIGHT];
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][grid[i].length - 1 - j] = WHITE;
            }
        }
        return grid;
    }

    private static JPanel createCustomJPanel(final int grid[][], final int antState[]) {
        return new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponents(g);

                // create board
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, Langtons_Ant.FRAME_WIDTH, Langtons_Ant.FRAME_HEIGHT);
                g.setColor(Color.BLACK);
                g.drawRect(0, 0, Langtons_Ant.FRAME_WIDTH, Langtons_Ant.FRAME_HEIGHT);

                // draw cells
                int width = FRAME_WIDTH / GRID_WIDTH;
                int height = FRAME_HEIGHT / GRID_HEIGHT;

                for (int i = 0; i < GRID_WIDTH; i++) {
                    for (int j = 0; j < GRID_HEIGHT; j++) {
                        int currentCell = grid[i][j];
                        int x = i * width;
                        int y = FRAME_HEIGHT - (j * height) - height;

                        if (currentCell == WHITE) {
                            g.setColor(Color.WHITE);
                        } else {
                            g.setColor(Color.BLACK);
                        }

                        g.fillRect(x, y, width, height);

                        if (i == antState[0] && j == antState[1]) {
                            g.setColor(Color.RED);
                            g.fillOval(x + width/4, y + height/4, width/2, height/2);

                            switch (antState[2]) {
                                case LEFT:
                                    g.drawLine(x + width/2, y + height/2, x, y + height/2);
                                    break;
                                case UP:
                                    g.drawLine(x + width/2, y + height/2, x + width/2, y);
                                    break;
                                case RIGHT:
                                    g.drawLine(x + width/2, y + height/2, x + width, y + height/2);
                                    break;
                                case DOWN:
                                    g.drawLine(x + width/2, y + height/2, x + width/2, y + height);
                                    break;
                            }
                        }

                        //g.setColor(Color.BLACK);
                        //g.drawRect(x, y, width, height);
                    }
                }
            }
        };
    }
}
