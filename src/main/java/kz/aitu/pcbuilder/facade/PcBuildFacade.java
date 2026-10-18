package kz.aitu.pcbuilder.facade;

import static kz.aitu.pcbuilder.facade.SubsystemCalls.translatingFailures;

import java.util.List;

import kz.aitu.pcbuilder.subsystem.assembly.AssemblyWorkshop;
import kz.aitu.pcbuilder.subsystem.catalog.CatalogSeed;
import kz.aitu.pcbuilder.subsystem.catalog.ComponentCatalog;
import kz.aitu.pcbuilder.subsystem.compatibility.CompatibilityChecker;
import kz.aitu.pcbuilder.subsystem.inventory.Inventory;
import kz.aitu.pcbuilder.subsystem.model.Component;
import kz.aitu.pcbuilder.subsystem.model.Computer;
import kz.aitu.pcbuilder.subsystem.model.PcConfiguration;
import kz.aitu.pcbuilder.subsystem.planning.BuildProfile;
import kz.aitu.pcbuilder.subsystem.planning.ConfigurationPlanner;
import kz.aitu.pcbuilder.subsystem.power.PowerCalculator;
import kz.aitu.pcbuilder.subsystem.pricing.PriceCalculator;
import kz.aitu.pcbuilder.subsystem.pricing.PriceQuote;
import kz.aitu.pcbuilder.subsystem.testing.StressTestReport;
import kz.aitu.pcbuilder.subsystem.testing.StressTester;


public final class PcBuildFacade {

    private final ComponentCatalog catalog;
    private final Inventory inventory;
    private final ConfigurationPlanner planner;
    private final CompatibilityChecker compatibilityChecker;
    private final AssemblyWorkshop workshop;
    private final StressTester stressTester;
    private final PriceCalculator priceCalculator;

    /** A shop with the default catalog and stock, ready to use. */
    public PcBuildFacade() {
        this(new ComponentCatalog(CatalogSeed.components()), new Inventory(CatalogSeed.stock()));
    }

    /** Lets an advanced client (or a test) plug in its own catalog and stock. */
    public PcBuildFacade(ComponentCatalog catalog, Inventory inventory) {
        PowerCalculator powerCalculator = new PowerCalculator();
        this.catalog = catalog;
        this.inventory = inventory;
        this.planner = new ConfigurationPlanner(catalog, powerCalculator);
        this.compatibilityChecker = new CompatibilityChecker(powerCalculator);
        this.workshop = new AssemblyWorkshop();
        this.stressTester = new StressTester(powerCalculator);
        this.priceCalculator = new PriceCalculator();
    }

    public BuildSummary buildGamingPc(int budgetKzt) {
        return buildForProfile(BuildProfile.GAMING, budgetKzt);
    }

    public BuildSummary buildOfficePc(int budgetKzt) {
        return buildForProfile(BuildProfile.OFFICE, budgetKzt);
    }

    public BuildSummary buildWorkstationPc(int budgetKzt) {
        return buildForProfile(BuildProfile.WORKSTATION, budgetKzt);
    }

    /** For customers who picked every part themselves, by id from {@link #listAvailableParts()}. */
    public BuildSummary buildCustomPc(List<String> partIds) {
        return translatingFailures("Custom PC order",
                () -> orderBuild(PcConfiguration.fromParts(catalog.findAllById(partIds))));
    }

    public List<PartOffer> listAvailableParts() {
        return catalog.allComponents().stream()
                .map(this::toOffer)
                .toList();
    }

    public int unitsInStock(String partId) {
        return inventory.unitsAvailable(partId);
    }

    private BuildSummary buildForProfile(BuildProfile profile, int budgetKzt) {
        return translatingFailures(profile.displayName() + " PC order",
                () -> orderBuild(planner.plan(profile, budgetKzt)));
    }

    /** The coordination core: the order of these steps is exactly what the client no longer needs to know. */
    private BuildSummary orderBuild(PcConfiguration configuration) {
        compatibilityChecker.verify(configuration);
        PriceQuote quote = priceCalculator.quote(configuration);
        inventory.reserve(configuration.parts());
        try {
            Computer computer = workshop.assemble(configuration);
            StressTestReport report = stressTester.certify(computer);
            return toSummary(computer, report, quote);
        } catch (RuntimeException failure) {
            inventory.release(configuration.parts());
            throw failure;
        }
    }

    private BuildSummary toSummary(Computer computer, StressTestReport report, PriceQuote quote) {
        List<String> parts = computer.configuration().parts().stream()
                .map(part -> part.type().label() + ": " + part.name())
                .toList();
        return new BuildSummary(computer.serialNumber(), parts, computer.assemblyLog(),
                quote.partsTotal(), quote.assemblyFee(), report.benchmarkScore(), report.verdict());
    }

    private PartOffer toOffer(Component component) {
        return new PartOffer(component.id(), component.type().label(), component.name(),
                component.price(), inventory.unitsAvailable(component.id()));
    }
}
