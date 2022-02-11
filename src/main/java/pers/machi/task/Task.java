package pers.machi.task;

import pers.machi.dag.Node;

public abstract class Task extends Node {
    abstract void run();
}
