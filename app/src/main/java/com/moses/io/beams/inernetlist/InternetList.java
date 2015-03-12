package com.moses.io.beams.inernetlist;

/**
 * Created by 丹 on 2015/1/14.
 */
public class InternetList {
    private int resultCode;
    private String resultInfo;
    private InternetInfo info;

    public void setInfo(InternetInfo info) {
        this.info = info;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public InternetInfo getInfo() {
        return info;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }

    public String getResultInfo() {
        return resultInfo;
    }

}
