package pers.machi;

import pers.machi.flow.FlowRegistry;
import pers.machi.server.NettyHttpServerSingleton;
import pers.machi.urimapper.UriMapperManagement;

public class NettySchedulerDemo {

    public static void main(String[] args) {
        contextPreparation();
        final UriMapperManagement uriMappingManagement = UriMapperManagement.getInstance();
        NettyHttpServerSingleton.getInstance().run();


    }

    public static void contextPreparation() {
        FlowRegistry.init();
    }
}
