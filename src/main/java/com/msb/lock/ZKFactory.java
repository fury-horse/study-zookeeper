package com.msb.lock;


import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
* @desc    
* @version 1.0
* @author  Liang Jun
* @date    2020年02月06日 23:52:34
**/
public class ZKFactory {
    private static String url = "192.168.24.128:2181,192.168.24.129:2181,192.168.24.130:2181,192.168.24.131:2181/testLock";
    private static ZooKeeper client;

    public static ZooKeeper getZK() throws IOException, InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        client = new ZooKeeper(url, 3000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                switch (watchedEvent.getState()) {
                    case Unknown:
                        break;
                    case Disconnected:
                        break;
                    case NoSyncConnected:
                        break;
                    case SyncConnected:
                        latch.countDown();
                        break;
                    case AuthFailed:
                        break;
                    case ConnectedReadOnly:
                        break;
                    case SaslAuthenticated:
                        break;
                    case Expired:
                        break;
                }
            }
        });
        latch.await();
        return client;
    }
}