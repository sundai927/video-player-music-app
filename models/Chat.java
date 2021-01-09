package edu.ds4jb.videoplayer.models;

public class Chat {
    public String displayName;
    public String message;
    public Long timeStamp;

    public Chat() {}

    public Chat(String displayName, String message, Long timeStamp) {
        this.displayName = displayName;
        this.message = message;
        this.timeStamp = timeStamp;
    }
}
