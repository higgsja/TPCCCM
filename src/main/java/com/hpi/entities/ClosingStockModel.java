package com.hpi.entities;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class ClosingStockModel {
    //acctOpeningStock: from openingStock and clientOpeningStock
    public static final String GET_CLOSING_STOCK_BY_ACCT = "select DMAcctId, JoomlaId, FiTId, Ticker, EquityId, DateOpen, DateClose, ShPerCtrct, Units, PriceOpen, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, TotalClose, CurSym, SubAcctSec, SubAcctFund, EquityType, OptionType, TransactionType, ReversalFiTId, Comment from hlhtxc5_dmOfx_dev.ClosingStock1 where DMAcctId = '%s' and EquityId = '%s' and JoomlaId = '%s' and not(Units = 0) union select DMAcctId, JoomlaId, FiTId, Ticker, EquityId, DateOpen, DateClose, ShPerCtrct, Units, PriceOpen, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, TotalClose, CurSym, SubAcctSec, SubAcctFund, EquityType, OptionType, TransactionType, ReversalFiTId, Comment from hlhtxc5_dmOfx_dev.ClientClosingStock where DMAcctId = '%s' and EquityId = '%s' and JoomlaId = '%s' and not(Units = 0) order by EquityId, DateClose, FiTId;";
    //sell to close
    public static final String DBOFX_DMOFX_SELL =
        "insert ignore into hlhtxc5_dmOfx_dev.ClosingStock1 (DMAcctId, JoomlaId, FiTId, EquityId, Ticker, DateClose, Units, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalClose, CurSym, SubAcctSec, SubAcctFund, TransactionType, ReversalFiTId, Comment, EquityType) select Accounts.DMAcctId, Accounts.JoomlaId, SellStock.FiTId, SecInfo.EquityId, SecInfo.Ticker, convert(InvTran.DtTrade, Date), InvSell.Units, InvSell.UnitPrice, InvSell.Markdown, InvSell.Commission, InvSell.Taxes, InvSell.Fees, InvSell.TransLoad, InvSell.Total, InvSell.CurSym, InvSell.SubAcctSec, InvSell.SubAcctFund, SellStock.SellType, InvTran.ReversalFiTId, '', 'STOCK' from hlhtxc5_dbOfx_dev.InvSell as InvSell, hlhtxc5_dbOfx_dev.SellStock as SellStock, hlhtxc5_dbOfx_dev.InvTran as InvTran, hlhtxc5_dbOfx_dev.SecInfo as SecInfo, hlhtxc5_dmOfx_dev.Accounts as Accounts where InvTran.Skip = 0 and InvTran.Complete = 0 and Accounts.JoomlaId = '%s' and SellStock.SellType <> 'SELLSHORT' and InvSell.AcctId = SellStock.AcctId and InvSell.AcctId = InvTran.AcctId and InvSell.FiTId = SellStock.FiTId and InvSell.FiTId = InvTran.FiTId and InvSell.SecId = SecInfo.SecId and SellStock.AcctId = Accounts.AcctId;";
    
    //buy to cover
    public static final String DBOFX_DMOFX_BUY =
        "insert ignore into hlhtxc5_dmOfx_dev.ClosingStock1 (DMAcctId, JoomlaId, FiTId, EquityId, Ticker, DateClose, Units, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalClose, CurSym, SubAcctSec, SubAcctFund, TransactionType, ReversalFiTId, Comment, EquityType) select Accounts.DMAcctId, Accounts.JoomlaId, BuyStock.FiTId, SecInfo.EquityId, SecInfo.Ticker, convert(InvTran.DtTrade, Date), InvBuy.Units, InvBuy.UnitPrice, InvBuy.Markup, InvBuy.Commission, InvBuy.Taxes, InvBuy.Fees, InvBuy.TransLoad, InvBuy.Total, InvBuy.CurSym, InvBuy.SubAcctSec, InvBuy.SubAcctFund, BuyStock.BuyType, InvTran.ReversalFiTId, '', 'STOCK' from hlhtxc5_dbOfx_dev.InvBuy as InvBuy, hlhtxc5_dbOfx_dev.BuyStock as BuyStock, hlhtxc5_dbOfx_dev.InvTran as InvTran, hlhtxc5_dbOfx_dev.SecInfo as SecInfo, hlhtxc5_dmOfx_dev.Accounts as Accounts where InvTran.Skip = 0 and InvTran.Complete = 0 and InvBuy.AcctId = BuyStock.AcctId and InvBuy.FiTId = BuyStock.FiTId and InvTran.AcctId = InvBuy.AcctId and InvTran.FiTId = InvBuy.FiTId and SecInfo.SecId = InvBuy.SecId and BuyStock.BuyType = 'BUYTOCOVER' and SecInfo.BrokerId = Accounts.BrokerId and BuyStock.AcctId = Accounts.AcctId and Accounts.JoomlaId = '%s';";

    //update InvTran.Complete
    public static final String DBOFX_UPDATE =
        "update hlhtxc5_dmOfx_dev.ClosingStock1, hlhtxc5_dbOfx_dev.InvTran set hlhtxc5_dbOfx_dev.InvTran.Complete = 1 where hlhtxc5_dmOfx_dev.ClosingStock1.FiTId = hlhtxc5_dbOfx_dev.InvTran.FiTId and hlhtxc5_dbOfx_dev.InvTran.AcctId = hlhtxc5_dmOfx_dev.ClosingStock1.DMAcctId and hlhtxc5_dmOfx_dev.ClosingStock1.JoomlaId  = '%s';";

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
