package pers.machi.dag;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import pers.machi.task.FakeTask;

import java.lang.reflect.Type;
import java.util.HashSet;

public class JSONSerializeTest {
    public static void main(String[] args) throws ClassNotFoundException {
        HashSet<FakeTask> nodes = new HashSet<>();
        FakeTask ft1 = new FakeTask(1);
        FakeTask ft2 = new FakeTask(2);
        FakeTask ft3 = new FakeTask(3);
        FakeTask ft4 = new FakeTask(4);

        nodes.add(ft1);
        nodes.add(ft2);
        nodes.add(ft3);
        nodes.add(ft4);


        HashSet<DAG.Edge<FakeTask>> edges = new HashSet<>();
        DAG.Edge<FakeTask> e1 = new DAG.Edge<FakeTask>(ft1, ft2, null);
        DAG.Edge<FakeTask> e2 = new DAG.Edge<FakeTask>(ft1, ft3, null);
        DAG.Edge<FakeTask> e3 = new DAG.Edge<FakeTask>(ft2, ft3, null);

        edges.add(e1);
        edges.add(e2);
        edges.add(e3);
        // edges.add(e4);


        String nodesDefine = "[{\"id\":1}" +
                ",{\"id\":3}" +
                ",{\"id\":2}" +
                ",{\"id\":4}]";
        String edgesDefine = "[{\"src\":{\"id\":1},\"tgt\":{\"id\":3}}" +
                ",{\"src\":{\"id\":1},\"tgt\":{\"id\":2}}" +
                ",{\"src\":{\"id\":2},\"tgt\":{\"id\":3}}]";
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        Type nodesType = new TypeToken<HashSet<FakeTask>>() {
        }.getType();
        Type edgesType = new TypeToken<HashSet<DAG.Edge<FakeTask>>>() {
        }.getType();

        HashSet<FakeTask> nodesx = gson.fromJson(nodesDefine, nodesType);
        HashSet<DAG.Edge<FakeTask>> edgesx = gson.fromJson(edgesDefine, edgesType);

        System.out.println(nodesx);
        System.out.println(edgesx);

}

}
