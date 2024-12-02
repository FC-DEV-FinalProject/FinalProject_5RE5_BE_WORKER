package com.oreo.concat.concatenator.dto;

import com.oreo.concat.concatenator.model.AudioInfo;

public class LambdaConcatResultDto {
    private final AudioInfo info;
    private final String s3Url;
    private final String contentType;
    private final String processId;
    private final int i;



    public LambdaConcatResultDto(AudioInfo info, String s3Url, String contentType, int i, String processId) {
        this.info = info;
        this.s3Url = s3Url;
        this.contentType = contentType;
        this.processId = processId;
        this.i = i;
    }

    public AudioInfo getInfo() {
        return info;
    }

    public String getS3Url() {
        return s3Url;
    }

    public String getContentType() {
        return contentType;
    }

    public String getProcessId() {
        return processId;
    }

    public int getI() {
        return i;
    }
}
