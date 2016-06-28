package shbd.download.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yh on 2016/6/28.
 * 线程信息的数据库
 */
public class DownLoadOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "download";
    private static final int DEFAULT_VERSION = 1;

    private static final String SQL_CREATE = "create table threadInfo(thread_id integer primary key autoincrement,url varchar,start integer,end integer ,finished integer)";

    public DownLoadOpenHelper(Context context) {
        super(context, DB_NAME, null, DEFAULT_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
