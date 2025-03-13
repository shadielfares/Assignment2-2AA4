
package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class Fly implements DroneAction {

    public JSONObject doAction(Heading heading) {

        JSONObject Decisione = new JSONObject();
        Decisione.put("action", "fly");
        return Decisione;

    }

}