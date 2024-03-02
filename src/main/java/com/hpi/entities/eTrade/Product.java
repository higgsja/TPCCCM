package com.hpi.entities.eTrade;

    import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
public class Product {
    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("securityType")
    private String securityType;

    @JsonProperty("callPut")
    private String callPut;

    @JsonProperty("expiryYear")
    private int expiryYear;

    @JsonProperty("expiryMonth")
    private int expiryMonth;

    @JsonProperty("expiryDay")
    private int expiryDay;

    @JsonProperty("strikePrice")
    private double strikePrice;

//    public String getSymbol() {
//        return symbol;
//    }
//
//    public void setSymbol(String symbol) {
//        this.symbol = symbol;
//    }
//
//    public String getSecurityType() {
//        return securityType;
//    }
//
//    public void setSecurityType(String securityType) {
//        this.securityType = securityType;
//    }
//
//    public String getCallPut() {
//        return callPut;
//    }
//
//    public void setCallPut(String callPut) {
//        this.callPut = callPut;
//    }
//
//    public int getExpiryYear() {
//        return expiryYear;
//    }
//
//    public void setExpiryYear(int expiryYear) {
//        this.expiryYear = expiryYear;
//    }
//
//    public int getExpiryMonth() {
//        return expiryMonth;
//    }
//
//    public void setExpiryMonth(int expiryMonth) {
//        this.expiryMonth = expiryMonth;
//    }
//
//    public int getExpiryDay() {
//        return expiryDay;
//    }
//
//    public void setExpiryDay(int expiryDay) {
//        this.expiryDay = expiryDay;
//    }
//
//    public double getStrikePrice() {
//        return strikePrice;
//    }
//
//    public void setStrikePrice(double strikePrice) {
//        this.strikePrice = strikePrice;
//    }
}

