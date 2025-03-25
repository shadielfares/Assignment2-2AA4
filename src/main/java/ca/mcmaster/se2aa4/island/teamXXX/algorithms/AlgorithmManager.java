package ca.mcmaster.se2aa4.island.teamXXX.algorithms;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.teamXXX.drone.Drone;
import ca.mcmaster.se2aa4.island.teamXXX.drone.DroneDecision;
import ca.mcmaster.se2aa4.island.teamXXX.drone.DroneResponse;
import ca.mcmaster.se2aa4.island.teamXXX.enumerations.State;
import ca.mcmaster.se2aa4.island.teamXXX.tools.RaycastMap;
import ca.mcmaster.se2aa4.island.teamXXX.tools.ActiveRoutines;


// Manages switching between the three primary algorithms: finding North-West corner
// creating raycast outline of map, and sweeping the terrain
public class AlgorithmManager {

    private DroneDecision droneDecision = DroneDecision.getInstance();
    private DroneResponse response = DroneResponse.getInstance();
    private RaycastMap raycastMap = new RaycastMap();

    private ActiveRoutines activeRoutines = ActiveRoutines.getInstance();

    private final Logger logger = LogManager.getLogger();

    // Create associations to each of the three algorithms
    private FindCornerAlgorithm findCornerAlgorithm = new FindCornerAlgorithm(droneDecision);
    private RaycastAlgorithm raycastAlgorithm = new RaycastAlgorithm(droneDecision, raycastMap);
    private SweepAlgorithm sweepAlgorithm = new SweepAlgorithm(droneDecision, raycastMap);

    private State state = State.FIND_CORNER;
    private boolean switchState = true;

    // Automatically called once a routine ends
    public void nextRoutine() {

        JSONObject jsonResponse = response.getResponse();
        JSONObject extras = jsonResponse.getJSONObject("extras");
        int cost = jsonResponse.getInt("cost");
        String status = jsonResponse.getString("status");


        // Determines the current state, and runs the appropriate algorithm
        if (state.equals(State.FIND_CORNER)) {

            if (switchState) {

                // Initializes find corner algorithm
                findCornerAlgorithm.findCornerConstructor();
                switchState = false;
            }  

            // Runs find corner algorithm and its associated routines
            boolean switchState = findCornerAlgorithm.findCornerBehavior(extras, cost, status);

            if (switchState) {

                // Initializes raycast algorithm
                state = State.RAYCAST;
                raycastAlgorithm.raycastConstructor();
                droneDecision.setRoutine(activeRoutines.selectRoutine("D"));
            }
        }
        else if (state.equals(State.RAYCAST)) {

            // Runs raycasting algorithm and its associated routines
            boolean switchState = raycastAlgorithm.raycastBehaviour(extras, cost, status);

            if (switchState) {

                // Initializes sweeping algorithm
                state = State.SWEEP;
                sweepAlgorithm.sweepConstructor();
                droneDecision.setRoutine(activeRoutines.selectRoutine("A"));
            }

        }
        else if (state.equals(State.SWEEP)) {
            
            // Runs sweeping algorithm and its associated routines
            boolean switchState = sweepAlgorithm.sweepBehaviour(extras, cost, status);
        }
        
    }
}