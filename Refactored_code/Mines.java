package mines;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Mines extends JFrame {
    private static final long serialVersionUID = 4772165125287256837L;
    private static final int WIDTH = 250;
    private static final int HEIGHT = 290;

    private JLabel statusbar;

    public Mines() {
        setupWindow();
    }

    private void setupWindow() {
        setTitle("Minesweeper");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        statusbar = new JLabel("");
        add(statusbar, BorderLayout.SOUTH);
        add(new Board(statusbar));

        setVisible(true);
    }

    public static void main(String[] args) {
        new Mines();
    }
}
