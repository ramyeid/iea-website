package com.iea.utils;

import java.util.Objects;

public class Tuple <T,Z> {

    private final T first;
    private final Z second;

    public Tuple(T first, Z second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public Z getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return Objects.equals(first, tuple.first) &&
                Objects.equals(second, tuple.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
