package com.oreo.concat.concatenator.model;

import com.oreo.concat.concatenator.audio.AudioExtensionConverter;

import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AudioInfo {
    private final byte[] audioData;
    private final String fileName;
    private final float contentLength;
    private final String contentType;

    public AudioInfo(AudioInputStream audioData, String fileName, float contentLength, String contentType) {
        this.audioData = AudioExtensionConverter.mp3ToWav(audioData);
        this.fileName = fileName;
        this.contentLength = contentLength;
        this.contentType = contentType;
    }

    private byte[] getAudioBytes(AudioInputStream audioStream) {

        try {
            return audioStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("오디오 변환 실패", e);
        }
    }

    public byte[] getAudioData() {
        return audioData;
    }

    public String getFileName() {
        return fileName;
    }

    public Long getContentSize() {
        return (long) audioData.length;
    }



    public InputStream toInputStream() {
            return new ByteArrayInputStream(audioData);
    }

    public float getContentLength() {
        return contentLength;
    }

    public String getContentType() {
        return contentType;
    }
}
