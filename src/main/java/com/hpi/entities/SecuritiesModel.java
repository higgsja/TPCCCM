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
 * Array of securities.
 *
 * @author Joe@Higgs-Tx.com
 */
public class SecuritiesModel {

    private final ArrayList<SecurityModel> securitiesModel;
    private final String errorPrefix;
    private String fErrorPrefix;

    public SecuritiesModel() {
        this.securitiesModel = new ArrayList<>();

        this.errorPrefix = "SecuritiesModel";
        this.fErrorPrefix = null;
    }

    protected Boolean doSecurities(String sBrokerId, String sAcctId) {
        fErrorPrefix = Thread.currentThread().getStackTrace()[1].getMethodName();

        Properties errorProps;
        Properties props;
        ResultSet resultSet;
        SecurityModel securityModel;

        props = CMLanguageController.getOfxSqlProps();
        errorProps = CMLanguageController.getErrorProps();

        // run query for unique securities
        try (//Connection con = CmdLineController.getInstance().
            //              getDbConnectionDmOfx().getConnection();
            PreparedStatement pStatement =
            CMDBController.getConnection().prepareStatement(
                props.getProperty("SQLSecurities"),
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {
            pStatement.clearWarnings();
            pStatement.setString(1, sBrokerId);
            pStatement.setString(2, sAcctId);
            resultSet = pStatement.executeQuery();

            while (resultSet.next()) {
                securityModel = new SecurityModel();

                if (!securityModel.
                    doSecurity(sBrokerId, sAcctId,
                        resultSet.getString("SecUniqueId"),
                        resultSet.getString("Ticker"))) {
                    return false;
                }

                this.securitiesModel.add(securityModel);
            }

            resultSet.close();
            pStatement.close();
//            con.close();
        }
        catch (SQLException ex) {
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

    protected Boolean exportSecurities(String sBrokerName,
        String sBrokerId,
        String sAcctId) {
        for (SecurityModel sec : this.securitiesModel) {
            if (!sec.exportSecurity(sBrokerName, sBrokerId, sAcctId)) {
                return false;
            }
        }

        return true;
    }
}
