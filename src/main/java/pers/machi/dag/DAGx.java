package pers.machi.dag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

public class DAGx<N extends Node> {
    public HashMap<N, HashMap<N, Object>> adjacentList = new HashMap<>();
    public HashSet<N> isolatedNodes = new HashSet<>();
    public HashMap<N, Indegree> indegreeMap = new HashMap<>();

    public static class Edge<N> {
        N src;
        N tgt;
        Object properties;

        public Edge(N n1, N n2, Object o) {
            src = n1;
            tgt = n2;
            properties = o;
        }

        @Override
        public String toString() {
            return "Edge " + src + "->" + tgt;
        }
    }

    public static class DAGxBuilder<N extends Node> {
        private final Logger logger = LogManager.getLogger(DAGxBuilder.class);

        private HashSet<N> nodes = new HashSet<>();
        private HashSet<DAGx.Edge<N>> edges = new HashSet<>();
        HashSet<N> nodeWithConnection = new HashSet<>();
        AtomicInteger cycleCounter = new AtomicInteger(0);

        public static <N extends Node> DAGxBuilder<N> builder() {
            return new DAGxBuilder<N>();
        }

        public DAGxBuilder<N> addNodes(Collection<N> nodes) {
            this.nodes.addAll(nodes);
            return this;
        }

        public DAGxBuilder<N> addEdges(Collection<Edge<N>> edges) {
            this.edges.addAll(edges);
            return this;
        }

        public DAGx<N> build() {
            DAGx<N> dagx = new DAGx<>();
            HashMap<N, HashMap<N, Object>> adjacentList = dagx.adjacentList;
            HashMap<N, Indegree> indegreeMap = dagx.indegreeMap;

            for (DAGx.Edge<N> edge : edges) {
                adjacentList.computeIfAbsent(edge.src, k -> new HashMap<>());
                adjacentList.get(edge.src).put(edge.tgt, edge.properties);
                indegreeMap.computeIfAbsent(edge.tgt,k-> new Indegree());
                indegreeMap.get(edge.tgt).increase();

                nodeWithConnection.add(edge.src);
                nodeWithConnection.add(edge.tgt);
            }

            logger.warn(indegreeMap);
            detectCycle(dagx.adjacentList);
            if (cycleCounter.get() > 0) {
                logger.warn("cycle detected: " + cycleCounter.get());
                return null;
            }

            nodes.removeAll(nodeWithConnection);
            dagx.isolatedNodes.addAll(nodes);
            return dagx;
        }

        // dfs to detect cycle in adjacentList
        public void detectCycle(HashMap<N, HashMap<N, Object>> adjacentList) {
            nodeWithConnection.stream()
                    .sorted((n, t1) -> -n.id + t1.id)
                    .filter(e -> !e.isVisited)
                    .forEach(e ->
                            dfs(adjacentList, e, e, cycleCounter));
        }

        // dfs from specified source node, this source node also used as the root node for this dfs span tree.
        void dfs(HashMap<N, HashMap<N, Object>> adjacentList, N src, final N root, AtomicInteger cycleCounter) {
            src.isVisited = true;
            src.root = root;
            if (adjacentList.get(src) != null)
                adjacentList.get(src).keySet()
                        .forEach(e ->
                        {
                            if (!e.isVisited)
                                dfs(adjacentList, e, root, cycleCounter);
                            else if (e.root == root)
                                cycleCounter.getAndIncrement();
                        });
        }

    }
}