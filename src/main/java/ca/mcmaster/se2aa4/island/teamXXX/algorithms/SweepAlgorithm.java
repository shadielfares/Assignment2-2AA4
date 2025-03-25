package ca.mcmaster.se2aa4.island.teamXXX.algorithms;

import ca.mcmaster.se2aa4.island.teamXXX.drone.DroneDecision;
import ca.mcmaster.se2aa4.island.teamXXX.tools.Routine;
import ca.mcmaster.se2aa4.island.teamXXX.tools.RaycastMap;
import ca.mcmaster.se2aa4.island.teamXXX.enumerations.Heading;
import ca.mcmaster.se2aa4.island.teamXXX.tools.ActiveRoutines;
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

// Defines the algorithm for sweeping the island
public class SweepAlgorithm {

    private DroneDecision droneDecision;
    private RaycastMap raycastMap;
    private Drone drone = Drone.getDroneInstance();

    private ActiveRoutines activeRoutines = ActiveRoutines.getInstance();

    public SweepAlgorithm(DroneDecision droneDecision, RaycastMap raycastMap) {

        this.droneDecision = droneDecision;
        this.raycastMap = raycastMap;
    }
    
    // Handles switching routines to sweep the island
    public boolean sweepBehaviour(JSONObject extras, int cost, String status) {

        boolean switchState = false;

        // Fly forward until top of island is reached
        if (droneDecision.getRoutineName().equals("A")) {
             
            if (raycastMap.getEntryRow() - 1 > drone.getY()) { // Check that top of island is reached
                droneDecision.setRoutine(activeRoutines.selectRoutine("A"));
            }
            else {
                droneDecision.setRoutine(activeRoutines.selectRoutine("B"));
            }

        }
        // Turn left to begin sweeping
        else if (droneDecision.getRoutineName().equals("B")) {
            droneDecision.setRoutine(activeRoutines.selectRoutine("C"));
        }
        // Travel East until island reached
        else if (droneDecision.getRoutineName().equals("C")) {
            
            if (drone.getX() < raycastMap.getMin(drone.getY())) { // Check when island is reached 
                droneDecision.setRoutine(activeRoutines.selectRoutine("C"));
            }
            else {
                droneDecision.setRoutine(activeRoutines.selectRoutine("D"));
            }
        }

        // Sweep rightward until end of island reached
        else if (droneDecision.getRoutineName().equals("D")) {


            if (drone.getY() <= raycastMap.getExitRow() - 1) { // Check when right-most part of island is reached
                if (drone.getX() < Math.max(raycastMap.getMax(drone.getY() + 2) + 1, raycastMap.getMax(drone.getY()) + 1)) {
                    droneDecision.setRoutine(activeRoutines.selectRoutine("D"));
                }
                else {
                    droneDecision.setRoutine(activeRoutines.selectRoutine("E"));
                }
            }
            else {

                // Complete sweep
                droneDecision.setRoutine(activeRoutines.selectRoutine("I"));
            }

        }
        // Turn right twice and sweep leftward until beginning of island reached
        else if (droneDecision.getRoutineName().equals("E")) {

            droneDecision.setRoutine(activeRoutines.selectRoutine("F"));

        }
        // Sweep leftward until beginning of island is reached
        else if (droneDecision.getRoutineName().equals("F")) {

            if (drone.getY() <= raycastMap.getExitRow() - 1) { // Check when left-most part of island is reached
                if (drone.getX() > Math.min(raycastMap.getMin(drone.getY() + 2) - 1, raycastMap.getMin(drone.getY()) - 1)) {
                    droneDecision.setRoutine(activeRoutines.selectRoutine("F"));
                }
                else {
                    droneDecision.setRoutine(activeRoutines.selectRoutine("G"));
                }
            }
            else {
                droneDecision.setRoutine(activeRoutines.selectRoutine("H"));
            }

        }
        // Turn left twice and resume sweeping rightward
        else if (droneDecision.getRoutineName().equals("G")) {

            droneDecision.setRoutine(activeRoutines.selectRoutine("D"));
        }
        // Once first scan of island is completed, begin second scan rightward
        else if (droneDecision.getRoutineName().equals("H")) {

            droneDecision.setRoutine(activeRoutines.selectRoutine("J"));

        } // Once first scan of island is completed, begin second scan leftward
        else if (droneDecision.getRoutineName().equals("I")) {

            droneDecision.setRoutine(activeRoutines.selectRoutine("K"));

        }
        // Sweep rightward until end of island reached
        else if (droneDecision.getRoutineName().equals("J")) {

            if (drone.getY() > raycastMap.getEntryRow()) { // Check when right-most part of island is reached
                if (drone.getX() < Math.max(raycastMap.getMax(drone.getY() + 2) + 1, raycastMap.getMax(drone.getY()) + 1)) {
                    droneDecision.setRoutine(activeRoutines.selectRoutine("J"));
                }
                else {
                    droneDecision.setRoutine(activeRoutines.selectRoutine("L"));
                }
            }
            else {

                // Complete sweep
                droneDecision.setRoutine(activeRoutines.selectRoutine("N"));
            }

        }
        // Sweep leftward until beginning of island reached
        else if (droneDecision.getRoutineName().equals("K")) {

            if (drone.getY() > raycastMap.getEntryRow()) { // Check when left-most part of island is reached
                if (drone.getX() > Math.min(raycastMap.getMin(drone.getY() + 2) - 1, raycastMap.getMin(drone.getY()) - 1)) {
                    droneDecision.setRoutine(activeRoutines.selectRoutine("K"));
                }
                else {
                    droneDecision.setRoutine(activeRoutines.selectRoutine("M"));
                }
            }
            else {

                // Complete sweep
                droneDecision.setRoutine(activeRoutines.selectRoutine("N"));
            }
            
        } // Turn left twice once beginning of island is reached
        else if (droneDecision.getRoutineName().equals("L")) {

            droneDecision.setRoutine(activeRoutines.selectRoutine("K"));
            
        } // Turn right twice once end of island is reached
        else if (droneDecision.getRoutineName().equals("M")) {

            droneDecision.setRoutine(activeRoutines.selectRoutine("J"));

        } // Finished sweeping, terminate algorithm
        else if (droneDecision.getRoutineName().equals("N")) {

            switchState = true;
    
        }

        return switchState;
    
    }

    // Define routines for the sweeping algorithm
    public void sweepConstructor() {

        // Defines sequences of drone actions that compose each routine
        Queue<DroneAction> sequenceA = new LinkedList<DroneAction>();
        sequenceA.add(new Fly());

        Queue<DroneAction> sequenceB = new LinkedList<DroneAction>();
        sequenceB.add(new HeadingLeft());

        Queue<DroneAction> sequenceC = new LinkedList<DroneAction>();
        sequenceC.add(new Fly());

        Queue<DroneAction> sequenceD = new LinkedList<DroneAction>();
        sequenceD.add(new Scan());
        sequenceD.add(new Fly());

        Queue<DroneAction> sequenceE = new LinkedList<DroneAction>();
        sequenceE.add(new HeadingRight());
        sequenceE.add(new HeadingRight());

        Queue<DroneAction> sequenceF = new LinkedList<DroneAction>();
        sequenceF.add(new Scan());
        sequenceF.add(new Fly());

        Queue<DroneAction> sequenceG = new LinkedList<DroneAction>();
        sequenceG.add(new HeadingLeft());
        sequenceG.add(new HeadingLeft());

        Queue<DroneAction> sequenceH = new LinkedList<DroneAction>();
        sequenceH.add(new HeadingLeft());
        sequenceH.add(new HeadingLeft());
        sequenceH.add(new HeadingLeft());
        sequenceH.add(new Fly());
        sequenceH.add(new HeadingRight());

        Queue<DroneAction> sequenceI = new LinkedList<DroneAction>();
        sequenceI.add(new HeadingRight());
        sequenceI.add(new HeadingRight());
        sequenceI.add(new HeadingRight());
        sequenceI.add(new Fly());
        sequenceI.add(new HeadingLeft());

        Queue<DroneAction> sequenceJ = new LinkedList<DroneAction>();
        sequenceJ.add(new Scan());
        sequenceJ.add(new Fly());

        Queue<DroneAction> sequenceK = new LinkedList<DroneAction>();
        sequenceK.add(new Scan());
        sequenceK.add(new Fly());

        Queue<DroneAction> sequenceL = new LinkedList<DroneAction>();
        sequenceL.add(new HeadingLeft());
        sequenceL.add(new HeadingLeft());

        Queue<DroneAction> sequenceM = new LinkedList<DroneAction>();
        sequenceM.add(new HeadingRight());
        sequenceM.add(new HeadingRight());

        Queue<DroneAction> sequenceN = new LinkedList<DroneAction>();
        sequenceN.add(new Stop());

        // Creating each routine in the algorithm
        Routine routine1 = new Routine("A", sequenceA);
        Routine routine2 = new Routine("B", sequenceB);
        Routine routine3 = new Routine("C", sequenceC);
        Routine routine4 = new Routine("D", sequenceD);
        Routine routine5 = new Routine("E", sequenceE);
        Routine routine6 = new Routine("F", sequenceF);
        Routine routine7 = new Routine("G", sequenceG);
        Routine routine8 = new Routine("H", sequenceH);
        Routine routine9 = new Routine("I", sequenceI);
        Routine routine10 = new Routine("J", sequenceJ);
        Routine routine11 = new Routine("K", sequenceK);
        Routine routine12 = new Routine("L", sequenceL);
        Routine routine13 = new Routine("M", sequenceM);
        Routine routine14 = new Routine("N", sequenceN);

        // Adding each routine to the active list of routines
        activeRoutines.addRoutine(routine1);
        activeRoutines.addRoutine(routine2);
        activeRoutines.addRoutine(routine3);
        activeRoutines.addRoutine(routine4);
        activeRoutines.addRoutine(routine5);
        activeRoutines.addRoutine(routine6);
        activeRoutines.addRoutine(routine7);
        activeRoutines.addRoutine(routine8);
        activeRoutines.addRoutine(routine9);
        activeRoutines.addRoutine(routine10);
        activeRoutines.addRoutine(routine11);
        activeRoutines.addRoutine(routine12);
        activeRoutines.addRoutine(routine13);
        activeRoutines.addRoutine(routine14);


    }

}