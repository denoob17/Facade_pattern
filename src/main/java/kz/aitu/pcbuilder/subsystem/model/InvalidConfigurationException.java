package kz.aitu.pcbuilder.subsystem.model;

import kz.aitu.pcbuilder.subsystem.SubsystemException;

public class InvalidConfigurationException extends SubsystemException {

    public InvalidConfigurationException(String message) {
        super(message);
    }
}
