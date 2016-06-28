package shbd.download.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import shbd.download.bean.ThreadBean;
import shbd.download.database.DownLoadOpenHelper;

/**
 * Created by yh on 2016/6/28.
 * 下载文件的实现类
 */
public class DownLoadImpl implements DownloadInterface {
    private final DownLoadOpenHelper mHelper;

    public DownLoadImpl(Context context) {
        mHelper = new DownLoadOpenHelper(context);
    }

    @Override
    public void insertThread(ThreadBean threadBean) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("insert into threadInfo values(?,?,?,?,?)", new Object[]{threadBean.getId(), threadBean.getUrl(),
                threadBean.getStart(), threadBean.getEnd(), threadBean.getFinished()});
        db.close();
    }

    @Override
    public void deleteThread(String url, int threadId) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("delete from threadInfo where url=? and thread_id=?", new Object[]{url, threadId});
        db.close();
    }

    @Override
    public void updateThread(String url, int threadId, int finished) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("update threadInfo set finished=? where url=? and thread_id=?", new Object[]{url, threadId});
        db.close();

    }

    @Override
    public List<ThreadBean> getThreads(String url) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from threadInfo where url=?", new String[]{url});

        List<ThreadBean> threadBeans = new ArrayList<>();
        while (cursor.moveToNext()) {
            ThreadBean threadBean = new ThreadBean();

            threadBean.setId(cursor.getInt(cursor.getColumnIndex("thread_id")));
            threadBean.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            threadBean.setStart(cursor.getInt(cursor.getColumnIndex("start")));
            threadBean.setEnd(cursor.getInt(cursor.getColumnIndex("end")));
            threadBean.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));

            threadBeans.add(threadBean);
        }
        db.close();
        cursor.close();

        return threadBeans;
    }

    @Override
    public boolean isExists(String url, int threadId) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from threadInfo where url=? and thread_id=?", new String[]{url, threadId + ""});
        boolean isExists = cursor.moveToNext();

        cursor.close();
        db.close();

        return isExists;
    }
}
