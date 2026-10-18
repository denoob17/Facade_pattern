package kz.aitu.pcbuilder.subsystem.inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kz.aitu.pcbuilder.subsystem.model.Component;

/** Tracks how many units of every part are on the shelf. Reservations are all-or-nothing. */
public final class Inventory {

    private final Map<String, Integer> unitsById;

    public Inventory(Map<String, Integer> initialUnitsById) {
        this.unitsById = new HashMap<>(initialUnitsById);
    }

    public int unitsAvailable(String componentId) {
        return unitsById.getOrDefault(componentId, 0);
    }

    /** Takes one unit of every part off the shelf, or nothing at all if any part is missing. */
    public void reserve(List<? extends Component> parts) {
        ensureAllInStock(parts);
        parts.forEach(part -> unitsById.merge(part.id(), -1, Integer::sum));
    }

    /** Puts previously reserved parts back on the shelf (used when an order is cancelled). */
    public void release(List<? extends Component> parts) {
        parts.forEach(part -> unitsById.merge(part.id(), 1, Integer::sum));
    }

    private void ensureAllInStock(List<? extends Component> parts) {
        List<String> missingPartNames = parts.stream()
                .filter(part -> unitsAvailable(part.id()) < 1)
                .map(Component::name)
                .toList();
        if (!missingPartNames.isEmpty()) {
            throw new OutOfStockException(missingPartNames);
        }
    }
}
