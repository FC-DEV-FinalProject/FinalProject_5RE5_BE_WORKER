package com.oreo.tts.dto.response;

import com.oreo.concat.concatenator.model.AudioInfo;

public class TtsMakeResponse {
    private AudioInfo audioInfo;
    private String url;

    public TtsMakeResponse() {}

    public TtsMakeResponse(AudioInfo audioInfo, String url) {
        this.audioInfo = audioInfo;
        this.url = url;
    }

    public AudioInfo getAudioInfo() {
        return audioInfo;
    }

    public void setAudioInfo(AudioInfo audioInfo) {
        this.audioInfo = audioInfo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "TtsMakeResponse{" +
                "audioInfo=" + audioInfo +
                ", url='" + url + '\'' +
                '}';
    }
}
