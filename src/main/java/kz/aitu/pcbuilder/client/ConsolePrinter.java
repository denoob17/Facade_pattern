package kz.aitu.pcbuilder.client;

import java.util.List;
import java.util.Locale;

import kz.aitu.pcbuilder.facade.BuildSummary;
import kz.aitu.pcbuilder.facade.PartOffer;
import kz.aitu.pcbuilder.facade.UpgradeAdvice;


final class ConsolePrinter {

    private static final String LINE = "-".repeat(78);

    void printTitle(String title) {
        System.out.println();
        System.out.println(LINE);
        System.out.println("  " + title);
        System.out.println(LINE);
    }

    void printMenu() {
        System.out.println();
        System.out.println("1. Show price list");
        System.out.println("2. Order a GAMING PC for a budget");
        System.out.println("3. Order an OFFICE PC for a budget");
        System.out.println("4. Order a WORKSTATION PC for a budget");
        System.out.println("5. Order a CUSTOM PC from part ids");
        System.out.println("6. Check an upgrade for my current PC");
        System.out.println("7. Run all demo scenarios");
        System.out.println("0. Exit");
    }

    void printPriceList(List<PartOffer> offers) {
        System.out.printf("%-15s %-14s %-32s %12s %6s%n", "ID", "CATEGORY", "NAME", "PRICE", "STOCK");
        offers.forEach(offer -> System.out.printf("%-15s %-14s %-32s %8s KZT %6d%n",
                offer.id(), offer.category(), offer.name(), money(offer.priceKzt()), offer.unitsInStock()));
    }

    void printBuild(BuildSummary build) {
        System.out.println("PC " + build.serialNumber() + " is ready!");
        System.out.println("Parts:");
        build.parts().forEach(part -> System.out.println("  - " + part));
        System.out.println("Assembly steps:");
        for (int step = 0; step < build.assemblySteps().size(); step++) {
            System.out.printf("  %d. %s%n", step + 1, build.assemblySteps().get(step));
        }
        System.out.println("Stress test:   " + build.stressTestVerdict());
        System.out.println("Benchmark:     " + build.benchmarkScore() + " points");
        System.out.println("Parts:         " + money(build.partsPriceKzt()) + " KZT");
        System.out.println("Assembly fee:  " + money(build.assemblyFeeKzt()) + " KZT");
        System.out.println("TOTAL:         " + money(build.totalPriceKzt()) + " KZT");
    }

    void printUpgradeAdvice(UpgradeAdvice advice) {
        System.out.printf("New part: %s (%s KZT)%n", advice.newPartName(), money(advice.newPartPriceKzt()));
        System.out.println("Recommended power supply after upgrade: " + advice.recommendedPsuWatts() + " W");
        if (advice.compatible()) {
            System.out.println("OK: the upgrade is compatible with your PC.");
            return;
        }
        System.out.println("NOT compatible:");
        advice.problems().forEach(problem -> System.out.println("  - " + problem));
    }

    void printInfo(String message) {
        System.out.println(message);
    }

    void printError(String message) {
        System.out.println("[!] " + message);
    }

    private String money(int amountKzt) {
        return String.format(Locale.ROOT, "%,d", amountKzt).replace(',', ' ');
    }
}
