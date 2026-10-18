package kz.aitu.pcbuilder.subsystem.pricing;

public record PriceQuote(int partsTotal, int assemblyFee) {

    public int total() {
        return partsTotal + assemblyFee;
    }
}
