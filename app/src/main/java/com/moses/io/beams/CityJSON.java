package com.moses.io.beams;

import java.util.List;

/**
 * Created by 丹 on 2015/1/12.
 */
public class CityJSON {
    private List<City> info;
    private String resultCode;
    private String resultInfo;

    public void setInfo(List<City> info) {
        this.info = info;
    }

    public List<City> getInfo() {
        return info;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }

    public String getResultInfo() {
        return resultInfo;
    }
}
