package mines;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Board extends JPanel {
    private static final long serialVersionUID = 6195235521361212179L;

    private static final int NUM_IMAGES = 13;
    private static final int CELL_SIZE = 15;
    private static final int MINE_COUNT = 40;
    private static final int ROWS = 16;
    private static final int COLS = 16;

    private int[] field;
    private boolean inGame;
    private int minesLeft;
    private Image[] images;
    private JLabel statusbar;

    public Board(JLabel statusbar) {
        this.statusbar = statusbar;
        loadImages();
        setupBoard();
        addMouseListener(new MinesAdapter());
        newGame();
    }

    private void loadImages() {
        images = new Image[NUM_IMAGES];
        for (int i = 0; i < NUM_IMAGES; i++) {
            images[i] = new ImageIcon(getClass().getClassLoader().getResource(i + ".gif")).getImage();
        }
    }

    private void setupBoard() {
        setDoubleBuffered(true);
        setFocusable(true);
    }

    public void newGame() {
        field = new int[ROWS * COLS];
        Random random = new Random();
        inGame = true;
        minesLeft = MINE_COUNT;
        statusbar.setText(String.valueOf(minesLeft));

        for (int i = 0; i < ROWS * COLS; i++) {
            field[i] = CellState.COVERED_CELL;
        }

        int minesPlaced = 0;
        while (minesPlaced < MINE_COUNT) {
            int position = random.nextInt(ROWS * COLS);
            if (field[position] != CellState.COVERED_MINE_CELL) {
                field[position] = CellState.COVERED_MINE_CELL;
                minesPlaced++;
                updateSurroundingCells(position);
            }
        }
    }

    private void updateSurroundingCells(int position) {
        int[] directions = {-1, 1, -COLS, COLS, -COLS - 1, -COLS + 1, COLS - 1, COLS + 1};
        for (int dir : directions) {
            int cell = position + dir;
            if (cell >= 0 && cell < field.length && field[cell] != CellState.COVERED_MINE_CELL) {
                field[cell]++;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                int cell = field[i * COLS + j];
                g.drawImage(images[cell], j * CELL_SIZE, i * CELL_SIZE, this);
            }
        }
        checkGameState();
    }

    private void checkGameState() {
        if (inGame) {
            statusbar.setText(minesLeft == 0 ? "Game won" : "");
        } else {
            statusbar.setText("Game lost");
        }
    }

    private class MinesAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            int x = e.getX() / CELL_SIZE;
            int y = e.getY() / CELL_SIZE;

            if (inGame) {
                handleLeftClick(x, y, e.getButton() == MouseEvent.BUTTON1);
            } else {
                newGame();
                repaint();
            }
        }

        private void handleLeftClick(int x, int y, boolean leftClick) {
            int position = y * COLS + x;
            if (leftClick && field[position] > CellState.COVERED_CELL) {
                field[position] -= CellState.COVER_MARKER;
                repaint();
            }
        }
    }

    private static class CellState {
        static final int COVERED_CELL = 10;
        static final int COVERED_MINE_CELL = 19;
        static final int COVER_MARKER = 10;
    }
}
