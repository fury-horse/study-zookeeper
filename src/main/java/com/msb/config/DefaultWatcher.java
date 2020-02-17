package com.msb.config;


import lombok.Data;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.atomic.AtomicBoolean;

/**
* @desc    
* @version 1.0
* @author  Liang Jun
* @date    2020年02月06日 20:41:04
**/
@Data
public class DefaultWatcher implements Watcher {
    private AtomicBoolean created;

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
                created.set(true);
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
}