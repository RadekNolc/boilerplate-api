package cz.radeknolc.boilerplate.infrastructure.mapping;

public interface MappableModel<T> {
    T toEntity();
}
