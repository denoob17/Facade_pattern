package kz.aitu.pcbuilder.subsystem.model;

public record Ram(String id, String name, int price, RamType ramType, int capacityGb) implements Component {

    @Override
    public ComponentType type() {
        return ComponentType.RAM;
    }
}
