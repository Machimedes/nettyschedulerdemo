package pers.machi.message;

import pers.machi.flow.Flow;
import pers.machi.task.FakeTask;
import pers.machi.task.Task;

public class TaskFinishMessage extends Message {
    public Task task;

    public TaskFinishMessage(FakeTask fakeTask) {
        this.task = task;
    }
}