package kz.aitu.pcbuilder.subsystem.model;

import java.util.List;

/** A physically assembled PC together with the log of every assembly step that was performed. */
public record Computer(String serialNumber, PcConfiguration configuration, List<String> assemblyLog) {

    public Computer {
        assemblyLog = List.copyOf(assemblyLog);
    }
}
