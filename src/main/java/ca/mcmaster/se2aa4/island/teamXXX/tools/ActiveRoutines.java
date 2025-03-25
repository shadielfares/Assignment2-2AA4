package ca.mcmaster.se2aa4.island.teamXXX.tools;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

import ca.mcmaster.se2aa4.island.teamXXX.drone.DroneAction;

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

        Routine fakeRoutine = new Routine("ERROR", new LinkedList<DroneAction>());

        for (Routine routine : activeRoutines) {
            if (routine.getRoutineName().equals(routineName)) {
                return routine.clone();
            }
        }

        // Include the return of an error
        return fakeRoutine;
    }

    public void clearRoutine() {
        
        activeRoutines.clear();
    }

    public void addRoutine(Routine routine) {
        activeRoutines.add(routine);
    }

}