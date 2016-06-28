package shbd.download.dao;

import java.util.List;

import shbd.download.bean.ThreadBean;

/**
 * Created by yh on 2016/6/28.
 * 下载文件的接口
 */
public interface DownloadInterface {
    /**
     * 插入线程信息
     *
     * @param threadBean
     */
    public void insertThread(ThreadBean threadBean);

    /**
     * 删除线程信息
     * @param url
     * @param threadId
     */
    public void deleteThread(String url, int threadId);

    /**
     * 更新下载进度
     * @param url
     * @param threadId
     * @param finished
     */
    public void updateThread(String url, int threadId, int finished);

    /**
     * 获取所有线程信息
     * @param url
     * @return
     */
    public List<ThreadBean> getThreads(String url);

    /**
     * 线路线程是否在数据库存在
     * @param url
     * @param threadId
     * @return
     */
    public boolean isExists(String url, int threadId);
}
