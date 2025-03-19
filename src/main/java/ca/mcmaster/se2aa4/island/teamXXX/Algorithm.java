package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class Algorithm {

    public void nextDecision() {

        Heading heading = Heading.SOUTH;

        DroneDecision decision = DroneDecision.getInstance();
        JSONObject jsonDecision = decision.getDecision();

        String action = jsonDecision.getString("action");

        DroneResponse response = DroneResponse.getInstance();
        JSONObject jsonResponse = response.getResponse();

        JSONObject extras = jsonResponse.getJSONObject("extras");
        int cost = jsonResponse.getInt("cost");
        String status = jsonResponse.getString("status");

        if (action.equals("fly")) {

            if (status.equals("OK")) {
                decision.setDecision(new Fly().doAction(heading));
            }

        }

    }

}