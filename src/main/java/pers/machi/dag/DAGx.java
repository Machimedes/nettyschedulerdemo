package pers.machi.dag;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class DAGx<N> {
    public HashMap<N, HashMap<N, Object>> adjacentList = new HashMap<>();
    public HashSet<N> islands = new HashSet<>();

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

}
