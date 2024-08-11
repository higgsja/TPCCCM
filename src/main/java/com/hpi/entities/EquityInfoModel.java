package com.hpi.entities;

import lombok.*;

@Getter @Setter
public class EquityInfoModel
{

    //get current ticker set
    public static final String TICKER
        //        = "select distinct Ticker from hlhtxc5_dmOfx.EquityInfo where `Date` > date_sub(now(), interval 365 day);";
        = "select distinct Ticker from hlhtxc5_dmOfx.EquityInfo where `Date` > date_sub(now(), interval 1 day);";

    public static final String SELECT_DISTINCT_SECTORS = "select distinct Sector from hlhtxc5_dmOfx.EquityInfo;";

    String ticker;
    String company;
    String sector;
    String industry;
    String country;
    Double mktCap;
    Double pe;
    Double fwdPE;
    Double peg;
    Double div;
    Double payOutRatio;
    Double eps;
    Double epsCY;
    Double epsNY;
    Double epsP5Y;
    Double epsN5Y;
    Double atr;
    Double sma20;
    Double sma50;
    Double sma200;
    Double d50Hi;
    Double d50Lo;
    Double w52Hi;
    Double w52Lo;
    Double rsi;
    Double anRec;
    Double price;
    Integer volume;
    Double tgtPrice;
    String earnDate;
    java.sql.Date date;
    Double beta;

    public EquityInfoModel(String ticker, String company, String sector,
        String industry, String country, Double mktCap, Double pe,
        Double fwdPE, Double peg, Double div, Double payOutRatio,
        Double eps, Double epsCY, Double epsNY, Double epsP5Y,
        Double epsN5Y, Double atr, Double sma20, Double sma50,
        Double sma200, Double d50Hi, Double d50Lo, Double w52Hi,
        Double w52Lo, Double rsi, Double anRec, Double price,
        Integer volume, Double tgtPrice, String earnDate, java.sql.Date date, Double beta)
    {
        this.ticker = ticker;
        this.company = company;
        this.sector = sector;
        this.industry = industry;
        this.country = country;
        this.mktCap = mktCap;
        this.pe = pe;
        this.fwdPE = fwdPE;
        this.peg = peg;
        this.div = div;
        this.payOutRatio = payOutRatio;
        this.eps = eps;
        this.epsCY = epsCY;
        this.epsNY = epsNY;
        this.epsP5Y = epsP5Y;
        this.epsN5Y = epsN5Y;
        this.atr = atr;
        this.sma20 = sma20;
        this.sma50 = sma50;
        this.sma200 = sma200;
        this.d50Hi = d50Hi;
        this.d50Lo = d50Lo;
        this.w52Hi = w52Hi;
        this.w52Lo = w52Lo;
        this.rsi = rsi;
        this.anRec = anRec;
        this.price = price;
        this.volume = volume;
        this.tgtPrice = tgtPrice;
        this.earnDate = earnDate;
        this.date = date;
        this.beta = beta;
    }
}
