package pers.machi.task;

import com.google.gson.annotations.Expose;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pers.machi.server.CustomHttpServerHandler;

public class FakeTask extends Task {

    private final Logger logger = LogManager.getLogger(FakeTask.class);

    public FakeTask(int i) {
        id = i;
    }

    @Override
    void run() {

        logger.warn("task " + id + "started");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.warn("task " + id + "finished");
    }

    @Override
    public String toString() {
        return "FakeTask" + id;
    }
}
