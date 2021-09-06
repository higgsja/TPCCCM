/*
 * Copyright 2015 all rights reserved.
 * HLH LLC
 */
package com.hpi.entities;

import java.util.Objects;

/**
 *
 * @author Joe@Higgs-Tx.com
 */
public class StockExchangeModel implements Comparable<StockExchangeModel>
{
    String exchange;
    
    public StockExchangeModel()
    {
        this.exchange = null;
    }
    
    public StockExchangeModel(String exchange)
    {
        this.exchange = exchange;
    }

    @Override
    public int compareTo(StockExchangeModel o)
    {
        return this.exchange.compareTo(o.exchange);
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.exchange);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final StockExchangeModel other = (StockExchangeModel) obj;
        
        return Objects.equals(this.exchange, other.exchange);
    }
}
