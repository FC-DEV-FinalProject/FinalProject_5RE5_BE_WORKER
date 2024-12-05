package com.oreo.concat.concatenator.dto;


public class ConcatOptionDto {
    private Long optionSequence;
    private String optionName;

    public ConcatOptionDto() {
    }

    public ConcatOptionDto(Long optionSequence, String optionName) {
        this.optionSequence = optionSequence;
        this.optionName = optionName;
    }

    public Long getOptionSequence() {
        return optionSequence;
    }

    public void setOptionSequence(Long optionSequence) {
        this.optionSequence = optionSequence;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }
}
