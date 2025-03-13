package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class Response {

    private static Response instance = null;

    private JSONObject response;

    public void setResponse(JSONObject response) {
        this.response = response;
    }

    public JSONObject getResponse() {
        return response;
    }

    public static Response getInstance() {

        if (instance == null) {
            instance = new Response();
        }
        return instance;
    }

}