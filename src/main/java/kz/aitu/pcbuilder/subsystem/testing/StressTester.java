package kz.aitu.pcbuilder.subsystem.testing;

import kz.aitu.pcbuilder.subsystem.model.Computer;
import kz.aitu.pcbuilder.subsystem.model.PcConfiguration;
import kz.aitu.pcbuilder.subsystem.power.PowerCalculator;

/** Runs a full-load test on an assembled computer: does the case cool it and how fast is it. */
public final class StressTester {

    private static final int CPU_POINTS_PER_WATT = 40;
    private static final int GPU_POINTS_PER_WATT = 60;
    private static final int RAM_POINTS_PER_GB = 50;

    private final PowerCalculator powerCalculator;

    public StressTester(PowerCalculator powerCalculator) {
        this.powerCalculator = powerCalculator;
    }

    /** Only reports the result. Advanced users call this to see the numbers even for a failing PC. */
    public StressTestReport run(Computer computer) {
        PcConfiguration c = computer.configuration();
        int heatOutput = powerCalculator.heatOutputWatts(c.cpu(), c.gpu());
        int coolingCapacity = c.pcCase().coolingCapacityWatts();
        return new StressTestReport(computer.serialNumber(), heatOutput <= coolingCapacity,
                heatOutput, coolingCapacity, benchmarkScore(c));
    }

    /** Same test, but a computer that fails it must not leave the workshop. */
    public StressTestReport certify(Computer computer) {
        StressTestReport report = run(computer);
        if (!report.passed()) {
            throw new StressTestFailedException(report);
        }
        return report;
    }

    private int benchmarkScore(PcConfiguration c) {
        return c.cpu().tdpWatts() * CPU_POINTS_PER_WATT
                + c.gpu().powerWatts() * GPU_POINTS_PER_WATT
                + c.ram().capacityGb() * RAM_POINTS_PER_GB;
    }
}
