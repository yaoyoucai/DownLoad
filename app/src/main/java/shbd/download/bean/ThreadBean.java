package shbd.download.bean;

import java.io.Serializable;

/**
 * Created by yh on 2016/6/28.
 * 线程类
 */
public class ThreadBean implements Serializable {
    private int id;
    private String url;
    //开始位置
    private int start;
    //结束位置
    private int end;
    //是否完成下载，标志位
    private long finished;

    public ThreadBean() {
    }

    public ThreadBean(int id, String url, int start, int end, long finished) {
        this.id = id;
        this.url = url;
        this.start = start;
        this.end = end;
        this.finished = finished;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public long getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    @Override
    public String toString() {
        return "ThreadBean{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", finished=" + finished +
                '}';
    }
}
