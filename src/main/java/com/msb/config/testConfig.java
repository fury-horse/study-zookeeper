package com.msb.config;


import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
* @desc    
* @version 1.0
* @author  Liang Jun
* @date    2020年02月06日 20:31:17
**/
public class testConfig {
    private ZooKeeper zkClient;
    @Before
    public void conn() throws IOException {
        zkClient = ZKUtils.getClient();
    }

    @After
    public void close() throws InterruptedException {
        zkClient.close();
    }

    @Test
    public void test() throws InterruptedException {
        CallbackWatcher watcher = new CallbackWatcher();
        watcher.setZkClient(zkClient);
        AppConfig appConfig = new AppConfig();
        watcher.setAppConfig(appConfig);

        watcher.loadData();

        while (true) {
            if (appConfig.getData() == null) {
                System.out.println("配置信息不存在...");
                watcher.loadData();
            }
            System.out.println(appConfig.getData());
            Thread.sleep(1000);
        }
    }
}