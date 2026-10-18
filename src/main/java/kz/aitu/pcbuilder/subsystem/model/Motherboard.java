package kz.aitu.pcbuilder.subsystem.model;

public record Motherboard(String id, String name, int price, String socket, RamType ramType, FormFactor formFactor)
        implements Component {

    @Override
    public ComponentType type() {
        return ComponentType.MOTHERBOARD;
    }
}
