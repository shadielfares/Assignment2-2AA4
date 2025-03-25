package ca.mcmaster.se2aa4.island.teamXXX.actions;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.teamXXX.drone.Drone;
import ca.mcmaster.se2aa4.island.teamXXX.drone.DroneAction;
import ca.mcmaster.se2aa4.island.teamXXX.enumerations.Heading;

// Defines fly action
public class Fly implements DroneAction {

    @Override

    // Creates JSONObject for fly action
    public JSONObject doAction(Heading heading) {

        JSONObject decision = new JSONObject();
        decision.put("action", "fly");

        Drone drone = Drone.getDroneInstance();

        drone.fly();

        return decision;
    }
}