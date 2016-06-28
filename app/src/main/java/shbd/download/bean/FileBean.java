package shbd.download.bean;

import java.io.Serializable;

/**
 * Created by yh on 2016/6/28.
 * 文件类
 */
public class FileBean implements Serializable {
    private int id;
    private String fileName;
    private int length;
    private String url;
    private int finished;

    public FileBean() {
    }

    public FileBean(int id, String fileName, int length, String url) {
        this.id = id;
        this.fileName = fileName;
        this.length = length;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    @Override
    public String toString() {
        return "FileBean{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", length=" + length +
                ", url='" + url + '\'' +
                ", finished=" + finished +
                '}';
    }

}
