package pers.machi.task;

import com.google.gson.annotations.Expose;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pers.machi.server.CustomHttpServerHandler;

public class FakeTask extends Task {

    private static Logger logger = LogManager.getLogger(FakeTask.class);

    public FakeTask(int i) {
        id = i;


    }

    public Task run() {
        logger.error("task " + id + " started");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("task " + id + " finished");
        return this;
    }


    @Override
    public String toString() {
        return "FakeTask" + id;
    }

}
