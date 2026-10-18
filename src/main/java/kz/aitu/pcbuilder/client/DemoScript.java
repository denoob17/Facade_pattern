package kz.aitu.pcbuilder.client;

import java.util.List;
import java.util.function.Supplier;

import kz.aitu.pcbuilder.facade.BuildSummary;
import kz.aitu.pcbuilder.facade.PcBuildFacade;
import kz.aitu.pcbuilder.facade.PcServiceException;
import kz.aitu.pcbuilder.facade.PcUpgradeFacade;


final class DemoScript {

    private static final String TOP_GPU = "gpu-rtx4090";

    private static final List<String> HOT_PC_IN_SMALL_CASE = List.of(
            "cpu-i7-14700k", "mb-z790", "ram-ddr5-32", TOP_GPU, "ssd-2tb", "psu-1000", "case-atx");
    private static final List<String> HOT_PC_IN_BIG_CASE = List.of(
            "cpu-i7-14700k", "mb-z790", "ram-ddr5-32", TOP_GPU, "ssd-2tb", "psu-1000", "case-atx-pro");
    private static final List<String> MIXED_UP_PARTS = List.of(
            "cpu-i5-13400f", "mb-b650", "ram-ddr4-16", "gpu-rtx4060", "ssd-1tb", "psu-450", "case-matx");
    private static final List<String> MY_CURRENT_GAMING_PC = List.of(
            "cpu-r5-7600", "mb-b650", "ram-ddr5-32", "gpu-rx7800xt", "ssd-1tb", "psu-650", "case-atx");

    private final PcBuildFacade shop;
    private final PcUpgradeFacade upgradeAdvisor;
    private final ConsolePrinter printer;

    DemoScript(PcBuildFacade shop, PcUpgradeFacade upgradeAdvisor, ConsolePrinter printer) {
        this.shop = shop;
        this.upgradeAdvisor = upgradeAdvisor;
        this.printer = printer;
    }

    void runAllScenarios() {
        orderScenario("1. Gaming PC for 800 000 KZT", () -> shop.buildGamingPc(800_000));
        orderScenario("2. Office PC for 300 000 KZT", () -> shop.buildOfficePc(300_000));
        orderScenario("3. Gaming PC for only 100 000 KZT (budget too low)", () -> shop.buildGamingPc(100_000));
        orderScenario("4. Custom PC with mixed-up parts (incompatible)", () -> shop.buildCustomPc(MIXED_UP_PARTS));
        orderScenario("5. Custom PC with a missing part", () -> shop.buildCustomPc(List.of("cpu-r5-7600", "mb-b650")));

        printer.printTitle("6. RTX 4090 in a small case: fails the stress test, parts go back to stock");
        printStockOfTopGpu("before");
        tryOrder(() -> shop.buildCustomPc(HOT_PC_IN_SMALL_CASE));
        printStockOfTopGpu("after rollback");

        orderScenario("7. Same PC in a big airflow case: passes", () -> shop.buildCustomPc(HOT_PC_IN_BIG_CASE));
        orderScenario("8. Same PC again: the only RTX 4090 is sold", () -> shop.buildCustomPc(HOT_PC_IN_BIG_CASE));

        upgradeScenario("9. Upgrade check: RX 7800 XT -> RTX 4090 on a 650 W power supply", TOP_GPU);
        upgradeScenario("10. Upgrade check: 32 GB -> 64 GB of DDR5", "ram-ddr5-64");
    }

    private void orderScenario(String title, Supplier<BuildSummary> order) {
        printer.printTitle(title);
        tryOrder(order);
    }

    private void tryOrder(Supplier<BuildSummary> order) {
        try {
            printer.printBuild(order.get());
        } catch (PcServiceException e) {
            printer.printError(e.getMessage());
        }
    }

    private void upgradeScenario(String title, String newPartId) {
        printer.printTitle(title);
        try {
            printer.printUpgradeAdvice(upgradeAdvisor.checkUpgrade(MY_CURRENT_GAMING_PC, newPartId));
        } catch (PcServiceException e) {
            printer.printError(e.getMessage());
        }
    }

    private void printStockOfTopGpu(String moment) {
        printer.printInfo("RTX 4090 units in stock " + moment + ": " + shop.unitsInStock(TOP_GPU));
    }
}
