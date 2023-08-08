package cz.radeknolc.boilerplate.infrastructure.mapping;

public interface MappableEntity<T> {
    T toModel();
}
