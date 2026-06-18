package com.hpi.entities;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class OpeningStockModel {
    //acctOpeningStock: from openingStock and clientOpeningStock
    public static final String GET_OPENING_STOCK_BY_ACCT = "select DMAcctId, JoomlaId, FiTId, Ticker, EquityId, DateOpen, DateClose, ShPerCtrct, Units, PriceOpen, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, TotalClose, CurSym, SubAcctSec, SubAcctFund, EquityType, OptionType, TransactionType, ReversalFiTId, Comment from hlhtxc5_dmOfx.OpeningStock where DMAcctId = '%s' and EquityId = '%s' and JoomlaId = '%s' and Units <> 0 union select DMAcctId, JoomlaId, FiTId, Ticker, EquityId, DateOpen, DateClose, ShPerCtrct, Units, PriceOpen, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, TotalClose, CurSym, SubAcctSec, SubAcctFund, EquityType, OptionType, TransactionType, ReversalFiTId, Comment from hlhtxc5_dmOfx.ClientOpeningStock where DMAcctId = '%s' and EquityId = '%s' and JoomlaId = '%s' and Units <> 0 order by EquityId, DateOpen, FiTId;";

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
