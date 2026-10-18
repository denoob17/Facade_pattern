package kz.aitu.pcbuilder.subsystem.compatibility;

import java.util.List;

import kz.aitu.pcbuilder.subsystem.SubsystemException;

public class IncompatibleComponentsException extends SubsystemException {

    private final List<String> problems;

    public IncompatibleComponentsException(List<String> problems) {
        super("incompatible parts: " + String.join("; ", problems));
        this.problems = List.copyOf(problems);
    }

    public List<String> problems() {
        return problems;
    }
}
