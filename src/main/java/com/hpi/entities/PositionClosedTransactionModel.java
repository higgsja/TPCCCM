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
 *  it may aggregate multiple lots *on the same day* in the fifoTransactions
 */
public class PositionClosedTransactionModel{
    //cannot set EquityId in options so cannot set at all
    public static final String POSITION_TRANSACTION_INSERT =
        "insert into hlhtxc5_dmOfx.PositionsClosedTransactions(DMAcctId, JoomlaId, PositionId, FiTId, TransactionName, Ticker, DateOpen, DateClose, Units, PriceOpen, PriceClose, Days, PositionType, TotalOpen, TotalClose, EquityType, Gain, GainPct, TransactionType, Complete) values (%s, %s, %s, '%s', '%s', '%s', '%s', '%s', %s, %s, %s, %s, '%s', %s, %s, '%s', %s, %s, '%s', %s)";

    public PositionClosedTransactionModel(PositionClosedTransactionModel pctm) {
        this.dmAcctId = pctm.dmAcctId;
        this.joomlaId = pctm.joomlaId;
        this.positionId = pctm.positionId;
        this.fiTId = pctm.fiTId;
        this.equityId = pctm.equityId;
        this.transactionName = pctm.transactionName;
        this.ticker = pctm.ticker;
        this.dateOpen = pctm.dateOpen;
        this.dateClose = pctm.dateClose;
        this.units = pctm.units;
        this.priceOpen = pctm.priceOpen;
        this.priceClose = pctm.priceClose;
        this.dateExpire = pctm.dateExpire;
        this.days = pctm.days;
        this.positionType = pctm.positionType;
        this.totalOpen = pctm.totalOpen;
        this.totalClose = pctm.totalClose;
        this.equityType = pctm.equityType;
        this.gain = pctm.gain;
        this.gainPct = pctm.gainPct;
        this.transactionType = pctm.transactionType;
        this.equityType = pctm.equityType;
        this.complete = pctm.complete;
        this.bComplete = pctm.bComplete;
        this.fifoClosedTransactionModels = new ArrayList<>();

        for (FIFOClosedTransactionModel fctm : pctm.getFifoClosedTransactionModels()) {
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
    private java.sql.Date dateClose;  //use the latest date in case where multiple close dates
    private Double units;
    private Double priceOpen;
    private Double priceClose;
    private java.sql.Date dateExpire;
    private Integer days;
    private String positionType;
    private Double totalOpen;
    private Double totalClose;
    private String equityType;
    private Double gain;
    private Double gainPct;
    private String transactionType;
    private Integer complete;
    private Boolean bComplete;

    //one or more transactions make up the transaction model (lots)
    @Builder.Default @Getter private final ArrayList<FIFOClosedTransactionModel> fifoClosedTransactionModels =
        new ArrayList<>();

    public void setBComplete(Boolean complete) {
        this.bComplete = complete;

        if (this.bComplete) {
            this.complete = 1;
        }
        else {
            this.complete = 0;
        }
    }

    public void setComplete(Integer complete) {
        this.complete = complete;

        this.bComplete = this.complete == 1;
    }
}
