package com.hpi.entities;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @ AllArgsConstructor
public class OpeningOptionModel {
    //update underlying prices
    public static final String DMOFX_UPDATE_PRICES = "update hlhtxc5_dmOfx.OpeningOptions oo, hlhtxc5_dmOfx.EquityHistory as eh set oo.OpeningOpen = eh.Open, oo.OpeningHigh = eh.High, oo.OpeningLow = eh.Low, oo.OpeningClose = eh.Close where oo.Ticker = eh.Ticker and oo.DateOpen = eh.Date and isnull(oo.OpeningClose) and oo.JoomlaId = '%s';";
    
    //buy to open
    public static final String DBOFX_DMOFX_BUY = "insert ignore into hlhtxc5_dmOfx.OpeningOptions (%s) select Accounts.DMAcctId, Accounts.JoomlaId, Opt.FiTId, SecInfo.Ticker, SecInfo.EquityId, '', convert(InvTran.DtTrade, Date), null, convert(DtExpire, Date), Opt.ShPerCtrct, Inv.Units, Inv.UnitPrice, null, Inv.Markup, Inv.Commission, Inv.Taxes, Inv.Fees, Inv.TransLoad, Inv.Total, null, Inv.CurSym, Inv.SubAcctSec, Inv.SubAcctFund, InvTran.ReversalFiTId, '', null, null, null, null, 'OPTION', OptInfo.OptType, Opt.OptBuyType, OptInfo.StrikePrice from hlhtxc5_dbOfx.InvBuy as Inv, hlhtxc5_dbOfx.BuyOpt as Opt, hlhtxc5_dbOfx.OptInfo, hlhtxc5_dbOfx.InvTran as InvTran, hlhtxc5_dbOfx.SecInfo as SecInfo, hlhtxc5_dmOfx.Accounts as Accounts where InvTran.Skip = 0 and InvTran.Complete = 0 and Inv.AcctId = Opt.AcctId and Inv.FiTId = Opt.FiTId and Inv.SecId = SecInfo.SecId and Inv.AcctId = Accounts.AcctId and Inv.SecId = OptInfo.SecId and Inv.AcctId = InvTran.AcctId and Inv.FiTId = InvTran.FiTId and Opt.OptBuyType = 'BUYTOOPEN' and Accounts.JoomlaId = '%s';";
    
    //sell to open
    public static final String DBOFX_DMOFX_SELL = "insert ignore into hlhtxc5_dmOfx.OpeningOptions (%s) select Accounts.DMAcctId, Accounts.JoomlaId, Opt.FiTId, SecInfo.Ticker, SecInfo.EquityId, '', convert(InvTran.DtTrade, Date), null, convert(DtExpire, Date), Opt.ShPerCtrct, Inv.Units, Inv.UnitPrice, null, Inv.Markdown, Inv.Commission, Inv.Taxes, Inv.Fees, Inv.TransLoad, Inv.Total, null, Inv.CurSym, Inv.SubAcctSec, Inv.SubAcctFund, InvTran.ReversalFiTId, '', null, null, null, null, 'OPTION', OptInfo.OptType, Opt.OptSellType, OptInfo.StrikePrice from hlhtxc5_dbOfx.InvSell as Inv, hlhtxc5_dbOfx.SellOpt as Opt, hlhtxc5_dbOfx.OptInfo, hlhtxc5_dbOfx.InvTran as InvTran, hlhtxc5_dbOfx.SecInfo as SecInfo, hlhtxc5_dmOfx.Accounts as Accounts where InvTran.Skip = 0 and InvTran.Complete = 0 and Inv.AcctId = Opt.AcctId and Inv.FiTId = Opt.FiTId and Inv.SecId = SecInfo.SecId and Inv.AcctId = Accounts.AcctId and Inv.SecId = OptInfo.SecId and Inv.AcctId = InvTran.AcctId and Inv.FiTId = InvTran.FiTId and Opt.OptSellType = 'SELLTOOPEN' and Accounts.JoomlaId = '%s';";
    
    //update InvTran.Complete
    public static final String DBOFX_UPDATE = "update hlhtxc5_dmOfx.OpeningOptions, hlhtxc5_dbOfx.InvTran set hlhtxc5_dbOfx.InvTran.Complete = 1 where hlhtxc5_dmOfx.OpeningOptions.FiTId = hlhtxc5_dbOfx.InvTran.FiTId and hlhtxc5_dbOfx.InvTran.AcctId = hlhtxc5_dmOfx.OpeningOptions.DMAcctId and hlhtxc5_dmOfx.OpeningOptions.JoomlaId = '%s';";
    
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
