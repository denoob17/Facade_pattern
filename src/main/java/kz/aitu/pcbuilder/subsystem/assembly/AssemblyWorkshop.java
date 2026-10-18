package kz.aitu.pcbuilder.subsystem.assembly;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import kz.aitu.pcbuilder.subsystem.model.Computer;
import kz.aitu.pcbuilder.subsystem.model.PcConfiguration;

/**
 * Puts the parts together in the order a technician really uses:
 * CPU, RAM and SSD go onto the motherboard while it is still outside the case.
 */
public final class AssemblyWorkshop {

    private static final String SERIAL_NUMBER_FORMAT = "PC-%04d";

    private final AtomicInteger assembledComputers = new AtomicInteger();

    public Computer assemble(PcConfiguration c) {
        List<String> assemblyLog = List.of(
                "Seat " + c.cpu().name() + " into " + c.cpu().socket() + " socket of " + c.motherboard().name(),
                "Install " + c.ram().name() + " into the memory slots",
                "Mount " + c.storage().name() + " into the M.2 slot",
                "Fix the motherboard inside " + c.pcCase().name(),
                "Insert " + c.gpu().name() + " into the PCIe x16 slot",
                "Install " + c.powerSupply().name() + " and connect all power cables",
                "Power on, pass POST and update BIOS");
        return new Computer(nextSerialNumber(), c, assemblyLog);
    }

    private String nextSerialNumber() {
        return String.format(SERIAL_NUMBER_FORMAT, assembledComputers.incrementAndGet());
    }
}
