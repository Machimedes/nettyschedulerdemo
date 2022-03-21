package pers.machi.flow;

import pers.machi.dag.Indegree;
import pers.machi.dag.Node;
import pers.machi.message.TaskFinishMessage;
import pers.machi.task.Task;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class FlowDispatcher implements Callable<Integer> {
    public Flow<Task> flow;

    public FlowDispatcher(Flow flow) {
        this.flow = flow;
    }

    public AtomicBoolean isFirstDispatch = new AtomicBoolean(true);

    @Override
    public Integer call() throws Exception {

        final HashMap<Task, HashMap<Task, Object>> adjacentList = flow.dag.adjacentList;
        final HashMap<Task, Indegree> indegreeMap = flow.dag.indegreeMap;
        if(isFirstDispatch.getAndSet(false)){
            for(Task task :flow.dag.isolatedNodes){
                task.run();
            }
        }

        while (true) {
            Object message = flow.inbox.take();
            if (message instanceof TaskFinishMessage) {
                Task finishedTask = ((TaskFinishMessage) message).task;
                for (Task pointedTask : (Set<Task>) adjacentList.get(finishedTask).keySet()) {
                    Indegree indegree = (Indegree) indegreeMap.get(pointedTask);
                    indegree.decrease();
                    if(indegree.get()==0){
                        pointedTask.run();
                    }
                }
            }
        }
    }
}
