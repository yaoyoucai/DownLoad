package shbd.download.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import shbd.download.R;
import shbd.download.bean.FileBean;
import shbd.download.service.DownloadService;
import shbd.download.view.HorizontalProgressBarWidthProgress;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTvFileName;
    private HorizontalProgressBarWidthProgress mPbProgress;

    private Button mBtStart;
    private Button mBtStop;
    private FileBean fileBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        IntentFilter intentFilter = new IntentFilter(DownloadService.ACTION_UPDATE);

        registerReceiver(mReceiver, intentFilter);
    }

    private void initView() {
        mTvFileName = (TextView) findViewById(R.id.tv_filename);
        mPbProgress = (HorizontalProgressBarWidthProgress) findViewById(R.id.pb_progress);
        mBtStart = (Button) findViewById(R.id.bt_start);
        mBtStop = (Button) findViewById(R.id.bt_stop);

        mBtStart.setOnClickListener(this);
        mBtStop.setOnClickListener(this);

        fileBean = new FileBean(0, "aaa", 0, "http://apk.hiapk.com/appdown/com.ichano.athome.camera?webparams=sviptodoc291cmNlPTkz");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //开始下载
            case R.id.bt_start:
                Intent startIntent = new Intent(MainActivity.this, DownloadService.class);

                startIntent.setAction(DownloadService.ACTION_START);
                startIntent.putExtra("fileBean", fileBean);

                startService(startIntent);
                break;

            //结束下载
            case R.id.bt_stop:
                Intent stopIntent = new Intent(MainActivity.this, DownloadService.class);

                stopIntent.setAction(DownloadService.ACTION_STOP);
                stopIntent.putExtra("fileBean", fileBean);

                startService(stopIntent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();

    }

    /**
     * 接收回传的进度，更新进度条
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (DownloadService.ACTION_UPDATE.equals(intent.getAction())) {
                mPbProgress.setProgress(intent.getIntExtra("finished", 0));
            }
        }
    };

}
