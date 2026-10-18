package kz.aitu.pcbuilder.subsystem.model;

public record PowerSupply(String id, String name, int price, int watts) implements Component {

    @Override
    public ComponentType type() {
        return ComponentType.POWER_SUPPLY;
    }
}
