package pers.machi.flow;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pers.machi.message.FlowStartMessage;
import pers.machi.message.Message;

import java.util.HashMap;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class FlowRegistry {
    private final Logger logger = LogManager.getLogger(FlowRegistry.class);

    private final ReentrantLock lock = new ReentrantLock();
    private final LinkedBlockingDeque<Message> inbox = new LinkedBlockingDeque<>();
    private final MessageLoop messageLoop = this.new MessageLoop();

    private final HashMap<Flow<?>, Future<?>> flows = new HashMap<>();
    private static final FlowRegistry instance = new FlowRegistry();

    private FlowRegistry() {
        messageLoop.start();
    }

    public static FlowRegistry getInstance() {
        return instance;
    }

    public static void init() {
        assert getInstance().messageLoop.isAlive();
    }

    public void addFlow(Flow<?> flow) {
        try {
            lock.lock();
            flows.put(flow, null);
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void send(Message message) {
        inbox.offer(message);
    }

    private class MessageLoop extends Thread {
        private MessageLoop() {
        }

        @Override
        public void run() {
            while (true) {
                Message message = null;
                try {
                    message = inbox.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (message instanceof FlowStartMessage) {
                    ((FlowStartMessage) message).flow.initDagx();
                    ((FlowStartMessage) message).flow.start();
                }
            }
        }
    }

    public void submit(Flow flow) {
        try {
            lock.lock();
            flows.put(flow, flow.flowContext.submit(new FlowDispatcher(flow)));
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


}
