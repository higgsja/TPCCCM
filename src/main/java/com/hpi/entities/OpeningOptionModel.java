package com.hpi.entities;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @ AllArgsConstructor
public class OpeningOptionModel {
    //update underlying prices
    public static final String DMOFX_UPDATE_PRICES = "update hlhtxc5_dmOfx.OpeningOptions oo, hlhtxc5_dmOfx.EquityHistory as eh set oo.OpeningOpen = eh.Open, oo.OpeningHigh = eh.High, oo.OpeningLow = eh.Low, oo.OpeningClose = eh.Close where oo.Ticker = eh.Ticker and oo.DateOpen = eh.Date and isnull(oo.OpeningClose) and oo.JoomlaId = '%s';";

    public static final String GET_ALL_AVAIL =
        "select DMAcctId, JoomlaId, FiTId, Ticker, EquityId, DateOpen, DateClose, DateExpire, ShPerCtrct, Units, PriceOpen, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, TotalClose, CurSym, SubAcctSec, SubAcctFund, ReversalFiTId, Comment, OpeningOpen, OpeningHigh, OpeningLow, OpeningClose, EquityType, OptionType, TransactionType, StrikePrice from hlhtxc5_dmOfx.OpeningOptions where DMAcctId = '%s' and EquityId = '%s' and TransactionType = '%s' and JoomlaId = '%s' and not(Units = 0) union select DMAcctId, JoomlaId, FiTId, Ticker, EquityId, DateOpen, DateClose, DateExpire, ShPerCtrct, Units, PriceOpen, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, TotalClose, CurSym, SubAcctSec, SubAcctFund, ReversalFiTId, Comment, OpeningOpen, OpeningHigh, OpeningLow, OpeningClose, 'OPTION', OptionType, TransactionType, StrikePrice from hlhtxc5_dmOfx.ClientOpeningOptions where DMAcctId = '%s' and EquityId = '%s' and TransactionType = '%s' and JoomlaId = '%s' and not(Units = 0) order by EquityId, DateOpen, FiTId;";
    
        public static final String ALL_FIELDS =
        "DMAcctId, JoomlaId, FiTId, Ticker, EquityId, TransactionName, DateOpen, DateClose, DateExpire, ShPerCtrct, Units, PriceOpen, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, TotalClose, CurSym, SubAcctSec, SubAcctFund, ReversalFiTId, Comment, OpeningOpen, OpeningHigh, OpeningLow, OpeningClose, EquityType, OptionType, TransactionType, StrikePrice";

//    public static final String INSERT_ALL_VALUES = "insert into hlhtxc5_dmOfx.OpenOptionFIFO (%s) values ('";
    //used solely for test cases
    public static final String INSERT_ALL_VALUES = "insert ignore into hlhtxc5_dmOfx.OpeningOptions (" 
        + ALL_FIELDS 
        + ") values (";

    public static final String UPDATE_UNITS =
        "update hlhtxc5_dmOfx.OpeningOptions set Units = '%s' where DMAcctId = '%s' and JoomlaId = '%s' and FiTId = '%s';";


    public static final String DEMO_FIELDS =
        "FiTId, Ticker, EquityId, TransactionName, DateOpen, DateClose, DateExpire, ShPerCtrct, Units, PriceOpen, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, TotalClose, CurSym, SubAcctSec, SubAcctFund, ReversalFiTId, Comment, OpeningOpen, OpeningHigh, OpeningLow, OpeningClose, EquityType, OptionType, TransactionType, StrikePrice";

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
    private Double openingOpen;
    private Double openingHigh;
    private Double openingLow;
    private Double openingClose;
    private String equityType;
    private String optionType; //call, put
    private String transactionType;   //buytoopen, selltoopen, buytoclose, selltoclose
    private Double strikePrice;
}
