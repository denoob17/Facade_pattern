package kz.aitu.pcbuilder.facade;

import static kz.aitu.pcbuilder.facade.SubsystemCalls.translatingFailures;

import java.util.List;

import kz.aitu.pcbuilder.subsystem.catalog.CatalogSeed;
import kz.aitu.pcbuilder.subsystem.catalog.ComponentCatalog;
import kz.aitu.pcbuilder.subsystem.compatibility.CompatibilityChecker;
import kz.aitu.pcbuilder.subsystem.model.Component;
import kz.aitu.pcbuilder.subsystem.model.PcConfiguration;
import kz.aitu.pcbuilder.subsystem.power.PowerCalculator;


public final class PcUpgradeFacade {

    private final ComponentCatalog catalog;
    private final CompatibilityChecker compatibilityChecker;
    private final PowerCalculator powerCalculator;

    public PcUpgradeFacade() {
        this(new ComponentCatalog(CatalogSeed.components()));
    }

    public PcUpgradeFacade(ComponentCatalog catalog) {
        this.catalog = catalog;
        this.powerCalculator = new PowerCalculator();
        this.compatibilityChecker = new CompatibilityChecker(powerCalculator);
    }

    /** Would the PC made of {@code currentPartIds} still work after swapping in {@code newPartId}? */
    public UpgradeAdvice checkUpgrade(List<String> currentPartIds, String newPartId) {
        return translatingFailures("Upgrade check", () -> {
            PcConfiguration currentPc = PcConfiguration.fromParts(catalog.findAllById(currentPartIds));
            Component newPart = catalog.findById(newPartId);
            PcConfiguration upgradedPc = currentPc.withReplaced(newPart);
            List<String> problems = compatibilityChecker.findProblems(upgradedPc);
            int recommendedPsuWatts = powerCalculator.recommendedPsuWatts(upgradedPc.cpu(), upgradedPc.gpu());
            return new UpgradeAdvice(newPart.name(), newPart.price(), problems.isEmpty(), problems,
                    recommendedPsuWatts);
        });
    }
}
