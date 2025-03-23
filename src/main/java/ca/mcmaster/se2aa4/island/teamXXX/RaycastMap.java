package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class RaycastMap {

    private char[][] grid = new char[60][60];

    private int[][] rowEnds = new int[60][2];

    private int entryRow;
    private int exitRow;

    private final Logger logger = LogManager.getLogger();

    public RaycastMap() {

        for (int i=0; i<60; i++) {
            for (int j=0; j<60; j++) {
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

        for (int i=0; i<60; i++) {
            String row = "";

            for (int j=0; j<60; j++) {
                row += grid[i][j] + " ";
            }

            logger.info(row);
        }

    }

    public void calculateRowEnds() {
        
        for (int i=0; i<60; i++) {

            int xMin = 61;
            int xMax = -1;

            for (int j=0; j<60;j++) {
                
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

        for (int i =0; i<60;i++) {

            if (rowEnds[i][0] != 61) {

                entryRow = i + 1;
                break;
            }
        }
    }

    public void calculateExitRow() {

        for (int i =0; i<60;i++) {

            if (rowEnds[i][0] != 61) {

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