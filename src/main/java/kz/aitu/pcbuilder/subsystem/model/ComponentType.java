package kz.aitu.pcbuilder.subsystem.model;

public enum ComponentType {
    CPU("CPU"),
    MOTHERBOARD("Motherboard"),
    RAM("RAM"),
    GPU("Graphics card"),
    STORAGE("Storage"),
    POWER_SUPPLY("Power supply"),
    CASE("Case");

    private final String label;

    ComponentType(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
