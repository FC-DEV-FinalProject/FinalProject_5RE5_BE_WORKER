package com.oreo.tts.dto;

public class TtsSentenceDto {
    private String text;
    private VoiceDto voice;
    private AudioOptionDto audioOption;

    public TtsSentenceDto() {}

    public TtsSentenceDto(String text, VoiceDto voice, AudioOptionDto audioOption) {
        this.text = text;
        this.voice = voice;
        this.audioOption = audioOption;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public VoiceDto getVoice() {
        return voice;
    }

    public void setVoice(VoiceDto voice) {
        this.voice = voice;
    }

    public AudioOptionDto getAudioOption() {
        return audioOption;
    }

    public void setAudioOption(AudioOptionDto audioOption) {
        this.audioOption = audioOption;
    }

    @Override
    public String toString() {
        return "TtsSentenceDto{" +
                "text='" + text + '\'' +
                ", voice=" + voice +
                ", audioOption=" + audioOption +
                '}';
    }
}
