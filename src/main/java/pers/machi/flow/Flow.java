package pers.machi.flow;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pers.machi.dag.DAG;
import pers.machi.dag.Node;
import pers.machi.message.FlowStartMessage;
import pers.machi.message.Message;
import pers.machi.task.FakeTask;
import pers.machi.urimapper.FlowMapper;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.LinkedBlockingDeque;

public class Flow<Task extends Node> {

    private final Logger logger = LogManager.getLogger(Flow.class);

    DAG<Task> dag;
    LinkedBlockingDeque<Message> inbox;
    String flowDefine;
    FlowRegistry fr = FlowRegistry.getInstance();

    public Flow(String flowDefine) {
        this.flowDefine = flowDefine;
        inbox = new LinkedBlockingDeque<>();
    }

    public static <Task extends Node> void createAndRegisterFlow(String flowDefine) {
        new Flow<Task>(flowDefine).register();
    }

    public void register() {
        fr.addFlow(this);
        fr.send(new FlowStartMessage(this));
    }

    public void initDagx() {
        String nodesDefine = flowDefine.split("&")[0];
        String edgesDefine = flowDefine.split("&")[1];

        logger.error(nodesDefine);
        logger.error(edgesDefine);

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        Type nodesType = new TypeToken<HashSet<FakeTask>>() {
        }.getType();
        Type edgesType = new TypeToken<HashSet<DAG.Edge<FakeTask>>>() {
        }.getType();

        Collection<Task> nodes = gson.fromJson(nodesDefine, nodesType);
        HashSet<DAG.Edge<Task>> edges = gson.fromJson(edgesDefine, edgesType);

        DAG.DAGxBuilder<Task> dagxBuilder = DAG.DAGxBuilder.builder();

        dag = dagxBuilder.addNodes(nodes).addEdges(edges).build();
    }

    public void start() {
        fr.submit(new FlowDispatcher(this));
    }


}
