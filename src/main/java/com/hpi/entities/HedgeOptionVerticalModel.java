package com.hpi.entities;

import java.util.*;

public class HedgeOptionVerticalModel
{
    private static final String SQL;
    private static final ArrayList<HedgeOptionVerticalModel> VERTICAL_MODELS;
    private String equityId;
    private String ticker;
    private String putCall;
    private Double buyStrike;
    private Double sellStrike;
    private Double spreadCost;
    private Double calcMultiple;
    private Double realizedMultiple;
    private Integer contracts;
    private Double breakEven;
    private Double cost;

    static
    {
        SQL =
            "select EquityId, Symbol, AskPrice, LastPrice, PutCall, StrikePrice, ImpliedVolatility, Delta, OpenInterest, DataDate from OptionHistory where Symbol = '%s' and DataDate = (select max(DataDate) as DataDate from OptionHistory where Symbol = '%s') and ExpirationDate = '%s' and PutCall = 'call' and StrikePrice >= '%s' and StrikePrice <= '%s' order by StrikePrice";

        VERTICAL_MODELS = new ArrayList<>();
    }

    public HedgeOptionVerticalModel(String equityId, String ticker,
        String putCall, Double buyStrike,
        Double sellStrike, Double spreadCost, Double calcMultiple,
        Double realizedMultiple, Integer contracts, Double breakEven,
        Double cost)
    {
        this.equityId = equityId;
        this.ticker = ticker;
        this.putCall = putCall;
        this.buyStrike = buyStrike;
        this.sellStrike = sellStrike;
        this.spreadCost = spreadCost;
        this.calcMultiple = calcMultiple;
        this.realizedMultiple = realizedMultiple;
        this.contracts = contracts;
        this.breakEven = breakEven;
        this.cost = cost;
    }

    public static ArrayList<HedgeOptionVerticalModel> getVERTICAL_MODELS()
    {
        return VERTICAL_MODELS;
    }

    public String getTicker()
    {
        return ticker;
    }

    public Double getBuyStrike()
    {
        return buyStrike;
    }

    public Double getSellStrike()
    {
        return sellStrike;
    }

    public Double getSpreadCost()
    {
        return spreadCost;
    }

    public Double getCalcMultiple()
    {
        return calcMultiple;
    }

    public Double getRealizedMultiple()
    {
        return realizedMultiple;
    }

    public Integer getContracts()
    {
        return contracts;
    }

    public Double getBreakEven()
    {
        return breakEven;
    }

    public Double getCost()
    {
        return cost;
    }

    public String getEquityId()
    {
        return equityId;
    }

    public void setEquityId(String equityId)
    {
        this.equityId = equityId;
    }

    public String getPutCall()
    {
        return putCall;
    }

    public void setPutCall(String putCall)
    {
        this.putCall = putCall;
    }

    public static String getSQL()
    {
        return SQL;
    }
    
    

}
