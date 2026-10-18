package kz.aitu.pcbuilder.subsystem.model;

public record Cpu(String id, String name, int price, String socket, int tdpWatts) implements Component {

    @Override
    public ComponentType type() {
        return ComponentType.CPU;
    }
}
