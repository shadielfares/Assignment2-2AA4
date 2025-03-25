package ca.mcmaster.se2aa4.island.teamXXX.actions;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.teamXXX.drone.DroneAction;
import ca.mcmaster.se2aa4.island.teamXXX.enumerations.Heading;

// Defines drone scan action
public class Scan implements DroneAction {

    @Override

    // Creates JSONObject for scan action
    public JSONObject doAction(Heading heading) {

        JSONObject decision = new JSONObject();
        decision.put("action", "scan");
        return decision;
    }
}