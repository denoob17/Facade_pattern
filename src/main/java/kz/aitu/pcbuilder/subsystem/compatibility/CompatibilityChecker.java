package kz.aitu.pcbuilder.subsystem.compatibility;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import kz.aitu.pcbuilder.subsystem.model.PcConfiguration;
import kz.aitu.pcbuilder.subsystem.power.PowerCalculator;

/** Knows the physical rules that decide whether a set of parts can work together. */
public final class CompatibilityChecker {

    private final PowerCalculator powerCalculator;

    public CompatibilityChecker(PowerCalculator powerCalculator) {
        this.powerCalculator = powerCalculator;
    }

    /** Fails fast with the full list of problems, so the customer can fix everything at once. */
    public void verify(PcConfiguration configuration) {
        List<String> problems = findProblems(configuration);
        if (!problems.isEmpty()) {
            throw new IncompatibleComponentsException(problems);
        }
    }

    public List<String> findProblems(PcConfiguration configuration) {
        return Stream.of(
                        checkCpuSocket(configuration),
                        checkRamType(configuration),
                        checkBoardFitsCase(configuration),
                        checkGpuFitsCase(configuration),
                        checkPowerSupply(configuration))
                .flatMap(Optional::stream)
                .toList();
    }

    private Optional<String> checkCpuSocket(PcConfiguration c) {
        if (c.cpu().socket().equals(c.motherboard().socket())) {
            return Optional.empty();
        }
        return Optional.of(String.format("%s needs socket %s, but %s has socket %s",
                c.cpu().name(), c.cpu().socket(), c.motherboard().name(), c.motherboard().socket()));
    }

    private Optional<String> checkRamType(PcConfiguration c) {
        if (c.ram().ramType() == c.motherboard().ramType()) {
            return Optional.empty();
        }
        return Optional.of(String.format("%s is %s, but %s supports only %s",
                c.ram().name(), c.ram().ramType(), c.motherboard().name(), c.motherboard().ramType()));
    }

    private Optional<String> checkBoardFitsCase(PcConfiguration c) {
        if (c.pcCase().canHost(c.motherboard().formFactor())) {
            return Optional.empty();
        }
        return Optional.of(String.format("%s motherboard does not fit into %s case %s",
                c.motherboard().formFactor(), c.pcCase().formFactor(), c.pcCase().name()));
    }

    private Optional<String> checkGpuFitsCase(PcConfiguration c) {
        if (c.gpu().lengthMm() <= c.pcCase().maxGpuLengthMm()) {
            return Optional.empty();
        }
        return Optional.of(String.format("%s is %d mm long, %s fits graphics cards up to %d mm",
                c.gpu().name(), c.gpu().lengthMm(), c.pcCase().name(), c.pcCase().maxGpuLengthMm()));
    }

    private Optional<String> checkPowerSupply(PcConfiguration c) {
        int requiredWatts = powerCalculator.recommendedPsuWatts(c.cpu(), c.gpu());
        if (c.powerSupply().watts() >= requiredWatts) {
            return Optional.empty();
        }
        return Optional.of(String.format("%s is too weak: this build needs at least %d W",
                c.powerSupply().name(), requiredWatts));
    }
}
