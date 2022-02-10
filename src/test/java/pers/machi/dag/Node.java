package pers.machi.dag;

public class Node {
    public int id;

    public Node(int i) {
        id = i;
    }

    @Override
    public String toString() {
        return "Node" + id;
    }
}
