package ca.mcmaster.se2aa4.island.teamXXX.tools;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

import ca.mcmaster.se2aa4.island.teamXXX.drone.DroneAction;

// Stores the current list of active routines each algorithm has access to
public class ActiveRoutines {

    private static ActiveRoutines instance = null;
    private List<Routine> activeRoutines;

    private ActiveRoutines() {
        this.activeRoutines = new ArrayList<>();
    }

    public static ActiveRoutines getInstance() {
        if (instance == null) {
            instance = new ActiveRoutines();
        }
        return instance;
    }

    public Routine selectRoutine(String routineName) {

        // Invalid routine selected
        Routine errorRoutine = new Routine("ERROR", new LinkedList<DroneAction>());

        for (Routine routine : activeRoutines) {
            if (routine.getRoutineName().equals(routineName)) {
                return routine.clone();
            }
        }

        return errorRoutine;
    }

    public void clearRoutine() {
        
        activeRoutines.clear();
    }

    public void addRoutine(Routine routine) {
        activeRoutines.add(routine);
    }

}