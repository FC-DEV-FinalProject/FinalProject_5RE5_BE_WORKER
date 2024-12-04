package com.oreo.tts.dto;

public class VoiceDto {
    private String langCode;
    private String name;
    private String gender;

    public VoiceDto() {}

    public VoiceDto(String langCode, String name, String gender) {
        this.langCode = langCode;
        this.name = name;
        this.gender = gender;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "VoiceDto{" +
                "langCode='" + langCode + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
