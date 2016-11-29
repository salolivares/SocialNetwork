package edu.cs174a.buzmo.util;

public class TopicWord {
    private String tid;
    private String word;

    public TopicWord(String tid, String word) {
        this.tid = tid;
        this.word = word;
    }

    public String getTid() {
        return tid;
    }

    public String getWord() {
        return word;
    }
}
