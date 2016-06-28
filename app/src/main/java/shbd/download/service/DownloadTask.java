package shbd.download.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import shbd.download.bean.FileBean;
import shbd.download.bean.ThreadBean;
import shbd.download.dao.DownLoadImpl;
import shbd.download.dao.DownloadInterface;

/**
 * Created by yh on 2016/6/28.
 * 下载任务
 */
public class DownloadTask {
    private Context mContext;
    private FileBean mFileBean;
    private DownloadInterface mDownLoadImpl;
    private ThreadBean mThreadBean;

    //当前下载进度
    private int mFinished;

    //是否暂停下载
    public boolean isPause = false;

    public DownloadTask(Context context, FileBean fileBean) {
        this.mContext = context;
        this.mFileBean = fileBean;
        this.mDownLoadImpl = new DownLoadImpl(context);
    }

    /**
     * 下载文件
     */
    public void download() {
        List<ThreadBean> threads = mDownLoadImpl.getThreads(mFileBean.getUrl());
        if (threads.size() > 0) {
            mThreadBean = threads.get(0);
        } else {
            mThreadBean = new ThreadBean(mFileBean.getId(), mFileBean.getUrl(), 0, mFileBean.getLength(), 0);
        }

        DownLoadThread thread = new DownLoadThread();
        thread.start();

    }

    /**
     * 下载文件的线程
     */
    class DownLoadThread extends Thread {
        @Override
        public void run() {
            //将线程信息保存到数据库中
            if (!mDownLoadImpl.isExists(mThreadBean.getUrl(), mThreadBean.getId())) {
                mDownLoadImpl.insertThread(mThreadBean);
            }

            HttpURLConnection con = null;
            RandomAccessFile raf = null;
            try {
                URL url = new URL(mThreadBean.getUrl());
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(50000);
                con.setReadTimeout(50000);

                Intent intent = new Intent();
                intent.setAction(DownloadService.ACTION_UPDATE);

                long time = System.currentTimeMillis();

                //设置文件开始下载的位置
                int start = mThreadBean.getStart() + mThreadBean.getFinished();
                con.setRequestProperty("Range", "bytes=" + start + "-" + mThreadBean.getEnd());

                //设置文件写入的位置
                File file = new File(DownloadService.DOWNlOAD_PATH, mFileBean.getFileName());
                raf = new RandomAccessFile(file, "rwd");
                raf.seek(start);

                //初始化下载进度
                mFinished += mThreadBean.getFinished();
                //开始下载
                if (con.getResponseCode() == 206) {
                    InputStream inputStream = con.getInputStream();
                    byte[] bytes = new byte[1024*8];
                    int len = -1;
                    while ((len = inputStream.read(bytes)) != -1) {
                        raf.write(bytes, 0, len);

                        //将进度通过广播的形式发送给activity
                        mFinished += len;
                      /*  if (time - System.currentTimeMillis() > 500) {
                            time = System.currentTimeMillis();

                        }*/
                        Log.e("tag", "mFinished: "+mFinished+"fileLength:"+mFileBean.getLength());
                        intent.putExtra("finished", mFinished * 100 / mFileBean.getLength());
                        mContext.sendBroadcast(intent);

                        if (isPause) {
                            //暂停下载，保存进度
                            mDownLoadImpl.updateThread(mThreadBean.getUrl(), mThreadBean.getId(), mFinished);
                            return;
                        }
                    }
                    mDownLoadImpl.deleteThread(mThreadBean.getUrl(),mThreadBean.getId());
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (con != null) {
                    con.disconnect();
                }
                try {
                    if (raf != null) {
                        raf.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
