package pers.machi.dag;


import java.util.Optional;

public class NodeTest extends Node {
    public NodeTest(int i) {
        id = i;
    }

    @Override
    public String toString() {
        return "Node" + id;
    }
}
