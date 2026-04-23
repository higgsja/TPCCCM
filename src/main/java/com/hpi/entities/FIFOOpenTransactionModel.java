package com.hpi.entities;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class FIFOOpenTransactionModel
{

    public static final Integer NOT_COMPLETE = 0;
    public static final Integer COMPLETE = 1;

    public static final String MKTVAL_TOTAL_CASH
        = "select sum(MktVal) from hlhtxc5_dmOfx.FIFOOpenTransactions where FIFOOpenTransactions.EquityId = 'CASH' and FIFOOpenTransactions.JoomlaId = '%s';";

    public static final String MKTVAL_TOTAL_STOCK
        = "select sum(MktVal) from hlhtxc5_dmOfx.FIFOOpenTransactions where EquityType = 'STOCK' and FIFOOpenTransactions.JoomlaId = '%s';";

    public static final String MKTVAL_TOTAL_OPTION
        = "select sum(MktVal) from hlhtxc5_dmOfx.FIFOOpenTransactions where EquityType = 'OPTION' and FIFOOpenTransactions.JoomlaId = '%s';";

    public static final String UPDATE_CLIENTSECTORID
        = "update hlhtxc5_dmOfx.ClientEquityAttributes, hlhtxc5_dmOfx.FIFOOpenTransactions set FIFOOpenTransactions.ClientSectorId = ClientEquityAttributes.ClientSectorId where ClientEquityAttributes.Ticker = FIFOOpenTransactions.Ticker and ClientEquityAttributes.Ticker <> 'CASH' and ClientEquityAttributes.JoomlaId = FIFOOpenTransactions.JoomlaId and FIFOOpenTransactions.JoomlaId = '%s';";

    //abs() only due to etrade bad data
    public static final String INSERT_CASH_BALANCE
        = "insert ignore into hlhtxc5_dmOfx.FIFOOpenTransactions (DMAcctId, JoomlaId, FiTId, Ticker, EquityId, TransactionName, DateOpen, Units, PriceOpen, TotalOpen, EquityType, PositionType, Complete, ShPerCtrct, Days, ClientSectorId, MktVal, LMktVal) select Balances.DMAcctId, Balances.JoomlaId, 'CASH', 'CASH', 'CASH', 'CASH', now(), Balances.`Value`, 1, Balances.`Value`, 'Cash', 'LONG', 0, 1, 0, %s, Balances.`Value`, abs(Balances.`Value`) from hlhtxc5_dmOfx.Balances inner join (select DMAcctId, max(DtAsOf) as DtAsOf from hlhtxc5_dmOfx.Balances where JoomlaId = '%s' group by DMAcctId) as A on Balances.DMAcctId = A.DMAcctId inner join hlhtxc5_dmOfx.ClientAccts on A.DMAcctId = ClientAccts.DMAcctId where Balances.`Name` = 'AvailCash' and Balances.DtAsOf = A.DtAsOf and ClientAccts.Active = 'Yes';";

    //abs() here as etrade delivers negative cash sometimes and inappropriately
    //not leveraged, like stock
    public static final String UPDATE_CASH_LMKTVAL
        = "update hlhtxc5_dmOfx.FIFOOpenTransactions set LMktVal = abs(MktVal) where FIFOOpenTransactions.JoomlaId = '%s' and EquityType = 'CASH';";

    public static final String UPDATE_ACTUAL_PORTFOLIO_PCT_IN_SECTOR_LIST
        = "update hlhtxc5_dmOfx.ClientSectorList set ActPct = if (%s = 0, 0, if (isnull(LMktVal), 0, round(100 * (abs(LMktVal) / %s), 4))) where JoomlaId = '%s';";

    public static final String UPDATE_OPTION_ACTUAL_PCT
        = "update hlhtxc5_dmOfx.FIFOOpenTransactions set ActPct = if(%s = 0, 0, round((100 * LMktVal) / %s, 4)) where EquityType = 'OPTION' and FIFOOpenTransactions.JoomlaId = '%s';";

    //already ensured all LMktVal are positive
    public static final String UPDATE_STOCK_ACTUAL_PCT
        = "update hlhtxc5_dmOfx.FIFOOpenTransactions set ActPct = if(%s = 0, 0, round(100 * LMktVal / %s, 4)) where EquityType = 'STOCK' and FIFOOpenTransactions.JoomlaId = '%s';";

    public static final String UPDATE_CASH_ACTUAL_PCT
        = "update hlhtxc5_dmOfx.FIFOOpenTransactions set ActPct = if(%s = 0, 0, round(100 * LMktVal / %s, 4)) where EquityType = 'CASH' and FIFOOpenTransactions.JoomlaId = '%s';";

    public static final String LEVERAGED_TOTAL
        = "select sum(LMktVal) from hlhtxc5_dmOfx.FIFOOpenTransactions where FIFOOpenTransactions.JoomlaId = '%s';";

    public static final String LEVERAGE_RATIO_OPTIONS
        = "select (sum(abs(LMktVal)) / sum(ldo.Price * abs(Units) * ShPerCtrct)) from hlhtxc5_dmOfx.FIFOOpenTransactions as fp, hlhtxc5_dmOfx.Util_LastDailyOption as ldo where fp.EquityId = ldo.EquityId and EquityType = 'OPTION' and fp.JoomlaId = '%s';";

    /**
     * stock mktVal and lMktVal are the same
     */
    public static final String UPDATE_STOCK_MKTVAL
        = "update hlhtxc5_dmOfx.FIFOOpenTransactions as fp, hlhtxc5_dmOfx.OpenStockFIFO as of, hlhtxc5_dmOfx.Util_LastDailyStock as lds set fp.MktVal = lds.Close * fp.Units where EquityType = 'STOCK' and fp.EquityId = of.EquityId and fp.EquityId = lds.EquityId and fp.JoomlaId = '%s';";

    /**
     * abs(mktVal) as whether long or short, on the hook for some bucks
     */
    public static final String UPDATE_STOCK_LMKTVAL
        = "update hlhtxc5_dmOfx.FIFOOpenTransactions set LMktVal = abs(MktVal) where FIFOOpenTransactions.JoomlaId = '%s' and EquityType = 'STOCK';";

    /**
     * mktVal is the value of the options; lMktVal is the value of the controlled
     * underlying (mostly, with some hpi special sauce ...)
     */
    public static final String UPDATE_OPTION_MKTVAL
        = "update hlhtxc5_dmOfx.FIFOOpenTransactions as fp, hlhtxc5_dmOfx.OpenOptionFIFO as of, hlhtxc5_dmOfx.Util_LastDailyOption as ldo set fp.MktVal = ldo.LastPrice * of.ShPerCtrct * of.Units where EquityType = 'OPTION' and fp.EquityId = of.EquityId and fp.EquityId = ldo.EquityId and fp.DateOpen = of.DateOpen and fp.Units = of.Units and fp.PriceOpen = of.PriceOpen and fp.JoomlaId = '%s';";

    //calculate total shares multiplied by current price
    public static final String UPDATE_OPTION_LMKTVAL
        = "update hlhtxc5_dmOfx.FIFOOpenTransactions as fp, hlhtxc5_dmOfx.Util_LastDailyStock as lds set LMktVal = abs(Units * ShPerCtrct * `Close`) where EquityType = 'OPTION' and fp.Ticker = lds.EquityId and fp.JoomlaId = '%s';";

    public static final String OPEN_OPTION_FIFO_2_FIFO_OPEN_TRANSACTIONS
        = "insert into hlhtxc5_dmOfx.FIFOOpenTransactions (DMAcctId, JoomlaId, FiTId, Ticker, EquityId, TransactionName, DateOpen, DateExpire, Units, PriceOpen, TotalOpen, EquityType, PositionType, TransactionType, Complete, OptionType, StrikePrice, ShPerCtrct, Days) select DMAcctId, JoomlaId, FiTId, Ticker, EquityId, TransactionName, DateOpen, DateExpire, Units, PriceOpen, TotalOpen, 'OPTION', if(strcmp(TransactionType,'BUYTOOPEN') = 0, 'LONG', 'SHORT') as PositionType, TransactionType, 0, OptionType, StrikePrice, ShPerCtrct, Days from hlhtxc5_dmOfx.OpenOptionFIFO where DateExpire >= CurDate() and JoomlaId = '%s'";

    public static final String OPEN_STOCK_FIFO_2_FIFO_OPEN_TRANSACTIONS
        = "insert into hlhtxc5_dmOfx.FIFOOpenTransactions (DMAcctId, JoomlaId, FiTId, EquityId, TransactionName, Ticker, DateOpen, ShPerCtrct, Units, PriceOpen, TotalOpen, TransactionType, EquityType, PositionType, Complete, Days) select DMAcctId, JoomlaId, FiTId, EquityId, TransactionName, Ticker, DateOpen, 1, Units, PriceOpen, TotalOpen, TransactionType, 'STOCK', if(strcmp(TransactionType,'BUY') = 0, 'LONG', 'SHORT'), 0, 0 from hlhtxc5_dmOfx.OpenStockFIFO where EquityId <> 'Unknown' and JoomlaId = '%s';";

    public static final String SELECT_INCOMPLETE_BY_JOOMLAID_EQUITYTYPE
        = "select * from hlhtxc5_dmOfx.%s as fct where fct.Complete <> 1 and fct.JoomlaId = '%s' and fct.EquityType = '%s' order by fct.EquityId, fct.DMAcctId";

    public static final String UPDATE_COMPLETE
        = "update hlhtxc5_dmOfx.%s set Complete = '%s' where DMAcctId = '%s' and JoomlaId = '%s' and FiTId = '%s'";

    public static final String ALL_FIELDS
        = "DMAcctId, JoomlaId, FiTId, Ticker, EquityId, TransactionName, DateOpen, DateClose, DateExpire, Units, PriceOpen, PriceClose, TotalOpen, TotalClose, Gain, GainPct, EquityType, PositionType, TransactionType, Complete, OptionType, StrikePrice, ShPerCtrct, Days, ClientSectorId, MktVal, LMktVal, ActPct";

    public static final String DEMO_FIELDS
        = "FiTId, TransactionGrp, Ticker, EquityId, TransactionName, DateOpen, DateClose, DateExpire, Units, PriceOpen, PriceClose, TotalOpen, TotalClose, Gain, GainPct, EquityType, PositionType, TransactionType, Complete, OptionType, StrikePrice, ShPerCtrct, Days, ClientSectorId, MktVal, LMktVal, ActPct";

    public FIFOOpenTransactionModel(FIFOOpenTransactionModel fotm)
    {
        this.dmAcctId = fotm.dmAcctId;
        this.joomlaId = fotm.joomlaId;
        this.fiTId = fotm.fiTId;
        this.ticker = fotm.ticker;
        this.equityId = fotm.equityId;
        this.transactionName = fotm.transactionName;
        this.dateOpen = fotm.dateOpen;
        this.dateClose = fotm.dateClose;
        this.dateExpire = fotm.dateExpire;
        this.units = fotm.units;
        this.priceOpen = fotm.priceOpen;
        this.priceClose = fotm.priceClose;
        this.totalOpen = fotm.totalOpen;
        this.totalClose = fotm.totalClose;
        this.gain = fotm.gain;
        this.gainPct = fotm.gainPct;
        this.equityType = fotm.equityType;
        this.positionType = fotm.positionType;
        this.transactionType = fotm.transactionType;
        this.complete = fotm.complete;
        this.bComplete = fotm.bComplete;
        this.optionType = fotm.optionType;
        this.strikePrice = fotm.strikePrice;
        this.shPerCtrct = fotm.shPerCtrct;
        this.days = fotm.days;
        this.clientSectorId = fotm.clientSectorId;
        this.mktVal = fotm.mktVal;
        this.lMktVal = fotm.lMktVal;
        this.actPct = fotm.actPct;
    }

    private Integer dmAcctId;
    private Integer joomlaId;
    private String fiTId;
    private String ticker;
    private String equityId;
    private String transactionName;
    private java.sql.Date dateOpen;
    private java.sql.Date dateClose;
    private java.sql.Date dateExpire;
    private Double units;
    private Double priceOpen;
    private Double priceClose;
    private Double totalOpen;
    private Double totalClose;
    private Double gain;
    private Double gainPct;
    private String equityType;
    private String positionType; //long, short
    private String transactionType;   //buy, sell, buytoopen, selltopen
    private Integer complete;
    private Boolean bComplete;
    //do not store clientSectorId here as it can change; get it from ClientEquityAttributes as required
    //for open that is where we get it so go ahead and store
    //todo: for consistency should eliminate this
    private String optionType;
    private Double strikePrice;
    private Integer shPerCtrct;
    private Integer days;
    private Integer clientSectorId;
    private Double mktVal;
    private Double lMktVal;
    private Double actPct;

    public void setBComplete(Boolean complete)
    {
        this.bComplete = complete;

        if (this.bComplete) {
            this.complete = 1;
        } else {
            this.complete = 0;
        }
    }

    public void setComplete(Integer complete)
    {
        this.complete = complete;

        this.bComplete = this.complete == 1;
    }
}
