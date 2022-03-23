package pers.machi.dag;

import pers.machi.message.Message;
import pers.machi.message.TaskFinishMessage;
import pers.machi.message.TaskoktoRunMessage;
import pers.machi.task.FakeTask;
import pers.machi.task.Task;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.*;

public class Test implements Callable<Integer> {
    public static LinkedBlockingDeque<Message> inbox = new LinkedBlockingDeque<>();
    public static ExecutorService taskexecutorService = Executors.newFixedThreadPool(100);

    public Integer call() throws Exception {
        inbox.offer(new TaskoktoRunMessage(new FakeTask(1)));

        while (true) {
            Object message = inbox.take();
            if (message instanceof TaskoktoRunMessage) {
                System.out.println(message);

                final FakeTask oktoRunTask = (FakeTask) ((TaskoktoRunMessage) message).task;

                CompletableFuture<?> completableFuture = CompletableFuture.supplyAsync(
                        oktoRunTask::run, taskexecutorService)
                        .thenAccept(System.out::println);


            } else {
                System.out.println("do something else");
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Test t = new Test();
        Executors.newSingleThreadExecutor().submit(t);
    }
}
