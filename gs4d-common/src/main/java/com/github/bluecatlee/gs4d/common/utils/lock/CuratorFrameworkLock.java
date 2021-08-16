package com.github.bluecatlee.gs4d.common.utils.lock;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLBackgroundPathAndBytesable;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CuratorFrameworkLock {

    private static String root = "/locks";
    private String lockName;
    private String waitNode;
    private String myZnode;
    private CountDownLatch latch;
    private int sessionTimeout = 3000;
    private List<Exception> exception = new ArrayList();

    private static ThreadLocal<String> lockRoot = new ThreadLocal<String>() {
        public String initialValue() {
            return "";
        }
    };
    private static ThreadLocal<List<String>> lockRoots = new ThreadLocal<List<String>>() {
        public List<String> initialValue() {
            return new ArrayList();
        }
    };
    private static ThreadLocal<Map<String, InterProcessMutex>> lockMap = new ThreadLocal<Map<String, InterProcessMutex>>() {
        public HashMap<String, InterProcessMutex> initialValue() {
            return new HashMap();
        }
    };

    public static CuratorFramework work;
    private Integer retryTime = 1000;
    private Integer retryNum = 3;
    public static CuratorFramework newClientDemo = null;
    private static TreeCache treeCache;

    CuratorFrameworkLock(){}

    public static CuratorFramework initeClient(final String zkAddress) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(5000, 3);
        newClientDemo = CuratorFrameworkFactory.newClient(zkAddress, retryPolicy);
        newClientDemo.start();
        treeCache = new TreeCache(newClientDemo, root);
        treeCache.getListenable().addListener(new TreeCacheListener() {
            public void childEvent(CuratorFramework client, TreeCacheEvent event) {
                if (event.getType().name().equals("CONNECTION_SUSPENDED") || event.getType().name().equals("CONNECTION_LOST")) {
                    CuratorFrameworkLock.newClientDemo.close();
                    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
                    CuratorFrameworkLock.newClientDemo = CuratorFrameworkFactory.newClient(zkAddress, retryPolicy);
                    CuratorFrameworkLock.newClientDemo.start();
                }

            }
        });
        treeCache.start();
        return newClientDemo;
    }

    public CuratorFrameworkLock(CuratorFramework zk, String lockName) throws Exception {
        lockRoot.set(lockName);

        try {
            work = zk;
            new String((byte[])work.getData().forPath(root));
        } catch (KeeperException e) {
            ((ACLBackgroundPathAndBytesable)work.create().withMode(CreateMode.PERSISTENT)).forPath(root, "".getBytes());    // 初始化root路径
        } catch (InterruptedException e) {
            this.exception.add(e);
        }

    }

    public CuratorFrameworkLock(String zkAddress, String lockName) throws Exception {
        if (newClientDemo == null) {
            initeClient(zkAddress);
        }

        CuratorFrameworkState curatorFrameworkState = newClientDemo.getState();
        newClientDemo.getState();
        if (!curatorFrameworkState.equals(CuratorFrameworkState.STARTED)) {
            newClientDemo.close();

            try {
                newClientDemo = initeClient(zkAddress);
            } catch (Exception e) {
                throw new Exception("分布式锁初始化失败.");
            }
        }

        lockRoot.set(lockName);
        work = newClientDemo;
    }

    public void curatorLock() throws Exception {
        if (this.exception.size() > 0) {
            throw new CuratorFrameworkLock.LockException((Exception)this.exception.get(0));
        } else {
            try {
                if (this.tryCuratorLock()) {
                    System.out.println("Thread " + Thread.currentThread().getId() + " " + this.myZnode + " get lock true");
                } else {
                    this.waitForCuratorLock(this.waitNode, (long)this.sessionTimeout);
                }
            } catch (KeeperException e) {
                throw new CuratorFrameworkLock.LockException(e);
            } catch (InterruptedException e) {
                throw new CuratorFrameworkLock.LockException(e);
            }
        }
    }

    public boolean tryCuratorLock() throws Exception {
        InterProcessMutex sharedLock = new InterProcessMutex(work, root + "/" + (String)lockRoot.get());
        boolean acquired = sharedLock.acquire(1L, TimeUnit.MILLISECONDS);
        if (acquired) {
            ((Map)lockMap.get()).put(lockRoot.get(), sharedLock);
        }

        return acquired;
    }

    public boolean tryCuratorLock(long time, TimeUnit unit) {
        try {
            return this.tryCuratorLock() ? true : this.waitForCuratorLock(this.waitNode, time);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean waitForCuratorLock(String lower, long waitTime) throws Exception {
        System.out.println("Thread " + Thread.currentThread().getId() + " waiting for " + root + "/" + lower);
        this.latch = new CountDownLatch(1);
        this.latch.await(waitTime, TimeUnit.MILLISECONDS);
        this.latch = null;
        return this.tryCuratorLock();
    }

    public void unCuratorlock() {
        try {
            if (((Map)lockMap.get()).get(lockRoot.get()) != null) {
                ((InterProcessMutex)((Map)lockMap.get()).get(lockRoot.get())).release();
                ((Map)lockMap.get()).remove(lockRoot.get());
                lockRoot.remove();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {
        new CuratorFrameworkLock();
        CuratorFrameworkLock lock = new CuratorFrameworkLock("192.168.26.77:2181", "qqq");

        try {
            if (!lock.tryCuratorLock(2000L, (TimeUnit)null)) {
                throw new Exception("分布式锁超时");
            }

            Thread.sleep(6000L);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            if (lock != null) {
                lock.unCuratorlock();
            }

        }

        System.in.read();
    }

    public String CuratorFrameworkGetUUidByLock() throws Exception {
        String returnIp = "";

        try {
            returnIp = new String((byte[])work.getData().forPath(root + "/" + (String)lockRoot.get()));
            List<String> paths = (List)work.getChildren().forPath(root + "/" + (String)lockRoot.get());

            String path;
            for(Iterator iterator = paths.iterator(); iterator.hasNext(); returnIp = new String((byte[])work.getData().forPath(root + "/" + (String)lockRoot.get() + "/" + path))) {
                path = (String)iterator.next();
            }
        } catch (KeeperException e) {
            ((ACLBackgroundPathAndBytesable)work.create().withMode(CreateMode.PERSISTENT)).forPath(root, "".getBytes());
        } catch (InterruptedException e) {
            this.exception.add(e);
        }

        return returnIp;
    }

    public String CuratorFrameworkSetUUidByLock(String uuid) throws Exception {
        String returnIp = "";

        try {
            returnIp = new String((byte[])work.getData().forPath(root + "/" + (String)lockRoot.get()));
            List<String> paths = (List)work.getChildren().forPath(root + "/" + (String)lockRoot.get());
            Iterator iterator = paths.iterator();

            while(iterator.hasNext()) {
                String path = (String)iterator.next();
                work.setData().forPath(root + "/" + (String)lockRoot.get() + "/" + path, uuid.getBytes());
            }
        } catch (KeeperException e) {
            ((ACLBackgroundPathAndBytesable)work.create().withMode(CreateMode.PERSISTENT)).forPath(root, "".getBytes());
        } catch (InterruptedException e) {
            this.exception.add(e);
        }

        return returnIp;
    }

    public Boolean islock() throws Exception {
        if (work == null) {
            throw new RuntimeException("在查看是否上锁前,请先初始化zk的客户端,初始化方法为 CuratorFrameworkLock(CuratorFramework zk, String lockName)");
        } else {
            Boolean islock = true;

            try {
                List<String> paths = (List)work.getChildren().forPath(root + "/" + (String)lockRoot.get());
                if (paths.isEmpty()) {
                    islock = false;
                }
            } catch (KeeperException e) {
                if (e.getMessage().contains("NoNode")) {
                    islock = false;
                }
            } catch (InterruptedException e) {
                this.exception.add(e);
            }

            return islock;
        }
    }

    public class LockException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public LockException(String e) {
            super(e);
        }

        public LockException(Exception e) {
            super(e);
        }
    }
}

