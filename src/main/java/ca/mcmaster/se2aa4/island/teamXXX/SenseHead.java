package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class SenseHead implements DroneAction {
    @Override
    public JSONObject doAction(Heading heading) {
        JSONObject Decisione = new JSONObject();
        // Decisione.put("action", "echo");
        return Decisione;
    }
}