package com.oreo.concat.concatenator.dto;


public class OriginAudioRequest { //순수한 오디오 파일
    private Long seq;

    private String audioUrl;

    private String extension;

    private Long fileSize;

    private Long fileLength;

    private String fileName;

    public OriginAudioRequest() {
    }

    public OriginAudioRequest(Long seq, String audioUrl, String extension, Long fileSize, Long fileLength, String fileName) {
        this.seq = seq;
        this.audioUrl = audioUrl;
        this.extension = extension;
        this.fileSize = fileSize;
        this.fileLength = fileLength;
        this.fileName = fileName;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Long getFileLength() {
        return fileLength;
    }

    public void setFileLength(Long fileLength) {
        this.fileLength = fileLength;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
