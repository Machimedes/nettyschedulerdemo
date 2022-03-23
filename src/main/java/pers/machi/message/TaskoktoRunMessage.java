package pers.machi.message;

import pers.machi.task.Task;

public class TaskoktoRunMessage extends Message {
    public Task task;

    public TaskoktoRunMessage(Task task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return "TaskoktoRun: " + task;
    }
}