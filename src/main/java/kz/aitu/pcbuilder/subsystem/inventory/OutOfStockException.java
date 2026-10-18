package kz.aitu.pcbuilder.subsystem.inventory;

import java.util.List;

import kz.aitu.pcbuilder.subsystem.SubsystemException;

public class OutOfStockException extends SubsystemException {

    public OutOfStockException(List<String> missingPartNames) {
        super("out of stock: " + String.join(", ", missingPartNames));
    }
}
