package com.hpi.entities;

import lombok.*;

/**
 * @author Joe@Higgs-Tx.com
 */
@Getter @Setter @Builder
public class ClosedOptionFIFOModel {
    public static final String INSERT_ALL_VALUES = "insert into hlhtxc5_dmOfx.ClosedOptionFIFO (%s) values (";
    
    public static final String UPDATE_TOTAL_TOTALCLOSE = "update hlhtxc5_dmOfx.ClosedOptionFIFO set Units = '%s', Commission = '%s', Taxes = '%s', Fees = '%s', TransLoad = '%s', TotalClose = '%s', PriceClose = '%s', DateClose = '%s' where TransactionGrp = '%s';";
    
    public static final String ALL_FIELDS =
        "DMAcctId, JoomlaId, FiTId, TransactionGrp, Ticker, EquityId, TransactionName, OptionType, StrikePrice, DateOpen, DateClose, DateExpire, ShPerCtrct, Units, PriceOpen, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, TotalClose, CurSym, TransactionType";

    public static final String DEMO_FIELDS =
        "FiTId, TransactionGrp, Ticker, EquityId, TransactionName, OptionType, StrikePrice, DateOpen, DateClose, DateExpire, ShPerCtrct, Units, PriceOpen, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, TotalClose, CurSym, TransactionType";

    private Integer dmAcctId;
    private Integer joomlaId;
    private String fiTId;
    private Integer transactionGrp;
    private String ticker;
    private String equityId;
    private String transactionName;
    private String optionType;
    private Double strikePrice;
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
    private String transactionType;
}
