package com.hpi.entities;

import java.math.BigDecimal;
import java.util.*;

public class IEXChartModel
{

    public static final ArrayList<IEXChartModel> IEXCHARTMODEL_LIST;
    private String tkrIEX;
    private String tkr;
    private String date;
    private Double uClose;
    private Double uOpen;
    private Double uHigh;
    private Double uLow;
    private BigDecimal uVolume;
    private Double close;
    private Double open;
    private Double high;
    private Double low;
    private BigDecimal volume;
    private Double change;
    private Double changePercent;
    private String label;
    private Double changeOverTime;

    static
    {
        IEXCHARTMODEL_LIST = new ArrayList<>();
    }

    public IEXChartModel(String date,
          Double uClose, Double uOpen, Double uHigh, Double uLow,
          BigDecimal uVolume,
          Double close, Double open, Double high, Double low,
          BigDecimal volume,
          Double change, Double changePct, String label, Double changeOverTime)
    {
        this.date = date;
        this.close = uClose;
        this.open = uOpen;
        this.high = uHigh;
        this.low = uLow;
        this.volume = uVolume;
        this.uClose = close;
        this.uOpen = open;
        this.uHigh = high;
        this.uLow = low;
        this.uVolume = volume;
        this.change = change;
        this.changePercent = changePct;
        this.label = label;
        this.changeOverTime = changeOverTime;
    }
    
    public IEXChartModel(String tickerIEX, String date, String ticker,
          Double close, Double open, Double high, Double low,
          BigDecimal volume)
    {
        this(date, null, null, null, null, null,
            close, open, high, low, volume, 
            null, null, null, null);
        
        this.tkr = ticker;
        this.tkrIEX = tickerIEX;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public Double getuClose()
    {
        return uClose;
    }

    public void setuClose(Double uClose)
    {
        this.uClose = uClose;
    }

    public Double getuOpen()
    {
        return uOpen;
    }

    public void setuOpen(Double uOpen)
    {
        this.uOpen = uOpen;
    }

    public Double getuHigh()
    {
        return uHigh;
    }

    public void setuHigh(Double uHigh)
    {
        this.uHigh = uHigh;
    }

    public Double getuLow()
    {
        return uLow;
    }

    public void setuLow(Double uLow)
    {
        this.uLow = uLow;
    }

    public BigDecimal getuVolume()
    {
        return uVolume;
    }

    public void setuVolume(BigDecimal uVolume)
    {
        this.uVolume = uVolume;
    }

    public Double getClose()
    {
        return close;
    }

    public void setClose(Double close)
    {
        this.close = close;
    }

    public Double getOpen()
    {
        return open;
    }

    public void setOpen(Double open)
    {
        this.open = open;
    }

    public Double getHigh()
    {
        return high;
    }

    public void setHigh(Double high)
    {
        this.high = high;
    }

    public Double getLow()
    {
        return low;
    }

    public void setLow(Double low)
    {
        this.low = low;
    }

    public BigDecimal getVolume()
    {
        return volume;
    }

    public void setVolume(BigDecimal volume)
    {
        this.volume = volume;
    }

    public Double getChange()
    {
        return change;
    }

    public void setChange(Double change)
    {
        this.change = change;
    }

    public Double getChangePercent()
    {
        return changePercent;
    }

    public void setChangePercent(Double changePercent)
    {
        this.changePercent = changePercent;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public Double getChangeOverTime()
    {
        return changeOverTime;
    }

    public void setChangeOverTime(Double changeOverTime)
    {
        this.changeOverTime = changeOverTime;
    }

    public String getTkrIEX()
    {
        return tkrIEX;
    }

    public void setTkrIEX(String iexTickerString)
    {
        this.tkrIEX = iexTickerString;
    }

    public String getTkr()
    {
        return tkr;
    }

    public void setTkr(String tkr)
    {
        this.tkr = tkr;
    }    
}
