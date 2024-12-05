package com.oreo.vc;

import java.util.List;

public class VcRequestDto {
    private  List<String> srcUrls;
    private  String trgUrl;

    public VcRequestDto(){}
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

    public void setSrcUrls(List<String> srcUrls) {
        this.srcUrls = srcUrls;
    }

    public void setTrgUrl(String trgUrl) {
        this.trgUrl = trgUrl;
    }
}
