package com.hpi.entities;

import java.sql.*;
import lombok.*;

@RequiredArgsConstructor
@Getter
@Setter
public class OpenStockFIFOModel
{

    public static final String UPDATE_GAIN_PCT
        = "update hlhtxc5_dmOfx.OpenStockFIFO, hlhtxc5_dmOfx.Util_LastDailyStock set OpenStockFIFO.GainPct = if (OpenStockFIFO.Units < 0, -round(100 * (Util_LastDailyStock.Close - OpenStockFIFO.PriceOpen)/OpenStockFIFO.PriceOpen, 4), round(100 * (Util_LastDailyStock.Close - OpenStockFIFO.PriceOpen)/OpenStockFIFO.PriceOpen, 4)) where Util_LastDailyStock.EquityId = OpenStockFIFO.EquityId and OpenStockFIFO.JoomlaId = '%s';";

    public static final String INSERT_ALL_VALUES = "insert ignore into hlhtxc5_dmOfx.OpenStockFIFO (%s) values ";

    public static final String ALL_FIELDS
        = "DMAcctId, JoomlaId, FiTId, EquityId, TransactionName, Ticker, DateOpen, DateClose, Units, PriceOpen, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, TotalClose, CurSym, SubAcctSec, SubAcctFund, TransactionType, ReversalFiTId";

    public static final String DEMO_FIELDS
        = "FiTId, EquityId, TransactionName, Ticker, DateOpen, DateClose, Units, PriceOpen, PriceClose, MarkUpDn, Commission, Taxes, Fees, TransLoad, TotalOpen, TotalClose, CurSym, SubAcctSec, SubAcctFund, TransactionType, ReversalFiTId";

    public static final String insertAll(OpeningStockModel os, Integer userId)
    {
        StringBuilder sb;

        sb = new StringBuilder();
//        sb.append(INSERT_ALL_VALUES);

        sb.append("(").append(os.getDmAcctId()).append(",");
        sb.append("").append(userId).append(",");
        sb.append("'").append(os.getFiTId()).append("',");
        sb.append("'").append(os.getEquityId()).append("',");
        sb.append("'").append(os.getEquityId()).append("',"); //transactionName
        sb.append("'").append(os.getTicker()).append("',");
        sb.append("'").append(os.getDateOpen()).append("',");
        sb.append("").append("null").append(",");   //dateClose, null
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
        sb.append("").append(os.getReversalFiTId() == null ? null : os.getReversalFiTId());
        sb.append(");");

        return sb.toString();
    }

    private Integer dmAcctId;
    private Integer joomlaId;
    private String fiTId;
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
    private Double mktVal;
}
