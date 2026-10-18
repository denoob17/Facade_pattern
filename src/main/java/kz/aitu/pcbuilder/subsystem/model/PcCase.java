package kz.aitu.pcbuilder.subsystem.model;

public record PcCase(String id, String name, int price, FormFactor formFactor,
                     int maxGpuLengthMm, int coolingCapacityWatts) implements Component {

    @Override
    public ComponentType type() {
        return ComponentType.CASE;
    }

    public boolean canHost(FormFactor boardFormFactor) {
        return formFactor.canHost(boardFormFactor);
    }
}
