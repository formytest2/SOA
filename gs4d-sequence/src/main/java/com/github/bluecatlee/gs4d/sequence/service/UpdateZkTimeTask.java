package com.github.bluecatlee.gs4d.sequence.service;

import com.github.bluecatlee.gs4d.sequence.model.CreateSequence;
import com.github.bluecatlee.gs4d.sequence.utils.PropertiesUtil;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;

public class UpdateZkTimeTask extends TimerTask implements Watcher {
    protected static Logger logger = LoggerFactory.getLogger(UpdateZkTimeTask.class);

    public UpdateZkTimeTask() {
    }

    public void run() {
        try {
            if (!IniteZkConfigService.zk.getState().equals(KeeperState.SyncConnected) && !IniteZkConfigService.zk.getState().equals("CONNECTED")) {
                IniteZkConfigService.zk.close();
                IniteZkConfigService.zk = new ZooKeeper(IniteZkConfigService.zkAdress, IniteZkConfigService.sessionTimeout, this);
            }

            IniteZkConfigService.zk.exists(IniteZkConfigService.currentDayDate, true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    public void process(WatchedEvent event) {
        try {
            if (!event.getState().equals(KeeperState.SyncConnected)) {
                IniteZkConfigService.zk.close();
                IniteZkConfigService.zk = new ZooKeeper(IniteZkConfigService.zkAdress, IniteZkConfigService.sessionTimeout, this);
                IniteZkConfigService.zkState = "ERROE";
                return;
            }

            IniteZkConfigService.zk.exists(IniteZkConfigService.currentDayDate, true);
            Set<Entry<String, List<CreateSequence>>> entries = SequenceCliActionService.sequenceObjMap.entrySet();
            if (entries.size() > 0) {
                Iterator iterator = entries.iterator();

                while(iterator.hasNext()) {
                    Entry<String, List<CreateSequence>> entry = (Entry)iterator.next();
                    String key = (String)entry.getKey();
                    String path = IniteZkConfigService.seqFilePath + key + ".properties";
                    File file = new File(path);
                    if (file.exists()) {
                        Properties uSingSequence = PropertiesUtil.loadProps(path);
                        PropertiesUtil.updateAllPropertiesNull(uSingSequence, path);
                    }
                }
            }

            SequenceCliActionService.sequenceObjMap.clear();
            SequenceCliActionService.seqValueMap.clear();
            SequenceCliActionService.seqStoreStatusMap.clear();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }
}

