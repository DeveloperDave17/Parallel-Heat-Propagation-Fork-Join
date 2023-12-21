package edu.oswego.cs;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Command(name = "HeatPropagation", description = "Runs a heat propagation simulation that utilizes fork join parallelism and jacobi relaxation.")
public class HeatPropagationSimulation implements Callable<Integer> {

    @Option(names = {"-s"}, description = "The top left corners constant temperature.")
    private static double s = 6000;

    @Option(names = {"-t"}, description = "The bottom right corners constant temperature.")
    private static double t = 6000;

    @Option(names = {"-c1"}, description = "The c1 thermal constant.")
    private static double c1 = 0.75;

    @Option(names = {"-c2"}, description = "The c2 thermal constant.")
    private static double c2 = 1.0;

    @Option(names = {"-c3"}, description = "The c3 thermal constant.")
    private static double c3 = 1.25;

    @Option(names = {"-h", "-height"}, description = "The height of the alloy.")
    private static int height = 80;

    @Option(names = {"-w", "-width"}, description = "The width of the alloy.")
    private static int width = 320;

    @Option(names = {"-e", "-executionThreshold"}, description = "The number of phases to be executed before the program terminates.")
    private static int threshold = 100000;

    private volatile boolean simulationIsActive;

    private volatile MetalAlloy alloyToBePainted;

    @Override
    public Integer call() throws Exception {
        MetalAlloy alloyA = new MetalAlloy(height, width, c1, c2, c3);
        alloyA.setTempOfRegion(s, 0, 0);
        alloyA.getMetalAlloyRegion(0, 0).calcRGB();
        alloyA.setTempOfRegion(t, height - 1, width - 1);
        alloyA.getMetalAlloyRegion(height - 1, width - 1).calcRGB();
        MetalAlloy alloyB = new MetalAlloy(height, width, c1, c2, c3);
        alloyA.deepCopyRegionsTo(alloyB);
        alloyToBePainted = alloyA;
        MetalAlloyView metalAlloyView = new MetalAlloyView(height, width, alloyA);
        metalAlloyView.displayRegions(alloyA);
        metalAlloyView.display();
        ExecutorService displayService = Executors.newFixedThreadPool(1);
        // Activate the display for the simulation
        simulationIsActive = true;
        displayService.submit(() -> {
            while (simulationIsActive) {
                metalAlloyView.displayRegions(alloyToBePainted);
                try {
                    Thread.sleep(80);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        boolean useAForPreOp = true;
        for (int i = 0; i < threshold; i++) {
            if (useAForPreOp) {
                HeatPropagationTask heatPropagationTask = new HeatPropagationTask(alloyA, alloyB, 0, height, 0, width);
                heatPropagationTask.fork();
                heatPropagationTask.join();
                alloyToBePainted = alloyB;
            } else {
                HeatPropagationTask heatPropagationTask = new HeatPropagationTask(alloyB, alloyA, 0, height, 0, width);
                heatPropagationTask.fork();
                heatPropagationTask.join();
                alloyToBePainted = alloyA;
            }
            useAForPreOp = !useAForPreOp;
        }
        return 0;
    }

    public static void main(String ...args) {
        int exitCode = new CommandLine(new HeatPropagationSimulation()).execute(args);
    }
}
