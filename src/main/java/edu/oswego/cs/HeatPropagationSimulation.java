package edu.oswego.cs;

import picocli.CommandLine;

import java.util.concurrent.Callable;

public class HeatPropagationSimulation implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        return 0;
    }

    public static void main(String ...args) {
        int exitCode = new CommandLine(new HeatPropagationSimulation()).execute(args);
    }
}
