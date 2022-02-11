package pers.machi.message;

import pers.machi.flow.Flow;
import pers.machi.message.Message;

public class FlowStartMessage extends Message {
    public Flow<?> flow;

    public FlowStartMessage(Flow<?> flow) {
        this.flow = flow;
    }
}
