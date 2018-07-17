package com.iea.circuit;

import java.util.Objects;

public abstract class Component {

    private final String id;

    public Component(String id){
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Component component = (Component) o;
        return Objects.equals(id, component.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}