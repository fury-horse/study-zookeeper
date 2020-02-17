package com.msb.lock;


import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
* @desc    
* @version 1.0
* @author  Liang Jun
* @date    2020年02月06日 23:51:34
**/
public class TestLock {
    private ZooKeeper zk;

    @Before
    public void conn() throws IOException, InterruptedException {
        zk = ZKFactory.getZK();
    }

    @After
    public void close() throws InterruptedException {
        zk.close();
    }

    @Test
    public void test() throws IOException {
        for (int i=0; i<10; i++) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        CallbackWatcher watcher = new CallbackWatcher();
                        watcher.setClient(zk);
                        watcher.setThreadName(Thread.currentThread().getName());

                        watcher.tryLock();

                        System.out.println(Thread.currentThread().getName() + ", work....");
                        Thread.sleep(1000);

                        watcher.unLock();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        System.in.read();
    }
}