package kz.aitu.pcbuilder.subsystem.catalog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kz.aitu.pcbuilder.subsystem.model.Component;
import kz.aitu.pcbuilder.subsystem.model.Cpu;
import kz.aitu.pcbuilder.subsystem.model.FormFactor;
import kz.aitu.pcbuilder.subsystem.model.Gpu;
import kz.aitu.pcbuilder.subsystem.model.Motherboard;
import kz.aitu.pcbuilder.subsystem.model.PcCase;
import kz.aitu.pcbuilder.subsystem.model.PowerSupply;
import kz.aitu.pcbuilder.subsystem.model.Ram;
import kz.aitu.pcbuilder.subsystem.model.RamType;
import kz.aitu.pcbuilder.subsystem.model.Storage;

/** Demo data: the parts a small computer shop in Astana could have on its shelves (prices in KZT). */
public final class CatalogSeed {

    private static final int DEFAULT_UNITS_IN_STOCK = 5;
    private static final String TOP_GPU_ID = "gpu-rtx4090";
    private static final int TOP_GPU_UNITS_IN_STOCK = 1;

    private CatalogSeed() {
    }

    public static List<Component> components() {
        return List.of(
                new Cpu("cpu-i3-12100f", "Intel Core i3-12100F", 45_000, "LGA1700", 58),
                new Cpu("cpu-i5-13400f", "Intel Core i5-13400F", 95_000, "LGA1700", 65),
                new Cpu("cpu-i7-14700k", "Intel Core i7-14700K", 210_000, "LGA1700", 125),
                new Cpu("cpu-r5-7600", "AMD Ryzen 5 7600", 100_000, "AM5", 65),
                new Cpu("cpu-r7-7800x3d", "AMD Ryzen 7 7800X3D", 195_000, "AM5", 120),
                new Cpu("cpu-r9-7950x", "AMD Ryzen 9 7950X", 290_000, "AM5", 170),

                new Motherboard("mb-h610m", "MSI PRO H610M-E", 40_000, "LGA1700", RamType.DDR4, FormFactor.MICRO_ATX),
                new Motherboard("mb-b760", "ASUS TUF B760-PLUS", 85_000, "LGA1700", RamType.DDR5, FormFactor.ATX),
                new Motherboard("mb-z790", "Gigabyte Z790 AORUS ELITE", 140_000, "LGA1700", RamType.DDR5, FormFactor.ATX),
                new Motherboard("mb-a620i", "ASRock A620I Lightning", 60_000, "AM5", RamType.DDR5, FormFactor.MINI_ITX),
                new Motherboard("mb-b650", "MSI MAG B650 TOMAHAWK", 95_000, "AM5", RamType.DDR5, FormFactor.ATX),
                new Motherboard("mb-x670e", "ASUS ROG STRIX X670E-E", 180_000, "AM5", RamType.DDR5, FormFactor.ATX),

                new Ram("ram-ddr4-16", "Kingston FURY 16GB DDR4", 18_000, RamType.DDR4, 16),
                new Ram("ram-ddr4-32", "Kingston FURY 32GB DDR4", 32_000, RamType.DDR4, 32),
                new Ram("ram-ddr5-16", "Corsair Vengeance 16GB DDR5", 25_000, RamType.DDR5, 16),
                new Ram("ram-ddr5-32", "G.Skill Trident Z5 32GB DDR5", 48_000, RamType.DDR5, 32),
                new Ram("ram-ddr5-64", "Corsair Dominator 64GB DDR5", 105_000, RamType.DDR5, 64),

                new Gpu("gpu-gt1030", "NVIDIA GeForce GT 1030", 40_000, 30, 170),
                new Gpu("gpu-rtx4060", "NVIDIA GeForce RTX 4060", 160_000, 115, 240),
                new Gpu("gpu-rx7800xt", "AMD Radeon RX 7800 XT", 270_000, 263, 267),
                new Gpu("gpu-rtx4080s", "NVIDIA GeForce RTX 4080 SUPER", 520_000, 320, 310),
                new Gpu(TOP_GPU_ID, "NVIDIA GeForce RTX 4090", 900_000, 450, 336),

                new Storage("ssd-500", "Kingston NV2 500GB NVMe", 20_000, 500),
                new Storage("ssd-1tb", "Samsung 980 1TB NVMe", 38_000, 1000),
                new Storage("ssd-2tb", "Samsung 990 PRO 2TB NVMe", 90_000, 2000),

                new PowerSupply("psu-450", "be quiet! System Power 10 450W", 22_000, 450),
                new PowerSupply("psu-650", "Cougar GEX 650W", 35_000, 650),
                new PowerSupply("psu-850", "Corsair RM850e 850W", 60_000, 850),
                new PowerSupply("psu-1000", "Seasonic FOCUS GX 1000W", 90_000, 1000),
                new PowerSupply("psu-1200", "ASUS ROG THOR 1200W", 150_000, 1200),

                new PcCase("case-itx", "Cooler Master NR200", 45_000, FormFactor.MINI_ITX, 330, 350),
                new PcCase("case-matx", "Deepcool MATREXX 40", 20_000, FormFactor.MICRO_ATX, 250, 250),
                new PcCase("case-atx", "NZXT H5 Flow", 45_000, FormFactor.ATX, 365, 550),
                new PcCase("case-atx-pro", "Lian Li O11 Dynamic EVO", 85_000, FormFactor.ATX, 420, 800));
    }

    /** Every part has a few units on the shelf, except the top graphics card: only one is left. */
    public static Map<String, Integer> stock() {
        Map<String, Integer> unitsById = new HashMap<>();
        components().forEach(component -> unitsById.put(component.id(), DEFAULT_UNITS_IN_STOCK));
        unitsById.put(TOP_GPU_ID, TOP_GPU_UNITS_IN_STOCK);
        return unitsById;
    }
}
