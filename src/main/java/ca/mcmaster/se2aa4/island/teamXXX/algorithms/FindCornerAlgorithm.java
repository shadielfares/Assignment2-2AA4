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

public class FindCornerAlgorithm {

    private DroneDecision droneDecision;
    private Drone drone;

    private ActiveRoutines activeRoutines = ActiveRoutines.getInstance();

    public FindCornerAlgorithm(DroneDecision droneDecision, Drone drone) {

        this.droneDecision = droneDecision;
        this.drone = drone;
    }

    public boolean findCornerBehavior(JSONObject extras, int cost, String status) {

        boolean switchState = false;
        
        if (droneDecision.getRoutineName().equals("START")) { // Start State

            droneDecision.setRoutine(activeRoutines.selectRoutine("I"));
        }   
        else if(droneDecision.getRoutineName().equals("I")) { // EchoLeft 

            if (extras.getInt("range") > 1) {
                droneDecision.setRoutine(activeRoutines.selectRoutine("A")); // HeadingLeft 
            }
            else {
                droneDecision.setRoutine(activeRoutines.selectRoutine("J")); // HeadingRight
            }
            
        }
        else if (droneDecision.getRoutineName().equals("J")) {

            if (extras.getInt("range") > 1) {
                if (!drone.getHeading().equals(Heading.NORTH)) {
                droneDecision.setRoutine(activeRoutines.selectRoutine("J"));
                }
                else {
                    droneDecision.setRoutine(activeRoutines.selectRoutine("K"));

                }
            }
            else {
                droneDecision.setRoutine(activeRoutines.selectRoutine("N"));
            }
            
        }
        else if (droneDecision.getRoutineName().equals("N")) {

            drone.placeOnRaycastMap();
            activeRoutines.clearRoutine();
            switchState = true;

        }
        else if (droneDecision.getRoutineName().equals("K")) { //Echo left

            if (extras.getInt("range") > 1) {
                droneDecision.setRoutine(activeRoutines.selectRoutine("E"));
            }
            else {
                droneDecision.setRoutine(activeRoutines.selectRoutine("L"));
            }

        }

        else if (droneDecision.getRoutineName().equals("L")) {
            
            drone.placeOnRaycastMap();
            activeRoutines.clearRoutine();
            switchState = true;

        }
        else if (droneDecision.getRoutineName().equals("A")) {

            if (extras.getInt("range") > 1) {
                if (!drone.getHeading().equals(Heading.NORTH)) {
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

        // Drone scan north
        else if (droneDecision.getRoutineName().equals("B")) {

            if (extras.getString("found").equals("GROUND")) {

                drone.setSafeTravelDistance(extras.getInt("range") + 1);

                droneDecision.setRoutine(activeRoutines.selectRoutine("C"));
            }

            else if (extras.getString("found").equals("OUT_OF_RANGE")) {

                if (extras.getInt("range") > 1) {
                    drone.setSafeTravelDistance(extras.getInt("range") - 1);

                    droneDecision.setRoutine(activeRoutines.selectRoutine("C"));
                }
                else {

                    // Finished travelling North
                    droneDecision.setRoutine(activeRoutines.selectRoutine("D"));
                }
            }

        }

        // Drone travel north
        else if (droneDecision.getRoutineName().equals("C")) {

            int distance_remaining = drone.getSafeTravelDistance();

            // More distance left to travel
            if (distance_remaining > 1) {
                distance_remaining -= 1;

                drone.setSafeTravelDistance(distance_remaining);

                droneDecision.setRoutine(activeRoutines.selectRoutine("C"));
            }
            else {

                // Repeat echoing process
                droneDecision.setRoutine(activeRoutines.selectRoutine("B"));
            }
        }

        else if (droneDecision.getRoutineName().equals("D")) {

            if (extras.getInt("range") > 1) {
                droneDecision.setRoutine(activeRoutines.selectRoutine("E"));
            }
            else {
                droneDecision.setRoutine(activeRoutines.selectRoutine("H"));
            }
        }

        else if (droneDecision.getRoutineName().equals("E")) {

            droneDecision.setRoutine(activeRoutines.selectRoutine("F"));
        }

        // Drone scan west
        else if (droneDecision.getRoutineName().equals("F")) {

            if (extras.getString("found").equals("GROUND")) {

                drone.setSafeTravelDistance(extras.getInt("range") + 1);

                droneDecision.setRoutine(activeRoutines.selectRoutine("G"));
            }
            else if (extras.getString("found").equals("OUT_OF_RANGE")) {

                if (extras.getInt("range") > 1) {
                    drone.setSafeTravelDistance(extras.getInt("range") - 1);

                    droneDecision.setRoutine(activeRoutines.selectRoutine("G"));
                }
                else {

                    droneDecision.setRoutine(activeRoutines.selectRoutine("M"));
                    
                }
            }
        }

        else if (droneDecision.getRoutineName().equals("M")) {

            drone.placeOnRaycastMap();
                    
            activeRoutines.clearRoutine();

            switchState = true;
        }

        // Drone travel west
        else if (droneDecision.getRoutineName().equals("G")) {

            int distance_remaining = drone.getSafeTravelDistance();

            // More distance left to travel
            if (distance_remaining > 0) {
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

                // Repeat echoing process
                droneDecision.setRoutine(activeRoutines.selectRoutine("F"));
            }
        }

        else if (droneDecision.getRoutineName().equals("H")) {
            droneDecision.setRoutine(activeRoutines.selectRoutine("F"));
        }

        return switchState;

    }

    public void findCornerConstructor() {

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

        Queue<DroneAction> sequenceN = new LinkedList<DroneAction>();
        sequenceN.add(new Scan());


        // Routine A: Sets Heading Left
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