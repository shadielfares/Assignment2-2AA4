package ca.mcmaster.se2aa4.island.teamXXX;

import ca.mcmaster.se2aa4.island.teamXXX.algorithms.AlgorithmManager;
import ca.mcmaster.se2aa4.island.teamXXX.drone.Drone;
import ca.mcmaster.se2aa4.island.teamXXX.enumerations.Heading;
import ca.mcmaster.se2aa4.island.teamXXX.drone.DroneDecision;
import ca.mcmaster.se2aa4.island.teamXXX.drone.DroneAction;
import ca.mcmaster.se2aa4.island.teamXXX.drone.DroneResponse;
import ca.mcmaster.se2aa4.island.teamXXX.poi.Creek;
import ca.mcmaster.se2aa4.island.teamXXX.poi.Site;
import ca.mcmaster.se2aa4.island.teamXXX.actions.Stop;
import ca.mcmaster.se2aa4.island.teamXXX.tools.Routine;

import java.io.StringReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import eu.ace_design.island.bot.IExplorerRaid;
import java.util.LinkedList;
import java.util.Queue;


public class Explorer implements IExplorerRaid {

    private AlgorithmManager algorithmManager = new AlgorithmManager();
    private final Logger logger = LogManager.getLogger();
    private Drone drone = Drone.getDroneInstance();
    private String lastAction;


    @Override
    public void initialize(String s) {
        logger.info("** Initializing the Exploration Command Center");
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Initialization info:\n {}", info.toString(2));
        String direction = info.getString("heading");
        
        drone.initializeHeading(direction);

        Integer batteryLevel = info.getInt("budget");
        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);
    }

    @Override
    public String takeDecision() {

        DroneDecision decision = DroneDecision.getInstance();

        JSONObject jsonDecision = decision.getDecision();
        String stringDecision = jsonDecision.toString();
        lastAction = jsonDecision.getString("action");

        logger.info("** DroneDecision: {}", stringDecision);
        return stringDecision;
    }

    @Override
    public void acknowledgeResults(String s) {
        DroneDecision decision = DroneDecision.getInstance();
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

        drone.decreaseBattery(cost);
        drone.displayBattery();
    
        if (lastAction.equals("scan")) {

            JSONArray creeks = extraInfo.getJSONArray("creeks");

            for (int i=0; i < creeks.length(); i++) {
                drone.addCreek(new Creek(drone.getX(), drone.getY(), creeks.getString(i)));
            }

            JSONArray sites = extraInfo.getJSONArray("sites");

            for (int i=0; i < sites.length(); i++) {
                drone.addSite(new Site(drone.getX(), drone.getY(), sites.getString(i)));
            }
        }

        if (drone.batteryDepleted()) {

            Queue<DroneAction> sequenceA = new LinkedList<DroneAction>();
            sequenceA.add(new Stop());

            Routine routine1 = new Routine("A", sequenceA);

            decision.setRoutine(routine1);
        }


        if (decision.isEmpty()) {
            algorithmManager.nextRoutine();
        }

    }

    @Override
    public String deliverFinalReport() {
        
        Drone drone = Drone.getDroneInstance();

        String closestSite = drone.findNearestCreek();

        logger.info(closestSite);

        return closestSite;
    }

}
