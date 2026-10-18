package kz.aitu.pcbuilder.subsystem.model;

public record Storage(String id, String name, int price, int capacityGb) implements Component {

    @Override
    public ComponentType type() {
        return ComponentType.STORAGE;
    }
}
