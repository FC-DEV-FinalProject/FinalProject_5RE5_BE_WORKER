package com.oreo.tts.dto;

public class AudioOptionDto {
    private Float speed;
    private Float pitch;
    private Integer volume;

    public AudioOptionDto() {}

    public AudioOptionDto(Float speed, Float pitch, Integer volume) {
        this.speed = speed;
        this.pitch = pitch;
        this.volume = volume;
    }

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public Float getPitch() {
        return pitch;
    }

    public void setPitch(Float pitch) {
        this.pitch = pitch;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "AudioConfigDto{" +
                "speed=" + speed +
                ", pitch=" + pitch +
                ", volume=" + volume +
                '}';
    }
}
