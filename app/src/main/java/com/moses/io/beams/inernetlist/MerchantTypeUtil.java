package com.moses.io.beams.inernetlist;

/**
 * Created by 丹 on 2015/1/14.
 */
public class MerchantTypeUtil {
    public static final String IS_COUPON_TYPE = "COUPON_TYPE";
    public static final String IS_CARD_TYPE = "CARD_TYPE";
    public static final String IS_GROUP_TYPE = "GROUP_TYPE";
    public static final String IS_COUPON_GROUP_TYPE = "COUPON_GROUP_TYPE";
    public static final String IS_COUPON_CARD_TYPE = "COUPON_CARD_TYPE";
    public static final String IS_CARD_GROUP_TYPE = "CARD_GROUP_TYPE";
    public static final String IS_COUPON_CARD_GROUP_TYPE = "COUPON_CARD_GROUP_TYPE";
    public static final String IS_NONE_TYPE = "NONE_TYPE";

    public static String isType(String couponType, String cardType, String groupType) {
        String type = null;
        if (couponType.equals("YES") && cardType.equals("NO") && groupType.equals("NO")) {
            type = MerchantTypeUtil.IS_COUPON_TYPE;
        } else if (couponType.equals("NO") && cardType.equals("YES") && groupType.equals("NO")) {
            type = MerchantTypeUtil.IS_CARD_TYPE;
        } else if (couponType.equals("NO") && cardType.equals("NO") && groupType.equals("YES")) {
            type = MerchantTypeUtil.IS_GROUP_TYPE;
        } else if (couponType.equals("YES") && cardType.equals("YES") && groupType.equals("NO")) {
            type = MerchantTypeUtil.IS_COUPON_CARD_TYPE;
        } else if (couponType.equals("YES") && cardType.equals("NO") && groupType.equals("YES")) {
            type = MerchantTypeUtil.IS_COUPON_GROUP_TYPE;
        } else if (couponType.equals("NO") && cardType.equals("YES") && groupType.equals("YES")) {
            type = MerchantTypeUtil.IS_CARD_GROUP_TYPE;
        } else if (couponType.equals("YES") && cardType.equals("YES") && groupType.equals("YES")) {
            type = MerchantTypeUtil.IS_COUPON_CARD_GROUP_TYPE;
        } else if (couponType.equals("NO") && cardType.equals("NO") && groupType.equals("NO")) {
            type = MerchantTypeUtil.IS_NONE_TYPE;
        }
        return type;
    }

}
