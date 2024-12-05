package com.oreo.vc;

import java.util.List;

public class VcRequestDto {
    private final List<String> srcUrls;
    private final String trgUrl;


    public VcRequestDto (List<String> srcUrls, String trgUrl){
        this.srcUrls = srcUrls;
        this.trgUrl = trgUrl;
    }
    public List<String> getSrcUrls() {
        return srcUrls;
    }

    public String getTrgUrl() {
        return trgUrl;
    }

    @Override
    public String toString() {
        return "VcRequestDto{" +
                "srcUrls=" + srcUrls +
                ", trgUrl='" + trgUrl + '\'' +
                '}';
    }
}
