package com.oreo.tts.dto.request;

import com.oreo.tts.dto.TtsSentenceDto;

public class TtsMakeRequest {
    private TtsSentenceDto ttsSentence;
    private String fileName;

    public TtsMakeRequest() {}

    public TtsMakeRequest(TtsSentenceDto ttsSentence, String fileName) {
        this.ttsSentence = ttsSentence;
        this.fileName = fileName;
    }

    public TtsSentenceDto getTtsSentence() {
        return ttsSentence;
    }

    public void setTtsSentence(TtsSentenceDto ttsSentence) {
        this.ttsSentence = ttsSentence;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "TtsMakeRequest{" +
                "ttsSentence=" + ttsSentence +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
