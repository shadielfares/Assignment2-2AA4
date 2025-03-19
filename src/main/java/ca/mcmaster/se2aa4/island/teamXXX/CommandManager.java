package ca.mcmaster.se2aa4.island.teamXXX;

import java.io.StringReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.ace_design.island.bot.IExplorerRaid;
import org.json.JSONObject;
import org.json.JSONTokener;

public class CommandManager implements IExplorerRaid {

    private Algorithm algorithm = new Algorithm();

    private final Logger logger = LogManager.getLogger();

    @Override
    public void initialize(String s) {
        logger.info("** Initializing the Exploration Command Center");
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Initialization info:\n {}", info.toString(2));
        String direction = info.getString("heading");
        Integer batteryLevel = info.getInt("budget");
        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);
    }

    @Override
    public String takeDecision() {

        DroneDecision decision = DroneDecision.getInstance();

        JSONObject jsonDecision = decision.getDecision();
        String stringDecision = jsonDecision.toString();

        logger.info("** DroneDecision: {}", stringDecision);
        return stringDecision;
    }

    @Override
    public void acknowledgeResults(String s) {

        DroneResponse response = DroneResponse.getInstance();

        JSONObject jsonResponse = new JSONObject(new JSONTokener(new StringReader(s)));

        response.setResponse(jsonResponse);

        // The errors below are inherint to source code
        logger.info("** DroneResponse received:\n" + jsonResponse.toString(2));
        Integer cost = jsonResponse.getInt("cost");
        logger.info("The cost of the action was {}", cost);
        String status = jsonResponse.getString("status");
        logger.info("The status of the drone is {}", status);
        JSONObject extraInfo = jsonResponse.getJSONObject("extras");
        logger.info("Additional information received: {}", extraInfo);

        algorithm.nextDecision();

    }

    @Override
    public String deliverFinalReport() {
        return "no creek found";
    }

}
