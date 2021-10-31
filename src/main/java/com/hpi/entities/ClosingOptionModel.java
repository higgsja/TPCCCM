package com.hpi.entities;

import lombok.*;

@Getter @Setter @Builder
public class ClosingOptionModel {
//    public static final String INSERT_ALL_VALUES = "insert into hlhtxc5_dmOfx.ClosingOption (%s) values ";
    //update underlying prices
    public static final String DMOFX_UPDATE_PRICES = "update hlhtxc5_dmOfx.ClosingOptions co, hlhtxc5_dmOfx.EquityHistory as eh set co.ClosingOpen = eh.Open, co.ClosingHigh = eh.High, co.ClosingLow = eh.Low, co.ClosingClose = eh.Close where co.Ticker = eh.Ticker and co.DateOpen = eh.Date and isnull(co.ClosingClose) and co.JoomlaId = '%s';";
    
    //add dbOfx.ClosureOpt
    public static final String DBOFX_CLOSUREOPT_DMOFX = "insert ignore into hlhtxc5_dmOfx.ClosingOptions (DMAcctId, JoomlaId, FiTId, Ticker, EquityId, TransactionName, DateOpen, DateClose, DateExpire, ShPerCtrct, Units, PriceOpen, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, TotalClose, CurSym, SubAcctSec, SubAcctFund, ReversalFiTId, Comment, ClosingOpen, ClosingHigh, ClosingLow, ClosingClose, EquityType, OptionType, TransactionType, StrikePrice) select Accounts.DMAcctId, Accounts.JoomlaId, ClosureOpt.FiTId, SecInfo.Ticker, SecInfo.EquityId, '', convert(InvTran.DtTrade, Date), null, convert(DtExpire, Date), ClosureOpt.ShPerCtrct, ClosureOpt.Units, null, 0, null, null, null, null, null, null, 0, null, ClosureOpt.SubAcctSec, null, null, '', null, null, null, null, 'OPTION', OptInfo.OptType, ClosureOpt.OptAction, OptInfo.StrikePrice from hlhtxc5_dbOfx.ClosureOpt as ClosureOpt, hlhtxc5_dbOfx.InvTran as InvTran, hlhtxc5_dbOfx.OptInfo, hlhtxc5_dbOfx.SecInfo as SecInfo, hlhtxc5_dmOfx.Accounts as Accounts where InvTran.Skip = 0 and InvTran.Complete = 0 and ClosureOpt.AcctId = InvTran.AcctId and ClosureOpt.FiTId = InvTran.FiTId and ClosureOpt.SecId = SecInfo.SecId and ClosureOpt.AcctId = Accounts.AcctId and SecInfo.BrokerId = OptInfo.BrokerId and SecInfo.SecId = OptInfo.SecId and Accounts.JoomlaId = '%s';";
    //buy to close
    public static final String DBOFX_DMOFX_BUY = "insert ignore into hlhtxc5_dmOfx.ClosingOptions (%s) select Accounts.DMAcctId, Accounts.JoomlaId, Opt.FiTId, SecInfo.Ticker, SecInfo.EquityId, '', null, convert(InvTran.DtTrade, Date), convert(DtExpire, Date), Opt.ShPerCtrct, Inv.Units, Inv.UnitPrice, null, Inv.Markup, Inv.Commission, Inv.Taxes, Inv.Fees, Inv.TransLoad, null, Inv.Total, Inv.CurSym, Inv.SubAcctSec, Inv.SubAcctFund, InvTran.ReversalFiTId, '', null, null, null, null, 'OPTION', OptInfo.OptType, Opt.OptBuyType, OptInfo.StrikePrice from hlhtxc5_dbOfx.InvBuy as Inv, hlhtxc5_dbOfx.BuyOpt as Opt, hlhtxc5_dbOfx.OptInfo, hlhtxc5_dbOfx.InvTran as InvTran, hlhtxc5_dbOfx.SecInfo as SecInfo, hlhtxc5_dmOfx.Accounts as Accounts where InvTran.Skip = 0 and InvTran.Complete = 0 and Inv.AcctId = Opt.AcctId and Inv.FiTId = Opt.FiTId and Inv.SecId = SecInfo.SecId and Inv.AcctId = Accounts.AcctId and Inv.SecId = OptInfo.SecId and Inv.AcctId = InvTran.AcctId and Inv.FiTId = InvTran.FiTId and Opt.OptBuyType = 'BUYTOCLOSE' and Accounts.JoomlaId = '%s';";
    
    //sell to close
    public static final String DBOFX_DMOFX_SELL = "insert ignore into hlhtxc5_dmOfx.ClosingOptions (%s) select Accounts.DMAcctId, Accounts.JoomlaId, Opt.FiTId, SecInfo.Ticker, SecInfo.EquityId, '', null, convert(InvTran.DtTrade, Date), convert(DtExpire, Date), Opt.ShPerCtrct, Inv.Units, null, Inv.UnitPrice, Inv.Markdown, Inv.Commission, Inv.Taxes, Inv.Fees, Inv.TransLoad, null, Inv.Total, Inv.CurSym, Inv.SubAcctSec, Inv.SubAcctFund, InvTran.ReversalFiTId, '', null, null, null, null, 'OPTION', OptInfo.OptType, Opt.OptSellType, OptInfo.StrikePrice from hlhtxc5_dbOfx.InvSell as Inv, hlhtxc5_dbOfx.SellOpt as Opt, hlhtxc5_dbOfx.OptInfo, hlhtxc5_dbOfx.InvTran as InvTran, hlhtxc5_dbOfx.SecInfo as SecInfo, hlhtxc5_dmOfx.Accounts as Accounts where InvTran.Skip = 0 and InvTran.Complete = 0 and Inv.AcctId = Opt.AcctId and Inv.FiTId = Opt.FiTId and Inv.SecId = SecInfo.SecId and Inv.AcctId = Accounts.AcctId and Inv.SecId = OptInfo.SecId and Inv.AcctId = InvTran.AcctId and Inv.FiTId = InvTran.FiTId and Opt.OptSellType = 'SELLTOCLOSE' and Accounts.JoomlaId = '%s';";
    
    //update InvTran.Complete
    public static final String DBOFX_UPDATE = "update hlhtxc5_dmOfx.ClosingOptions, hlhtxc5_dbOfx.InvTran set hlhtxc5_dbOfx.InvTran.Complete = 1 where hlhtxc5_dmOfx.ClosingOptions.FiTId = hlhtxc5_dbOfx.InvTran.FiTId and hlhtxc5_dbOfx.InvTran.AcctId = hlhtxc5_dmOfx.ClosingOptions.DMAcctId and hlhtxc5_dmOfx.ClosingOptions.JoomlaId = '%s';";
    
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
