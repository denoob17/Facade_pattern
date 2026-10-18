package kz.aitu.pcbuilder.subsystem.pricing;

import kz.aitu.pcbuilder.subsystem.model.PcConfiguration;

/** Calculates what the customer pays: the parts plus the workshop fee for assembling them. */
public final class PriceCalculator {

    private static final double ASSEMBLY_FEE_RATE = 0.05;
    private static final int MIN_ASSEMBLY_FEE = 15_000;

    public PriceQuote quote(PcConfiguration configuration) {
        int partsTotal = configuration.totalPartsPrice();
        return new PriceQuote(partsTotal, assemblyFee(partsTotal));
    }

    public int assemblyFee(int partsTotal) {
        int percentageFee = (int) Math.round(partsTotal * ASSEMBLY_FEE_RATE);
        return Math.max(MIN_ASSEMBLY_FEE, percentageFee);
    }
}
