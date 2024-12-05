package com.oreo.tts.dto.response;

public class TtsMakeResponse {
    private String fileName;
    private String fileExtension;
    private Integer fileLength;
    private String fileSize;
    private String url;

    public TtsMakeResponse() {}

    public TtsMakeResponse(String fileName, String fileExtension, Integer fileLength, String fileSize, String url) {
        this.fileName = fileName;
        this.fileExtension = fileExtension;
        this.fileLength = fileLength;
        this.fileSize = fileSize;
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public Integer getFileLength() {
        return fileLength;
    }

    public void setFileLength(Integer fileLength) {
        this.fileLength = fileLength;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
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
                "fileName='" + fileName + '\'' +
                ", fileExtension='" + fileExtension + '\'' +
                ", fileLength=" + fileLength +
                ", fileSize=" + fileSize +
                ", url='" + url + '\'' +
                '}';
    }
}
