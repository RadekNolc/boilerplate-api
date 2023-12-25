package com.radeknolc.libs.ddd.domain.base;

public abstract class ValueObject<T> {

    private final T value;

    protected ValueObject(T value) {
        this.value = value;
        if (isEnabledValidation() && !isValid(value)) {
            throw new IllegalValueObjectException(this);
        }
    }

    public T getValue() {
        return value;
    }

    protected boolean isValid(@SuppressWarnings("unused") T value) {
        throw new IllegalStateException("Method isValid not implemented in ValueObject: %s".formatted(getClass().getName()));
    }

    protected boolean isEnabledValidation() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ValueObject<?> that = (ValueObject<?>) o;

        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }

    private static class IllegalValueObjectException extends RuntimeException {
        public IllegalValueObjectException(ValueObject<?> valueObject) {
            super("ValueObject (%s) has invalid value: %s".formatted(valueObject.getClass().getName(), valueObject.getValue()));
        }
    }
}
