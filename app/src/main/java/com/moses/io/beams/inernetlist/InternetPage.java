package com.moses.io.beams.inernetlist;

/**
 * Created by 丹 on 2015/1/14.
 */
public class InternetPage {
    private int total;
    private int pageSize;
    private int lastPageNumber;
    private int nowPage;
    private int currNum;

    public void setCurrNum(int currNum) {
        this.currNum = currNum;
    }

    public int getCurrNum() {
        return currNum;
    }

    public void setLastPageNumber(int lastPageNumber) {
        this.lastPageNumber = lastPageNumber;
    }

    public int getLastPageNumber() {
        return lastPageNumber;
    }

    public void setNowPage(int nowPage) {
        this.nowPage = nowPage;
    }

    public int getNowPage() {
        return nowPage;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return total;
    }
}
