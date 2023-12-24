package edu.oswego.cs;

import java.util.concurrent.RecursiveAction;

public class HeatPropagationTask extends RecursiveAction {
    private final MetalAlloy alloyUsedForCalculations;
    private final MetalAlloy alloyToStoreResults;
    private final int rowStart;
    private final int rowEnd;
    private final int colStart;
    private final int colEnd;

    /**
     * A container object that contains all the necessary data to fork the metal alloy problem.
     * @param alloyUsedForCalculations Metal alloy that is treated as immutable and used for calculations
     * @param alloyToStoreResults Metal alloy that is treated as mutable and used to store the results of the calculations.
     * @param rowStart Inclusive row start
     * @param rowEnd Exclusive row end
     * @param colStart Inclusive column end
     * @param colEnd Exclusive column end
     */
    HeatPropagationTask(MetalAlloy alloyUsedForCalculations, MetalAlloy alloyToStoreResults, int rowStart, int rowEnd, int colStart, int colEnd) {
        this.alloyUsedForCalculations = alloyUsedForCalculations;
        this.alloyToStoreResults = alloyToStoreResults;
        this.rowStart = rowStart;
        this.rowEnd = rowEnd;
        this.colStart = colStart;
        this.colEnd = colEnd;
    }

    /**
     * If the specified metal alloy width and height are less than or equal to the calculationThreshold calculate the
     * temperature and rgb values for the all the enclosed metal alloy regions; Storing the results after each
     * calculation. Else break the action down in one of three ways, split in half horizontally, split in half
     * vertically, or split into four corners. The splitting condition is based entirely on whether the width and/or
     * height of the specified region is within the calculation threshold.
     */
    protected void compute() {
        int rowMidpoint = (rowStart + rowEnd) >> 1;
        int colMidpoint = (colStart + colEnd) >> 1;
        int height = rowEnd - rowStart;
        int width = colEnd - colStart;
        int calculationThreshold = 16;
        if (height <= calculationThreshold && width <= calculationThreshold) {
            for (int row = rowStart; row < rowEnd; row++) {
                for (int col = colStart; col < colEnd; col++) {
                    double result = alloyUsedForCalculations.calculateNewTempForRegion(row, col);
                    alloyToStoreResults.setTempOfRegion(result, row, col);
                    alloyToStoreResults.getMetalAlloyRegion(row, col).calcRGB();
                }
            }
        } else if (height <= calculationThreshold) {
            // split in half width wise and execute
            HeatPropagationTask left = new HeatPropagationTask(alloyUsedForCalculations, alloyToStoreResults, rowStart, rowEnd, colStart, colMidpoint);
            HeatPropagationTask right = new HeatPropagationTask(alloyUsedForCalculations, alloyToStoreResults, rowStart, rowEnd, colMidpoint, colEnd);
            left.fork();
            right.fork();
            left.join();
            right.join();
        } else if (width <= calculationThreshold) {
            // split in half height wise and execute
            HeatPropagationTask top = new HeatPropagationTask(alloyUsedForCalculations, alloyToStoreResults, rowStart, rowMidpoint, colStart, colEnd);
            HeatPropagationTask bottom = new HeatPropagationTask(alloyUsedForCalculations, alloyToStoreResults, rowMidpoint, rowEnd, colStart, colEnd);
            top.fork();
            bottom.fork();
            top.join();
            bottom.join();
        } else {
            // split into quarters and execute
            HeatPropagationTask topLeft = new HeatPropagationTask(alloyUsedForCalculations, alloyToStoreResults, rowStart, rowMidpoint, colStart, colMidpoint);
            HeatPropagationTask topRight = new HeatPropagationTask(alloyUsedForCalculations, alloyToStoreResults, rowStart, rowMidpoint, colMidpoint, colEnd);
            HeatPropagationTask bottomLeft = new HeatPropagationTask(alloyUsedForCalculations, alloyToStoreResults, rowMidpoint, rowEnd, colStart, colMidpoint);
            HeatPropagationTask bottomRight = new HeatPropagationTask(alloyUsedForCalculations, alloyToStoreResults, rowMidpoint, rowEnd, colMidpoint, colEnd);
            topLeft.fork();
            topRight.fork();
            bottomLeft.fork();
            bottomRight.fork();
        }
    }
}
