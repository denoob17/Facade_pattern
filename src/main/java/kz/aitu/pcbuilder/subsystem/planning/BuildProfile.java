package kz.aitu.pcbuilder.subsystem.planning;

import java.util.EnumMap;
import java.util.Map;

import kz.aitu.pcbuilder.subsystem.model.ComponentType;

/** How a budget is split between part types for a given kind of PC. Each profile's shares add up to 1.0. */
public enum BuildProfile {

    //          CPU   board  RAM   GPU   SSD   PSU   case
    GAMING("Gaming", 0.20, 0.12, 0.07, 0.38, 0.07, 0.08, 0.08),
    OFFICE("Office", 0.28, 0.16, 0.10, 0.14, 0.12, 0.10, 0.10),
    WORKSTATION("Workstation", 0.30, 0.12, 0.12, 0.22, 0.10, 0.07, 0.07);

    private final String displayName;
    private final Map<ComponentType, Double> budgetShares = new EnumMap<>(ComponentType.class);

    BuildProfile(String displayName, double cpu, double motherboard, double ram, double gpu,
                 double storage, double powerSupply, double pcCase) {
        this.displayName = displayName;
        budgetShares.put(ComponentType.CPU, cpu);
        budgetShares.put(ComponentType.MOTHERBOARD, motherboard);
        budgetShares.put(ComponentType.RAM, ram);
        budgetShares.put(ComponentType.GPU, gpu);
        budgetShares.put(ComponentType.STORAGE, storage);
        budgetShares.put(ComponentType.POWER_SUPPLY, powerSupply);
        budgetShares.put(ComponentType.CASE, pcCase);
    }

    public String displayName() {
        return displayName;
    }

    public int spendingLimit(ComponentType type, int totalBudget) {
        return (int) (totalBudget * budgetShares.get(type));
    }
}
