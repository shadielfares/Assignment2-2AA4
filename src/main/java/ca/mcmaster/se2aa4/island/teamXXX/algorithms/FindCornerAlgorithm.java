package ca.mcmaster.se2aa4.island.teamXXX.algorithms;

import ca.mcmaster.se2aa4.island.teamXXX.drone.DroneDecision;
import ca.mcmaster.se2aa4.island.teamXXX.tools.Routine;
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

// Defines the algorithm for locating the North-West corner
public class FindCornerAlgorithm {

    private DroneDecision droneDecision;
    private Drone drone = Drone.getDroneInstance();

    private ActiveRoutines activeRoutines = ActiveRoutines.getInstance();

    public FindCornerAlgorithm(DroneDecision droneDecision) {

        this.droneDecision = droneDecision;
    }

    // Handles switching routines to find the North-West corner
    public boolean findCornerBehavior(JSONObject extras, int cost, String status) {

        boolean switchState = false;
        
        // Start State
        if (droneDecision.getRoutineName().equals("START")) { 

            droneDecision.setRoutine(activeRoutines.selectRoutine("I")); 
        }   

        // Try turning left if space is available, otherwise turn right
        else if(droneDecision.getRoutineName().equals("I")) {

            if (extras.getInt("range") > 1) { // Check if there is space to turn left

                droneDecision.setRoutine(activeRoutines.selectRoutine("A")); 
            }
            else {
                droneDecision.setRoutine(activeRoutines.selectRoutine("J")); 
            }
            
        }

        // After turning right, continue turning right until facing North
        // or echo left if no space to turn
        else if (droneDecision.getRoutineName().equals("J")) {

            if (extras.getInt("range") > 1) { // Check if there is space to turn right

                if (!drone.getHeading().equals(Heading.NORTH)) { // Check if drone is facing North

                    droneDecision.setRoutine(activeRoutines.selectRoutine("J"));
                }
                else {
                    droneDecision.setRoutine(activeRoutines.selectRoutine("K"));

                }
            }
            else { // Terminates Algorithm
                drone.placeOnRaycastMap();
                activeRoutines.clearRoutine();
                switchState = true;
            }
            
        } 

        // After echoing left, turn left if there is space, otherwise face South and terminate algorithm
        else if (droneDecision.getRoutineName().equals("K")) { 

            if (extras.getInt("range") > 1) { // Check if there is space to turn left

                droneDecision.setRoutine(activeRoutines.selectRoutine("E")); 
            }
            else {
                droneDecision.setRoutine(activeRoutines.selectRoutine("L")); 
            }

        }

        // Now that drone faces South, terminate algorithm
        else if (droneDecision.getRoutineName().equals("L")) { 

            // Terminates Algorithm
            drone.placeOnRaycastMap();
            activeRoutines.clearRoutine();
            switchState = true;

        }

        // Continue turning left until facing North, or begin turning right if no space
        else if (droneDecision.getRoutineName().equals("A")) {

            if (extras.getInt("range") > 1) { // Check if there is space to turn left

                if (!drone.getHeading().equals(Heading.NORTH)) { // Check if drone is facing North

                    droneDecision.setRoutine(activeRoutines.selectRoutine("A")); 
                }
                else {
                    droneDecision.setRoutine(activeRoutines.selectRoutine("B")); 

                    }
            }
            else {
                droneDecision.setRoutine(activeRoutines.selectRoutine("J")); 
            }

        }

        // Look forward and assess how long to fly North until obstacle reached
        else if (droneDecision.getRoutineName().equals("B")) { 

            if (extras.getString("found").equals("GROUND")) { // Checks if drone is facing ground 

                drone.setSafeTravelDistance(extras.getInt("range") + 1);   

                droneDecision.setRoutine(activeRoutines.selectRoutine("C")); 
            }

            else if (extras.getString("found").equals("OUT_OF_RANGE")) { // Checks if drone is facing out of range

                if (extras.getInt("range") > 1) { // Checks if drone has space to fly forward

                    drone.setSafeTravelDistance(extras.getInt("range") - 1);

                    droneDecision.setRoutine(activeRoutines.selectRoutine("C")); 
                }
                else {

                    droneDecision.setRoutine(activeRoutines.selectRoutine("D")); 
                }
            }

        }
        // Fly North until obstacle reached
        else if (droneDecision.getRoutineName().equals("C")) { 

            int distance_remaining = drone.getSafeTravelDistance();

            if (distance_remaining > 1) { // Continues travelling North until obstacle encountered
                distance_remaining -= 1;

                drone.setSafeTravelDistance(distance_remaining);

                droneDecision.setRoutine(activeRoutines.selectRoutine("C")); 
            }
            else {

                droneDecision.setRoutine(activeRoutines.selectRoutine("B"));
            }
        }
        // Turn left if there is space, otherwise turn right
        else if (droneDecision.getRoutineName().equals("D")) { 

            if (extras.getInt("range") > 1) { // Checks if drone can turn left
                droneDecision.setRoutine(activeRoutines.selectRoutine("E"));
            }
            else {
                droneDecision.setRoutine(activeRoutines.selectRoutine("H"));
            }
        }
        // Now facing West, drone has fully travelled North
        else if (droneDecision.getRoutineName().equals("E")) { 

            droneDecision.setRoutine(activeRoutines.selectRoutine("F")); 
        }

        // Look forward and assess how long to fly West until obstacle reached
        else if (droneDecision.getRoutineName().equals("F")) { 

            if (extras.getString("found").equals("GROUND")) { // Check if drone is facing ground

                drone.setSafeTravelDistance(extras.getInt("range") + 1);

                droneDecision.setRoutine(activeRoutines.selectRoutine("G")); 
            }
            else if (extras.getString("found").equals("OUT_OF_RANGE")) { // Check if drone is facing out of range

                if (extras.getInt("range") > 1) { // Check if drone can move forward
                    drone.setSafeTravelDistance(extras.getInt("range") - 1);

                    droneDecision.setRoutine(activeRoutines.selectRoutine("G")); 
                }
                else {

                    droneDecision.setRoutine(activeRoutines.selectRoutine("M")); 
                    
                }
            }
        }
        // Face South and terminate algorithm
        else if (droneDecision.getRoutineName().equals("M")) { 

            // Terminates Algorithm
            drone.placeOnRaycastMap();
            activeRoutines.clearRoutine();
            switchState = true;
        }
        // Fly West until obstacle reached
        else if (droneDecision.getRoutineName().equals("G")) {

            int distance_remaining = drone.getSafeTravelDistance();

            if (distance_remaining > 0) { // Continues forward until obstacle reached
                distance_remaining -= 1;

                drone.setSafeTravelDistance(distance_remaining);

                if (distance_remaining != 0) {
                    droneDecision.setRoutine(activeRoutines.selectRoutine("G"));
                }
                else {
                    droneDecision.setRoutine(activeRoutines.selectRoutine("F")); 
                }
                
            }
            else {

                droneDecision.setRoutine(activeRoutines.selectRoutine("F")); 
            }
        }
        // Turn right to face West
        else if (droneDecision.getRoutineName().equals("H")) { 

            droneDecision.setRoutine(activeRoutines.selectRoutine("F"));
        }

        return switchState;

    }
    // Define routines for the find corner algorithm    
    public void findCornerConstructor() { 

        // Defines sequences of drone actions that compose each routine
        Queue<DroneAction> sequenceA = new LinkedList<DroneAction>();
        sequenceA.add(new HeadingLeft());
        sequenceA.add(new EchoLeft());

        Queue<DroneAction> sequenceB = new LinkedList<DroneAction>();
        sequenceB.add(new EchoForward());

        Queue<DroneAction> sequenceC = new LinkedList<DroneAction>();
        sequenceC.add(new Fly());

        Queue<DroneAction> sequenceD = new LinkedList<DroneAction>();
        sequenceD.add(new EchoLeft());

        Queue<DroneAction> sequenceE = new LinkedList<DroneAction>();
        sequenceE.add(new HeadingLeft());

        Queue<DroneAction> sequenceF = new LinkedList<DroneAction>();
        sequenceF.add(new EchoForward());

        Queue<DroneAction> sequenceG = new LinkedList<DroneAction>();
        sequenceG.add(new Fly());

        Queue<DroneAction> sequenceH = new LinkedList<DroneAction>();
        sequenceH.add(new HeadingRight());
        sequenceH.add(new HeadingRight());

        Queue<DroneAction> sequenceI = new LinkedList<DroneAction>();
        sequenceI.add(new EchoLeft());

        Queue<DroneAction> sequenceJ = new LinkedList<DroneAction>();
        sequenceJ.add(new HeadingRight());
        sequenceJ.add(new EchoRight());

        Queue<DroneAction> sequenceK = new LinkedList<DroneAction>();
        sequenceK.add(new EchoLeft());

        Queue<DroneAction> sequenceL = new LinkedList<DroneAction>();
        sequenceL.add(new HeadingRight());
        sequenceL.add(new Fly());
        sequenceL.add(new HeadingRight());
        sequenceL.add(new HeadingRight());
        sequenceL.add(new HeadingLeft());

        Queue<DroneAction> sequenceM = new LinkedList<DroneAction>();
        sequenceM.add(new HeadingLeft());

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
       
    }
    
}