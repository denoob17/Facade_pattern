package kz.aitu.pcbuilder.subsystem.model;

public record Gpu(String id, String name, int price, int powerWatts, int lengthMm) implements Component {

    @Override
    public ComponentType type() {
        return ComponentType.GPU;
    }
}
