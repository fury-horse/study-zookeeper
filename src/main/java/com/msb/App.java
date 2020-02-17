package com.msb;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    static volatile boolean isRun = false;

    public static void main( String[] args ) throws IOException, InterruptedException, KeeperException {
        System.out.println( "Hello World!" );

        final ZooKeeper zooKeeper = new ZooKeeper(
                "192.168.24.128:2181,192.168.24.129:2181,192.168.24.130:2181,192.168.24.131:2181",
                5000,
                new Watcher() {
                    @Override
                    public void process(WatchedEvent event) {
                        System.out.println("new zk watch bgn----------------->");
                        System.out.println("new zk watch: " + event);
                        System.out.println("path : " + event.getPath());
                        System.out.println("type : " + event.getType());
                        System.out.println("state : " + event.getState());
                        System.out.println("new zk watch end----------------->");

                        switch (event.getState()) {
                            case Unknown: break;
                            case Disconnected: break;
                            case NoSyncConnected: break;
                            case SyncConnected: isRun = true; break;
                            case AuthFailed: break;
                            case ConnectedReadOnly: break;
                            case SaslAuthenticated: break;
                            case Expired: break;
                        }
                    }
                }
        );

        while (!isRun) {
            Thread.sleep(200);
            System.out.print(".");
        }
        ZooKeeper.States state = zooKeeper.getState();
        switch (state) {
            case CONNECTING: break;
            case ASSOCIATING: break;
            case CONNECTED: break;
            case CONNECTEDREADONLY: break;
            case CLOSED: break;
            case AUTH_FAILED: break;
            case NOT_CONNECTED: break;
        }
        System.out.println("lunche zk state : " + state);

        zooKeeper.create("/p1", "1111".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        final Stat stat = new Stat();
        byte[] data = zooKeeper.getData("/p1", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("get data watch bgn----------------->");
                System.out.println("getData watch:" + event);
                System.out.println("get data watch end----------------->");
                try {
                    zooKeeper.getData("/p1", this, stat);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, stat);

        System.out.println("getData.result = " + new String(data));

        //第一次触发回调
        Stat stat1 = zooKeeper.setData("/p1", "1111-1".getBytes(), stat.getVersion());
        zooKeeper.setData("/p1", "1111-2".getBytes(), stat1.getVersion());


        System.out.println("---------------async call bgn---------------");

        zooKeeper.getData("/p1", false, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
                System.out.println("---------------async call ！！！---------------");
                System.out.println(i);
                System.out.println(s);
                System.out.println(o);
                System.out.println(new String(bytes));
            }
        }, "AAAA");

        System.out.println("---------------async call end---------------");

        System.in.read();
    }
}
