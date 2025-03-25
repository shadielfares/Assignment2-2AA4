package ca.mcmaster.se2aa4.island.teamXXX.actions;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.teamXXX.drone.DroneAction;
import ca.mcmaster.se2aa4.island.teamXXX.enumerations.Heading;

// Defines drone stop action
public class Stop implements DroneAction {

    @Override

    // Creates JSONObject for stop action
    public JSONObject doAction(Heading heading) {

        JSONObject Decisione = new JSONObject();
        Decisione.put("action", "stop");
        return Decisione;

    }

}