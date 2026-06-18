package com.hpi.entities;

import com.hpi.TPCCMcontrollers.*;
import static com.hpi.TPCCMcontrollers.CMDBController.getConnection;
import com.hpi.hpiUtils.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import org.jfree.data.general.*;
import org.jfree.data.time.*;

public class TransactionLogXYModel
{

    private static final String SQL;
    private static final ArrayList<TransactionLogXYModel> LOG_XYMODELS;
    private static TimeSeriesCollection dataSetCollection;
    private static TimeSeries timeSeries1;
    private static TimeSeries timeSeries2;

    private String acctName;
    private String equityId;
    private String gmtDtTrade;
    private Double units;
    private Double unitPrice;
    private String transType;
    private String tacticName;
    private Double stopLoss;
    private Double targetGain;
    private String triggerName;
    private String comment;
    private String openClose;
    private String equityType;

    static
    {
        SQL = "select Accts.ClientAcctName, EquityId, GMTDtTrade, Units, UnitPrice, TransType, Tactics.TacticName, StopLoss, TargetGain, Triggers.TriggerName, Comment, OpenClose, Log.EquityType from hlhtxc5_dmOfx.TransactionLog as Log, hlhtxc5_dmOfx.TradeTactics as `Tactics`, hlhtxc5_dmOfx.TradeTriggers as `Triggers`, hlhtxc5_dmOfx.ClientAccts as Accts where Log.JoomlaId = '%s' and Log.TradeTactic = `Tactics`.TacticIndex and Log.TradeTrigger = `Triggers`.TriggerIndex and Log.DMAcctId = Accts.DMAcctId and Log.EquityId like '%s%%' order by EquityId, GMTDtTrade";

        LOG_XYMODELS = new ArrayList<>();
        dataSetCollection = null;
    }

    public TransactionLogXYModel(String acctName, String equityId,
          String gmtDtTrade, Double units, double unitPrice, String transType,
          String tacticName, Double stopLoss, Double targetGain,
          String triggerName, String comment,
          String openClose, String equityType)
    {
        this.acctName = acctName;
        this.equityId = equityId;
        this.gmtDtTrade = gmtDtTrade;
        this.units = units;
        this.unitPrice = unitPrice;
        this.transType = transType;
        this.tacticName = tacticName;
        this.stopLoss = stopLoss;
        this.targetGain = targetGain;
        this.triggerName = triggerName;
        this.comment = comment;
        this.openClose = openClose;
        this.equityType = equityType;
    }

    public static final void initCustom()
    {

    }

    public static void doQuery(String userId, String equityId)
    {
        ResultSet rs;
        String sql, s;
        TransactionLogXYModel tempTLXYModel;

        LOG_XYMODELS.clear();

        sql = String.format(SQL, userId, equityId);

        // query to get ohlcv data
        try (Connection con = getConnection();
             PreparedStatement pStmt = con.prepareStatement(sql))
        {
            pStmt.clearWarnings();
            rs = pStmt.executeQuery();

            while (rs.next())
            {

                tempTLXYModel = new TransactionLogXYModel(
                      rs.getString("ClientAcctName"),
                      rs.getString("EquityId"),
                      rs.getString("GMTDtTrade"),
                      rs.getDouble("Units"),
                      rs.getDouble("UnitPrice"),
                      rs.getString("TransType"),
                      rs.getString("TacticName"),
                      rs.getDouble("StopLoss"),
                      rs.getDouble("TargetGain"),
                      rs.getString("TriggerName"),
                      rs.getString("Comment"),
                      rs.getString("OpenClose"),
                      rs.getString("EquityType"));

                LOG_XYMODELS.add(tempTLXYModel);
            }
            pStmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            s = String.format(CMLanguageController.getErrorProps().
                  getProperty("Formatted14"),
                  e.toString());

            CMHPIUtils.showDefaultMsg(
                  CMLanguageController.getErrorProps().getProperty("Title"),
                  Thread.currentThread().getStackTrace()[1].getClassName(),
                  Thread.currentThread().getStackTrace()[1].getMethodName(),
                  s,
                  JOptionPane.ERROR_MESSAGE);
            return;
        }

        doDataSet();
    }

    private static void doDataSet()
    {
        dataSetCollection = new TimeSeriesCollection();
        timeSeries1 = new TimeSeries("Buy");
        timeSeries2 = new TimeSeries("Sell");

        for (TransactionLogXYModel logXYModel : LOG_XYMODELS)
        {
            if (logXYModel.equityType.equalsIgnoreCase("OPTION"))
            {
                // not doing now
            }
            if (logXYModel.equityType.equalsIgnoreCase("STOCK"))
            {
                doStock(logXYModel);
            }
        }
        dataSetCollection.addSeries(timeSeries1);
        dataSetCollection.addSeries(timeSeries2);

    }

    private static void doStock(TransactionLogXYModel aModel)
    {
        try
        {
            if (aModel.getTransType().equalsIgnoreCase("buytoopen")
                  || aModel.getTransType().equalsIgnoreCase("buytoclose"))
            {
                // 2019-08-02 00:00:00
                // have a second observation on 19 dec 2018, not allowed
                timeSeries1.add(new Day(
                      Integer.parseInt(aModel.getGmtDtTrade().
                            substring(8, 10)),
                      Integer.parseInt(aModel.getGmtDtTrade().
                            substring(5, 7)),
                      Integer.parseInt(aModel.getGmtDtTrade().
                            substring(0, 4))),
                      aModel.getUnitPrice());
            }
            else
            {
                // 2019-08-02 00:00:00
                timeSeries2.add(new Day(
                      Integer.parseInt(aModel.getGmtDtTrade().
                            substring(8, 10)),
                      Integer.parseInt(aModel.getGmtDtTrade().
                            substring(5, 7)),
                      Integer.parseInt(aModel.getGmtDtTrade().
                            substring(0, 4))),
                      aModel.getUnitPrice());
            }
        }
        catch (SeriesException e)
        {
            //todo: cannot have multiple values on the same day
            // ignoring for now
            int i = 0;
        }
    }

    public String getAcctName()
    {
        return acctName;
    }

    public void setAcctName(String acctName)
    {
        this.acctName = acctName;
    }

    public String getEquityId()
    {
        return equityId;
    }

    public void setEquityId(String equityId)
    {
        this.equityId = equityId;
    }

    public String getGmtDtTrade()
    {
        return gmtDtTrade;
    }

    public void setGmtDtTrade(String gmtDtTrade)
    {
        this.gmtDtTrade = gmtDtTrade;
    }

    public Double getUnits()
    {
        return units;
    }

    public void setUnits(Double units)
    {
        this.units = units;
    }

    public String getTransType()
    {
        return transType;
    }

    public void setTransType(String transType)
    {
        this.transType = transType;
    }

    public String getTacticName()
    {
        return tacticName;
    }

    public void setTacticName(String tacticName)
    {
        this.tacticName = tacticName;
    }

    public Double getStopLoss()
    {
        return stopLoss;
    }

    public void setStopLoss(Double stopLoss)
    {
        this.stopLoss = stopLoss;
    }

    public Double getTargetGain()
    {
        return targetGain;
    }

    public void setTargetGain(Double targetGain)
    {
        this.targetGain = targetGain;
    }

    public String getTriggerName()
    {
        return triggerName;
    }

    public void setTriggerName(String triggerName)
    {
        this.triggerName = triggerName;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public String getOpenClose()
    {
        return openClose;
    }

    public void setOpenClose(String openClose)
    {
        this.openClose = openClose;
    }

    public String getEquityType()
    {
        return equityType;
    }

    public void setEquityType(String equityType)
    {
        this.equityType = equityType;
    }

    public static TimeSeriesCollection getDataSetCollection()
    {
        return dataSetCollection;
    }

    public static void setDataSetCollection(TimeSeriesCollection dataSetCollection)
    {
        TransactionLogXYModel.dataSetCollection = dataSetCollection;
    }

    public Double getUnitPrice()
    {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice)
    {
        this.unitPrice = unitPrice;
    }

    public static TimeSeries getTimeSeries1()
    {
        return timeSeries1;
    }

    public static TimeSeries getTimeSeries2()
    {
        return timeSeries2;
    }
    
}
