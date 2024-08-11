package com.hpi.entities;

import lombok.*;

@AllArgsConstructor
public class ClientClosingStockModel
{
public static final String DMOFX = "insert ignore into hlhtxc5_dmOfx.ClosingStock (%s) select %s from hlhtxc5_dmOfx.ClientClosingStock where not(Units = 0) and JoomlaId = '%s';";
    
    public static final String DMOFX_UNITS_UPDATE = "update hlhtxc5_dmOfx.ClosingStock, hlhtxc5_dmOfx.ClientClosingStock set ClientClosingStock.Units = 0.0 where ClosingStock.DMAcctId = ClientClosingStock.DMAcctId and ClosingStock.JoomlaId = ClientClosingStock.JoomlaId and ClosingStock.FiTId = ClientClosingStock.FiTId and ClientClosingStock.JoomlaId = '%s';";
    
    private final Integer dmAcctId;
    private final Integer joomlaId;
    private final String fiTId;
    private final String secId;
    private final String equityId;
    private final String ticker;
    private final java.sql.Date dateOpen;
    private final java.sql.Date dateClose;
    private final Double origUnits;
    private final Double units;
    private final Double priceOpen;
    private final Double priceClose;
    private final Double markUpDn;
    private final Double commission;
    private final Double taxes;
    private final Double fees;
    private final Double transLoad;
    private final Double totalOpen;
    private final Double totalClose;
    private final String curSym;
    private final String subAcctSec;
    private final String subAcctFund;
    private final String equityType;
    private final String optionType;
    private final String transactionType;
    private final String reversalFiTId;
    private final String comment;
    
    public static final String ALL_FIELDS = "DMAcctId, JoomlaId, FiTId, EquityId, Ticker, DateOpen, DateClose, OrigUnits, Units, PriceOpen, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, TotalClose, CurSym, SubAcctSec, SubAcctFund, EquityType, OptionType, TransactionType, ReversalFiTId, Comment";
    
    public static final String DEMO_FIELDS = "FiTId, EquityId, Ticker, DateOpen, DateClose, OrigUnits, Units, PriceOpen, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, TotalClose, CurSym, SubAcctSec, SubAcctFund, EquityType, OptionType, TransactionType, ReversalFiTId, Comment";
}
