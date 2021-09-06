package com.hpi.entities;

import java.sql.*;

public class OHLCVModel
{

    private static final String SQL_DAILY;
    private String ticker;
    private Date date;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Double volume;

    static
    {
        SQL_DAILY = "select Ticker, `Date`, Open, High, Low, Close, Volume from `equityHistory` where Ticker = '%s' and `Date` >= date_sub(now(), interval 1 year) order by `Date` asc";
    }

    public OHLCVModel()
    {
        this.ticker = null;
        this.date = null;
        this.open = null;
        this.high = null;
        this.low = null;
        this.close = null;
        this.volume = null;
    }

    public OHLCVModel(String ticker, Date date, Double open, 
          Double high, Double low, Double close, Double volume)
    {
        this.ticker = ticker;
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }
    
    public OHLCVModel(OHLCVModel ohlcvModel)
    {
        this.ticker = ohlcvModel.getTicker();
        this.date = ohlcvModel.getDate();
        this.open = ohlcvModel.getOpen();
        this.high = ohlcvModel.getHigh();
        this.low = ohlcvModel.getLow();
        this.close = ohlcvModel.getClose();
        this.volume = ohlcvModel.getVolume();
    }

    public String getTicker()
    {
        return ticker;
    }

    public void setTicker(String ticker)
    {
        this.ticker = ticker;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
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

    public Double getClose()
    {
        return close;
    }

    public void setClose(Double close)
    {
        this.close = close;
    }

    public Double getVolume()
    {
        return volume;
    }

    public void setVolume(Double volume)
    {
        this.volume = volume;
    }

    public static String getSQL_DAILY()
    {
        return SQL_DAILY;
    }
}
