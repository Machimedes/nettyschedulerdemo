package pers.machi.dag;

import com.sun.javafx.binding.Logging;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class DAGxTest {

    public static void main(String[] args) {
        Node n1 = new Node(1);
        Node n2 = new Node(2);
        Node n3 = new Node(3);
        Node n4 = new Node(4);
        Node n5 = new Node(5);

        DAGx.Edge<Node> e1 = new DAGx.Edge<>(n1, n2, null);
        DAGx.Edge<Node> e2 = new DAGx.Edge<>(n2, n3, null);
        DAGx.Edge<Node> e3 = new DAGx.Edge<>(n1, n3, null);
        DAGx.Edge<Node> e4 = new DAGx.Edge<>(n1, n4, null);

        Collection<Node> nodes = new HashSet<>();
        nodes.add(n1);
        nodes.add(n2);
        nodes.add(n3);
        nodes.add(n4);
        nodes.add(n5);

        Collection<DAGx.Edge<Node>> edges = new HashSet<>();
        edges.add(e1);
        edges.add(e2);
        edges.add(e3);
        edges.add(e4);

        DAGxBuilder<Node> daGxBuilder = DAGxBuilder.newInstance();
        DAGx<Node> dagx = daGxBuilder.addEdges(edges).addNodes(nodes).build();
        System.out.println(dagx.adjacentList);
        System.out.println(dagx.islands);
    }
}
