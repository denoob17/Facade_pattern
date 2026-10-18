package kz.aitu.pcbuilder.subsystem.catalog;

import kz.aitu.pcbuilder.subsystem.SubsystemException;

public class ComponentNotFoundException extends SubsystemException {

    public ComponentNotFoundException(String componentId) {
        super("no component with id '" + componentId + "' in the catalog");
    }
}
