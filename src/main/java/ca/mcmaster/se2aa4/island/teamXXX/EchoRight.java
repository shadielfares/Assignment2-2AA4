package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class EchoRight implements DroneAction {
    @Override
    public JSONObject doAction(Heading heading) {
        JSONObject decision = new JSONObject();
        JSONObject parameters = new JSONObject();
        decision.put("action", "echo");

        if (heading.equals(Heading.EAST)) {
            parameters.put("direction", "S");
        } else if (heading.equals(Heading.SOUTH)) {
            parameters.put("direction", "W");
        } else if (heading.equals(Heading.WEST)) {
            parameters.put("direction", "N");
        } else {
            parameters.put("direction", "E");
        }
        decision.put("parameters", parameters);

        return decision;
    }
}