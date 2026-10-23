package kz.aitu.pcbuilder.subsystem.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import kz.aitu.pcbuilder.facade.PcServiceException;
import kz.aitu.pcbuilder.facade.PcUpgradeFacade;
import kz.aitu.pcbuilder.facade.UpgradeAdvice;
import org.junit.jupiter.api.Test;

class PcUpgradeFacadeTest {

    private static final List<String> MY_CURRENT_GAMING_PC = List.of(
            "cpu-r5-7600", "mb-b650", "ram-ddr5-32", "gpu-rx7800xt", "ssd-1tb", "psu-650", "case-atx");

    private final PcUpgradeFacade advisor = new PcUpgradeFacade();

    // Demo scenario 9: a 650 W power supply is too weak for the RTX 4090
    @Test
    void rtx4090_onWeakPowerSupply_isNotCompatible() {
        UpgradeAdvice advice = advisor.checkUpgrade(MY_CURRENT_GAMING_PC, "gpu-rtx4090");

        assertFalse(advice.compatible());
        assertEquals(1, advice.problems().size());
        assertEquals(750, advice.recommendedPsuWatts());
    }

    // Demo scenario 10
    @Test
    void moreRam_isCompatible() {
        UpgradeAdvice advice = advisor.checkUpgrade(MY_CURRENT_GAMING_PC, "ram-ddr5-64");

        assertTrue(advice.compatible());
        assertTrue(advice.problems().isEmpty());
    }

    @Test
    void unknownPart_throwsPcServiceException() {
        assertThrows(PcServiceException.class,
                () -> advisor.checkUpgrade(MY_CURRENT_GAMING_PC, "no-such-part"));
    }
}
