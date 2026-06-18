/*
 * Copyright 2015 all rights reserved.
 * HLH LLC
 */
package com.hpi.entities;

import com.hpi.hpiUtils.CMHPIUtils;
import com.hpi.TPCCMcontrollers.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 * Array of accounts
 *
 * @author Joe@Higgs-Tx.com
 */
public class AccountsModel
{

    public static final String SYNC_ACCTS_DB_TO_DM
        = "select JoomlaId, hlhtxc5_dbOfx.Brokers.BrokerId, AcctId, Org, FId, BrokerIdFi, InvAcctIdFi from hlhtxc5_dbOfx.Accounts, hlhtxc5_dbOfx.Brokers where Accounts.BrokerId = Brokers.BrokerId and JoomlaId = '%s';";

    private final ArrayList<AccountModel> accountsModel;
    private final String errorPrefix;
    private String fErrorPrefix;

    public AccountsModel()
    {
        this.accountsModel = new ArrayList<>();

        this.errorPrefix = "AccountsModel";
        this.fErrorPrefix = null;
    }

    /**
     *
     * @param sBrokerId
     *
     * @return
     */
    public Boolean doAccounts(String sBrokerId)
    {
        fErrorPrefix = Thread.currentThread().getStackTrace()[1].getMethodName();

        Properties ofxSQLProps;
        Properties errorProps;
        ResultSet resultSet;
        AccountModel accountModel;

        ofxSQLProps = CMLanguageController.getOfxSqlProps();
        errorProps = CMLanguageController.getErrorProps();

        // run query for unique brokers
        try (PreparedStatement pStatement = CMDBController.getConnection().
            prepareStatement(ofxSQLProps.
                getProperty("SQLAccounts"),
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_READ_ONLY))
        {
            pStatement.clearWarnings();
            pStatement.setString(1, sBrokerId);
            resultSet = pStatement.executeQuery();

            while (resultSet.next())
            {
                accountModel = new AccountModel();

                if (!accountModel.doAccount(sBrokerId,
                    resultSet.getString("AcctId")))
                {
                    return false;
                }

                this.accountsModel.add(accountModel);
            }

            resultSet.close();
            pStatement.close();
//            CMDBController.getConnection().close();
        } catch (SQLException ex)
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

    public Boolean exportAccounts(String sBrokerName, String sBrokerId)
    {
        for (AccountModel am : accountsModel)
        {
            if (!am.exportAccount(sBrokerName, sBrokerId))
            {
                return false;
            }
        }
        return true;
    }
}
