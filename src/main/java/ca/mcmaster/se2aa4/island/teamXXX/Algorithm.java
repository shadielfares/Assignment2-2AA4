package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.json.JSONObject;

public class Algorithm {
    // Singleton Classes

    private DroneDecision droneDecision = DroneDecision.getInstance();
    private DroneResponse response = DroneResponse.getInstance();
    private boolean initalize = false;
    // Routines currently being ran
    private List<Routine> activeRoutines = new ArrayList<>();
    // Routines A,B,C exist here...

    // Problem is if we put the conditional behaviour inside the routineMethods then
    // we need a seperate unique method for constructing its routines - these will
    // be constant and non-changing.

    // I don't want to call the routineMethod more than once, it doesn't make sense

    // However, we need to figure out where the conditonal behaviour should exist if
    // we don't call it repeatedly.

    // Conditional behaviour tells us which routine to copy into our current routine

    // Current routine is the one being processed by the DroneDecision

    // algorithm.brain()

    // public void brain(State state){
    // if state == "Find Inlet":
    // algorithm.findInlet
    // }

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

        // If state is FIND_INLET, call findInlet, but for now just test findInlet

        // Pass the following parameters in for any new additions
        if (!initalize) {
            initalize = true;
            findInletConstructor();
        }
        findInletBehavior(extras, cost, status);
    }

    public void findInletBehavior(JSONObject extras, int cost, String status) {
        // Here we will be running nextRoutine and iterating over the activeRoutines...

        // Problem: How do we know what the previous name was?

        if (droneDecision.getRoutineName().equals("START")) {
            droneDecision.setRoutine(selectRoutine("A"));

        } else if (droneDecision.getRoutineName().equals("A")) {
            // Want to run B
            if (extras.getString("found").equals("GROUND")) {
                droneDecision.setRoutine(selectRoutine("B"));
            } else {
                droneDecision.setRoutine(selectRoutine("C"));
            }

        } else if (droneDecision.getRoutineName().equals("B")) {
            droneDecision.setRoutine(selectRoutine("A"));

        } else if (droneDecision.getRoutineName().equals("C")) {
            droneDecision.setRoutine(selectRoutine("A"));
        }
    }

    public void findInletConstructor() {
        Queue<DroneAction> sequenceA = new LinkedList<DroneAction>();
        sequenceA.add(new Fly());
        sequenceA.add(new EchoForward());

        Queue<DroneAction> sequenceB = new LinkedList<DroneAction>();
        sequenceB.add(new HeadingLeft());
        sequenceB.add(new Fly());

        Queue<DroneAction> sequenceC = new LinkedList<DroneAction>();
        sequenceC.add(new HeadingRight());
        sequenceC.add(new Fly());

        Routine routine1 = new Routine("A", sequenceA);
        Routine routine2 = new Routine("B", sequenceB);
        Routine routine3 = new Routine("C", sequenceC);

        activeRoutines.add(routine1);
        activeRoutines.add(routine2);
        activeRoutines.add(routine3);
    }

    public void findEmergencySite() {
        // Call it once and it finds it.
    }

}