package com.msb.lock;


import lombok.Data;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
* @desc    
* @version 1.0
* @author  Liang Jun
* @date    2020年02月06日 23:56:59
**/
@Data
public class CallbackWatcher implements AsyncCallback.StringCallback, AsyncCallback.Children2Callback, Watcher, AsyncCallback.StatCallback {
    private ZooKeeper client;
    private String threadName;
    private String pathName;
    private CountDownLatch latch = new CountDownLatch(1);

    public void tryLock() throws InterruptedException {
        System.out.println(threadName + ", create...");
        client.create("/lock", threadName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL, this, "AAAA");
        latch.await();
    }

    @Override
    public void processResult(int i, String s, Object o, String name) {
        if (name != null) {
            System.out.println(threadName + ", " + name);
            pathName = name;
            client.getChildren("/", false, this, "BBBB");
        }
    }

    @Override
    public void processResult(int i, String s, Object o, List<String> list, Stat stat) {
        Collections.sort(list);
        int idx = list.indexOf(pathName.substring(1));

        //如果是第一个直接获得锁
        if (idx == 0) {
            try {
                System.out.println(threadName + ", I'm the first.");
                client.setData("/", threadName.getBytes(), -1);
                latch.countDown();
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            client.exists("/" + list.get(idx - 1), this, this, "DDDD");
        }
    }

    @Override
    public void processResult(int i, String s, Object o, Stat stat) {

    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        switch (watchedEvent.getType()) {
            case None:
                break;
            case NodeCreated:
                break;
            case NodeDeleted:
                client.getChildren("/", false, this, "BBBB");
                break;
            case NodeDataChanged:
                break;
            case NodeChildrenChanged:
                break;
        }
    }

    public void unLock() throws KeeperException, InterruptedException {
        System.out.println(threadName + ", work over.");
        client.delete(pathName, -1);
    }
}