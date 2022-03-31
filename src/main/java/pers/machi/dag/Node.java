package pers.machi.dag;

import com.google.gson.annotations.Expose;

import java.util.Objects;

public class Node {
    @Expose
    public int id = -1;
    public boolean isVisited = false;

    // important. Deserialized node object from json should be "equals" if they have same id number.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return id == node.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
