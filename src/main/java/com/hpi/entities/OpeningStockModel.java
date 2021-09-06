package com.hpi.entities;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class OpeningStockModel {
    //acctOpeningStock: from openingStock and clientOpeningStock
    public static final String GET_OPENING_STOCK_BY_ACCT = "select DMAcctId, JoomlaId, FiTId, Ticker, EquityId, DateOpen, DateClose, ShPerCtrct, Units, PriceOpen, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, TotalClose, CurSym, SubAcctSec, SubAcctFund, EquityType, OptionType, TransactionType, ReversalFiTId, Comment from hlhtxc5_dmOfx.OpeningStock where DMAcctId = '%s' and EquityId = '%s' and JoomlaId = '%s' and Units <> 0 union select DMAcctId, JoomlaId, FiTId, Ticker, EquityId, DateOpen, DateClose, ShPerCtrct, Units, PriceOpen, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, TotalClose, CurSym, SubAcctSec, SubAcctFund, EquityType, OptionType, TransactionType, ReversalFiTId, Comment from hlhtxc5_dmOfx.ClientOpeningStock where DMAcctId = '%s' and EquityId = '%s' and JoomlaId = '%s' and Units <> 0 order by EquityId, DateOpen, FiTId;";
    //buy
    public static final String DBOFX_DMOFX_BUY = "insert ignore into hlhtxc5_dmOfx.OpeningStock (DMAcctId, JoomlaId, DateOpen, FiTId, ReversalFiTId, Units, PriceOpen, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, CurSym, SubAcctSec, SubAcctFund, EquityId, Ticker, TransactionType, Comment, EquityType) select Accounts.DMAcctId, Accounts.JoomlaId, convert(InvTran.DtTrade, Date), InvTran.FiTId, InvTran.ReversalFiTId, InvBuy.Units, InvBuy.UnitPrice, InvBuy.Markup, InvBuy.Commission, InvBuy.Taxes, InvBuy.Fees, InvBuy.TransLoad, InvBuy.Total, InvBuy.CurSym, InvBuy.SubAcctSec, InvBuy.SubAcctFund, SecInfo.EquityId, SecInfo.Ticker, BuyStock.BuyType, '', 'STOCK' from hlhtxc5_dmOfx.Accounts as Accounts, hlhtxc5_dbOfx.InvTran as InvTran, hlhtxc5_dbOfx.InvBuy as InvBuy, hlhtxc5_dbOfx.SecInfo as SecInfo, hlhtxc5_dbOfx.BuyStock as BuyStock where InvTran.Skip = 0 and InvTran.Complete = 0 and BuyStock.BuyType <> 'BUYTOCOVER' and Accounts.JoomlaId = '%s' and Accounts.AcctId  = InvTran.AcctId and Accounts.AcctId = InvBuy.AcctId and InvTran.FiTId  = InvBuy.FiTId and Accounts.BrokerId = SecInfo.BrokerId and SecInfo.SecId = InvBuy.SecId and Accounts.AcctId = BuyStock.AcctId and InvTran.FiTId  = BuyStock.FiTId order by FiTId";
    
    //sell short
    public static final String DBOFX_DMOFX_SELL = "insert ignore into hlhtxc5_dmOfx.OpeningStock(DMAcctId, JoomlaId, DateOpen, FiTId, ReversalFiTId, Units, PriceOpen, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, CurSym, SubAcctSec, SubAcctFund, EquityId, Ticker, TransactionType, Comment, EquityType) select Accounts.DMAcctId, Accounts.JoomlaId, convert(InvTran.DtTrade, Date),  InvTran.FiTId, InvTran.ReversalFiTId, InvSell.Units, InvSell.UnitPrice, InvSell.MarkDown, InvSell.Commission, InvSell.Taxes, InvSell.Fees, InvSell.TransLoad, InvSell.Total, InvSell.CurSym, InvSell.SubAcctSec, InvSell.SubAcctFund, SecInfo.EquityId, SecInfo.Ticker, SellStock.SellType, '', 'STOCK' from hlhtxc5_dbOfx.InvSell as InvSell, hlhtxc5_dbOfx.SellStock as SellStock, hlhtxc5_dbOfx.InvTran as InvTran, hlhtxc5_dbOfx.SecId as SecId, hlhtxc5_dbOfx.SecInfo as SecInfo, hlhtxc5_dmOfx.Accounts as Accounts where  InvTran.Skip = 0 and InvTran.Complete = 0 and InvSell.AcctId = SellStock.AcctId and InvSell.FiTId = SellStock.FiTId and InvTran.AcctId = InvSell.AcctId and InvTran.FiTId = InvSell.FiTId and SecId.SecId = InvSell.SecId and SecInfo.BrokerId = SecId.BrokerId and SecInfo.SecId = SecId.SecId and SellStock.SellType = 'SELLSHORT' and SecId.BrokerId = Accounts.BrokerId and SellStock.AcctId = Accounts.AcctId and Accounts.JoomlaId = '%s';";
    
    //update InvTran.Complete
    public static final String DBOFX_UPDATE = "update hlhtxc5_dmOfx.OpeningStock, hlhtxc5_dbOfx.InvTran set hlhtxc5_dbOfx.InvTran.Complete = 1 where hlhtxc5_dmOfx.OpeningStock.FiTId = hlhtxc5_dbOfx.InvTran.FiTId and hlhtxc5_dbOfx.InvTran.AcctId = hlhtxc5_dmOfx.OpeningStock.DMAcctId and hlhtxc5_dmOfx.OpeningStock.JoomlaId = '%s';";
    
    
    
    private Integer dmAcctId;
    private Integer joomlaId;
    private String fiTId;
//    private String secId;
    private String ticker;
    private String equityId;
    private java.sql.Date dateOpen;
    private java.sql.Date dateClose;
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
    private String equityType;  //EquityTypeEnum
    private String optionType;  //OptionTypeEnum
    private String transactionType;   //TransTypeEnum
    private String reversalFiTId;
    private String comment;

    public static final String ALL_FIELDS =
        "DMAcctId, JoomlaId, FiTId, Ticker, EquityId, DateOpen, DateClose, ShPerCtrct, Units, PriceOpen, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, TotalClose, CurSym, SubAcctSec, SubAcctFund, EquityType, OptionType, TransactionType, ReversalFiTId, Comment";

    public static final String DEMO_FIELDS =
        "FiTId, Ticker, EquityId, DateOpen, DateClose, ShPerCtrct, Units, PriceOpen, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, TotalClose, CurSym, SubAcctSec, SubAcctFund, EquityType, OptionType, TransactionType, ReversalFiTId, Comment";
}
