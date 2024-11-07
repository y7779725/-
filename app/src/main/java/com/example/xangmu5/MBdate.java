package com.example.xangmu5;

public class MBdate {
    private String title;
    private String content;
    private String imgpath;
    private String mtime;

    public MBdate(String title, String content, String imgpath, String mtime) {
        this.title = title;
        this.content = content;
        this.imgpath = imgpath;
        this.mtime = mtime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }
    public String getMtime() {
        return mtime;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
    }
}
