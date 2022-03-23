package pers.machi.dag;

import java.util.Objects;

public class Indegree {
    int indegree;

    public Indegree() {
        indegree = 0;
    }

    public void increase() {
        indegree++;
    }

    public void decrease() {
        indegree--;
    }

    public int get() {
        return indegree;
    }

    @Override
    public String toString() {
        return "indegree" + indegree;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Indegree indegree1 = (Indegree) o;
        return indegree == indegree1.indegree;
    }

    public boolean isZero() {
        return indegree == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(indegree);
    }
}
