/*
 * Copyright 2015 all rights reserved.
 * HLH LLC
 */
package com.hpi.entities;

import com.hpi.hpiUtils.CMHPIUtils;
import com.hpi.TPCCMcontrollers.*;
import static java.lang.Math.abs;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author Joe@Higgs-Tx.com
 */
public class LotsModel
{

    private final ArrayList<LotModel> lotsModel;
    private final TransactionsModel tempTransactionsModel;
    private final String errorPrefix;
    private String fErrorPrefix;

    public LotsModel()
    {
        this.lotsModel = new ArrayList<>();
        tempTransactionsModel = new TransactionsModel();

        this.errorPrefix = "LotsModel";
        this.fErrorPrefix = null;
    }

    public Boolean doLots(String sBrokerId, String sAcctId, String sSecUniqueId)
    {
        fErrorPrefix = Thread.currentThread().getStackTrace()[1].getMethodName();

//        Connection connection;
        Properties ofxSqlProps;
        Properties errorProps;
        ResultSet resultSet;
        TransactionModel transactionModel;

        ofxSqlProps = CMLanguageController.getOfxSqlProps();
        errorProps = CMLanguageController.getErrorProps();


        // run query for unique brokers
        try (//Connection con = CmdLineController.getInstance().
//              getDbConnectionDmOfx().getConnection();
             PreparedStatement pStatement
             = CMDBController.getConnection().prepareStatement(ofxSqlProps.
                   getProperty("SQLSecurity"),
                   ResultSet.TYPE_SCROLL_SENSITIVE,
                   ResultSet.CONCUR_READ_ONLY))
        {
            pStatement.clearWarnings();
            pStatement.setString(1, sBrokerId);
            pStatement.setString(2, sAcctId);
            pStatement.setString(3, sSecUniqueId);
            resultSet = pStatement.executeQuery();

            while (resultSet.next())
            {
                // this will have transactions returned
                // put them in an array to work them
                transactionModel = new TransactionModel(
                      resultSet.getString("TransName"),
                      resultSet.getString("TransType"),
                      resultSet.getDouble("ShPerCtrct"),
                      resultSet.getDouble("Units"),
                      resultSet.getDouble("Price"),
                      resultSet.getDouble("Cmmsn"),
                      resultSet.getDouble("Tax"),
                      resultSet.getDouble("Fees"),
                      resultSet.getDouble("Total"),
                      resultSet.getDate("DtTrade"));

                tempTransactionsModel.getListTransactions().
                      add(transactionModel);
            }

            // were there rows to handle?
            if (resultSet.last())
            {
                // have all the transactions in the array, now work them into lots
                if (!doTransactionsModel())
                {
                    return false;
                }
            }

            resultSet.close();
            pStatement.close();
//            con.close();
        }
        catch (SQLException ex)
        {
            CMHPIUtils.showDefaultMsg(
                  errorProps.getProperty("Title"),
                  errorPrefix,
                  fErrorPrefix,
                  ex.toString(),
                  JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private Boolean doTransactionsModel()
    {
        fErrorPrefix = Thread.currentThread().getStackTrace()[1].getMethodName();

        Properties errorProps;
        String s;

        errorProps = CMLanguageController.getErrorProps();

        // the array has transactions in date order
        // there may be a closing transaction before an opening transaction
        // indicating an issue with the dataset; deal with it.
        for (TransactionModel tm : tempTransactionsModel.
              getListTransactions())
        {
            switch (tm.getSTransType().toLowerCase())
            {
                case "buy": // stock
                case "sellshort": // stock
                case "buytoopen": // option
                case "selltoopen": // option
                    // opening transaction, create a new lot
                    if (!doOpenLot(tm))
                    {
                        return false;
                    }
                    break;
                case "sell": // stock
                    doCloseLot(tm, "buy");
                    break;
                case "buytocover": // stock
                    doCloseLot(tm, "sellshort");
                    break;
                case "buytoclose": // option
                    doCloseLot(tm, "selltoopen");
                    break;
                case "selltoclose": // option
                    doCloseLot(tm, "buytoopen");
                    break;
                case "null":
                    //todo: handle someday
                    // deal with transfers
                    if (tm.getSTransName().equalsIgnoreCase("transfer"))
                    {
                        // assuming all transfers are closing
                        // not strictly true but works for our uses
                        doCloseLot(tm, "transfer");
                    }
                    break;
                default:
                    s = String.format(CMLanguageController.getErrorProps().
                          getProperty("Formatted3"),
                          tm.getSTransType());

                    CMHPIUtils.showDefaultMsg(
                          errorProps.getProperty("Title"),
                          errorPrefix,
                          fErrorPrefix,
                          s,
                          JOptionPane.ERROR_MESSAGE);
                    return false;
            }
        }
        return true;
    }

    private Boolean doOpenLot(TransactionModel tm)
    {
        fErrorPrefix = Thread.currentThread().getStackTrace()[1].getMethodName();

        LotModel lotModel;
        lotModel = new LotModel();

        if (!lotModel.doOpenLot(tm))
        {
            return false;
        }

        this.lotsModel.add(lotModel);

        return true;
    }

    private Boolean doCloseLot(TransactionModel tm,
          String sOpenTransType)
    {
        fErrorPrefix = Thread.currentThread().getStackTrace()[1].getMethodName();

        /*
         * NOTE: We force there to be no fractional units here
         */
        TransactionModel openTm;
        TransactionModel splitTm;
        Boolean bFound;
        LotModel lotModel;
        Double dCloseRatio;
        Long lClosingShares;

        // come in with a closing lot. 
        // Cases:
        //  may be there is no open lot to match
        //  have open match with same size
        //  have open match with different sizes
        while (tm.getDUnits() != 0)
        {
            bFound = false;

            // loop through lots, look at those with remaining open units
            for (LotModel lot : this.lotsModel)
            {
                openTm = lot.getOpeningTransaction();
                if (lot.getDOpenUnitsRemaining() != 0.0
                      && (openTm.getSTransType().toLowerCase().
                            equalsIgnoreCase(sOpenTransType)
                      || sOpenTransType.equalsIgnoreCase("transfer")))
                {
                    bFound = true;

                    dCloseRatio = 1.0;

                    if (abs(tm.getDUnits()) > abs(lot.getDOpenUnitsRemaining()))
                    {
                        // only take a fraction of the transaction
                        dCloseRatio
                              = abs(lot.getDOpenUnitsRemaining() / tm.getDUnits());
                    }

                    lClosingShares = Math.round(dCloseRatio * tm.getDUnits());

                    splitTm = new TransactionModel(
                          tm.getSTransName(), tm.getSTransType(),
                          tm.getDShPerCtrct(),
                          lClosingShares.doubleValue(),
                          tm.getDPrice(),
                          tm.getDCmmsn() * dCloseRatio,
                          tm.getDTax() * dCloseRatio,
                          tm.getDFees() * dCloseRatio,
                          tm.getDTotal() * dCloseRatio,
                          tm.getDateDtTrade());

                    // fix tm to hold the remainder
                    tm.setDUnits(tm.getDUnits() - lClosingShares.doubleValue());
                    tm.setDCmmsn(
                          tm.getDCmmsn() - (tm.getDCmmsn() * dCloseRatio));
                    tm.setDTax(tm.getDTax() - (tm.getDTax() * dCloseRatio));
                    tm.setDFees(tm.getDFees() - (tm.getDFees() * dCloseRatio));
                    tm.setDTotal(
                          tm.getDTotal() - (tm.getDTotal() * dCloseRatio));

                    // add closing to the lot
                    if (tm.getDUnits() != 0)
                    {
                        lot.getClosingTransactions().
                              getListTransactions().add(splitTm);
                    }
//                    lot.setDOpenUnitsRemaining(
//                            lot.getDOpenUnitsRemaining() + splitTm.getDUnits());

                    lot.setDOpenUnitsRemaining(
                          abs(lot.getDOpenUnitsRemaining()) - abs(splitTm.getDUnits()));

                    break;
                }
            }

            if (!bFound)
            {
                // have a close without an open
                // create a lot for it, put the transaction in the closing list
                // leave dOpenUnitsRemaining on the lot as 0
                lotModel = new LotModel();
                lotModel.getClosingTransactions().getListTransactions().add(tm);

                this.lotsModel.add(lotModel);

                return true;
            }
        }

        return true;
    }

    public Boolean exportLots(String sBrokerName, String sBrokerId,
          String sAcctId, String sSecUniqueId,
          String sTicker)
    {
        for (LotModel lot : lotsModel)
        {
            if (!lot.exportLot(sBrokerName, sBrokerId,
                  sAcctId, sSecUniqueId,
                  sTicker))
            {
                return false;
            }
        }
        return true;
    }
}
