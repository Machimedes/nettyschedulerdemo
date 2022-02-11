package pers.machi.dag;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import pers.machi.task.FakeTask;

import java.lang.reflect.Type;
import java.util.HashSet;

public class JSONSerializeTest {
    public static void main(String[] args) {
        HashSet<FakeTask> nodes = new HashSet<>();
        FakeTask ft1 = new FakeTask(1);
        FakeTask ft2 = new FakeTask(2);
        nodes.add(ft1);


        HashSet<DAGx.Edge<FakeTask>> edges = new HashSet<>();
        DAGx.Edge<FakeTask> e1 = new DAGx.Edge<FakeTask>(ft1, ft2, null);

        edges.add(e1);


        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        System.out.println(gson.toJson(nodes));
        Type edgeType = new TypeToken<HashSet<DAGx.Edge<FakeTask>>>() {
        }.getType();
        System.out.println(e1);

        System.out.println(gson.toJson(edges, edgeType));

    }
}
