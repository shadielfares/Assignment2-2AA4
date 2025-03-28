package ca.mcmaster.se2aa4.island.teamXXX.drone;

import ca.mcmaster.se2aa4.island.teamXXX.actions.Scan;
import ca.mcmaster.se2aa4.island.teamXXX.tools.Routine;

import java.util.Queue;
import java.util.LinkedList;

import org.json.JSONObject;

// Stores the drone's current decision and routine
public class DroneDecision {

    private static DroneDecision instance = null;
    private Routine routine;

    public boolean isEmpty() {
        return routine.routineIsEmpty();
    }

    private DroneDecision() {
    
        Queue<DroneAction> startSequence = new LinkedList<DroneAction>();
        startSequence.add(new Scan());

        Routine initalizeRoutine = new Routine("START", startSequence);
        setRoutine(initalizeRoutine);
    }

    public String getRoutineName() {
        return this.routine.getRoutineName();
    }

    public void setRoutine(Routine routine) {
        this.routine = routine;
    }

    // Retrieves the next decision the drone makes according to its routine
    public JSONObject getDecision() {
        return routine.getRoutineDecision();
    }

    public static DroneDecision getInstance() {

        if (instance == null) {
            instance = new DroneDecision();
        }
        return instance;
    }

}