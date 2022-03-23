package pers.machi.flow;

import java.util.concurrent.*;

public class FlowContext {
    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    public FlowContext() {
    }

    Future<Integer> submit(Callable<Integer> callable) {
        return singleThreadExecutor.submit(callable);
    }
}
