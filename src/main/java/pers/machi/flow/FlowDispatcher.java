package pers.machi.flow;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pers.machi.dag.Indegree;
import pers.machi.message.TaskFinishMessage;
import pers.machi.message.TaskoktoRunMessage;
import pers.machi.task.FakeTask;
import pers.machi.task.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class FlowDispatcher implements Callable<Integer> {
    private final Logger logger = LogManager.getLogger(FlowDispatcher.class);
    AtomicInteger finishedTaskCount = new AtomicInteger(0);


    public static ExecutorService taskexecutorService = Executors.newFixedThreadPool(100);
    public Flow<Task> flow;

    public FlowDispatcher(Flow flow) {
        this.flow = flow;
    }

    public AtomicBoolean isFirstDispatch = new AtomicBoolean(true);

    @Override
    public Integer call() throws Exception {
        final HashMap<Task, HashMap<Task, Object>> adjacentList = flow.dag.adjacentList;
        final HashMap<Task, Indegree> indegreeMap = flow.dag.indegreeMap;

        if (isFirstDispatch.getAndSet(false)) {
            for (Task task : flow.dag.isolatedNodes) {
                flow.inbox.offer(new TaskoktoRunMessage(task.setFlow(flow)));
            }
            for (Map.Entry<Task, Indegree> entry : flow.dag.indegreeMap.entrySet()) {
                if (entry.getValue().isZero())
                    flow.inbox.offer(new TaskoktoRunMessage(entry.getKey().setFlow(flow)));
            }
        }

        while (true) {
            Object message = flow.inbox.take();
            logger.error(message);
            if (message instanceof TaskoktoRunMessage) {
                final Task oktoRunTask = (FakeTask) ((TaskoktoRunMessage) message).task;
                oktoRunTask.setFlow(flow).runAsync();
            } else if (message instanceof TaskFinishMessage) {
                finishedTaskCount.getAndIncrement();
                if (finishedTaskCount.get() == indegreeMap.size() + flow.dag.isolatedNodes.size())
                    break;

                Task finishedTask = ((TaskFinishMessage) message).task;
                if (flow.dag.isolatedNodes.contains(finishedTask))
                    continue;
                for (Task pointedTask : (Set<Task>) adjacentList.get(finishedTask).keySet()) {
                    Indegree indegree = (Indegree) indegreeMap.get(pointedTask);
                    indegree.decrease();
                    if (indegree.isZero()) {
                        pointedTask.setFlow(flow).runAsync();
                    }
                }
            }
        }
        System.out.println("flow finished");
        return finishedTaskCount.get();
    }
}
