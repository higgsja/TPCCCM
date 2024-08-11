package com.hpi.entities;

import java.util.*;

public class EquityHistoryModel
{
    public static String UPDATE = "insert ignore into hlhtxc5_dmOfx.EquityHistory (TickerIEX, Date, Ticker, `Open`, High, Low, `Close`, Volume) values ";
    
    public static String SQL_GET_LAST = "select eh.Ticker, eh.`Date`, eh.Open, eh.High, eh.Low, eh.Close, eh.Volume from hlhtxc5_dmOfx.EquityHistory as eh, (select Ticker, max(`Date`) as `Date` from hlhtxc5_dmOfx.EquityHistory as eh group by Ticker) as A where eh.`Date` = A.`Date` and eh.Ticker = A.Ticker and eh.Ticker = '%s';";

    String tkr;
    ArrayList<IEXChartModel> dataList;

    public EquityHistoryModel(String Tkr)
    {
        this.tkr = Tkr;
        this.dataList = new ArrayList<>();
    }

    public EquityHistoryModel()
    {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public String getTkr()
    {
        return tkr;
    }

    public ArrayList<IEXChartModel> getDataList()
    {
        return dataList;
    }    
}
