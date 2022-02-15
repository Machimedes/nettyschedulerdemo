package pers.machi.dag;

import com.google.gson.annotations.Expose;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DAGx<N extends Node> {
    public HashMap<N, HashMap<N, Object>> adjacentList = new HashMap<>();
    public HashSet<N> isolatedNodes = new HashSet<>();
    public HashMap<N, Indegree> indegreeMap = new HashMap<>();

    public static class Edge<N extends Node> {
        @Expose

        N src;
        @Expose

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
                indegreeMap.computeIfAbsent(edge.tgt, k -> new Indegree());
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
            Stack<N> visitedNodeStack = new Stack<>();
            nodeWithConnection.stream()
                    .sorted((n, t1) -> -n.id + t1.id)
                    .filter(e -> !e.isVisited)
                    .forEach(e ->
                            dfs(adjacentList, e, cycleCounter, visitedNodeStack));
        }

        // dfs from specified source node, this source node also used as the root node for this dfs span tree.
        void dfs(HashMap<N, HashMap<N, Object>> adjacentList, N src, AtomicInteger cycleCounter, Stack<N> visitedNodeStack) {
            src.isVisited = true;
            visitedNodeStack.push(src);
            if (adjacentList.get(src) != null) {
                adjacentList.get(src).keySet()
                        .forEach(e ->
                        {
                            if (!visitedNodeStack.contains(e))
                                dfs(adjacentList, e, cycleCounter, visitedNodeStack);
                            else {
                                cycleCounter.getAndIncrement();
                                System.out.println(visitedNodeStack);
                            }
                        });
            }
            visitedNodeStack.pop();
        }

    }
}