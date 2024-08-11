/*
 * Copyright 2015 all rights reserved.
 * HLH LLC
 */
package com.hpi.entities;

import java.util.Objects;
import java.util.TreeSet;

/**
 *
 * @author Joe@Higgs-Tx.com
 */
public class OptionExpiryModel implements Comparable<OptionExpiryModel>
{

    String sDate;
    String sLastStrike;
    TreeSet<Double> strikes;

    public OptionExpiryModel()
    {
        this(null);
    }

    /**
     *
     */
    public OptionExpiryModel(String sDate)
    {
        this.sDate = sDate;
        this.sLastStrike = "";
        this.strikes = new TreeSet<>();
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.sDate);
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
        final OptionExpiryModel other = (OptionExpiryModel) obj;
        return Objects.equals(this.sDate, other.sDate);
    }

    @Override
    public String toString()
    {
        return sDate;
    }

    @Override
    public int compareTo(OptionExpiryModel o)
    {
        return this.sDate.compareTo(o.sDate);
    }

    public String getsDate()
    {
        return sDate;
    }

    public void setsDate(String sDate)
    {
        this.sDate = sDate;
    }

    public String getsLastStrike()
    {
        return sLastStrike;
    }

    public void setsLastStrike(String sLastStrike)
    {
        this.sLastStrike = sLastStrike;
    }

    public TreeSet<Double> getStrikes()
    {
        return strikes;
    }
}
