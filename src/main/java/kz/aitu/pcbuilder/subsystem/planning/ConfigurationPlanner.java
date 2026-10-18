package kz.aitu.pcbuilder.subsystem.planning;

import java.util.function.Predicate;

import kz.aitu.pcbuilder.subsystem.catalog.ComponentCatalog;
import kz.aitu.pcbuilder.subsystem.model.Component;
import kz.aitu.pcbuilder.subsystem.model.ComponentType;
import kz.aitu.pcbuilder.subsystem.model.Cpu;
import kz.aitu.pcbuilder.subsystem.model.Gpu;
import kz.aitu.pcbuilder.subsystem.model.Motherboard;
import kz.aitu.pcbuilder.subsystem.model.PcCase;
import kz.aitu.pcbuilder.subsystem.model.PcConfiguration;
import kz.aitu.pcbuilder.subsystem.model.PowerSupply;
import kz.aitu.pcbuilder.subsystem.model.Ram;
import kz.aitu.pcbuilder.subsystem.model.Storage;
import kz.aitu.pcbuilder.subsystem.power.PowerCalculator;

/**
 * Picks a matching set of parts for a budget. Every part is chosen to fit the parts chosen before it
 * (board for the CPU socket, RAM for the board, PSU for the CPU + GPU, case for board, GPU and heat).
 */
public final class ConfigurationPlanner {

    private final ComponentCatalog catalog;
    private final PowerCalculator powerCalculator;

    public ConfigurationPlanner(ComponentCatalog catalog, PowerCalculator powerCalculator) {
        this.catalog = catalog;
        this.powerCalculator = powerCalculator;
    }

    public PcConfiguration plan(BuildProfile profile, int budget) {
        if (budget <= 0) {
            throw new BudgetTooLowException(budget);
        }
        Cpu cpu = pick(Cpu.class, profile.spendingLimit(ComponentType.CPU, budget), any -> true);
        Motherboard board = pick(Motherboard.class, profile.spendingLimit(ComponentType.MOTHERBOARD, budget),
                candidate -> candidate.socket().equals(cpu.socket()));
        Ram ram = pick(Ram.class, profile.spendingLimit(ComponentType.RAM, budget),
                candidate -> candidate.ramType() == board.ramType());
        Gpu gpu = pick(Gpu.class, profile.spendingLimit(ComponentType.GPU, budget), any -> true);
        Storage storage = pick(Storage.class, profile.spendingLimit(ComponentType.STORAGE, budget), any -> true);
        PowerSupply powerSupply = pick(PowerSupply.class, profile.spendingLimit(ComponentType.POWER_SUPPLY, budget),
                powerSupplyStrongEnoughFor(cpu, gpu));
        PcCase pcCase = pick(PcCase.class, profile.spendingLimit(ComponentType.CASE, budget),
                caseThatFits(board, cpu, gpu));

        PcConfiguration configuration = new PcConfiguration(cpu, board, ram, gpu, storage, powerSupply, pcCase);
        ensureWithinBudget(configuration, budget);
        return configuration;
    }

    /** The best part within the spending limit; if nothing is that cheap, the cheapest part that still fits. */
    private <T extends Component> T pick(Class<T> type, int spendingLimit, Predicate<? super T> requirement) {
        return catalog.findMostExpensiveWithin(type, spendingLimit, requirement)
                .or(() -> catalog.findCheapest(type, requirement))
                .orElseThrow(() -> new NoSuitableComponentException(type.getSimpleName()));
    }

    private Predicate<PowerSupply> powerSupplyStrongEnoughFor(Cpu cpu, Gpu gpu) {
        int requiredWatts = powerCalculator.recommendedPsuWatts(cpu, gpu);
        return candidate -> candidate.watts() >= requiredWatts;
    }

    private Predicate<PcCase> caseThatFits(Motherboard board, Cpu cpu, Gpu gpu) {
        int heatOutput = powerCalculator.heatOutputWatts(cpu, gpu);
        return candidate -> candidate.canHost(board.formFactor())
                && candidate.maxGpuLengthMm() >= gpu.lengthMm()
                && candidate.coolingCapacityWatts() >= heatOutput;
    }

    private void ensureWithinBudget(PcConfiguration configuration, int budget) {
        int price = configuration.totalPartsPrice();
        if (price > budget) {
            throw new BudgetTooLowException(budget, price);
        }
    }
}
