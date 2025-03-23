package ca.mcmaster.se2aa4.island.teamXXX;

import java.io.File;

import static eu.ace_design.island.runner.Runner.run;

public class Runner {

    public static void main(String[] args) {
        String filename = args[0];
        try {
            run(CommandManager.class)
                    .exploring(new File(filename))
                    .withSeed(42L)
                    .startingAt(30, 30, "EAST")
                    .backBefore(100000)
                    .withCrew(5)
                    .collecting(1000, "WOOD")
                    .storingInto("./outputs")
                    .withName("Island")
                    .fire();
        } catch(Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

}
