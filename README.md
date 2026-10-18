# PC Builder — Facade Pattern (Assignment 4)

Course: ShP-2216 Software Design Patterns, Astana IT University
Topic: **Option C — free topic: ordering a custom-built PC from components**

A customer wants to say *"build me a gaming PC for 800 000 KZT"*. Behind that one sentence a real
computer shop has to pick matching parts, check that they physically fit together, reserve them in stock,
assemble them in the right order, stress-test the machine, calculate the price, and put the parts back on
the shelf if anything fails half-way. `PcBuildFacade` turns all of that into **one method call**.

## Structure

```
src/main/java/kz/aitu/pcbuilder
├── client/                 # uses ONLY the facades
│   ├── Main.java               entry point (menu or --demo)
│   ├── ConsoleApp.java         interactive menu
│   ├── DemoScript.java         10 ready scenarios for the defense
│   ├── ConsolePrinter.java     all console output
│   └── AdvancedClientDemo.java advanced client that bypasses the facade on purpose
├── facade/
│   ├── PcBuildFacade.java      main facade: order a PC
│   ├── PcUpgradeFacade.java    second facade: check an upgrade of an existing PC
│   ├── BuildSummary, PartOffer, UpgradeAdvice   plain result records (no subsystem types leak out)
│   └── PcServiceException.java the only exception the client has to know
└── subsystem/              # knows nothing about the facades
    ├── catalog/        ComponentCatalog, CatalogSeed
    ├── planning/       ConfigurationPlanner, BuildProfile
    ├── compatibility/  CompatibilityChecker
    ├── power/          PowerCalculator
    ├── inventory/      Inventory
    ├── assembly/       AssemblyWorkshop
    ├── testing/        StressTester
    ├── pricing/        PriceCalculator
    └── model/          Component, Cpu, Motherboard, Ram, Gpu, Storage, PowerSupply, PcCase, PcConfiguration, Computer
docs/uml/class-diagram.puml   # UML class diagram (PlantUML)
```

## Subsystem classes (8)

| Class | Responsibility |
|---|---|
| `ComponentCatalog` | Finds parts by id / type / price |
| `ConfigurationPlanner` | Splits a budget by `BuildProfile` and picks parts that fit each other |
| `CompatibilityChecker` | Socket, RAM type, board vs case size, GPU length, PSU power |
| `PowerCalculator` | Power draw, heat output, recommended PSU wattage |
| `Inventory` | All-or-nothing reservation of parts, release on cancel |
| `AssemblyWorkshop` | Assembles parts in the real technician's order, gives a serial number |
| `StressTester` | Full-load test: can the case cool the CPU + GPU, benchmark score |
| `PriceCalculator` | Parts total + assembly fee |

What `PcBuildFacade.orderBuild(...)` coordinates, in order:
`plan → verify compatibility → quote price → reserve stock → assemble → stress test → summary`,
and **if assembly or the stress test fails, the reserved parts are released back to stock**.

## How to run

Requires **JDK 17+**.

Option 1 — IntelliJ IDEA / VS Code: open the folder, run `kz.aitu.pcbuilder.client.Main`
(add program argument `--demo` for the scripted scenarios).

Option 2 — Maven:

```bash
mvn -q compile exec:java
```

```bash
mvn -q compile exec:java -Dexec.args="--demo"
```

Option 3 — plain JDK, no Maven (Git Bash):

```bash
javac -d out $(find src/main/java -name "*.java") && java -cp out kz.aitu.pcbuilder.client.Main --demo
```

Advanced client (bypasses the facade): run `kz.aitu.pcbuilder.client.AdvancedClientDemo`.

## Demo scenarios (`--demo` or menu item 7)

1. Gaming PC for 800 000 KZT — success
2. Office PC for 300 000 KZT — success
3. Gaming PC for 100 000 KZT — *budget too low*
4. Custom PC with mixed-up parts — *3 compatibility problems listed at once*
5. Custom PC with missing parts — *invalid configuration*
6. RTX 4090 in a small case — *stress test fails, parts go back to stock (1 → 0 → 1)*
7. Same PC in a big airflow case — success
8. Same PC again — *the only RTX 4090 is sold, out of stock*
9. Upgrade RX 7800 XT → RTX 4090 on a 650 W PSU — *PSU too weak, needs 750 W*
10. Upgrade 32 GB → 64 GB DDR5 — compatible

## UML class diagram

Source: [docs/uml/class-diagram.puml](docs/uml/class-diagram.puml)

How to view it in VS Code:
1. Install the **PlantUML** extension (`jebbs.plantuml`) — VS Code also suggests it from `.vscode/extensions.json`.
2. Open `docs/uml/class-diagram.puml` and press **Alt+D** to preview.
3. Right click → **PlantUML: Export Current Diagram** → `png` to get the picture for the report.

The extension needs Java (already installed for this project). Graphviz is not needed because the diagram uses
the built-in `smetana` layout.

## Facade rules followed

- `Main` / `ConsoleApp` / `DemoScript` never create or call subsystem classes — only the facades.
- Subsystem classes have no reference to the facades.
- Subsystem classes are still public and usable directly — see `AdvancedClientDemo`.
- Two facades for two unrelated use cases: ordering a new PC vs. upgrading an existing one.
