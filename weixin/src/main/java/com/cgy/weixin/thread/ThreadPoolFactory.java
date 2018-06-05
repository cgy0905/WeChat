package com.cgy.weixin.thread;

import java.util.concurrent.ThreadFactory;

/**
 * Created by cgy
 * 2018/6/4  15:32
 * 线程池工厂
 */
public class ThreadPoolFactory {

    static ThreadPoolProxy mNormalPool;
    static ThreadPoolProxy mDownloadPool;

    /**
     * 得到一个普通的线程池
     * @return
     */
    public static ThreadPoolProxy getNormalPool () {
        if (mNormalPool == null) {
            synchronized (ThreadFactory.class) {
                if (mNormalPool == null) {
                    mNormalPool = new ThreadPoolProxy(5, 5, 3000);
                }
            }
        }
        return mNormalPool;
    }

    /**
     * 得到一个下载的头像
     * @return
     */
    public static ThreadPoolProxy getDownloadPool () {
        if (mDownloadPool == null) {
            synchronized (ThreadPoolFactory.class) {
                if (mDownloadPool == null) {
                    mDownloadPool = new ThreadPoolProxy(3, 3, 3000);
                }
            }
        }
        return mDownloadPool;
    }
}
