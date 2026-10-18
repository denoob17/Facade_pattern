package kz.aitu.pcbuilder.facade;

import java.util.function.Supplier;

import kz.aitu.pcbuilder.subsystem.SubsystemException;


final class SubsystemCalls {

    private SubsystemCalls() {
    }

    static <T> T translatingFailures(String operation, Supplier<T> call) {
        try {
            return call.get();
        } catch (SubsystemException e) {
            throw new PcServiceException(operation + " failed: " + e.getMessage(), e);
        }
    }
}
