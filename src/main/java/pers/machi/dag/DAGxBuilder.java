package pers.machi.dag;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class DAGxBuilder<N> {
    private HashSet<N> nodes = new HashSet<>();
    private HashSet<DAGx.Edge<N>> edges = new HashSet<>();

    public static <T> DAGxBuilder<T> newInstance() {
        return new DAGxBuilder<T>();
    }

    public DAGxBuilder<N> addNodes(Collection<N> nodes) {
        this.nodes.addAll(nodes);
        return this;
    }

    public DAGxBuilder<N> addEdges(Collection<DAGx.Edge<N>> edges) {
        this.edges.addAll(edges);
        return this;
    }

    public DAGx<N> build() {
        DAGx<N> dagx = new DAGx<>();
        HashMap<N, HashMap<N, Object>> adjacentList = dagx.adjacentList;
        HashSet<N> nodeWithConnection = new HashSet<>();
        for (DAGx.Edge<N> edge : edges) {
            adjacentList.computeIfAbsent(edge.src, k -> new HashMap<>());
            adjacentList.get(edge.src).put(edge.tgt, edge.properties);
            nodeWithConnection.add(edge.src);
            nodeWithConnection.add(edge.tgt);
        }

        nodes.removeAll(nodeWithConnection);
        dagx.islands.addAll(nodes);
        return dagx;
    }

    public boolean hasCycle() {
        return true;
    }
}
