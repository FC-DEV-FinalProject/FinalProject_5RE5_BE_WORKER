package com.oreo.concat.concatenator.dto;

import javax.sound.sampled.AudioFormat;

public class AudioFormatDto {
    private Float sampleRate;
    private Integer bitDepth;
    private Integer channels;
    private Integer frameSize;
    private Float frameRate;

    public AudioFormatDto() {
    }

    public AudioFormatDto(Float sampleRate, Integer bitDepth, Integer channels, Integer frameSize, Float frameRate) {
        this.sampleRate = sampleRate;
        this.bitDepth = bitDepth;
        this.channels = channels;
        this.frameSize = frameSize;
        this.frameRate = frameRate;
    }

    public void setSampleRate(Float sampleRate) {
        this.sampleRate = sampleRate;
    }

    public void setBitDepth(Integer bitDepth) {
        this.bitDepth = bitDepth;
    }

    public void setChannels(Integer channels) {
        this.channels = channels;
    }

    public void setFrameSize(Integer frameSize) {
        this.frameSize = frameSize;
    }

    public void setFrameRate(Float frameRate) {
        this.frameRate = frameRate;
    }

    public AudioFormat toAudioFormat() {
        return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                sampleRate, bitDepth, channels, frameSize, frameRate, false);
    }

}
