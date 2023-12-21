package edu.oswego.cs;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

@Command(name = "HeatPropagation", description = "Runs a heat propagation simulation that utilizes fork join parallelism and jacobi relaxation.")
public class HeatPropagationSimulation implements Callable<Integer> {

    @Option(names = {"-s"}, description = "The top left corners constant temperature.")
    private static final double DEFAULT_S = 6000;

    @Option(names = {"-t"}, description = "The bottom right corners constant temperature.")
    private static final double DEFAULT_T = 6000;

    @Option(names = {"-c1"}, description = "The c1 thermal constant.")
    private static final double DEFAULT_C1 = 0.75;

    @Option(names = {"-c2"}, description = "The c2 thermal constant.")
    private static final double DEFAULT_C2 = 1.0;

    @Option(names = {"-c3"}, description = "The c3 thermal constant.")
    private static final double DEFAULT_C3 = 1.25;

    @Option(names = {"-h", "-height"}, description = "The height of the alloy.")
    private static final int DEFAULT_HEIGHT = 80;

    @Option(names = {"-w", "-width"}, description = "The width of the alloy.")
    private static final int DEFAULT_WIDTH = 320;

    @Option(names = {"-heatThreshold", "-colorThreshold"}, description = "The temperature threshold for the ending color heat signature.")
    private static final int DEFAULT_THRESHOLD = 10000;

    @Override
    public Integer call() throws Exception {
        return 0;
    }

    public static void main(String ...args) {
        int exitCode = new CommandLine(new HeatPropagationSimulation()).execute(args);
        System.exit(exitCode);
    }
}
