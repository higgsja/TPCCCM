/*
 * Copyright 2015 all rights reserved.
 * HLH LLC
 */
package com.hpi.entities;

import java.util.Objects;

/**
 * Manages currency data.
 *
 * @author Joe@Higgs-Tx.com
 */
public class StockCurrencyModel implements Comparable<StockCurrencyModel>
{

    String sCurrency;
    String sLongName;

    public StockCurrencyModel(String sCurrency, String sLongName)
    {
        this.sCurrency = sCurrency;
        this.sLongName = sLongName;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.sCurrency);
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
        final StockCurrencyModel other = (StockCurrencyModel) obj;
        return Objects.equals(this.sCurrency, other.sCurrency);
    }

    @Override
    public String toString()
    {
        return sCurrency;
    }

    @Override
    public int compareTo(StockCurrencyModel o)
    {
        return this.sCurrency.compareTo(o.sCurrency);
    }

    public String getsCurrency()
    {
        return sCurrency;
    }

    public String getsLongName()
    {
        return sLongName;
    }
}
