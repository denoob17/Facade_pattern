package kz.aitu.pcbuilder.subsystem.power;

import kz.aitu.pcbuilder.subsystem.model.Cpu;
import kz.aitu.pcbuilder.subsystem.model.Gpu;

/** Estimates power draw and heat of a build, and the power supply it needs. */
public final class PowerCalculator {

    /** Motherboard, RAM, SSD and fans together. */
    private static final int BASE_SYSTEM_WATTS = 75;
    /** A PSU should not run at 100% load: keep 25% in reserve for spikes. */
    private static final double SAFETY_HEADROOM = 1.25;
    /** Power supplies are sold in 50 W steps (450, 500, 550, ...). */
    private static final int PSU_STEP_WATTS = 50;

    /** Almost all electrical power of the CPU and GPU ends up as heat inside the case. */
    public int heatOutputWatts(Cpu cpu, Gpu gpu) {
        return cpu.tdpWatts() + gpu.powerWatts();
    }

    public int estimatedLoadWatts(Cpu cpu, Gpu gpu) {
        return heatOutputWatts(cpu, gpu) + BASE_SYSTEM_WATTS;
    }

    public int recommendedPsuWatts(Cpu cpu, Gpu gpu) {
        int loadWithHeadroom = (int) Math.ceil(estimatedLoadWatts(cpu, gpu) * SAFETY_HEADROOM);
        return roundUpToPsuStep(loadWithHeadroom);
    }

    private int roundUpToPsuStep(int watts) {
        return (watts + PSU_STEP_WATTS - 1) / PSU_STEP_WATTS * PSU_STEP_WATTS;
    }
}
