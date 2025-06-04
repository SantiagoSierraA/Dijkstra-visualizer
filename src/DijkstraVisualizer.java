import javax.swing.*;
import java.awt.*;

public class DijkstraVisualizer {
    JFrame frame  = new JFrame("Dijkstra Visualizer");
    JPanel gridPanel = new JPanel();
    JButton[][] cells;
    int startRow = 1;
    int startCol = 1;
    int endRow = 8;
    int endCol = 8;

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
        cells = new JButton[10][10];

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
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
                    cell.setBackground(new Color(245, 249, 228));
                }

                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                cells[i][j] = cell;
                gridPanel.add(cell);
            }
        }
    }
}
