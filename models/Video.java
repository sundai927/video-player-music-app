package edu.ds4jb.videoplayer.models;

public class Video {
    public String fileName;
    public String displayName;
    public String uploader;
    public String id;
    public String thumbnail;
    public Long timeStamp;

    public Video(){}
    public Video(String fileName, String displayName, String uploader, String id, Long timeStamp, String thumbnail) {
        this.fileName = fileName;
        this.displayName = displayName;
        this.uploader = uploader;
        this.id = id;
        this.timeStamp = timeStamp;
        this.thumbnail = thumbnail;
    }
}
