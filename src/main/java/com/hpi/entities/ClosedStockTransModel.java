package com.hpi.entities;

import lombok.*;

@RequiredArgsConstructor
@Getter
public class ClosedStockTransModel {
    public static final String INSERT_VALUES = "insert into hlhtxc5_dmOfx.ClosedStockTrans (%s) values ";

    public static final String ALL_FIELDS =
        "DMAcctId, JoomlaId, FiTIdOpening, TransactionGrp, FiTIdClosing, EquityId, TransactionName, Ticker, DateOpen, DateClose, Units, PriceOpen, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, TotalClose, CurSym, SubAcctSec, SubAcctFund, TransactionType, ReversalFiTId";

    public static final String DEMO_FIELDS =
        "FiTIdOpening, TransactionGrp, FiTIdClosing, EquityId, TransactionName, Ticker, DateOpen, DateClose, Units, PriceOpen, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, TotalClose, CurSym, SubAcctSec, SubAcctFund, TransactionType, ReversalFiTId";

    public static final String insertAll(OpeningStockModel os, ClosingStockModel cs, Integer rowIndex, Integer userId) {
        StringBuilder sb;

        sb = new StringBuilder();

        sb.append("(").append(os.getDmAcctId()).append(",");
        sb.append("").append(userId).append(",");
        sb.append("'").append(os.getFiTId()).append("',");  //fiTIdOpening
        sb.append(rowIndex).append(",");  //transactionGroup
        sb.append("'").append(cs.getFiTId()).append("',");  //fiTIdClosing
        sb.append("'").append(cs.getEquityId()).append("',");
        sb.append("'").append(cs.getEquityId()).append("',"); //transactionName
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
        sb.append("").append(os.getTotalClose()).append(",");
        sb.append("").append(os.getCurSym() == null ? null : os.getCurSym()).append(",");
        sb.append("'").append(os.getSubAcctSec()).append("',");
        sb.append("'").append(os.getSubAcctFund()).append("',");
        sb.append("'").append(os.getTransactionType()).append("',");

        if (os.getReversalFiTId() == null) {
            sb.append("").append(os.getReversalFiTId()).append("");
        }
        else {
            sb.append("'").append(os.getReversalFiTId()).append("'");
        }

        sb.append(");");

        return sb.toString();
    }

    private final Integer dmAcctId;
    private final Integer joomlaId;
    private final String fiTIdOpening;
    private final Integer transactionGrp;
    private final String fiTIdClosing;
    private final String equityId;
    private final String ticker;
    private final java.sql.Date dateOpen;
    private final java.sql.Date dateClose;
    private final Double units;
    private final Double PriceOpen;
    private final Double PriceClose;
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
    private final String transactionType;
    private final String reversalFiTId;
}
