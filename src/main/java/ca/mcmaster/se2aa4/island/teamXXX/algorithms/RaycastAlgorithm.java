package ca.mcmaster.se2aa4.island.teamXXX.algorithms;

import ca.mcmaster.se2aa4.island.teamXXX.drone.DroneDecision;
import ca.mcmaster.se2aa4.island.teamXXX.tools.Routine;
import ca.mcmaster.se2aa4.island.teamXXX.tools.RaycastMap;
import ca.mcmaster.se2aa4.island.teamXXX.tools.ActiveRoutines;
import ca.mcmaster.se2aa4.island.teamXXX.enumerations.Heading;
import ca.mcmaster.se2aa4.island.teamXXX.enumerations.State;
import ca.mcmaster.se2aa4.island.teamXXX.drone.DroneAction;
import ca.mcmaster.se2aa4.island.teamXXX.drone.Drone;

import ca.mcmaster.se2aa4.island.teamXXX.actions.EchoLeft;
import ca.mcmaster.se2aa4.island.teamXXX.actions.EchoRight;
import ca.mcmaster.se2aa4.island.teamXXX.actions.EchoForward;
import ca.mcmaster.se2aa4.island.teamXXX.actions.Fly;
import ca.mcmaster.se2aa4.island.teamXXX.actions.HeadingLeft;
import ca.mcmaster.se2aa4.island.teamXXX.actions.HeadingRight;
import ca.mcmaster.se2aa4.island.teamXXX.actions.Scan;
import ca.mcmaster.se2aa4.island.teamXXX.actions.Stop;

import java.util.Queue;
import java.util.List;
import java.util.LinkedList;
import org.json.JSONObject;

public class RaycastAlgorithm {

    private DroneDecision droneDecision;
    private RaycastMap raycastMap;
    private Drone drone;

    private ActiveRoutines activeRoutines = ActiveRoutines.getInstance();

    public RaycastAlgorithm(DroneDecision droneDecision, Drone drone, RaycastMap raycastMap) {

        this.droneDecision = droneDecision;
        this.raycastMap = raycastMap;
        this.drone = drone;
    }

    public boolean raycastBehaviour(JSONObject extras, int cost, String status) {

        boolean switchState = false;

        if (droneDecision.getRoutineName().equals("D")) {

            drone.incrementCornersTravelled();
            droneDecision.setRoutine(activeRoutines.selectRoutine("B"));

        }

        else if (droneDecision.getRoutineName().equals("A")) {

            if (!drone.allCornersTravelled()) {
                
                drone.incrementCornersTravelled();
                droneDecision.setRoutine(activeRoutines.selectRoutine("B"));
            }
            else {

                activeRoutines.clearRoutine();

                raycastMap.calculateScanRegion();
                raycastMap.calculateEntryRow();
                raycastMap.calculateExitRow();

                switchState = true;

            }
        }
        else if (droneDecision.getRoutineName().equals("B")) {

            drone.setSafeTravelDistance(extras.getInt("range") - 3);
            droneDecision.setRoutine(activeRoutines.selectRoutine("C"));
        }
        else if (droneDecision.getRoutineName().equals("C")) {

            int distance_remaining = drone.getSafeTravelDistance();

            if (extras.getString("found").equals("GROUND")) {
                raycastMap.setRaycast(drone.getHeading(), extras.getInt("range"), drone.getX(), drone.getY());
                raycastMap.displayMap();
            }

            // More distance left to travel

            if (distance_remaining > 0) {
                distance_remaining -= 1;
                drone.setSafeTravelDistance(distance_remaining);

                droneDecision.setRoutine(activeRoutines.selectRoutine("C"));
            }
            else {

                // Repeat cornering process
                droneDecision.setRoutine(activeRoutines.selectRoutine("A"));
            }

        }

        return switchState;
    
    }

    public void raycastConstructor() {

        Queue<DroneAction> sequenceA = new LinkedList<DroneAction>();
        sequenceA.add(new HeadingLeft());

        Queue<DroneAction> sequenceB = new LinkedList<DroneAction>();
        sequenceB.add(new EchoForward());

        Queue<DroneAction> sequenceC = new LinkedList<DroneAction>();
        sequenceC.add(new Fly());
        sequenceC.add(new EchoLeft());

        Queue<DroneAction> sequenceD = new LinkedList<DroneAction>();
        sequenceD.add(new Scan());

        Routine routine1 = new Routine("A", sequenceA);
        Routine routine2 = new Routine("B", sequenceB);
        Routine routine3 = new Routine("C", sequenceC);
        Routine routine4 = new Routine("D", sequenceD);

        activeRoutines.addRoutine(routine1);
        activeRoutines.addRoutine(routine2);
        activeRoutines.addRoutine(routine3);
        activeRoutines.addRoutine(routine4);

    }


}