package com.hpi.entities;

import java.util.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
/**
 * positionTransaction is a leg in a final position
 * it may aggregate multiple lots *on the same day* in the fifoTransactions
 */
public class PositionOpenTransactionModel
{

    public static final String POSITION_TRANSACTION_INSERT
        = "insert ignore into hlhtxc5_dmOfx.PositionsOpenTransactions(DMAcctId, JoomlaId, PositionId, FiTId, TransactionName, Ticker, DateOpen, Units, PriceOpen, DateExpire, Days, PositionType, TotalOpen, EquityType, Gain, GainPct, TransactionType, Complete, MktVal, LMktVal, ActPct) values (%s, %s, %s, '%s', '%s', '%s', '%s', %s, %s, '%s', %s, '%s', %s, '%s', %s, %s, '%s', %s, %s, %s, %s)";

    public PositionOpenTransactionModel(PositionOpenTransactionModel potm)
    {
        this.dmAcctId = potm.dmAcctId;
        this.joomlaId = potm.joomlaId;
        this.positionId = potm.positionId;
        this.fiTId = potm.fiTId;
        this.equityId = potm.equityId;
        this.transactionName = potm.transactionName;
        this.ticker = potm.ticker;
        this.dateOpen = potm.dateOpen;
        this.units = potm.units;
        this.priceOpen = potm.priceOpen;
        this.dateExpire = potm.dateExpire;
        this.days = potm.days;
        this.positionType = potm.positionType;
        this.totalOpen = potm.totalOpen;
        this.equityType = potm.equityType;
        this.gain = potm.gain;
        this.gainPct = potm.gainPct;
        this.transactionType = potm.transactionType;
        this.complete = potm.complete;
        this.bComplete = potm.bComplete;

        this.mktVal = potm.mktVal;
        this.lMktVal = potm.lMktVal;
        this.actPct = potm.actPct;

        this.fifoOpenTransactionModels = new ArrayList<>();
        this.fifoClosedTransactionModels = new ArrayList<>();

        for (FIFOOpenTransactionModel fotm : potm.getFifoOpenTransactionModels())
        {
            this.fifoOpenTransactionModels.add(new FIFOOpenTransactionModel(fotm));
        }
        
        for (FIFOClosedTransactionModel fctm : potm.getFifoClosedTransactionModels())
        {
            this.fifoClosedTransactionModels.add(new FIFOClosedTransactionModel(fctm));
        }
    }

    private Integer dmAcctId;
    private Integer joomlaId;
    private Integer positionId;
    private String fiTId;
    private String equityId;
    private String transactionName;
    private String ticker;
    private java.sql.Date dateOpen;
    private Double units;
    private Double priceOpen;
    private java.sql.Date dateExpire;
    private Integer days;
    private String positionType;
    private Double totalOpen;
    private String equityType;
    private Double gain;
    private Double gainPct;
    private String transactionType;
    private Integer complete;
    private Boolean bComplete;
    private Double mktVal;
    private Double lMktVal;
    private Double actPct;

    //have fifoOpenTransactionModels and fifoClosedTransactionModels extend a base fifoTransactionModel
    //one or more transactions make up the transaction model (lots)
    @Builder.Default @Getter private final ArrayList<FIFOOpenTransactionModel> fifoOpenTransactionModels
        = new ArrayList<>();

    @Builder.Default @Getter private final ArrayList<FIFOClosedTransactionModel> fifoClosedTransactionModels
        = new ArrayList<>();

    public void setBComplete(Boolean complete)
    {
        this.bComplete = complete;

        if (this.bComplete)
        {
            this.complete = 1;
        } else
        {
            this.complete = 0;
        }
    }

    public void setComplete(Integer complete)
    {
        this.complete = complete;

        this.bComplete = this.complete == 1;
    }
}
