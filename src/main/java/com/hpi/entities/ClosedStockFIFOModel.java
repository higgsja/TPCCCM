package com.hpi.entities;

import java.sql.*;
import lombok.*;

@AllArgsConstructor
@Getter @Setter
@Builder
public class ClosedStockFIFOModel {
    public static final String INSERT_VALUES = "insert into hlhtxc5_dmOfx.ClosedStockFIFO (%s) values ";

    public static final String ALL_FIELDS =
        "DMAcctId, JoomlaId, FiTId, TransactionGrp, EquityId, TransactionName, Ticker, DateOpen, DateClose, Units, PriceOpen, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, TotalClose, CurSym, SubAcctSec, SubAcctFund, TransactionType, ReversalFiTId, GainPct, Complete";
    
public static final String STUB_FIELDS =
        "DMAcctId, JoomlaId, FiTId, TransactionGrp, EquityId, TransactionName, Ticker, DateOpen, DateClose, Units, PriceOpen, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, CurSym, SubAcctSec, SubAcctFund, TransactionType, ReversalFiTId, GainPct, Complete";

    public static final String DEMO_FIELDS =
        "FiTId, TransactionGrp, EquityId, TransactionName, Ticker, DateOpen, DateClose, Units, PriceOpen, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, TotalClose, CurSym, SubAcctSec, SubAcctFund, TransactionType, ReversalFiTId, GainPct, Complete";

    public static final String insertAll(OpeningStockModel os, Integer userId) {
        StringBuilder sb;

        sb = new StringBuilder();

        sb.append("(").append(os.getDmAcctId()).append(",");
        sb.append("").append(userId).append(",");
        sb.append("'").append(os.getFiTId()).append("',");
        sb.append("").append("null").append(",");  //transactionGroup
        sb.append("'").append(os.getEquityId()).append("',");
        sb.append("'").append(os.getEquityId()).append("',"); //transactionName
        sb.append("'").append(os.getTicker()).append("',");
        if (os.getDateOpen() == null) {
            sb.append("").append(os.getDateOpen()).append(",");
        }
        else {
            sb.append("'").append(os.getDateOpen()).append("',");
        }

        if (os.getDateClose() == null) {
            sb.append("").append(os.getDateClose()).append(",");
        }
        else {
            sb.append("'").append(os.getDateClose()).append("',");
        }

        sb.append("").append(os.getUnits()).append(",");
        sb.append("").append(os.getPriceOpen()).append(",");
        sb.append("").append(os.getPriceClose()).append(",");
        sb.append("").append(os.getMarkUpDn()).append(",");
        sb.append("").append(os.getCommission()).append(",");
        sb.append("").append(os.getTaxes()).append(",");
        sb.append("").append(os.getFees()).append(",");
        sb.append("").append(os.getTransLoad()).append(",");
        sb.append("").append(os.getTotalOpen()).append(",");
//        sb.append("").append(os.getTotalClose()).append(",");
        sb.append("").append(os.getCurSym() == null ? null : os.getCurSym()).append(",");
        sb.append("'").append(os.getSubAcctSec()).append("',");
        sb.append("'").append(os.getSubAcctFund()).append("',");
        sb.append("'").append(os.getTransactionType()).append("',");

        if (os.getReversalFiTId() == null) {
            sb.append("").append(os.getReversalFiTId()).append(",");
        }
        else {
            sb.append("'").append(os.getReversalFiTId()).append("',");
        }
        
        sb.append("").append("null").append(","); //gainPct should be null but cannot
        sb.append("").append(0).append("");  //complete
        sb.append(");");

        return sb.toString();
    }

    private Integer dmAcctId;
    private Integer joomlaId;
    private String fiTId;
    private Integer tranactionGrp;
    private String equityId;
    private String transactionName;
    private String ticker;
    private Date dateOpen;
    private Date dateClose;
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
    private Double gainPct;
    private Integer complete;
    private Boolean bComplete;  //derived from complete

    public void setBComplete(Boolean complete) {
        this.bComplete = complete;

        if (this.bComplete) {
            this.complete = 1;
        }
        else {
            this.complete = 0;
        }
    }

    public void setComplete(Integer complete) {
        this.complete = complete;

        this.bComplete = this.complete == 1;
    }
}
