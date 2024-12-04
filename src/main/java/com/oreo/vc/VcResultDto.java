package com.oreo.vc;

public class VcResultDto {
    private final String name;
    private final String size;      //크기 (용량)
    private final Integer length;    // 초 단위 길이
    private final String extension; //확장자
    private final String s3Url;

    public VcResultDto(String name, String size, Integer length, String extension, String s3Url){
        this.name = name;
        this.size = size;
        this.length = length;
        this.extension = extension;
        this.s3Url = s3Url;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public Integer getLength() {
        return length;
    }

    public String getExtension() {
        return extension;
    }

    public String getS3Url() {
        return s3Url;
    }

    @Override
    public String toString() {
        return "VcResultDto{" +
                "name='" + name + '\'' +
                ", size='" + size + '\'' +
                ", length=" + length +
                ", extension='" + extension + '\'' +
                ", s3Url='" + s3Url + '\'' +
                '}';
    }
}
