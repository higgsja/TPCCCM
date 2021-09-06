package com.hpi.entities;

import java.util.*;
import lombok.*;

@Getter @Setter
/**
 * base for positionsClosed, positionsClosedTransactions, positionsOpen, positionsOpenTransactions
 * <p>
 * goal is to remove duplicate code in handling legs, positionName, etc.
 * <p>
 * however, each table is a bit different
 * <p>
 * so, make the tables the same ...
 *
 * @author Joe@Higgs-Tx.com
 */
abstract class PositionModelAbstract {
    public static final Integer NOT_COMPLETE = 0;
    public static final Integer COMPLETE = 1;
    
    private Integer posId;
    private Integer dmAcctId;
    private Integer joomlaId;
    private String fiTId;
    private Integer transGrp;
    private String ticker;
    private String equityId;
    private Integer tacticId;
    private String posName;
    private Double units;
    private Double PriceOpen;
    private Double PriceClose;
    private java.sql.Date dateOpen;
    private java.sql.Date dateClose;
    private java.sql.Date dateExpire;
    private Integer days;
    private String posType; //long, short
    private Double totalOpen;
    private Double totalClose;
    private String equityType; //option, stock
    private Double gain;
    private Double gainPct;
    private String transType;   //buytoopen, selltoopen
    private Integer complete;
    private Boolean bComplete;

    private final ArrayList<PositionClosedTransactionModel> positionClosedTransactionModels = new ArrayList<>();

    private final ArrayList<PositionOpenTransactionModel> positionOpenTransactionModels = new ArrayList<>();

    private final ArrayList<FIFOClosedTransactionModel> positionFifoClosedTransactionModels = new ArrayList<>();

    private final ArrayList<FIFOOpenTransactionModel> positionFifoOpenTransactionModels = new ArrayList<>();

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
