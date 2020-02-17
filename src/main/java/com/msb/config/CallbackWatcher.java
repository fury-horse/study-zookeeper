package com.msb.config;


import lombok.Data;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
* @desc    
* @version 1.0
* @author  Liang Jun
* @date    2020年02月06日 20:49:53
**/
@Data
public class CallbackWatcher implements Watcher, AsyncCallback.StatCallback, AsyncCallback.DataCallback {
    private ZooKeeper zkClient;
    private AppConfig appConfig;
    private CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void process(WatchedEvent watchedEvent) {
        switch (watchedEvent.getType()) {
            case None:
                break;
            case NodeCreated:
                zkClient.getData("/AppConfig", this, this, "CCCC");
                break;
            case NodeDeleted:
                appConfig.setData(null);
                latch = new CountDownLatch(1);
                break;
            case NodeDataChanged:
                zkClient.getData("/AppConfig", this, this, "DDDD");
                latch = new CountDownLatch(1);
                break;
            case NodeChildrenChanged:
                break;
        }
    }

    @Override
    public void processResult(int i, String s, Object o, Stat stat) {
        if (stat != null) {
            zkClient.getData("/AppConfig", this, this, "BBBB");
        }
    }

    @Override
    public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
        String data = new String(bytes);
        appConfig.setData(data);
        latch.countDown();
    }

    public void loadData() throws InterruptedException {
        zkClient.exists("/AppConfig", this, this, "AAAA");
        latch.await();
    }
}