package com.moses.io.beams.inernetlist;

/**
 * Created by 丹 on 2015/1/14.
 */
public class InternetMerchant {
    private String merchantID;
    private String name;
    private String coupon;
    private String location;
    private String distance;
    private String picUrl;
    private String couponType;
    private String cardType;
    private String groupType;
    private float gpsX;
    private float gpsY;
    private int goodSayNum;
    private int midSayNum;
    private int badSayNum;

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setBadSayNum(int badSayNum) {
        this.badSayNum = badSayNum;
    }

    public float getGpsX() {
        return gpsX;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public float getGpsY() {
        return gpsY;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public int getBadSayNum() {
        return badSayNum;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public int getGoodSayNum() {
        return goodSayNum;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getMidSayNum() {
        return midSayNum;
    }

    public void setGoodSayNum(int goodSayNum) {
        this.goodSayNum = goodSayNum;
    }

    public String getCardType() {
        return cardType;
    }

    public void setGpsX(float gpsX) {
        this.gpsX = gpsX;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setGpsY(float gpsY) {
        this.gpsY = gpsY;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getDistance() {
        return distance;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setMerchantID(String merchantID) {
        this.merchantID = merchantID;
    }

    public String getLocation() {
        return location;
    }

    public void setMidSayNum(int midSayNum) {
        this.midSayNum = midSayNum;
    }

    public String getMerchantID() {
        return merchantID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
