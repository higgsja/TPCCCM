package com.hpi.entities.eTrade;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
public class All
{
public boolean adjustedFlag;
    public double ask;
    public int askSize;
    public String askTime;
    public double bid;
    public String bidExchange;
    public int bidSize;
    public String bidTime;
    public double changeClose;
    public double changeClosePercentage;
    public String companyName;
    public int daysToExpiration;
    public String dirLast;
    public double dividend;
    public double eps;
    public double estEarnings;
    public long exDividendDate;
    public double high;
    public double high52;
    public double lastTrade;
    public double low;
    public double low52;
    @JsonProperty("open") 
    public double myopen;
    public int openInterest;
    public String optionStyle;
    public String optionUnderlier;
    public String optionUnderlierExchange;
    public double previousClose;
    public int previousDayVolume;
    public String primaryExchange;
    public String symbolDescription;
    public int totalVolume;
    public int upc;
    public int cashDeliverable;
    public double marketCap;
    public long sharesOutstanding;
    public String nextEarningDate;
    public double beta;
    @JsonProperty("yield") 
    public double myyield;
    public double declaredDividend;
    public int dividendPayableDate;
    public double pe;
    public long week52LowDate;
    public long week52HiDate;
    public double intrinsicValue;
    public double timePremium;
    public double optionMultiplier;
    public double contractSize;
    public long expirationDate;
    public double optionPreviousBidPrice;
    public double optionPreviousAskPrice;
    public String osiKey;
    public long timeOfLastTrade;
    public long averageVolume;
}
