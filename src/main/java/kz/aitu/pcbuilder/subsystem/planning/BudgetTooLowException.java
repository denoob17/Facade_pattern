package kz.aitu.pcbuilder.subsystem.planning;

import java.util.Locale;

import kz.aitu.pcbuilder.subsystem.SubsystemException;

public class BudgetTooLowException extends SubsystemException {

    public BudgetTooLowException(int budget, int cheapestPossiblePrice) {
        super(String.format(Locale.ROOT,
                "budget of %,d KZT is too low, the cheapest working set of parts costs %,d KZT",
                budget, cheapestPossiblePrice));
    }

    public BudgetTooLowException(int budget) {
        super(String.format(Locale.ROOT, "budget must be a positive amount of KZT, got %,d", budget));
    }
}
