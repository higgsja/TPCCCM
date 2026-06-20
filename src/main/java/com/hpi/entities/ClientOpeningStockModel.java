package com.hpi.entities;

import lombok.*;

@AllArgsConstructor
public class ClientOpeningStockModel
{
    public static final String DMOFX = "insert ignore into hlhtxc5_dmOfx.OpeningStock (%s) select %s from hlhtxc5_dmOfx.ClientOpeningStock where not(Units = 0) and JoomlaId = '%s';";
    
    public static final String DMOFX_UNITS_UPDATE = "update hlhtxc5_dmOfx.OpeningStock, hlhtxc5_dmOfx.ClientOpeningStock set ClientOpeningStock.Units = 0.0 where OpeningStock.DMAcctId = ClientOpeningStock.DMAcctId and OpeningStock.JoomlaId = ClientOpeningStock.JoomlaId and OpeningStock.FiTId = ClientOpeningStock.FiTId and ClientOpeningStock.JoomlaId = '%s';";
    
    
    private final Integer dmAcctId;
    private final Integer joomlaId;
    private final String fiTId;
    private final String secId;
    private final String equityId;
    private final String ticker;
    private final java.sql.Date dataOpen;
    private final java.sql.Date dateClose;
    private final Integer shPerCtrct;
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
