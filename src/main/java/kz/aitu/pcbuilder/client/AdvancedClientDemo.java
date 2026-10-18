package kz.aitu.pcbuilder.client;

import java.util.List;

import kz.aitu.pcbuilder.subsystem.catalog.CatalogSeed;
import kz.aitu.pcbuilder.subsystem.catalog.ComponentCatalog;
import kz.aitu.pcbuilder.subsystem.compatibility.CompatibilityChecker;
import kz.aitu.pcbuilder.subsystem.model.Cpu;
import kz.aitu.pcbuilder.subsystem.model.Gpu;
import kz.aitu.pcbuilder.subsystem.model.PcConfiguration;
import kz.aitu.pcbuilder.subsystem.power.PowerCalculator;


public final class AdvancedClientDemo {

    private AdvancedClientDemo() {
    }

    public static void main(String[] args) {
        ComponentCatalog catalog = new ComponentCatalog(CatalogSeed.components());
        PowerCalculator powerCalculator = new PowerCalculator();
        CompatibilityChecker checker = new CompatibilityChecker(powerCalculator);

        Cpu cpu = (Cpu) catalog.findById("cpu-r9-7950x");
        Gpu gpu = (Gpu) catalog.findById("gpu-rtx4080s");
        System.out.println("Estimated load: " + powerCalculator.estimatedLoadWatts(cpu, gpu) + " W");
        System.out.println("Recommended PSU: " + powerCalculator.recommendedPsuWatts(cpu, gpu) + " W");

        PcConfiguration draft = PcConfiguration.fromParts(catalog.findAllById(List.of(
                "cpu-r9-7950x", "mb-a620i", "ram-ddr5-64", "gpu-rtx4080s", "ssd-2tb", "psu-650", "case-matx")));
        System.out.println("Problems found by the compatibility checker:");
        checker.findProblems(draft).forEach(problem -> System.out.println("  - " + problem));
    }
}
