package kz.aitu.pcbuilder.subsystem.model;

/** Any part that can be sold and installed into a PC. Prices are in Kazakhstani tenge (KZT). */
public interface Component {

    String id();

    String name();

    int price();

    ComponentType type();
}
