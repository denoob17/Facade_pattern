package kz.aitu.pcbuilder.subsystem.testing;

import kz.aitu.pcbuilder.subsystem.SubsystemException;

public class StressTestFailedException extends SubsystemException {

    private final StressTestReport report;

    public StressTestFailedException(StressTestReport report) {
        super("computer " + report.serialNumber() + " failed the stress test (" + report.verdict() + ")");
        this.report = report;
    }

    public StressTestReport report() {
        return report;
    }
}
