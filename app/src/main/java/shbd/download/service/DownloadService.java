package shbd.download.service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import shbd.download.bean.FileBean;

/**
 * Created by yh on 2016/6/28.
 * 下载文件的service
 */
public class DownloadService extends Service {
    public static final String ACTION_START = "start";
    public static final String ACTION_STOP = "stop";
    public static final String ACTION_UPDATE = "update";
    private static final String TAG = "tag";
    private static final int MSG_INIT = 0;

    //下载任务
    private DownloadTask downloadTask;
    //sd卡根目录路径
    public static final String DOWNlOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/fileDownLoad";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (ACTION_START.equals(action)) {
            FileBean fileBean = (FileBean) intent.getSerializableExtra("fileBean");
            Log.e(TAG, "ACTION_START: " + fileBean.toString());

            //开始下载
            InitThread thread = new InitThread(fileBean);
            thread.start();

        } else if (ACTION_STOP.equals(action)) {
            if (downloadTask != null) {
                //暂停下载
                downloadTask.isPause = true;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * 下载文件的thread
     */
    class InitThread extends Thread {
        private FileBean mFileBean;

        public InitThread(FileBean fileBean) {
            this.mFileBean = fileBean;
        }

        @Override
        public void run() {
            String url = mFileBean.getUrl();
            HttpURLConnection con = null;
            try {

                URL fileUrl = new URL(url);
                con = (HttpURLConnection) fileUrl.openConnection();
                con.setRequestMethod("GET");
                con.setReadTimeout(50000);
                con.setConnectTimeout(50000);
                con.connect();
                int responseCode = con.getResponseCode();

                if (responseCode == 200) {
                    int contentLength = con.getContentLength();
                    mFileBean.setLength(contentLength);

                    //创建文件夹
                    File dir = new File(DOWNlOAD_PATH);
                    if (!dir.isDirectory()) {
                        dir.mkdir();
                    }

                    //将文件保存到文件夹
                    File file = new File(DOWNlOAD_PATH, mFileBean.getFileName());
                    RandomAccessFile raf = new RandomAccessFile(file, "rwd");
                    raf.setLength(contentLength);

                    //将filebean回传给主线程
                    mHandler.obtainMessage(MSG_INIT, mFileBean).sendToTarget();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                if (con != null) {
                    con.disconnect();
                }

            }
        }
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_INIT:
                    FileBean fileBean = (FileBean) msg.obj;
                    downloadTask = new DownloadTask(DownloadService.this, fileBean);
                    downloadTask.download();
                    break;
            }
        }
    };
}
