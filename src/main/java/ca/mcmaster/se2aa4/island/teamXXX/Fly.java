package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class Fly implements DroneAction {

    @Override
    public JSONObject doAction(Heading heading) {

        JSONObject decision = new JSONObject();
        decision.put("action", "fly");

        Drone drone = Drone.getDroneInstance();

        int x = drone.getX();
        int y = drone.getY();

        if (heading.equals(Heading.NORTH)) {
            drone.setY(y - 1);
        }
        else if (heading.equals(Heading.EAST)) {
            drone.setX(x + 1);
        }
        else if (heading.equals(Heading.SOUTH)) {
            drone.setY(y + 1);
        }
        else {
            drone.setX(x - 1);
        }

        return decision;
    }
}