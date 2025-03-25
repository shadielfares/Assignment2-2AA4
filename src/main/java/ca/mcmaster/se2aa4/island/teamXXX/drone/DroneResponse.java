package ca.mcmaster.se2aa4.island.teamXXX.drone;

import org.json.JSONObject;

public class DroneResponse {

    private static DroneResponse instance = null;

    private JSONObject response;

    public void setResponse(JSONObject response) {
        this.response = response;
    }

    public JSONObject getResponse() {
        return response;
    }

    public static DroneResponse getInstance() {

        if (instance == null) {
            instance = new DroneResponse();
        }
        return instance;
    }

}