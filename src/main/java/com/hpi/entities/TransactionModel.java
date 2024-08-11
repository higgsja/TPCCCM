/*
 * Copyright 2015 all rights reserved.
 * HLH LLC
 */
package com.hpi.entities;

import com.hpi.TPCCMcontrollers.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import lombok.*;

/**
 * Data model for a transaction.
 *
 * @author Joe@Higgs-Tx.com
 */
@Getter
@Setter
public class TransactionModel
{
    private final String sTransName;
    private final String sTransType;
    private final Double dShPerCtrct;
    private Double dUnits;
    private final Double dPrice;
    private Double dCmmsn;
    private Double dTax;
    private Double dFees;
    private Double dTotal;
    private final Date dateDtTrade;

    public TransactionModel(String sTransName, String sTransType,
                            Double dShPerCtrct, Double dUnits, Double dPrice,
                            Double dCmmsn, Double dTax, Double dFees,
                            Double dTotal, Date dateDtTrade)
    {
        this.sTransName = sTransName;
        this.sTransType = sTransType;
        this.dShPerCtrct = dShPerCtrct == null ? (Double) 0.0 : dShPerCtrct;
        this.dUnits = dUnits == null ? (Double) 0.0 : dUnits;
        this.dPrice = dPrice == null ? (Double) 0.0 : dPrice;
        this.dCmmsn = dCmmsn == null ? (Double) 0.0 : dCmmsn;
        this.dTax = dTax == null ? (Double) 0.0 : dTax;
        this.dFees = dFees == null ? (Double) 0.0 : dFees;
        this.dTotal = dTotal == null ? (Double) 0.0 : dTotal;
        Calendar calendar = new GregorianCalendar(1900, 1, 1, 0, 0, 0);
        this.dateDtTrade = dateDtTrade == null
                ? calendar.getTime()
                : (Date) dateDtTrade.clone();

        Calendar.getInstance().set(1900, 1, 1, 0, 0, 0);
    }

    protected Boolean exportTransaction()
    {
        StringBuffer sbDetail;

        sbDetail = BrokersController.getInstance().getSbDetail();

        sbDetail.append(this.getSTransName());
        sbDetail.append("\t");
        sbDetail.append(this.getSTransType());
        sbDetail.append("\t");
        sbDetail.append(this.getDUnits());
        sbDetail.append("\t");
        sbDetail.append(this.getDShPerCtrct());
        sbDetail.append("\t");
        sbDetail.append(this.getDPrice());
        sbDetail.append("\t");
        sbDetail.append(this.getDCmmsn());
        sbDetail.append("\t");
        sbDetail.append(this.getDFees());
        sbDetail.append("\t");
        sbDetail.append(this.getDTax());
        sbDetail.append("\t");
        sbDetail.append(this.getDTotal());
        sbDetail.append("\t");
        sbDetail.append(this.getDateDtTrade());
        sbDetail.append("\n");

        return true;
    }
    
    protected Boolean exportSummaryTransaction()
    {
        StringBuffer sbSummary;

        sbSummary = BrokersController.getInstance().getSbSummary();

        sbSummary.append(this.getSTransName());
        sbSummary.append("\t");
        sbSummary.append(this.getSTransType());
        sbSummary.append("\t");
        sbSummary.append(this.getDUnits());
        sbSummary.append("\t");
        sbSummary.append(this.getDShPerCtrct());
        sbSummary.append("\t");
        sbSummary.append(this.getDPrice());
        sbSummary.append("\t");
        sbSummary.append(this.getDCmmsn());
        sbSummary.append("\t");
        sbSummary.append(this.getDFees());
        sbSummary.append("\t");
        sbSummary.append(this.getDTax());
        sbSummary.append("\t");
        sbSummary.append(this.getDTotal());
        sbSummary.append("\t");
        sbSummary.append(this.getDateDtTrade());
        sbSummary.append("\t");

        return true;
    }
}