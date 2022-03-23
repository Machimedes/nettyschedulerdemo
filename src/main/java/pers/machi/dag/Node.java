package pers.machi.dag;

import com.google.gson.annotations.Expose;

import java.util.Objects;

public class Node {
    @Expose
    public int id = -1;
    public boolean isVisited = false;

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
