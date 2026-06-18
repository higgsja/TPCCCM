package com.hpi.entities;

import lombok.*;

/**
 * @author Joe@Higgs-Tx.com
 */
@Getter @Setter @Builder
public class ClosedOptionTrans
{
    public static final String INSERT_ALL_VALUES = "insert into hlhtxc5_dmOfx.ClosedOptionTrans (%s) values (";
    
    public static final String ALL_FIELDS = "DMAcctId, JoomlaId, FiTIdOpening, TransactionGrp, FiTIdClosing, Ticker, EquityId, TransactionName, OptionType, StrikePrice, DateOpen, DateClose, DateExpire, ShPerCtrct, Units, PriceOpen, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, TotalClose, CurSym, SubAcctSec, SubAcctFund, TransactionType, ReversalFiTId";
    
    public static final String DEMO_FIELDS = "FiTIdOpening, TransactionGrp, FiTIdClosing, Ticker, EquityId, TransactionName, OptionType, StrikePrice, DateOpen, DateClose, DateExpire, ShPerCtrct, Units, PriceOpen, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, TotalClose, CurSym, SubAcctSec, SubAcctFund, TransactionType, ReversalFiTId";

    private Integer dmAcctId;
    private Integer joomlaId;
    private String fiTIdOpening;
    private Integer transactionGrp;
    private String fiTIdClosing;
    private String ticker;
    private String equityId;
    private String transactionName;
    private String optionType;
    private Double strikePrice;
    private java.sql.Date DateOpen;
    private java.sql.Date DateClose;
    private java.sql.Date DateExpire;
    private Integer shPerCtrct;
    private Double units;
    private Double PriceOpen;
    private Double PriceClose;
    private Double markUpDn;
    private Double commission;
    private Double taxes;
    private Double fees;
    private Double transLoad;
    private Double totalOpen;
    private Double totalClose;
    private String curSym;
    private String subAcctSec;
    private String subAcctFund;
    private String transactionType;
    private String reversalFiTId;    
}
