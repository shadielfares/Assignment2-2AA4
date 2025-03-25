package ca.mcmaster.se2aa4.island.teamXXX.tools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.island.teamXXX.enumerations.Heading;


public class RaycastMap {

    private final int SIZE = 60;

    private char[][] grid = new char[SIZE][SIZE];

    // Defines the first and last tile in each row that represents the island border
    private int[][] terminalRowTiles = new int[SIZE][2];

    private int entryRow;
    private int exitRow;

    private final Logger logger = LogManager.getLogger();

    // Initializes the raycast map
    public RaycastMap() {

        for (int i=0; i<SIZE; i++) {
            for (int j=0; j<SIZE; j++) {
                grid[i][j] = '_';
            }
        }
    }
    
    // Find the row the island starts on
    public int getEntryRow() {
        return entryRow;
    }

    // Find the row the island ends on
    public int getExitRow() {
        return exitRow;
    }

    // Update tile in raycast map
    private void setTile(int x, int y) {
        grid[y-1][x-1] = 'X';
    }

    // Generate map outline
    public void setRaycast(Heading heading, int distance, int x, int y) {

        distance += 1;

        if (heading.equals(Heading.NORTH)) {
            setTile(x - distance, y);
        }
        else if (heading.equals(Heading.EAST)) {
            setTile(x, y - distance);
        }
        else if (heading.equals(Heading.SOUTH)) {
            setTile(x + distance, y);
        }
        else {
            setTile(x, y + distance);
        }

    }

    // Calculate the region the drone needs to scan over the island
    public void calculateScanRegion() {
        
        for (int i=0; i<SIZE; i++) {

            int xMin = SIZE + 1;
            int xMax = -1;

            for (int j=0; j<SIZE;j++) {
                
                if (grid[i][j] == 'X') {
                    xMin = Math.min(j + 1, xMin);
                    xMax = Math.max(j + 1, xMax);
                }
            }

            terminalRowTiles[i][0] = xMin;
            terminalRowTiles[i][1] = xMax;

        }
    }

    // Calculate the row the island starts on
    public void calculateEntryRow() {

        for (int i =0; i<SIZE;i++) {
            if (terminalRowTiles[i][0] != SIZE + 1) {
                entryRow = i + 1;
                break;
            }
        }
    }

    // Calculate the row the island ends on
    public void calculateExitRow() {

        for (int i =0; i<SIZE;i++) {
            if (terminalRowTiles[i][0] != SIZE + 1) {
                exitRow = i + 1;
            }
        }
    }

    // Get the last tile in a row
    public int getMax(int row) {
        return terminalRowTiles[row - 1][1];
    }

    // Get the first tile in a row
    public int getMin(int row) {
        return terminalRowTiles[row - 1][0];
    }
}