package kz.aitu.pcbuilder.client;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import kz.aitu.pcbuilder.facade.PcBuildFacade;
import kz.aitu.pcbuilder.facade.PcServiceException;
import kz.aitu.pcbuilder.facade.PcUpgradeFacade;


final class ConsoleApp {

    private static final String EXIT_OPTION = "0";

    private final PcBuildFacade shop;
    private final PcUpgradeFacade upgradeAdvisor;
    private final ConsolePrinter printer;
    private final Scanner input;

    ConsoleApp(PcBuildFacade shop, PcUpgradeFacade upgradeAdvisor, ConsolePrinter printer, Scanner input) {
        this.shop = shop;
        this.upgradeAdvisor = upgradeAdvisor;
        this.printer = printer;
        this.input = input;
    }

    void run() {
        printer.printTitle("PC Builder - Facade pattern demo");
        String choice;
        do {
            printer.printMenu();
            choice = readLine("Your choice: ");
            handleSafely(choice);
        } while (!EXIT_OPTION.equals(choice));
    }

    private void handleSafely(String choice) {
        try {
            handle(choice);
        } catch (PcServiceException e) {
            printer.printError(e.getMessage());
        }
    }

    private void handle(String choice) {
        switch (choice) {
            case "1" -> printer.printPriceList(shop.listAvailableParts());
            case "2" -> printer.printBuild(shop.buildGamingPc(readBudget()));
            case "3" -> printer.printBuild(shop.buildOfficePc(readBudget()));
            case "4" -> printer.printBuild(shop.buildWorkstationPc(readBudget()));
            case "5" -> printer.printBuild(shop.buildCustomPc(readPartIds("Part ids of your PC (7 ids): ")));
            case "6" -> printer.printUpgradeAdvice(upgradeAdvisor.checkUpgrade(
                    readPartIds("Part ids of your current PC (7 ids): "),
                    readLine("Id of the new part: ")));
            case "7" -> new DemoScript(shop, upgradeAdvisor, printer).runAllScenarios();
            case EXIT_OPTION -> System.out.println("Bye!");
            default -> printer.printError("unknown option '" + choice + "'");
        }
    }

    private int readBudget() {
        while (true) {
            String typed = readLine("Budget in KZT (e.g. 800000): ").replace(" ", "").replace("_", "");
            try {
                return Integer.parseInt(typed);
            } catch (NumberFormatException e) {
                printer.printError("'" + typed + "' is not a whole number, try again");
            }
        }
    }

    private List<String> readPartIds(String prompt) {
        return Arrays.stream(readLine(prompt).split("[,\\s]+"))
                .filter(id -> !id.isBlank())
                .toList();
    }

    /** Treats a closed input stream (Ctrl+Z / Ctrl+D) as a request to exit. */
    private String readLine(String prompt) {
        System.out.print(prompt);
        return input.hasNextLine() ? input.nextLine().trim() : EXIT_OPTION;
    }
}
