package kz.aitu.pcbuilder.facade;

import java.util.List;


public record BuildSummary(String serialNumber,
                           List<String> parts,
                           List<String> assemblySteps,
                           int partsPriceKzt,
                           int assemblyFeeKzt,
                           int benchmarkScore,
                           String stressTestVerdict) {

    public BuildSummary {
        parts = List.copyOf(parts);
        assemblySteps = List.copyOf(assemblySteps);
    }

    public int totalPriceKzt() {
        return partsPriceKzt + assemblyFeeKzt;
    }
}
