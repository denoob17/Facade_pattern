package kz.aitu.pcbuilder.subsystem;

/**
 * Common parent for every failure raised inside the PC-building subsystem.
 * Lets callers (including the facade) catch "anything that went wrong in the subsystem"
 * without depending on each concrete exception type.
 */
public abstract class SubsystemException extends RuntimeException {

    protected SubsystemException(String message) {
        super(message);
    }
}
