package com.hpi.entities;

import lombok.*;

@Getter @Setter @Builder
public class ClosingOptionModel {
    public static final String DMOFX_UPDATE_PRICES = "update hlhtxc5_dmOfx.ClosingOptions co, hlhtxc5_dmOfx.EquityHistory as eh set co.ClosingOpen = eh.Open, co.ClosingHigh = eh.High, co.ClosingLow = eh.Low, co.ClosingClose = eh.Close where co.Ticker = eh.Ticker and co.DateOpen = eh.Date and isnull(co.ClosingClose) and co.JoomlaId = '%s';";

    public static final String GET_ALL_AVAIL =
        "select * from hlhtxc5_dmOfx.ClosingOptions where DMAcctId = '%s' and EquityId = '%s' and TransactionType = '%s' and JoomlaId = '%s' and not(Units = 0) union select DMAcctId, JoomlaId, FiTId, Ticker, EquityId, TransactionName, DateOpen, DateClose, DateExpire, ShPerCtrct, Units, PriceOpen, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, TotalClose, CurSym, SubAcctSec, SubAcctFund, ReversalFiTId, Comment, ClosingOpen, ClosingHigh, ClosingLow, ClosingClose, EquityType, OptionType, TransactionType, StrikePrice from hlhtxc5_dmOfx.ClientClosingOptions where DMAcctId = '%s' and EquityId = '%s' and TransactionType = '%s' and JoomlaId = '%s' and not(Units = 0) order by EquityId, DateClose, FiTId";
    
    public static final String UPDATE_UNITS = "update hlhtxc5_dmOfx.ClosingOptions set Units = %s where DMAcctId = %s and JoomlaId = %s and FiTId = '%s';";

    public static final String ALL_FIELDS =
        "DMAcctId, JoomlaId, FiTId, Ticker, EquityId, TransactionName, DateOpen, DateClose, DateExpire, ShPerCtrct, Units, PriceOpen, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, TotalClose, CurSym, SubAcctSec, SubAcctFund, ReversalFiTId, Comment, ClosingOpen, ClosingHigh, ClosingLow, ClosingClose, EquityType, OptionType, TransactionType, StrikePrice";

    public static final String DEMO_FIELDS =
        "EquityId, TransactionName, DateOpen, DateClose, DateExpire, ShPerCtrct, Units, PriceOpen, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, TotalClose, CurSym, SubAcctSec, SubAcctFund, ReversalFiTId, Comment, ClosingOpen, ClosingHigh, ClosingLow, ClosingClose, EquityType, OptionType, TransactionType, StrikePrice";
    
    public static final String INSERT_ALL_VALUES = "insert ignore into hlhtxc5_dmOfx.ClosingOptions (" 
        + ALL_FIELDS 
        + ") values (";

    private Integer dmAcctId;
    private Integer joomlaId;
    private String fiTId;
    private String ticker;
    private String equityId;
    private String transactionName;
    private java.sql.Date dateOpen;
    private java.sql.Date dateClose;
    private java.sql.Date dateExpire;
    private Integer shPerCtrct;
    private Double units;
    private Double priceOpen;
    private Double priceClose;
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
    private String reversalFiTId;
    private String comment;
    private Double closingOpen;
    private Double closingHigh;
    private Double closingLow;
    private Double closingClose;
    private String equityType;
    private String optionType; //call, put
    private String transactionType;   //buytoopen, selltoopen, buytoclose, selltoclose
    private Double strikePrice;
}
