package pers.machi.flow;

import pers.machi.dag.DAGx;
import pers.machi.dag.Node;
import pers.machi.message.FlowStartMessage;
import pers.machi.message.Message;

import java.util.concurrent.LinkedBlockingDeque;

public class Flow<Task extends Node> {
    DAGx<Task> dagx;
    LinkedBlockingDeque<Message> inbox;
    String flowDefine;
    FlowRegistry fr = FlowRegistry.getInstance();

    public Flow(String flowDefine) {
        this.flowDefine = flowDefine;
    }

    public static <Task extends Node> Flow<Task> createFlow(String flowDefine) {
        return new Flow<Task>(flowDefine);
    }

    public void register() {
        fr.addFlow(this);
        fr.send(new FlowStartMessage(this));
    }

    public void initDagx() {


    }

    public void start() {
    }


}
