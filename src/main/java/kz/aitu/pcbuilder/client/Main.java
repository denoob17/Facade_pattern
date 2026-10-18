package kz.aitu.pcbuilder.client;

import java.util.Scanner;

import kz.aitu.pcbuilder.facade.PcBuildFacade;
import kz.aitu.pcbuilder.facade.PcUpgradeFacade;


public final class Main {

    private static final String DEMO_FLAG = "--demo";

    private Main() {
    }

    public static void main(String[] args) {
        PcBuildFacade shop = new PcBuildFacade();
        PcUpgradeFacade upgradeAdvisor = new PcUpgradeFacade();
        ConsolePrinter printer = new ConsolePrinter();

        if (args.length > 0 && DEMO_FLAG.equals(args[0])) {
            new DemoScript(shop, upgradeAdvisor, printer).runAllScenarios();
            return;
        }
        new ConsoleApp(shop, upgradeAdvisor, printer, new Scanner(System.in)).run();
    }
}
