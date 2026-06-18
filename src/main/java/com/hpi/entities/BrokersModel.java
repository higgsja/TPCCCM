package com.hpi.entities;

/*
 * Copyright 2015 all rights reserved.
 * HLH LLC
 */
import com.hpi.TPCCMcontrollers.*;
import com.hpi.hpiUtils.CMHPIUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 * Array of brokers.
 *
 * @author Joe@Higgs-Tx.com
 */
public class BrokersModel
{

    public static final String SYNC_BROKER_DB_TO_DM
        = "insert ignore into hlhtxc5_dmOfx.Brokers (BrokerId, Org, FId, BrokerIdFi) select BrokerId, Org, FId, BrokerIdFi from hlhtxc5_dbOfx.Brokers;";
    private final ArrayList<BrokerModel> listBrokers;
    private final String errorPrefix;
    private String fErrorPrefix;

    public BrokersModel()
    {
        listBrokers = new ArrayList<>();

        errorPrefix = "BrokersModel";
        fErrorPrefix = null;
    }

    public Boolean doBrokers()
    {
        fErrorPrefix = "doBrokers";

//        Connection connection;
        Properties ofxSqlProps;
        ResultSet resultSet;

        ofxSqlProps = CMLanguageController.getOfxSqlProps();

        // run query for unique brokers
        try (Connection con = CMDBController.getConnection();
            PreparedStatement pStatement
            = con.prepareStatement(
                ofxSqlProps.getProperty("SQLBrokers"),
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_READ_ONLY))
        {
            pStatement.clearWarnings();
            resultSet = pStatement.executeQuery();

            while (resultSet.next())
            {
                BrokerModel brokerModel;

                brokerModel = new BrokerModel();

                if (!brokerModel.doBroker("",
                    resultSet.getString("BrokerId")))
                {
                    return false;
                }

                this.listBrokers.add(brokerModel);
            }

            resultSet.close();
            pStatement.close();
            con.close();
        } catch (SQLException ex)
        {
            CMHPIUtils.showDefaultMsg(
                CMLanguageController.getAppProp("Title") + CMLanguageController.getErrorProp("Title"),
                errorPrefix,
                fErrorPrefix,
                ex.toString(),
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        return true;
    }

    public Boolean exportBrokers()
    {
        // loop through the brokers calling their export method
        for (BrokerModel bm : listBrokers)
        {
            if (!bm.exportBroker())
            {
                return false;
            }
        }

        return true;
    }
}
