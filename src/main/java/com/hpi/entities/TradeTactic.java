package com.hpi.entities;

import lombok.*;

@AllArgsConstructor
public class TradeTactic 
{
    @Getter @Setter private Integer tacticId;
    @Getter @Setter private String tacticName;
    @Getter @Setter private String tacticDescr;
    @Getter @Setter private String tacticEquityType;
    @Getter @Setter private Boolean active;
    
    public TradeTactic(TradeTactic tactic)
    {
        this.tacticId = tactic.getTacticId();
        this.tacticName = tactic.getTacticName();
        this.tacticDescr = tactic.getTacticDescr();
        this.tacticEquityType = tactic.getTacticEquityType();
        this.active = tactic.getActive();
    }
    
    @Override
    public String toString()
    {
        return this.tacticName;
    }
}
