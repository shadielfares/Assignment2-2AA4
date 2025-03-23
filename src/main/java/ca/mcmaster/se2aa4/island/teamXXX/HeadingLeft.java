package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class HeadingLeft implements DroneAction {

    @Override
    public JSONObject doAction(Heading heading) {
        JSONObject decision = new JSONObject();
        JSONObject parameters = new JSONObject();
        decision.put("action", "heading");

        Drone drone = Drone.getDroneInstance();

        int x = drone.getX();
        int y = drone.getY();

        if (heading.equals(Heading.EAST)) {
            parameters.put("direction", "N");
            drone.setHeading(Heading.NORTH);
            drone.setX(x+1);
            drone.setY(y-1);

        } else if (heading.equals(Heading.SOUTH)) {
            parameters.put("direction", "E");
            drone.setHeading(Heading.EAST);
            drone.setX(x+1);
            drone.setY(y+1);

        } else if (heading.equals(Heading.WEST)) {
            parameters.put("direction", "S");
            drone.setHeading(Heading.SOUTH);
            drone.setX(x-1);
            drone.setY(y+1);

        } else {
            parameters.put("direction", "W");
            drone.setHeading(Heading.WEST);
            drone.setX(x-1);
            drone.setY(y-1);
        }
        decision.put("parameters", parameters);

        return decision;
    }
}
