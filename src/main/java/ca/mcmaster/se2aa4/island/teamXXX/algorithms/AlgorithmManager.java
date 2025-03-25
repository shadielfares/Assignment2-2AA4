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

public class AlgorithmManager {


    private Drone drone = Drone.getDroneInstance();
    private DroneDecision droneDecision = DroneDecision.getInstance();
    private DroneResponse response = DroneResponse.getInstance();
    private RaycastMap raycastMap = new RaycastMap();

    private ActiveRoutines activeRoutines = ActiveRoutines.getInstance();

    private final Logger logger = LogManager.getLogger();

    private FindCornerAlgorithm findCornerAlgorithm = new FindCornerAlgorithm(droneDecision, drone);
    private RaycastAlgorithm raycastAlgorithm = new RaycastAlgorithm(droneDecision, drone, raycastMap);
    private SweepAlgorithm sweepAlgorithm = new SweepAlgorithm(droneDecision, drone, raycastMap);

    private State state = State.FIND_CORNER;
    private boolean switchState = true;

    public void nextRoutine() {

        JSONObject jsonResponse = response.getResponse();
        JSONObject extras = jsonResponse.getJSONObject("extras");
        int cost = jsonResponse.getInt("cost");
        String status = jsonResponse.getString("status");


        if (state.equals(State.FIND_CORNER)) {

            if (switchState) {
                findCornerAlgorithm.findCornerConstructor();
                switchState = false;
            }

            boolean switchState = findCornerAlgorithm.findCornerBehavior(extras, cost, status);

            if (switchState) {
                state = State.RAYCAST;
                raycastAlgorithm.raycastConstructor();
                droneDecision.setRoutine(activeRoutines.selectRoutine("D"));
            }
        }
        else if (state.equals(State.RAYCAST)) {

            boolean switchState = raycastAlgorithm.raycastBehaviour(extras, cost, status);

            if (switchState) {
                state = State.SWEEP;
                sweepAlgorithm.sweepConstructor();
                droneDecision.setRoutine(activeRoutines.selectRoutine("A"));
            }

        }
        else if (state.equals(State.SWEEP)) {
            
            boolean switchState = sweepAlgorithm.sweepBehaviour(extras, cost, status);
        }
        
    }
}