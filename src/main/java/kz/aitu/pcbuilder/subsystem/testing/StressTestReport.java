package kz.aitu.pcbuilder.subsystem.testing;

public record StressTestReport(String serialNumber, boolean passed, int heatOutputWatts,
                               int coolingCapacityWatts, int benchmarkScore) {

    public String verdict() {
        if (passed) {
            return String.format("PASSED: %d W of heat, case cools up to %d W", heatOutputWatts, coolingCapacityWatts);
        }
        return String.format("FAILED: overheating, %d W of heat but the case cools only %d W",
                heatOutputWatts, coolingCapacityWatts);
    }
}
