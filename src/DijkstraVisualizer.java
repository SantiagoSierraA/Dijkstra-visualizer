import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DijkstraVisualizer {
    JFrame frame  = new JFrame("Dijkstra Visualizer");
    JPanel gridPanel = new JPanel();
    JPanel controlPanel = new JPanel();
    JButton[][] cells = new JButton[10][10];
    int startRow = 1;
    int startCol = 1;
    int endRow = 8;
    int endCol = 8;

    Color wallColor = new Color(21, 21, 20);
    Color cellColor = new Color(245, 249, 228);
    Color pathColor = new Color(255, 0, 255);

    boolean isDragging = false;
    boolean isAddingWalls = true;

    DijkstraVisualizer() {
        // Frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(600, 600);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        // Control panel
        JButton runButton = new JButton("Run Dijkstra");
        runButton.addActionListener(_ -> runDijkstra());

        JButton clearButton = new JButton("Clear all");
        clearButton.addActionListener(_ -> clearAll());

        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 10));
        controlPanel.add(runButton);
        controlPanel.add(clearButton);
        frame.add(controlPanel, BorderLayout.NORTH);


        // Grid panel
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
        cell.addActionListener(_ -> {
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

    int[] dijkstraAlgorithm(int originX, int originY, int destinationX, int destinationY) {
        int row = 10;
        int col = 10;
        int total = row * col;

        int[] distances = new int[total];
        int[] previous = new int[total];
        boolean[] visited = new boolean[total];

        // Initialization
        for (int i = 0; i < total; i++) {
            distances[i] = Integer.MAX_VALUE;
            previous[i] = -1;
            visited[i] = false;
        }

        int origin = originX * col + originY;
        int destination = destinationX * col + destinationY;
        distances[origin] = 0;

        // Main loop
        for (int count = 0; count < total - 1; count++) {
            int u = -1;

            // Minimum distance node
            for (int v = 0; v < total; v++) {
                if (!visited[v] && (u == -1 || distances[v] < distances[u])) {
                    u = v;
                }
            }

            if (u == -1 || distances[u] == Integer.MAX_VALUE) break;

            visited[u] = true;

            int uX = u / col;
            int uY = u % col;

            // 4-directional movement
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

            for (int[] dir : directions) {
                int vX = uX + dir[0];
                int vY = uY + dir[1];

                // Out of bounds
                if (vX < 0 || vX >= row || vY < 0 || vY >= col) continue;

                // Skip wall cells
                if (cells[vX][vY].getBackground().equals(wallColor)) continue;

                int v = vX * col + vY;
                int weight = 1;

                // Relaxation
                if (!visited[v] && distances[u] != Integer.MAX_VALUE && distances[u] + weight < distances[v]) {
                    distances[v] = distances[u] + weight;
                    previous[v] = u;
                }
            }
        }

        // No path found
        if (distances[destination] == Integer.MAX_VALUE) {
            return null;
        }

        // Reconstruct path
        int[] temp = new int[total];
        int size = 0;
        int actual = destination;

        while (actual != -1) {
            temp[size++] = actual;
            actual = previous[actual];
        }

        int[] path = new int[size + 1];
        path[0] = size;

        for (int i = 0; i < size; i++) {
            path[i + 1] = temp[size - 1 - i];
        }

        return path;
    }

    void runDijkstra() {
        clearPath();
        int[] path = dijkstraAlgorithm(startRow, startCol, endRow, endCol);

        if (path == null) {
            JOptionPane.showMessageDialog(frame, "There is no possible way!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (int i = 1; i < path.length - 1; i++) {
            int pos = path[i];
            int r = pos / 10;
            int c = pos % 10;
            if (!(r == startRow && c == startCol) && !(r == endRow && c == endCol)) {
                cells[r][c].setBackground(pathColor);
            }
        }
    }

    void clearPath() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Color current = cells[i][j].getBackground();
                if (current.equals(pathColor)) {
                    cells[i][j].setBackground(cellColor);
                }
            }
        }
    }

    void clearAll() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if ((i == startRow && j == startCol) || (i == endRow && j == endCol)) {
                    continue;
                }
                cells[i][j].setBackground(cellColor);
            }
        }
    }
}
