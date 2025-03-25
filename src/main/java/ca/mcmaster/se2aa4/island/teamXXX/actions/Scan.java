package ca.mcmaster.se2aa4.island.teamXXX.actions;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.teamXXX.drone.DroneAction;
import ca.mcmaster.se2aa4.island.teamXXX.enumerations.Heading;

public class Scan implements DroneAction {

    @Override
    public JSONObject doAction(Heading heading) {

        JSONObject decision = new JSONObject();
        decision.put("action", "scan");
        return decision;
    }
}