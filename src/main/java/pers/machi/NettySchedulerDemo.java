package pers.machi;

import pers.machi.server.NettyHttpServerSingleton;
import pers.machi.urimapper.UriMapperManagement;

public class NettySchedulerDemo {

    public static void main(String[] args) {

        final UriMapperManagement uriMappingManagement = UriMapperManagement.getInstance();
        NettyHttpServerSingleton.getInstance().run();


    }
}
