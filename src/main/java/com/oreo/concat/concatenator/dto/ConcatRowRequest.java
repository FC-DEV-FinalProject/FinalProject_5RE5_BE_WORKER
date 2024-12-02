package com.oreo.concat.concatenator.dto;

public class ConcatRowRequest { //화면을 저장하기 위해 SelectedConcatRowRequest와 달리 selected여부도 저장해야함
    private Long seq;

    private OriginAudioRequest originAudioRequest; //행마다 매칭되는 원본 오디오파일

    private String rowText;

    private Character selected;

    private Float rowSilence;

    private Integer rowIndex; //행 순서

    private Character status;

    public ConcatRowRequest() {
    }

    public ConcatRowRequest(Long seq, OriginAudioRequest originAudioRequest, String rowText,
                            Character selected, Float rowSilence, Integer rowIndex, Character status) {
        this.seq = seq;
        this.originAudioRequest = originAudioRequest;
        this.rowText = rowText;
        this.selected = selected;
        this.rowSilence = rowSilence;
        this.rowIndex = rowIndex;
        this.status = status;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public OriginAudioRequest getOriginAudioRequest() {
        return originAudioRequest;
    }

    public void setOriginAudioRequest(OriginAudioRequest originAudioRequest) {
        this.originAudioRequest = originAudioRequest;
    }

    public String getRowText() {
        return rowText;
    }

    public void setRowText(String rowText) {
        this.rowText = rowText;
    }

    public Character getSelected() {
        return selected;
    }

    public void setSelected(Character selected) {
        this.selected = selected;
    }

    public Float getRowSilence() {
        return rowSilence;
    }

    public void setRowSilence(Float rowSilence) {
        this.rowSilence = rowSilence;
    }

    public Integer getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ConcatRowRequest{" +
                "seq=" + seq +
                ", originAudioRequest=" + originAudioRequest +
                ", rowText='" + rowText + '\'' +
                ", selected=" + selected +
                ", rowSilence=" + rowSilence +
                ", rowIndex=" + rowIndex +
                ", status=" + status +
                '}';
    }
}
