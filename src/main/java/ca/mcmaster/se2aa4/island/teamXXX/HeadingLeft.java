package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class HeadingLeft implements DroneAction {

    @Override
    public JSONObject doAction(Heading heading) {
        JSONObject decision = new JSONObject();
        JSONObject parameters = new JSONObject();
        decision.put("action", "heading");

        Drone drone = Drone.getDroneInstance();

        if (heading.equals(Heading.EAST)) {
            parameters.put("direction", "N");
            drone.setHeading(Heading.NORTH);
        } else if (heading.equals(Heading.SOUTH)) {
            parameters.put("direction", "E");
            drone.setHeading(Heading.EAST);
        } else if (heading.equals(Heading.WEST)) {
            parameters.put("direction", "S");
            drone.setHeading(Heading.SOUTH);
        } else {
            parameters.put("direction", "W");
            drone.setHeading(Heading.WEST);
        }
        decision.put("parameters", parameters);

        return decision;
    }
}
