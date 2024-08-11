package com.hpi.entities;

import java.util.*;

public class TradeTrigger
{

    private final Integer index;
    private final String triggerName;
    private final String triggerType;
    private final String equityType;

    public TradeTrigger(Integer index, String triggerName,
          String triggerType, String equityType)
    {
        this.index = index;
        this.triggerName = triggerName;
        this.triggerType = triggerType;
        this.equityType = equityType;
    }
    
    public TradeTrigger(TradeTrigger trigger)
    {
        this.index = trigger.getIndex();
        this.triggerName = trigger.getTriggerName();
        this.triggerType = trigger.getTriggerType();
        this.equityType = trigger.getEquityType();
    }
    
    @Override
    public String toString()
    {
        return this.triggerName;
    }
    
    @Override
    public boolean equals(Object tt)
    {
        if (this == tt)
        {
            return true;
        }
        if (tt == null)
        {
            return false;
        }
        if (getClass() != tt.getClass())
        {
            return false;
        }
        
        final TradeTrigger other = (TradeTrigger) tt;
        
        return (other.getIndex().equals(this.getIndex()));
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.index);
        return hash;
    }

    public Integer getIndex()
    {
        return index;
    }

    public String getTriggerName()
    {
        return triggerName;
    }

    public String getTriggerType()
    {
        return triggerType;
    }

    public String getEquityType()
    {
        return equityType;
    }
}
