package com.moses.io.beams.inernetlist;

import java.util.List;

/**
 * Created by 丹 on 2015/1/14.
 */
public class InternetInfo {
    private InternetInfo pageInfo;
    private List<InternetMerchant> merchantKey;

    public void setMerchantKey(List<InternetMerchant> merchantKey) {
        this.merchantKey = merchantKey;
    }

    public InternetInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(InternetInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<InternetMerchant> getMerchantKey() {
        return merchantKey;
    }

}
