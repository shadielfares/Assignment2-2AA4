package ca.mcmaster.se2aa4.island.teamXXX.drone;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.island.teamXXX.enumerations.Heading;
import ca.mcmaster.se2aa4.island.teamXXX.poi.Creek;
import ca.mcmaster.se2aa4.island.teamXXX.poi.Site;

// Defines the flying drone
public class Drone {

    private Heading heading;
    private static Drone instance = null;

    private int x = 0;
    private int y = 0;

    private int safeTravelDistance = 0;
    private int cornersTravelled = 0;
    private int battery = 15000;
    
    private List<Creek> creekList = new ArrayList<>();
    private List<Site> siteList = new ArrayList<>();

    private final Logger logger = LogManager.getLogger();

    private Drone() {
        this.creekList = new ArrayList<>();
        this.heading = Heading.EAST;
    }

    public static Drone getDroneInstance() {
        if (instance == null) {
            instance = new Drone();
        }
        return instance;
    }

    // Tracks located creeks
    public void addCreek(Creek creek) {
        creekList.add(creek);
    }
    
    // Tracks located sites
    public void addSite(Site site) {
        siteList.add(site);
    }

    // Calculates the creek nearest to the emergency site
    public String findNearestCreek() {

        if (siteList.size() > 0) {

            String minCreek = creekList.get(0).getId();

            double minDistance = Math.hypot(creekList.get(0).getX() - siteList.get(0).getX(), creekList.get(0).getY() - siteList.get(0).getY());


            for (int i=0;i<creekList.size();i++) {
                
                double distance = Math.hypot(creekList.get(i).getX() - siteList.get(0).getX(), creekList.get(i).getY() - siteList.get(0).getY());
                
                if (minDistance > distance) {
                    minDistance = distance;
                    minCreek = creekList.get(i).getId();
                }
            
            }

            return "Nearest Creek ID: " + minCreek + ", Emergency Site ID: " + siteList.get(0).getId();

        }
        else {
            return "No emergency site found";
        }
    }

    // Updates drone location when flying
    public void fly() {

        if (heading.equals(Heading.NORTH)) {
            decrementY();
        }
        else if (heading.equals(Heading.EAST)) {
            incrementX();
        }
        else if (heading.equals(Heading.SOUTH)) {
            incrementY();
        }
        else {
            decrementX();
        }
    }

    // Updates drone location and heading when turning left
    public JSONObject headingLeft(JSONObject parameters) {

        if (heading.equals(Heading.EAST)) {
            parameters.put("direction", "N");
            heading = Heading.NORTH;
            incrementX();
            decrementY();

        } else if (heading.equals(Heading.SOUTH)) {
            parameters.put("direction", "E");
            heading = Heading.EAST;
            incrementX();
            incrementY();

        } else if (heading.equals(Heading.WEST)) {
            parameters.put("direction", "S");
            heading = Heading.SOUTH;
            decrementX();
            incrementY();

        } else {
            parameters.put("direction", "W");
            heading = Heading.WEST;
            decrementX();
            decrementY();
        }

        return parameters;

    }

    // Updates drone location and heading when turning right
    public JSONObject headingRight(JSONObject parameters) {

        if (heading.equals(Heading.EAST)) {

            parameters.put("direction", "S");
            heading = Heading.SOUTH;
            incrementX();
            incrementY();

        } else if (heading.equals(Heading.SOUTH)) {

            parameters.put("direction", "W");
            heading = Heading.WEST;
            decrementX();
            incrementY();


        } else if (heading.equals(Heading.WEST)) {

            parameters.put("direction", "N");
            heading = Heading.NORTH;
            decrementX();
            decrementY();

        } else {

            parameters.put("direction", "E");
            heading = Heading.EAST;
            incrementX();
            decrementY();
        }

        return parameters;

    }

    // Places drone on the raycast version of the map
    public void placeOnRaycastMap() {
        this.x = 2;
        this.y = 1;
    }

    // Tracks number of corners the drone has travelled through
    public void incrementCornersTravelled() {
        this.cornersTravelled += 1;
    }

    public boolean allCornersTravelled() {
        return (this.cornersTravelled >= 4);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Initialize the drones starting heading
    public void initializeHeading(String direction) {

        if (direction.equals("N")) {
            heading = Heading.NORTH;
        }
        else if (direction.equals("E")) {
            heading = Heading.EAST;
        }
        else if (direction.equals("S")) {
            heading = Heading.SOUTH;
        }
        else {
            heading = Heading.WEST;
        }
    }  

    // Save how far the drone can travel before colliding with an obstacle
    public void setSafeTravelDistance(int distance) {
        this.safeTravelDistance = distance;
    }

    public int getSafeTravelDistance() {
        return safeTravelDistance;
    }

    public boolean batteryDepleted() {
        return (battery <= 150);
    }

    public void decreaseBattery(int amount) {
        this.battery -= amount;
    }

    public void displayBattery() {
        logger.info("Current battery level: " + battery);
    }

    public Heading getHeading() {
        return heading;
    }

    private void incrementY() {
        this.y += 1;
    }

    private void decrementY() {
        this.y -= 1;
    }

    private void incrementX() {
        this.x += 1;
    }

    private void decrementX() {
        this.x -= 1;
    }

}