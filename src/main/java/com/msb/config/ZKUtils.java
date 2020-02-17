package com.msb.config;


import lombok.Data;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
* @desc    
* @version 1.0
* @author  Liang Jun
* @date    2020年02月06日 20:34:51
**/
@Data
public class ZKUtils {
    final private static String url = "192.168.24.128:2181,192.168.24.129:2181,192.168.24.130:2181,192.168.24.131:2181/testConfig";
    private static ZooKeeper client;

    public static ZooKeeper getClient() throws IOException {
        DefaultWatcher watcher = new DefaultWatcher();
        AtomicBoolean created = new AtomicBoolean(false);

        watcher.setCreated(created);
        client = new ZooKeeper(url, 3000, watcher);

        while (!created.get()) {
        }

        return client;
    }
}