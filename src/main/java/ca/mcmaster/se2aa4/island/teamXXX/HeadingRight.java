package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class HeadingRight implements DroneAction {
    @Override
    public JSONObject doAction(Heading heading) {
        JSONObject decision = new JSONObject();
        JSONObject parameters = new JSONObject();
        decision.put("action", "echo");

        Drone drone = Drone.getDroneInstance();

        if (heading.equals(Heading.EAST)) {
            parameters.put("direction", "S");
            drone.setHeading(Heading.SOUTH);
        } else if (heading.equals(Heading.SOUTH)) {
            parameters.put("direction", "W");
            drone.setHeading(Heading.WEST);
        } else if (heading.equals(Heading.WEST)) {
            parameters.put("direction", "N");
            drone.setHeading(Heading.NORTH);
        } else {
            parameters.put("direction", "E");
            drone.setHeading(Heading.EAST);
        }
        decision.put("parameters", parameters);

        return decision;
    }
}
