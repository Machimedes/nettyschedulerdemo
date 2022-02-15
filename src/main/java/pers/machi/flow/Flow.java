package pers.machi.flow;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import pers.machi.dag.DAGx;
import pers.machi.dag.Node;
import pers.machi.message.FlowStartMessage;
import pers.machi.message.Message;
import pers.machi.task.FakeTask;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.LinkedBlockingDeque;

public class Flow<Task extends Node> {
    DAGx<Task> dagx;
    LinkedBlockingDeque<Message> inbox;
    String flowDefine;
    FlowRegistry fr = FlowRegistry.getInstance();


    public Flow(String flowDefine) {
        this.flowDefine = flowDefine;
        inbox = new LinkedBlockingDeque<>();
    }

    public static <Task extends Node> Flow<Task> createFlow(String flowDefine) {
        return new Flow<Task>(flowDefine);
    }

    public void register() {
        fr.addFlow(this);
        fr.send(new FlowStartMessage(this));
    }

    public void initDagx() {
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
        Type edgesType = new TypeToken<HashSet<DAGx.Edge<FakeTask>>>() {
        }.getType();

        Collection<Task> nodes = gson.fromJson(nodesDefine, nodesType);
        HashSet<DAGx.Edge<Task>> edges = gson.fromJson(edgesDefine, edgesType);

        DAGx.DAGxBuilder<Task> dagxBuilder = DAGx.DAGxBuilder.builder();

        dagx = dagxBuilder.addNodes(nodes).addEdges(edges).build();
    }

    public void start() {



    }


}
