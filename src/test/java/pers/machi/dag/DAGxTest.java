package pers.machi.dag;

import java.util.Collection;
import java.util.HashSet;

public class DAGxTest {

    public static void main(String[] args) {
        Collection<Node> nodes = new HashSet<>();
        Node n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12, n13, n14, n15;
        n1 = new NodeTest(1);
        n2 = new NodeTest(2);
        n3 = new NodeTest(3);
        n4 = new NodeTest(4);
        n5 = new NodeTest(5);
        n6 = new NodeTest(6);
        n7 = new NodeTest(7);
        n8 = new NodeTest(8);
        n9 = new NodeTest(9);
        n10 = new NodeTest(10);
        n11 = new NodeTest(11);
        n12 = new NodeTest(12);
        n13 = new NodeTest(13);
        n14 = new NodeTest(14);
        n15 = new NodeTest(15);

        nodes.add(n1);
        nodes.add(n2);
        nodes.add(n3);
        nodes.add(n4);
        nodes.add(n5);
        nodes.add(n6);
        nodes.add(n7);
        nodes.add(n8);
        nodes.add(n9);
        nodes.add(n10);
        nodes.add(n11);
        nodes.add(n12);
        nodes.add(n13);
        nodes.add(n14);
        nodes.add(n15);


        DAGx.Edge<Node> e1 = new DAGx.Edge<>(n1, n2, null);
        DAGx.Edge<Node> e2 = new DAGx.Edge<>(n2, n3, null);
        DAGx.Edge<Node> e3 = new DAGx.Edge<>(n3, n4, null);
        DAGx.Edge<Node> e4 = new DAGx.Edge<>(n4, n1, null);
        DAGx.Edge<Node> e5 = new DAGx.Edge<>(n1, n5, null);
        DAGx.Edge<Node> e6 = new DAGx.Edge<>(n6, n5, null);

        DAGx.Edge<Node> e7 = new DAGx.Edge<>(n6, n7, null);
        DAGx.Edge<Node> e8 = new DAGx.Edge<>(n7, n8, null);
        DAGx.Edge<Node> e9 = new DAGx.Edge<>(n8, n6, null);
        DAGx.Edge<Node> e10 = new DAGx.Edge<>(n2, n4, null);


        Collection<DAGx.Edge<Node>> edges = new HashSet<>();
        edges.add(e1);
        edges.add(e2);
        edges.add(e3);
        edges.add(e4);
        edges.add(e5);
        edges.add(e6);
        edges.add(e7);
        edges.add(e8);
        edges.add(e9);
        edges.add(e10);

        DAGx.DAGxBuilder<Node> daGxBuilder = DAGx.DAGxBuilder.builder();
        DAGx<Node> dagx = daGxBuilder.addEdges(edges).addNodes(nodes).build();
        System.out.println(dagx.adjacentList);
        System.out.println(dagx.isolatedNodes);
    }
}
