package pers.machi.message;

import pers.machi.flow.Flow;
import pers.machi.task.FakeTask;
import pers.machi.task.Task;

public class TaskFinishMessage extends Message {
    public Task task;

    public TaskFinishMessage(Task task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return "TaskFinishMessage: " + task.id;
    }
}