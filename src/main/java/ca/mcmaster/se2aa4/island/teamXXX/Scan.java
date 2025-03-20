package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class Scan implements DroneAction {

    @Override
    public JSONObject doAction(Heading heading) {

        JSONObject decision = new JSONObject();
        decision.put("action", "scan");
        return decision;
    }
}