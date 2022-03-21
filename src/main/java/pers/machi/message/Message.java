package pers.machi.message;

public class Message {
    public int id;

    @Override
    public String toString() {
        return this.getClass().getName() + id;
    }
}
