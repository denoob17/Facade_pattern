package kz.aitu.pcbuilder.subsystem.model;

/** Motherboard / case size standard. A bigger case can host a smaller board, never the other way round. */
public enum FormFactor {
    MINI_ITX(1),
    MICRO_ATX(2),
    ATX(3);

    private final int size;

    FormFactor(int size) {
        this.size = size;
    }

    public boolean canHost(FormFactor board) {
        return board.size <= size;
    }
}
