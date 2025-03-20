package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.Queue;
import java.util.LinkedList;

import org.json.JSONObject;

public class DroneDecision {
    private static DroneDecision instance = null;
    private Routine routine;

    public boolean isEmpty() {
        return routine.routineIsEmpty();
    }

    private DroneDecision() {
        // ISSUE TBD
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