package com.horstmann.violet.application.gui.util.chenzuo.Bean;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by geek on 2017/8/12.
 */
public class IPNode {
    private String ip;
    private static ReentrantLock lock = new ReentrantLock();

    public IPNode(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

}
