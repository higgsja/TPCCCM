package com.hpi.entities;

import lombok.*;

/**
 * Data model for table fifoClosedTransactionModel
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class FIFOClosedTransactionModel
{

    public static final Integer NOT_COMPLETE = 0;
    public static final Integer COMPLETE = 1;
    
    public static final String COLUMNS = "DMAcctId, JoomlaId, FiTId, TransactionGrp, Ticker, EquityId, TransactionName, DateOpen, DateClose, DateExpire, Units, PriceOpen, PriceClose, TotalOpen, TotalClose, Gain, GainPct, EquityType, PositionType, TransactionType, Complete, Days";

    public static final String SELECT_ALL_BY_JOOMLAID_EQUITYTYPE
        = "select DMAcctId, JoomlaId, FiTId, TransactionGrp, Ticker, EquityId, TransactionName, DateOpen, DateClose, DateExpire, Units, PriceOpen, PriceClose, TotalOpen, TotalClose, Gain, GainPct, EquityType, PositionType, Transactiontype, Complete, Days from hlhtxc5_dmOfx.FIFOClosedTransactions where JoomlaId = '%s' and EquityType = '%s' order by EquityId, DateOpen, DateClose ;";

    //limit to current year
    public static final String SELECT_INCOMPLETE_BY_JOOMLAID_EQUITYTYPE
        = "select DMAcctId, JoomlaId, FiTId, TransactionGrp, Ticker, EquityId, TransactionName, DateOpen, DateClose, DateExpire, Units, PriceOpen, PriceClose, TotalOpen, TotalClose, Gain, GainPct, EquityType, PositionType, Transactiontype, Complete, Days from hlhtxc5_dmOfx.FIFOClosedTransactions where Complete <> 1 and JoomlaId = '%s' and EquityType = '%s' order by EquityId, DateOpen, DateClose";

    public static final String INSERT_CLOSED_OPTION_FIFO_2_FIFO_CLOSED_TRANSACTIONS
        = "insert ignore into hlhtxc5_dmOfx.FIFOClosedTransactions (DMAcctId, JoomlaId, FiTId, TransactionGrp, EquityId, TransactionName, Ticker, DateOpen, DateClose, Units, PriceOpen, PriceClose, DateExpire, Days, PositionType, TotalOpen, TotalClose, EquityType, Gain, GainPct, TransactionType) select ClosedOptionFIFO.DMAcctId, ClosedOptionFIFO.JoomlaId, FiTId, TransactionGrp, EquityId, TransactionName, ClosedOptionFIFO.Ticker, DateOpen, DateClose, Units, PriceOpen, PriceClose, DateExpire, datediff(DateExpire, CurDate()) as `Days`, if(TotalOpen < 0, 'LONG', 'SHORT') as PositionType, TotalOpen, TotalClose, 'OPTION' as EquityType, (TotalOpen + TotalClose) as Gain, if (TotalOpen = 0, 0, round(100 * (TotalClose + TotalOpen) / abs(TotalOpen), 4)) as GainPct, TransactionType from hlhtxc5_dmOfx.ClosedOptionFIFO where ClosedOptionFIFO.JoomlaId = '%s'";

    public static final String INSERT_CLOSED_STOCK_FIFO_2_FIFO_CLOSED_TRANSACTIONS
        = "insert ignore into hlhtxc5_dmOfx.FIFOClosedTransactions (DMAcctId, JoomlaId, FiTId, TransactionGrp, EquityId, Ticker, DateOpen, DateClose, Units, PriceOpen, PriceClose, PositionType, TotalOpen, TotalClose, EquityType, Gain, GainPct, TransactionType, Days) select csf.DMAcctId, csf.JoomlaId, FiTId, TransactionGrp, EquityId, csf.Ticker, DateOpen as 'Open', DateClose as `Close`, Units, PriceOpen, PriceClose, if(csf.TransactionType = 'BUY', 'LONG', if (csf.TransactionType = 'SELLSHORT', 'SHORT', 'UNKNOWN')) as PositionType, TotalOpen, TotalClose, 'STOCK' as EquityType, (TotalClose + TotalOpen) as Gain, round(100 * (TotalClose + TotalOpen) / abs(TotalOpen), 4) as GainPct, csf.TransactionType, 0 as Days from hlhtxc5_dmOfx.ClosedStockFIFO as csf where csf.JoomlaId = '%s'";

    public static final String UPDATE_COMPLETE
        = "update hlhtxc5_dmOfx.FIFOClosedTransactions set Complete = '%s' where DMAcctId = '%s' and JoomlaId = '%s' and FiTId = '%s'";

    public static final String ALL_FIELDS
        = "DMAcctId, JoomlaId, FiTId, TransactionGrp, Ticker, EquityId, TransactionName, DateOpen, DateClose, DateExpire, Units, PriceOpen, PriceClose, TotalOpen, TotalClose, Gain, GainPct, EquityType, PositionType, Transactiontype, Complete, Days";

    public static final String DEMO_FIELDS
        = "FiTId, TransactionGrp, Ticker, EquityId, TransactionName, DateOpen, DateClose, DateExpire, Units, PriceOpen, PriceClose, TotalOpen, TotalClose, Gain, GainPct, EquityType, PositionType, Transactiontype, Complete, Days";

    public FIFOClosedTransactionModel(FIFOClosedTransactionModel fotm)
    {
        this.dmAcctId = fotm.dmAcctId;
        this.joomlaId = fotm.joomlaId;
        this.fiTId = fotm.fiTId;
        this.transactionGrp = fotm.transactionGrp;
        this.ticker = fotm.ticker;
        this.equityId = fotm.equityId;
        this.transactionName = fotm.transactionName;
        this.dateOpen = fotm.dateOpen;
        this.dateClose = fotm.dateClose;
        this.dateExpire = fotm.dateExpire;
        this.units = fotm.units;
        this.priceOpen = fotm.priceOpen;
        this.priceClose = fotm.priceClose;
        this.totalOpen = fotm.totalOpen;
        this.totalClose = fotm.totalClose;
        this.gain = fotm.gain;
        this.gainPct = fotm.gainPct;
        this.equityType = fotm.equityType;
        this.positionType = fotm.positionType;
        this.transactionType = fotm.transactionType;
        this.complete = fotm.complete;
        this.bComplete = complete == 1;
        this.days = fotm.days;
    }

    private Integer dmAcctId;
    private Integer joomlaId;
    private String fiTId;
    private Integer transactionGrp;
    private String ticker;
    private String equityId;
    private String transactionName;
    private java.sql.Date dateOpen;
    private java.sql.Date dateClose;
    private java.sql.Date dateExpire;
    private Double units;
    private Double priceOpen;
    private Double priceClose;
    private Double totalOpen;
    private Double totalClose;
    private Double gain;
    private Double gainPct;
    private String equityType;
    private String positionType; //long, short
    private String transactionType;   //buy, sell, buytoopen, selltopen
    private Integer complete;
    private Integer days;
    private Boolean bComplete;

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
