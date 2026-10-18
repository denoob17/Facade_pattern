package kz.aitu.pcbuilder.facade;

import java.util.List;


public record UpgradeAdvice(String newPartName,
                            int newPartPriceKzt,
                            boolean compatible,
                            List<String> problems,
                            int recommendedPsuWatts) {

    public UpgradeAdvice {
        problems = List.copyOf(problems);
    }
}
