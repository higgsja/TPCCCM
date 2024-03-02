package com.hpi.entities.eTrade;

    import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
public class QuoteData {
    @JsonProperty("dateTime")
    private String dateTime;

    @JsonProperty("dateTimeUTC")
    private long dateTimeUTC;

    @JsonProperty("quoteStatus")
    private String quoteStatus;

    @JsonProperty("ahFlag")
    private String ahFlag;

    @JsonProperty("All")
    private All all;

    @JsonProperty("Product")
    private Product product;

//    public String getDateTime() {
//        return dateTime;
//    }
//
//    public void setDateTime(String dateTime) {
//        this.dateTime = dateTime;
//    }
//
//    public long getDateTimeUTC() {
//        return dateTimeUTC;
//    }
//
//    public void setDateTimeUTC(long dateTimeUTC) {
//        this.dateTimeUTC = dateTimeUTC;
//    }
//
//    public String getQuoteStatus() {
//        return quoteStatus;
//    }
//
//    public void setQuoteStatus(String quoteStatus) {
//        this.quoteStatus = quoteStatus;
//    }
//
//    public String getAhFlag() {
//        return ahFlag;
//    }
//
//    public void setAhFlag(String ahFlag) {
//        this.ahFlag = ahFlag;
//    }
//
//    public All getAll() {
//        return all;
//    }
//
//    public void setAll(All all) {
//        this.all = all;
//    }
//
//    public Product getProduct() {
//        return product;
//    }
//
//    public void setProduct(Product product) {
//        this.product = product;
//    }
}

