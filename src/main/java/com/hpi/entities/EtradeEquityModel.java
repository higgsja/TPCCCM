package com.hpi.entities;

import com.fasterxml.jackson.annotation.*;
import java.util.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Builder
/**
 * Json so do not change field names
 */
public class EtradeEquityModel
{
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
        public int exDividendDate;
        public double high;
        public double high52;
        public double lastTrade;
        public double low;
        public double low52;
        public double open;
        public int openInterest;
        public String optionStyle;
        public String optionUnderlier;
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
        public double yield;
        public double declaredDividend;
        public int dividendPayableDate;
        public double pe;
        public int week52LowDate;
        public int week52HiDate;
        public double intrinsicValue;
        public double timePremium;
        public double optionMultiplier;
        public double contractSize;
        public int expirationDate;
        public int timeOfLastTrade;
        public int averageVolume;
    }

    public class Product
    {

        public String symbol;
        public String securityType;
    }

    public class QuoteDatum
    {

        public String dateTime;
        public int dateTimeUTC;
        public String quoteStatus;
        public String ahFlag;
        public boolean hasMiniOptions;
        @JsonProperty("All")
        public All all;
        @JsonProperty("Product")
        public Product product;
    }

    public class QuoteResponse
    {

        @JsonProperty("QuoteData")
        public ArrayList<QuoteDatum> quoteData;
    }

    public class Root
    {

        @JsonProperty("QuoteResponse")
        public QuoteResponse quoteResponse;
    }

}
