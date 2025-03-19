package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class DroneDecision {

    private static DroneDecision instance = null;

    private JSONObject decision;

    private DroneDecision() {

        Heading heading = Heading.SOUTH;
        setDecision(new Fly().doAction(heading));
    }

    public void setDecision(JSONObject decision) {
        this.decision = decision;
    }

    public JSONObject getDecision() {
        return decision;
    }

    public static DroneDecision getInstance() {

        if (instance == null) {
            instance = new DroneDecision();
        }
        return instance;
    }

}