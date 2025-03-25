package ca.mcmaster.se2aa4.island.teamXXX.actions;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.teamXXX.drone.DroneAction;
import ca.mcmaster.se2aa4.island.teamXXX.enumerations.Heading;

// Defines drone echo left action
public class EchoLeft implements DroneAction {

    @Override

    // Creates JSONObject with correct echo parameters (heading)
    public JSONObject doAction(Heading heading) {
        
        JSONObject decision = new JSONObject();
        JSONObject parameters = new JSONObject();
        decision.put("action", "echo");

        if (heading.equals(Heading.EAST)) {
            parameters.put("direction", "N");
        } else if (heading.equals(Heading.SOUTH)) {
            parameters.put("direction", "E");
        } else if (heading.equals(Heading.WEST)) {
            parameters.put("direction", "S");
        } else {
            parameters.put("direction", "W");
        }

        decision.put("parameters", parameters);

        return decision;
    }
}