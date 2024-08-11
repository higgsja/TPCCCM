package com.hpi.entities;

import lombok.*;

@Getter @Setter @Builder
public class OpenOptionFIFOModel {
//    public static final String UPDATE_MKTVAL = "update hlhtxc5_dmOfx.OpenOptionFIFO as osf, hlhtxc5_dmOfx.Util_LastDailyOption as lds set MktVal = Price * Units * ShPerCtrct where osf.EquityId = lds.EquityId and JoomlaId = '%s';";
//    
//    public static final String CLEAR_MKTVAL = "update hlhtxc5_dmOfx.OpenOptionFIFO set MktVal = 0.0 where JoomlaId = '%s';";
    
    public static final String INSERT_ALL_VALUES = "insert into hlhtxc5_dmOfx.OpenOptionFIFO (%s) values ";
    
//    public static final String UPDATE_GAIN = "update hlhtxc5_dmOfx.OpenOptionFIFO, hlhtxc5_dmOfx.Util_LastDailyOption set OpenOptionFIFO.GainPct = if (Units < 0, -round(100 * (Util_LastDailyOption.Price - OpenOptionFIFO.PriceOpen)/OpenOptionFIFO.PriceOpen, 4), round(100 * (Util_LastDailyOption.Price - OpenOptionFIFO.PriceOpen)/OpenOptionFIFO.PriceOpen, 4)) where Util_LastDailyOption.EquityId = OpenOptionFIFO.EquityId and OpenOptionFIFO.JoomlaId ='%s'";
    
    public static final String ALL_FIELDS =
        "DMAcctId, JoomlaId, FiTId, Ticker, EquityId, TransactionName, OptionType, StrikePrice, DateOpen, DateClose, DateExpire, ShPerCtrct, Units, PriceOpen, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, TotalClose, CurSym, SubAcctSec, SubAcctFund, TransactionType, ReversalFiTId, Complete, OpeningOpen, OpeningHigh, OpeningLow, OpeningClose, Days";

    public static final String DEMO_FIELDS =
        "FiTId, Ticker, EquityId, TransactionName, OptionType, StrikePrice, DateOpen, DateClose, DateExpire, ShPerCtrct, Units, PriceOpen, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, TotalClose, CurSym, SubAcctSec, SubAcctFund, TransactionType, ReversalFiTId, Complete, OpeningOpen, OpeningHigh, OpeningLow, OpeningClose, Days";
    
public static final String ACTIVE_OPTIONS = "select EquityId from hlhtxc5_dmOfx.OpenOptionFIFO oof where oof.DateExpire >= curdate();";
    
public static final String insertAll(OpeningOptionModel oo, Integer userId) {
        StringBuilder sb;

        sb = new StringBuilder();
        sb.append("(").append(oo.getDmAcctId()).append(",");
        sb.append("").append(userId).append(",");
        sb.append("'").append(oo.getFiTId()).append("',");
        sb.append("'").append(oo.getTicker()).append("',");   //ticker
        sb.append("'").append(oo.getEquityId()).append("',");   //equityId
        sb.append("'").append(oo.getEquityId()).append("',"); //transactionName
        sb.append("'").append(oo.getOptionType()).append("',");
        sb.append("").append(oo.getStrikePrice()).append(",");
        sb.append("'").append(oo.getDateOpen()).append("',");
        sb.append("").append("null").append(",");   //dateClose, null
        sb.append("'").append(oo.getDateExpire()).append("',");
        sb.append("'").append(oo.getShPerCtrct()).append("',");
        sb.append("").append(oo.getUnits()).append(",");
        sb.append("").append(oo.getPriceOpen()).append(",");
        sb.append("").append(oo.getPriceClose()).append(",");
        sb.append("").append(oo.getMarkUpDn()).append(",");
        sb.append("").append(oo.getCommission()).append(",");
        sb.append("").append(oo.getTaxes()).append(",");
        sb.append("").append(oo.getFees()).append(",");
        sb.append("").append(oo.getTransLoad()).append(",");
        sb.append("").append(oo.getTotalOpen()).append(",");
        sb.append("").append(oo.getTotalClose()).append(",");
        sb.append("").append(oo.getCurSym() == null ? null : oo.getCurSym()).append(",");
        sb.append("'").append(oo.getSubAcctSec()).append("',");
        sb.append("'").append(oo.getSubAcctFund()).append("',");
        sb.append("'").append(oo.getTransactionType()).append("',");
        sb.append("").append(oo.getReversalFiTId() == null ? null : oo.getReversalFiTId());
        sb.append(",").append(0).append(",");    //complete
        sb.append("").append(oo.getOpeningOpen()).append(",");
        sb.append("").append(oo.getOpeningHigh()).append(",");
        sb.append("").append(oo.getOpeningLow()).append(",");
        sb.append("").append(oo.getOpeningClose()).append(",");
        sb.append("").append("datediff('").append(oo.getDateExpire()).append("', now())");
        sb.append(");");

        return sb.toString();
    }

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
    private String subAcctSec;
    private String subAcctFund;
    private String transactionType;
    private String reversalFiTId;
    private Integer complete;
    private Boolean bComplete;
    private Double openingOpen;
    private Double openingHigh;
    private Double openingLow;
    private Double openingClose;
}
