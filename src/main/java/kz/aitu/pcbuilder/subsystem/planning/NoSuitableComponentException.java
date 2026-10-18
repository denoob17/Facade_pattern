package kz.aitu.pcbuilder.subsystem.planning;

import kz.aitu.pcbuilder.subsystem.SubsystemException;

public class NoSuitableComponentException extends SubsystemException {

    public NoSuitableComponentException(String componentKind) {
        super("the catalog has no " + componentKind + " that matches the other chosen parts");
    }
}
