package pers.machi.dag;

import com.google.gson.annotations.Expose;

public class Node {
    @Expose
    public int id = -1;
    public boolean isVisited = false;
}
