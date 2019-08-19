package com.cn.demo.bean;

import java.io.Serializable;

/**
 * Date: 2019/7/9
 * <p>
 * Time: 3:07 PM
 * <p>
 * author: 鹿文龙
 */
public class BankBean implements Serializable {


    /**
     * bankCode : 10030
     * bankName : 中国银联支付标记
     * cardName : 中国银联移动支付标记化产品
     * cardNumLength : 19
     * cardNum : 623529xxxxxxxxxxxxx
     * binLength : 6
     * binCode : 623529
     * cardType : 借记卡
     */

    private String bankCode;
    private String bankName;
    private String cardName;
    private String cardNumLength;
    private String cardNum;
    private String binLength;
    private String binCode;
    private String cardType;

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardNumLength() {
        return cardNumLength;
    }

    public void setCardNumLength(String cardNumLength) {
        this.cardNumLength = cardNumLength;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getBinLength() {
        return binLength;
    }

    public void setBinLength(String binLength) {
        this.binLength = binLength;
    }

    public String getBinCode() {
        return binCode;
    }

    public void setBinCode(String binCode) {
        this.binCode = binCode;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}
