package pers.machi.dag;

import pers.machi.flow.FlowRegistry;
import pers.machi.message.FlowStartMessage;
import pers.machi.message.Message;

public class FlowRegistryTest {
    public static void main(String[] args) {

        FlowRegistry.init();

        new Thread() {
            FlowRegistry fr = FlowRegistry.getInstance();

            @Override
            public void run() {
                while (true) {
                    System.out.println("send");

                    fr.send(new FlowStartMessage(null));
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }
}
