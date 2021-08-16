package com.github.bluecatlee.gs4d.sequence.service;

import com.github.bluecatlee.gs4d.sequence.exception.SequenceException;
import com.github.bluecatlee.gs4d.sequence.model.CreateSequence;
import com.github.bluecatlee.gs4d.sequence.utils.SequenceConstant;
import com.github.bluecatlee.gs4d.sequence.utils.DubboImpl;
import com.github.bluecatlee.gs4d.sequence.utils.PropertiesUtil;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;

/**
 * 初始化服务
 */
public class IniteZkConfigService implements Watcher {

    protected static Logger logger = LoggerFactory.getLogger(IniteZkConfigService.class);
    public static ZooKeeper zk;
    public static String seqFilePath;
    public static int sessionTimeout = 30000;
    public static final Integer updateTimeSche = 21600000;
    public static ExecutorService executor;
    public static String currentDayDate = "/seqNode/currentDayDate";
    public static String seqNode = "/seqNode";
    public static String zkAdress;
    public static String zkState = "NORMAL";

    public IniteZkConfigService() {
    }

    public void initZkConfig(String addresses) throws SequenceException {
        Timer updateZkTime = null;

        try {
            SequenceCliActionService.setSequenceActionService((SequenceActionService) DubboImpl.initDubbo(addresses));
            zk = new ZooKeeper(addresses, sessionTimeout, this);
            zkAdress = addresses;
            SequenceCliActionService.locklists = new ArrayList();

            for(int i = 0; i < 50; ++i) {
                SequenceCliActionService.locklists.add(new Object());       // locklists中初始化为50个对象 作为锁
            }

            Stat root2 = zk.exists(currentDayDate, true);           // exist操作也可以注册监听
            if (root2 == null) {
                logger.error("没有寻找到节点" + currentDayDate);
                zk.create(seqNode, " ".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                zk.create(currentDayDate, " ".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

            File file = null;

            try {
                String path = Thread.currentThread().getContextClassLoader().getResource("").getPath(); // 获取classpath的绝对路径
                seqFilePath = path + SequenceConstant.SEQUENCE_PATH_WINDOWS;
                file = new File(seqFilePath);                       // 不同项目中使用工具类初始化到这时 获取到的classpath不一样 会在对应项目的classpath下生成该目录
                if (!file.exists() && !file.isDirectory()) {
                    file.mkdir();
                    logger.info("存储序列号的文件路径创建成功！");
                }

                updateZkTime = new Timer(true);
                updateZkTime.schedule(new UpdateZkTimeTask(), 0L, (long) updateTimeSche); // 创建定时器 定时执行任务(定时续命)

            } catch (Exception e) {
                logger.error("zk状态监听失败" + e.getMessage(), e);
            }

            this.buildThreadPool();
        } catch (Exception e) {
            throw new SequenceException(e.getMessage());
        }
    }

    /**
     * 处理节点监听事件 即currentDayDate发生变化时，清空properties文件和本地缓存  【有对currentDayDate的修改的定时任务】
     * @param event
     */
    public void process(WatchedEvent event) {
        try {
            if (!event.getState().equals(KeeperState.SyncConnected)) {
                zk.close();
                zk = new ZooKeeper(zkAdress, sessionTimeout, this);
                zkState = "ERROE";
                return;
            }

            zk.exists(currentDayDate, true);
            logger.info("zookeeper节点更新");
            Set<Entry<String, List<CreateSequence>>> entries = SequenceCliActionService.sequenceObjMap.entrySet();
            if (entries.size() > 0) {
                Iterator iterator = entries.iterator();

                while(iterator.hasNext()) {
                    Entry<String, List<CreateSequence>> entry = (Entry)iterator.next();
                    String key = (String)entry.getKey();
                    String path = seqFilePath + key + ".properties";
                    File file = new File(path);
                    if (file.exists()) {
                        Properties uSingSequence = PropertiesUtil.loadProps(path);
                        PropertiesUtil.updateAllPropertiesNull(uSingSequence, path);    // properties文件中值清空
                    }
                }
            }

            SequenceCliActionService.sequenceObjMap.clear();
            SequenceCliActionService.seqValueMap.clear();
            SequenceCliActionService.seqStoreStatusMap.clear();
        } catch (Exception e) {
            logger.error("zk时间更新出错" + e.getMessage(), e);
        }

    }

    /**
     * 创建线程池
     */
    private void buildThreadPool() {
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue();
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(3, 3, 0L, TimeUnit.MILLISECONDS, queue);
        executor = threadPool;
    }
}

