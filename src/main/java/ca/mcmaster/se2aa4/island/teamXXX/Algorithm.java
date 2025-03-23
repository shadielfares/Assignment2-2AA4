package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.json.JSONObject;
import org.junit.rules.Stopwatch;

public class Algorithm {
    // Singleton Classes

    private Drone drone = Drone.getDroneInstance();
    private DroneDecision droneDecision = DroneDecision.getInstance();
    private DroneResponse response = DroneResponse.getInstance();
    private final Logger logger = LogManager.getLogger();

    private State state = State.FIND_CORNER;
    private boolean switchedStates = true;
    private RaycastMap raycastMap = new RaycastMap();


    // Routines currently being ran

    private List<Routine> activeRoutines = new ArrayList<>();

    public Routine selectRoutine(String routineName) {

        Routine fakeRoutine = new Routine("ERROR", new LinkedList<DroneAction>());

        for (Routine routine : activeRoutines) {
            if (routine.getRoutineName().equals(routineName)) {
                return routine.clone();
            }
        }

        // Include the return of an error
        return fakeRoutine;
    }

    public void nextRoutine() {
        JSONObject jsonResponse = response.getResponse();

        JSONObject extras = jsonResponse.getJSONObject("extras");
        int cost = jsonResponse.getInt("cost");
        String status = jsonResponse.getString("status");


        if (state.equals(State.FIND_CORNER)) {

            if (switchedStates) {
                findCornerConstructor();
                switchedStates = false;
            }

            findCornerBehavior(extras, cost, status);
        }
        else if (state.equals(State.RAYCAST)) {

            raycastBehaviour(extras, cost, status);

        }
        else if (state.equals(State.SWEEP)) {
            
            sweepBehaviour(extras, cost, status);
        }
        
    }

    public void findCornerBehavior(JSONObject extras, int cost, String status) {

        if (droneDecision.getRoutineName().equals("START")) {

            if (!drone.getHeading().equals(Heading.NORTH)) {
                droneDecision.setRoutine(selectRoutine("A"));
            }
            else {
                droneDecision.setRoutine(selectRoutine("B"));

            }
        }
        
        // Note: See if we can reduce duplication... since start state does nothing, leads to some redundancy
        else if (droneDecision.getRoutineName().equals("A")) {

            if (!drone.getHeading().equals(Heading.NORTH)) {
                droneDecision.setRoutine(selectRoutine("A"));
            }
            else {
                droneDecision.setRoutine(selectRoutine("B"));

            }
        }

        // Drone scan north
        else if (droneDecision.getRoutineName().equals("B")) {

            if (extras.getString("found").equals("GROUND")) {

                drone.setSafeTravelDistance(extras.getInt("range") + 1);

                droneDecision.setRoutine(selectRoutine("C"));
            }

            else if (extras.getString("found").equals("OUT_OF_RANGE")) {

                if (extras.getInt("range") > 1) {
                    drone.setSafeTravelDistance(extras.getInt("range") - 1);

                    droneDecision.setRoutine(selectRoutine("C"));
                }
                else {

                    // Finished travelling North
                    droneDecision.setRoutine(selectRoutine("D"));
                }
            }

        }

        // Drone travel north
        else if (droneDecision.getRoutineName().equals("C")) {

            int distance_remaining = drone.getSafeTravelDistance();

            // More distance left to travel
            if (distance_remaining > 0) {
                distance_remaining -= 1;
                drone.setSafeTravelDistance(distance_remaining);

                droneDecision.setRoutine(selectRoutine("C"));
            }
            else {

                // Repeat echoing process
                droneDecision.setRoutine(selectRoutine("B"));
            }
        }

        else if (droneDecision.getRoutineName().equals("D")) {

            if (extras.getInt("range") > 1) {
                droneDecision.setRoutine(selectRoutine("E"));
            }
            else {
                // EDGE CASE WHERE DRONE DOESN'T HAVE ENOUGH SPACE TO TURN LEFT @ TOP LEFT CORNER
            }
        }

        else if (droneDecision.getRoutineName().equals("E")) {

            droneDecision.setRoutine(selectRoutine("F"));
        }

        // Drone scan west
        else if (droneDecision.getRoutineName().equals("F")) {

            if (extras.getString("found").equals("GROUND")) {

                drone.setSafeTravelDistance(extras.getInt("range") + 1);

                droneDecision.setRoutine(selectRoutine("G"));
            }
            else if (extras.getString("found").equals("OUT_OF_RANGE")) {

                if (extras.getInt("range") > 1) {
                    drone.setSafeTravelDistance(extras.getInt("range") - 1);

                    droneDecision.setRoutine(selectRoutine("G"));
                }
                else {

                    // Finished travelling West
                    drone.setInPosition();
                    
                    activeRoutines.clear();
                    raycastConstructor();

                    state = State.RAYCAST;
                    droneDecision.setRoutine(selectRoutine("A"));
                }
            }

        }

        // Drone travel west
        else if (droneDecision.getRoutineName().equals("G")) {

            int distance_remaining = drone.getSafeTravelDistance();

            // More distance left to travel
            if (distance_remaining > 0) {
                distance_remaining -= 1;
                drone.setSafeTravelDistance(distance_remaining);

                if (distance_remaining != 0) {
                    droneDecision.setRoutine(selectRoutine("G"));
                }
                else {
                    droneDecision.setRoutine(selectRoutine("F"));
                }
                
            }
            else {

                // Repeat echoing process
                droneDecision.setRoutine(selectRoutine("F"));
            }
        }

    }

    public void findCornerConstructor() {

        Queue<DroneAction> sequenceA = new LinkedList<DroneAction>();
        sequenceA.add(new HeadingLeft());

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

        Routine routine1 = new Routine("A", sequenceA);
        Routine routine2 = new Routine("B", sequenceB);
        Routine routine3 = new Routine("C", sequenceC);
        Routine routine4 = new Routine("D", sequenceD);
        Routine routine5 = new Routine("E", sequenceE);
        Routine routine6 = new Routine("F", sequenceF);
        Routine routine7 = new Routine("G", sequenceG);

        
        activeRoutines.add(routine1);
        activeRoutines.add(routine2);
        activeRoutines.add(routine3);
        activeRoutines.add(routine4);
        activeRoutines.add(routine5);
        activeRoutines.add(routine6);
        activeRoutines.add(routine7);
       
    }

    public void raycastBehaviour(JSONObject extras, int cost, String status) {


        if (droneDecision.getRoutineName().equals("A")) {

            int cornersTravelled = drone.getCornersTravelled();

            if (cornersTravelled < 4) {
                drone.setCornersTravelled(cornersTravelled + 1);
                droneDecision.setRoutine(selectRoutine("B"));
            }
            else {

                activeRoutines.clear();
                sweepConstructor();

                raycastMap.calculateRowEnds();
                raycastMap.calculateEntryRow();
                raycastMap.calculateExitRow();

                state = State.SWEEP;
                droneDecision.setRoutine(selectRoutine("A"));

            }
        }
        else if (droneDecision.getRoutineName().equals("B")) {

            drone.setSafeTravelDistance(extras.getInt("range") - 1);
            droneDecision.setRoutine(selectRoutine("C"));
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

                droneDecision.setRoutine(selectRoutine("C"));
            }
            else {

                // Repeat cornering process
                droneDecision.setRoutine(selectRoutine("A"));
            }

        }
    
    }

    public void raycastConstructor() {

        Queue<DroneAction> sequenceA = new LinkedList<DroneAction>();
        sequenceA.add(new HeadingLeft());

        Queue<DroneAction> sequenceB = new LinkedList<DroneAction>();
        sequenceB.add(new EchoForward());

        Queue<DroneAction> sequenceC = new LinkedList<DroneAction>();
        sequenceC.add(new Fly());
        sequenceC.add(new EchoLeft());

        Routine routine1 = new Routine("A", sequenceA);
        Routine routine2 = new Routine("B", sequenceB);
        Routine routine3 = new Routine("C", sequenceC);


        activeRoutines.add(routine1);
        activeRoutines.add(routine2);
        activeRoutines.add(routine3);

    }

    public void sweepBehaviour(JSONObject extras, int cost, String status) {

        if (droneDecision.getRoutineName().equals("A")) {
            
            if (raycastMap.getEntryRow() - 1 > drone.getY()) {
                droneDecision.setRoutine(selectRoutine("A"));
            }
            else {
                droneDecision.setRoutine(selectRoutine("B"));
            }

        }
        else if (droneDecision.getRoutineName().equals("B")) {
            droneDecision.setRoutine(selectRoutine("C"));
        }
        else if (droneDecision.getRoutineName().equals("C")) {
            
            if (drone.getX() < raycastMap.getMin(drone.getY())) {
                droneDecision.setRoutine(selectRoutine("C"));
            }
            else {
                droneDecision.setRoutine(selectRoutine("D"));
            }
        }
        else if (droneDecision.getRoutineName().equals("D")) {


            if (drone.getY() <= raycastMap.getExitRow()) {
                if (drone.getX() < raycastMap.getMax(drone.getY()) + 1) {
                    droneDecision.setRoutine(selectRoutine("D"));
                }
                else {
                    droneDecision.setRoutine(selectRoutine("E"));
                }
            }
            else {

                // Complete sweep
                droneDecision.setRoutine(selectRoutine("H"));
            }

        }
        else if (droneDecision.getRoutineName().equals("E")) {

            droneDecision.setRoutine(selectRoutine("F"));

        }
        else if (droneDecision.getRoutineName().equals("F")) {

            if (drone.getY() <= raycastMap.getExitRow()) {
                if (drone.getX() > raycastMap.getMin(drone.getY()) - 1) {
                    droneDecision.setRoutine(selectRoutine("F"));
                }
                else {
                    droneDecision.setRoutine(selectRoutine("G"));
                }
            }
            else {

                // Complete sweep
                droneDecision.setRoutine(selectRoutine("H"));
            }

        }
        else if (droneDecision.getRoutineName().equals("G")) {

            droneDecision.setRoutine(selectRoutine("D"));
        }
        else if (droneDecision.getRoutineName().equals("H")) {

            logger.info("COMPLETE :D");
        }
    
    }

    public void sweepConstructor() {

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
        sequenceE.add(new HeadingLeft());
        sequenceE.add(new HeadingRight());
        sequenceE.add(new HeadingRight());
        sequenceE.add(new Fly());
        sequenceE.add(new HeadingRight());
        sequenceE.add(new Fly());
        sequenceE.add(new Fly());

        Queue<DroneAction> sequenceF = new LinkedList<DroneAction>();
        sequenceF.add(new Scan());
        sequenceF.add(new Fly());

        Queue<DroneAction> sequenceG = new LinkedList<DroneAction>();
        sequenceG.add(new HeadingRight());
        sequenceG.add(new HeadingLeft());
        sequenceG.add(new HeadingLeft());
        sequenceG.add(new Fly());
        sequenceG.add(new HeadingLeft());
        sequenceG.add(new Fly());
        sequenceG.add(new Fly());

        Queue<DroneAction> sequenceH = new LinkedList<DroneAction>();
        sequenceH.add(new Stop());

        Routine routine1 = new Routine("A", sequenceA);
        Routine routine2 = new Routine("B", sequenceB);
        Routine routine3 = new Routine("C", sequenceC);
        Routine routine4 = new Routine("D", sequenceD);
        Routine routine5 = new Routine("E", sequenceE);
        Routine routine6 = new Routine("F", sequenceF);
        Routine routine7 = new Routine("G", sequenceG);
        Routine routine8 = new Routine("H", sequenceH);

        activeRoutines.add(routine1);
        activeRoutines.add(routine2);
        activeRoutines.add(routine3);
        activeRoutines.add(routine4);
        activeRoutines.add(routine5);
        activeRoutines.add(routine6);
        activeRoutines.add(routine7);
        activeRoutines.add(routine8);

    }
}