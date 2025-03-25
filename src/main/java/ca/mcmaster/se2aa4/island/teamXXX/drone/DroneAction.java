package ca.mcmaster.se2aa4.island.teamXXX.drone;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.teamXXX.enumerations.Heading;

public interface DroneAction {
    public JSONObject doAction(Heading heading);
}
