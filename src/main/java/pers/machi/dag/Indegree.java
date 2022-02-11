package pers.machi.dag;

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
        return "indegree"+indegree;
    }
}
