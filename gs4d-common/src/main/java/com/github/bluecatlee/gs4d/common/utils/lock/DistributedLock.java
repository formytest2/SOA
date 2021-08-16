package com.github.bluecatlee.gs4d.common.utils.lock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class DistributedLock implements Lock, Watcher {
    private ZooKeeper zk;
    private String root = "/locks";
    private String lockName;
    private String waitNode;
    private String myZnode;
    private CountDownLatch latch;
    private int sessionTimeout = 3000;
    private List<Exception> exception = new ArrayList();

    public DistributedLock(String config, String lockName) {
        this.lockName = lockName;

        try {
            this.zk = new ZooKeeper(config, this.sessionTimeout, this);
            Stat stat = this.zk.exists(this.root, false);
            if (stat == null) {
                this.zk.create(this.root, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (IOException e) {
            this.exception.add(e);
        } catch (KeeperException e) {
            this.exception.add(e);
        } catch (InterruptedException e) {
            this.exception.add(e);
        }

    }

    public void process(WatchedEvent event) {
        if (this.latch != null) {
            this.latch.countDown();
        }

    }

    public void lock() {
        if (this.exception.size() > 0) {
            throw new DistributedLock.LockException((Exception)this.exception.get(0));
        } else {
            try {
                if (!this.tryLock()) {
                    this.waitForLock(this.waitNode, (long)this.sessionTimeout);
                }
            } catch (KeeperException e) {
                throw new DistributedLock.LockException(e);
            } catch (InterruptedException e) {
                throw new DistributedLock.LockException(e);
            }
        }
    }

    public boolean tryLock() {
        try {
            String splitStr = "_lock_";
            if (this.lockName.contains(splitStr)) {
                throw new DistributedLock.LockException("lockName can not contains _lock_");
            } else {
                this.myZnode = this.zk.create(this.root + "/" + this.lockName + splitStr, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
                List<String> subNodes = this.zk.getChildren(this.root, false);
                List<String> lockObjNodes = new ArrayList();
                Iterator iterator = subNodes.iterator();

                while(iterator.hasNext()) {
                    String node = (String)iterator.next();
                    String _node = node.split(splitStr)[0];
                    if (_node.equals(this.lockName)) {
                        lockObjNodes.add(node);
                    }
                }

                Collections.sort(lockObjNodes);
                if (this.myZnode.equals(this.root + "/" + (String)lockObjNodes.get(0))) {
                    return true;
                } else {
                    String subMyZnode = this.myZnode.substring(this.myZnode.lastIndexOf("/") + 1);
                    this.waitNode = (String)lockObjNodes.get(Collections.binarySearch(lockObjNodes, subMyZnode) - 1);
                    return false;
                }
            }
        } catch (KeeperException e) {
            throw new DistributedLock.LockException(e);
        } catch (InterruptedException e) {
            throw new DistributedLock.LockException(e);
        }
    }

    public boolean tryLock(long time, TimeUnit unit) {
        try {
            return this.tryLock() ? true : this.waitForLock(this.waitNode, time, unit);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean waitForLock(String lower, long waitTime, TimeUnit unit) throws InterruptedException, KeeperException {
        Stat stat = this.zk.exists(this.root + "/" + lower, true);
        if (stat != null) {
            this.latch = new CountDownLatch(1);
            this.latch.await(waitTime, unit);
            this.latch = null;
        }

        return true;
    }

    private boolean waitForLock(String lower, long waitTime) throws InterruptedException, KeeperException {
        Stat stat = this.zk.exists(this.root + "/" + lower, true);
        if (stat != null) {
            this.latch = new CountDownLatch(1);
            this.latch.await(waitTime, TimeUnit.MILLISECONDS);
            this.latch = null;
        }

        return true;
    }

    public void unlock() {
        try {
            this.zk.delete(this.myZnode, -1);
            this.myZnode = null;
            this.zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }

    }

    public void lockInterruptibly() throws InterruptedException {
        this.lock();
    }

    public Condition newCondition() {
        return null;
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

