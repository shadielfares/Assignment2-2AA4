package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class Decisione {

    private static Decisione instance = null;

    private JSONObject decision;

    private Decisione() {

        Heading heading = Heading.SOUTH;
        setDecision(new Fly().doAction(heading));
    }

    public void setDecision(JSONObject decision) {
        this.decision = decision;
    }

    public JSONObject getDecision() {
        return decision;
    }

    public static Decisione getInstance() {

        if (instance == null) {
            instance = new Decisione();
        }
        return instance;
    }

}