package kz.aitu.pcbuilder.subsystem.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import kz.aitu.pcbuilder.facade.BuildSummary;
import kz.aitu.pcbuilder.facade.PcBuildFacade;
import kz.aitu.pcbuilder.facade.PcServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PcBuildFacadeTest {

    private static final String TOP_GPU = "gpu-rtx4090";

    private static final List<String> HOT_PC_IN_SMALL_CASE = List.of(
            "cpu-i7-14700k", "mb-z790", "ram-ddr5-32", TOP_GPU, "ssd-2tb", "psu-1000", "case-atx");
    private static final List<String> HOT_PC_IN_BIG_CASE = List.of(
            "cpu-i7-14700k", "mb-z790", "ram-ddr5-32", TOP_GPU, "ssd-2tb", "psu-1000", "case-atx-pro");
    private static final List<String> MIXED_UP_PARTS = List.of(
            "cpu-i5-13400f", "mb-b650", "ram-ddr4-16", "gpu-rtx4060", "ssd-1tb", "psu-450", "case-matx");

    private PcBuildFacade shop;


    @BeforeEach
    void setUp() {
        shop = new PcBuildFacade();
    }

    // Demo scenario 1
    @Test
    void gamingPc_withEnoughBudget_isBuilt() {
        BuildSummary summary = shop.buildGamingPc(800_000);

        assertNotNull(summary.serialNumber());
        assertEquals(7, summary.parts().size());
        assertEquals(summary.partsPriceKzt() + summary.assemblyFeeKzt(), summary.totalPriceKzt());
        assertTrue(summary.totalPriceKzt() <= 800_000);
        assertTrue(summary.stressTestVerdict().startsWith("PASSED"));
    }


    @Test
    void officePc_withEnoughBudget_isBuilt() {
        BuildSummary summary = shop.buildOfficePc(300_000);

        assertNotNull(summary.serialNumber());
        assertTrue(summary.totalPriceKzt() <= 300_000);
    }


    @Test
    void gamingPc_withTooSmallBudget_throwsPcServiceException() {
        PcServiceException e = assertThrows(PcServiceException.class, () -> shop.buildGamingPc(100_000));

        assertTrue(e.getMessage().contains("too low"));
        assertNotNull(e.getCause());
    }


    @Test
    void customPc_withIncompatibleParts_isRejected() {
        PcServiceException e = assertThrows(PcServiceException.class, () -> shop.buildCustomPc(MIXED_UP_PARTS));

        assertTrue(e.getMessage().contains("incompatible"));
    }


    @Test
    void customPc_withMissingParts_isRejected() {
        PcServiceException e = assertThrows(PcServiceException.class,
                () -> shop.buildCustomPc(List.of("cpu-r5-7600", "mb-b650")));

        assertTrue(e.getMessage().contains("missing parts"));
    }

    // Demo scenario 6: the most important test, it checks the rollback inside the facade
    @Test
    void failedStressTest_returnsReservedPartsToStock() {
        assertEquals(1, shop.unitsInStock(TOP_GPU));

        assertThrows(PcServiceException.class, () -> shop.buildCustomPc(HOT_PC_IN_SMALL_CASE));

        assertEquals(1, shop.unitsInStock(TOP_GPU));
    }

    @Test
    void lastRtx4090_canBeSoldOnlyOnce() {
        shop.buildCustomPc(HOT_PC_IN_BIG_CASE);
        assertEquals(0, shop.unitsInStock(TOP_GPU));

        PcServiceException e = assertThrows(PcServiceException.class,
                () -> shop.buildCustomPc(HOT_PC_IN_BIG_CASE));

        assertTrue(e.getMessage().contains("out of stock"));
    }
}
