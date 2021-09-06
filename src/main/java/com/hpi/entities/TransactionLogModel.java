package com.hpi.entities;

import java.util.*;
import lombok.*;

@Getter
@Setter
public class TransactionLogModel {
    private Integer joomlaId;
    private Integer dmAcctId;
    private String acctName;
    private String fiTId;
    private String equityId;
    private String gmtDtTrade;
    private Double units;
    private Double unitPrice;
    private String transType;
    private TradeTactic tradeTactic;
    private Double stopLoss;
    private Double targetGain;
    private TradeTrigger tradeTrigger;
    private String comment;
    private String openClose;
    private String equityType;
    
    public static final String ALL_FIELDS =
        "JoomlaId, DMAcctId, AcctName, FiTId, EquityId, GMTDtTrade, Units, UnitPrice, TransType, TradeTactic, StopLoss, TargetGain, TradeTrigger, Comment, OpenClose, EquityType";

    public static final String DEMO_FIELDS =
        "AcctName, FiTId, EquityId, GMTDtTrade, Units, UnitPrice, TransType, TradeTactic, StopLoss, TargetGain, TradeTrigger, Comment, OpenClose, EquityType";

    public TransactionLogModel(Integer joomlaId, Integer dmAcctId,
        String acctName, String fiTId, String equityId,
        String gmtDtTrade, Double units, Double unitPrice,
        String transType, TradeTactic tradeTactic, Double stopLoss,
        Double targetGain, TradeTrigger tradeTrigger, String comment,
        String openClose,
        String equityType) {
        this.joomlaId = joomlaId;
        this.dmAcctId = dmAcctId;
        this.acctName = acctName;
        this.fiTId = fiTId;
        this.equityId = equityId;
        this.gmtDtTrade = gmtDtTrade;
        this.units = units;
        this.unitPrice = unitPrice;
        this.transType = transType;
        this.tradeTactic = tradeTactic == null ? null :
                           new TradeTactic(tradeTactic);
        this.stopLoss = stopLoss;
        this.targetGain = targetGain;
        this.tradeTrigger = tradeTrigger == null ? null :
                            new TradeTrigger(tradeTrigger);
        this.comment = comment;
        this.openClose = openClose;
        this.equityType = equityType;
    }

    @Override
    public boolean equals(Object tlm) {
        if (this == tlm) {
            return true;
        }
        if (tlm == null) {
            return false;
        }
        if (getClass() != tlm.getClass()) {
            return false;
        }

        final TransactionLogModel other = (TransactionLogModel) tlm;

        return (Objects.equals(other.joomlaId, this.joomlaId) &&
                Objects.equals(other.dmAcctId, this.dmAcctId) &&
                other.fiTId.equalsIgnoreCase(this.fiTId) &&
                Objects.equals(other.tradeTactic.getTacticId(),
                    this.tradeTactic.getTacticId()) &&
                Objects.equals(other.stopLoss, this.stopLoss) &&
                Objects.equals(other.targetGain, this.targetGain) &&
                Objects.equals(other.tradeTrigger, this.tradeTrigger) &&
                other.comment.equalsIgnoreCase(this.comment));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.joomlaId);
        hash = 79 * hash + Objects.hashCode(this.dmAcctId);
        hash = 79 * hash + Objects.hashCode(this.fiTId);
        hash = 79 * hash + Objects.hashCode(this.tradeTactic);
        hash = 79 * hash + Objects.hashCode(this.stopLoss);
        hash = 79 * hash + Objects.hashCode(this.targetGain);
        hash = 79 * hash + Objects.hashCode(this.tradeTrigger);
        hash = 79 * hash + Objects.hashCode(this.comment);
        return hash;
    }    
}
