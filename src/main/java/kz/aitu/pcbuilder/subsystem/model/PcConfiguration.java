package kz.aitu.pcbuilder.subsystem.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** A full set of parts for one PC: exactly one component of every {@link ComponentType}. */
public record PcConfiguration(Cpu cpu, Motherboard motherboard, Ram ram, Gpu gpu,
                              Storage storage, PowerSupply powerSupply, PcCase pcCase) {

    public PcConfiguration {
        Objects.requireNonNull(cpu, "cpu");
        Objects.requireNonNull(motherboard, "motherboard");
        Objects.requireNonNull(ram, "ram");
        Objects.requireNonNull(gpu, "gpu");
        Objects.requireNonNull(storage, "storage");
        Objects.requireNonNull(powerSupply, "powerSupply");
        Objects.requireNonNull(pcCase, "pcCase");
    }

    /** Builds a configuration from a loose list of parts, e.g. the ones a customer picked by hand. */
    public static PcConfiguration fromParts(List<? extends Component> parts) {
        Map<ComponentType, Component> partsByType = groupByType(parts);
        ensureNothingMissing(partsByType);
        return new PcConfiguration(
                (Cpu) partsByType.get(ComponentType.CPU),
                (Motherboard) partsByType.get(ComponentType.MOTHERBOARD),
                (Ram) partsByType.get(ComponentType.RAM),
                (Gpu) partsByType.get(ComponentType.GPU),
                (Storage) partsByType.get(ComponentType.STORAGE),
                (PowerSupply) partsByType.get(ComponentType.POWER_SUPPLY),
                (PcCase) partsByType.get(ComponentType.CASE));
    }

    /** Returns a copy of this configuration where the part of the same type is swapped for {@code newPart}. */
    public PcConfiguration withReplaced(Component newPart) {
        List<Component> upgradedParts = new ArrayList<>(parts());
        upgradedParts.removeIf(part -> part.type() == newPart.type());
        upgradedParts.add(newPart);
        return fromParts(upgradedParts);
    }

    public List<Component> parts() {
        return List.of(cpu, motherboard, ram, gpu, storage, powerSupply, pcCase);
    }

    public int totalPartsPrice() {
        return parts().stream().mapToInt(Component::price).sum();
    }

    private static Map<ComponentType, Component> groupByType(List<? extends Component> parts) {
        Map<ComponentType, Component> partsByType = new EnumMap<>(ComponentType.class);
        for (Component part : parts) {
            Component alreadyChosen = partsByType.putIfAbsent(part.type(), part);
            if (alreadyChosen != null) {
                throw new InvalidConfigurationException(String.format(
                        "only one %s is allowed, got '%s' and '%s'",
                        part.type().label(), alreadyChosen.name(), part.name()));
            }
        }
        return partsByType;
    }

    private static void ensureNothingMissing(Map<ComponentType, Component> partsByType) {
        List<String> missingTypes = Arrays.stream(ComponentType.values())
                .filter(type -> !partsByType.containsKey(type))
                .map(ComponentType::label)
                .toList();
        if (!missingTypes.isEmpty()) {
            throw new InvalidConfigurationException("missing parts: " + String.join(", ", missingTypes));
        }
    }
}
