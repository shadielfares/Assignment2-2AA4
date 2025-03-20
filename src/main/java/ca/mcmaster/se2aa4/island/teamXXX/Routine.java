package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.Queue;

import org.json.JSONObject;

import java.util.LinkedList;

public class Routine {
    private String name;
    private Queue<DroneAction> decisionQueue = new LinkedList<DroneAction>();
    private Drone drone = Drone.getDroneInstance();

    public Routine(String name, Queue<DroneAction> decisionQueue) {
        this.name = name;
        this.decisionQueue = decisionQueue;
    }

    public JSONObject getRoutineDecision() {
        // Check if the queue is not empty
        DroneAction decision = decisionQueue.remove();
        JSONObject jsonDecision = decision.doAction(drone.getHeading());
        return jsonDecision;
    }

    public boolean routineIsEmpty() {
        return decisionQueue.size() < 1;
    }

    // Fix Leaky Abstraction
    public String getRoutineName() {
        return this.name;
    }

    @Override
    public Routine clone() {

        Queue<DroneAction> clonedQueue = new LinkedList<DroneAction>();

        for (DroneAction action : this.decisionQueue) {
            clonedQueue.add(action);
        }

        return new Routine(this.name, clonedQueue);
    }
}