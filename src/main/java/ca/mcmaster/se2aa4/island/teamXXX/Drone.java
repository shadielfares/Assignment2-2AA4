package ca.mcmaster.se2aa4.island.teamXXX;

public class Drone {
    private Heading heading;
    private static Drone instance = null;

    private Drone() {

    }

    public static Drone getDroneInstance() {
        if (instance == null) {
            instance = new Drone();
        }
        return instance;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    public Heading getHeading(Heading heading) {
        return heading;
    }
}