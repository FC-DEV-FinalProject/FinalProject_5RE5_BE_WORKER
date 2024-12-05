package com.oreo.concat.concatenator.dto;

public class ConcatTabResponseDto {
    private Long tabId;
    private ConcatOptionDto concatOptionDto;
    private float frontSilence;
    private Character status;

    public ConcatTabResponseDto() {
    }

    public ConcatTabResponseDto(Long tabId, ConcatOptionDto concatOptionDto, float frontSilence, Character status) {
        this.tabId = tabId;
        this.concatOptionDto = concatOptionDto;
        this.frontSilence = frontSilence;
        this.status = status;
    }

    public Long getTabId() {
        return tabId;
    }

    public void setTabId(Long tabId) {
        this.tabId = tabId;
    }

    public ConcatOptionDto getConcatOptionDto() {
        return concatOptionDto;
    }

    public void setConcatOptionDto(ConcatOptionDto concatOptionDto) {
        this.concatOptionDto = concatOptionDto;
    }

    public float getFrontSilence() {
        return frontSilence;
    }

    public void setFrontSilence(float frontSilence) {
        this.frontSilence = frontSilence;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }
}
