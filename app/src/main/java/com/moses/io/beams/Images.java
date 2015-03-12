package com.moses.io.beams;

/**
 * Created by 丹 on 2015/1/12.
 */
public class Images {
    private Img info;
    private String resultCode;
    private String resultInfo;

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }

    public String getResultInfo() {
        return resultInfo;
    }

    public void setInfo(Img info) {
        this.info = info;
    }

    public Img getInfo() {
        return info;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultCode() {
        return resultCode;
    }

}
