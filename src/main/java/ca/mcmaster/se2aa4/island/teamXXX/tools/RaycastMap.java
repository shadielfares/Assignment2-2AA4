package ca.mcmaster.se2aa4.island.teamXXX.tools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.island.teamXXX.enumerations.Heading;


public class RaycastMap {

    private final int SIZE = 60;

    private char[][] grid = new char[SIZE][SIZE];

    private int[][] rowEnds = new int[SIZE][2];

    private int entryRow;
    private int exitRow;

    private final Logger logger = LogManager.getLogger();

    public RaycastMap() {

        for (int i=0; i<SIZE; i++) {
            for (int j=0; j<SIZE; j++) {
                grid[i][j] = '_';
            }
        }
    }
    
    public int getEntryRow() {
        return entryRow;
    }

    public int getExitRow() {
        return exitRow;
    }
    
    public void setTile(int x, int y) {
        grid[y-1][x-1] = 'X';
    }

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

    public void displayMap() {

        for (int i=0; i<SIZE; i++) {
            String row = "";

            for (int j=0; j<SIZE; j++) {
                row += grid[i][j] + " ";
            }

            logger.info(row);
        }

    }

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

            rowEnds[i][0] = xMin;
            rowEnds[i][1] = xMax;

        }
    }

    public void calculateEntryRow() {

        for (int i =0; i<SIZE;i++) {

            if (rowEnds[i][0] != SIZE + 1) {

                entryRow = i + 1;
                break;
            }
        }
    }

    public void calculateExitRow() {

        for (int i =0; i<SIZE;i++) {

            if (rowEnds[i][0] != SIZE + 1) {

                exitRow = i + 1;
            }
        }
    }

    public int getMax(int row) {
        return rowEnds[row - 1][1];
    }

    public int getMin(int row) {
        return rowEnds[row - 1][0];
    }



}