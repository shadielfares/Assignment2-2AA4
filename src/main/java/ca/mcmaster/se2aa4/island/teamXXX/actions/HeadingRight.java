package ca.mcmaster.se2aa4.island.teamXXX.actions;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.teamXXX.drone.Drone;
import ca.mcmaster.se2aa4.island.teamXXX.drone.DroneAction;
import ca.mcmaster.se2aa4.island.teamXXX.enumerations.Heading;

public class HeadingRight implements DroneAction {

    @Override

    public JSONObject doAction(Heading heading) {

        JSONObject decision = new JSONObject();
        JSONObject parameters = new JSONObject();
        decision.put("action", "heading");

        Drone drone = Drone.getDroneInstance();
        
        parameters = drone.headingRight(parameters);

        decision.put("parameters", parameters);

        return decision;
    }
}
