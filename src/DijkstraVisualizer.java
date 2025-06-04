import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DijkstraVisualizer {
    JFrame frame  = new JFrame("Dijkstra Visualizer");
    JPanel gridPanel = new JPanel();
    JButton[][] cells = new JButton[10][10];
    int startRow = 1;
    int startCol = 1;
    int endRow = 8;
    int endCol = 8;

    Color wallColor = new Color(21, 21, 20);
    Color cellColor = new Color(245, 249, 228);

    boolean isDragging = false;
    boolean isAddingWalls = true;

    DijkstraVisualizer() {
        // Frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(600, 600);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);


        // Panel
        gridPanel.setLayout(new GridLayout(10, 10,2,2));
        gridPanel.setBackground(Color.BLACK);

        frame.add(gridPanel);
        createGrid();
    }

    void createGrid(){
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                JButton cell = new JButton();
                cell.setFocusable(false);
                cell.setBorderPainted(true);
                cell.setOpaque(true);

                if (i == startRow && j == startCol) {
                    cell.setBackground(Color.RED);
                    cell.setEnabled(false);
                }
                else if (i == endRow && j == endCol) {
                    cell.setBackground(Color.BLUE);
                    cell.setEnabled(false);
                }
                else{
                    cell.setBackground(cellColor);
                    addCellListeners(cell);
                }

                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                cells[i][j] = cell;
                gridPanel.add(cell);
            }
        }
    }

    void addCellListeners(JButton cell) {

        cell.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                isDragging = true;
                Color currentColor = cell.getBackground();

                if (currentColor.equals(cellColor)) {
                    isAddingWalls = true;
                    cell.setBackground(wallColor);
                }
                else if (currentColor.equals(wallColor)) {
                    isAddingWalls = false;
                    cell.setBackground(cellColor);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isDragging = false;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (isDragging) {
                    Color currentColor = cell.getBackground();

                    if (currentColor.equals(Color.RED) || currentColor.equals(Color.BLUE)) {
                        return;
                    }

                    if (isAddingWalls && currentColor.equals(cellColor)) {
                        cell.setBackground(wallColor);
                    }
                    else if (!isAddingWalls && currentColor.equals(wallColor)) {
                        cell.setBackground(cellColor);
                    }
                }
            }
        });

        // Simple click
        cell.addActionListener(e -> {
            if (!isDragging) {
                Color currentColor = cell.getBackground();
                if (currentColor.equals(cellColor)) {
                    cell.setBackground(wallColor);
                }
                else if (currentColor.equals(wallColor)) {
                    cell.setBackground(cellColor);
                }
            }
        });
    }
}
