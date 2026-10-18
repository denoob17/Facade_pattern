package kz.aitu.pcbuilder.subsystem.catalog;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import kz.aitu.pcbuilder.subsystem.model.Component;

/** Read-only list of every part the shop sells. Knows how to look parts up; knows nothing about stock. */
public final class ComponentCatalog {

    private static final Comparator<Component> BY_PRICE = Comparator.comparingInt(Component::price);

    private final Map<String, Component> componentsById = new LinkedHashMap<>();

    public ComponentCatalog(List<? extends Component> components) {
        components.forEach(component -> componentsById.put(component.id(), component));
    }

    public Component findById(String componentId) {
        Component component = componentsById.get(componentId);
        if (component == null) {
            throw new ComponentNotFoundException(componentId);
        }
        return component;
    }

    public List<Component> findAllById(List<String> componentIds) {
        return componentIds.stream().map(this::findById).toList();
    }

    public List<Component> allComponents() {
        return List.copyOf(componentsById.values());
    }

    /** The best (most expensive) part of the given type that still fits into {@code maxPrice}. */
    public <T extends Component> Optional<T> findMostExpensiveWithin(Class<T> type, int maxPrice,
                                                                     Predicate<? super T> requirement) {
        return streamOf(type)
                .filter(requirement)
                .filter(component -> component.price() <= maxPrice)
                .max(BY_PRICE);
    }

    public <T extends Component> Optional<T> findCheapest(Class<T> type, Predicate<? super T> requirement) {
        return streamOf(type)
                .filter(requirement)
                .min(BY_PRICE);
    }

    private <T extends Component> Stream<T> streamOf(Class<T> type) {
        return componentsById.values().stream()
                .filter(type::isInstance)
                .map(type::cast);
    }
}
