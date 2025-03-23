package ca.mcmaster.se2aa4.island.teamXXX;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Drone {

    private Heading heading;
    private static Drone instance = null;
    private int x = 0;
    private int y = 0;
    private int safeTravelDistance = 0;
    private int cornersTravelled = 0;
    private int battery = 7000;

    private final Logger logger = LogManager.getLogger();

    private Drone() {
        this.heading = Heading.EAST;
    }

    public static Drone getDroneInstance() {
        if (instance == null) {
            instance = new Drone();
        }
        return instance;
    }

    public void setInPosition() {
        this.x = 2;
        this.y = 1;
    }

    public void setCornersTravelled(int amount) {
        this.cornersTravelled = amount;
    }

    public int getCornersTravelled() {
        return this.cornersTravelled;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int newX) {
        this.x = newX;
    }

    public void setY(int newY) {
        this.y = newY;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    public void setSafeTravelDistance(int distance) {
        this.safeTravelDistance = distance;
    }

    public int getSafeTravelDistance() {
        return safeTravelDistance;
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

}