package com.hpi.TPCCMcontrollers;

import com.hpi.hpiUtils.CMHPIUtils;
import com.hpi.TPCCMprefs.CMDBModel;
import com.hpi.TPCCMsql.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;

/**
 * Changes TPCPrefsVersion number when changes are made to this
 *
 * @author Joe@Higgs-Tx.com
 */
public class CMTPCPrefsController
{

    private static String CURRENT_BUILD_VERSION;
    private static String CURRENT_BUILD;
    private static String BUILD_TIMESTAMP;

    private static final String[][] KEY_VALUE = new String[][]
    {
        {
            "TPCPrefsVersion", "1.0"
        },
        {
            "HedgeContractCost", "0.5"
        },
        {
            "HedgeAmount", "10000"
        },
        {
            "HedgeInstrument", "VXX"
        },
        {
            "HedgeMaxStrike", "40"
        },
        {
            "HedgeMinStrike", "20"
        },
        {
            "HedgeMonth", "Jan"
        },
        {
            "HedgeType", "Long"
        },
        {
            "HedgeRealizationPct", "80"
        },
        {
            "HedgeTicketCost", "4.95"
        }
    };

    public static void initCustom()
    {

    }

    public static void configVersionChange()
    {

    }

    public synchronized static final void read()
    {
        String sql;
        ResultSet rs;

        sql = String.format(CMPrefsController.getSQL_TPC_GET(),
            CMDBModel.getUserId().toString());

        // read from database
        try (Connection con = CMDBController.getConnection();
             PreparedStatement sSQL = con.prepareStatement(sql))
        {
            sSQL.clearWarnings();
            rs = sSQL.executeQuery();

            while (rs.next())
            {
                CMPrefsController.getTPC_PROPERTIES().
                    setProperty(rs.getString("KeyId"),
                        rs.getString("KeyValue"));
            }

//            sSQL.close();
//            con.close();
        }
        catch (SQLException ex)
        {
            CMHPIUtils.showDefaultMsg(
                CMLanguageController.getAppProp("Title"),
                Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                ex.getMessage(), JOptionPane.ERROR_MESSAGE);

            throw new CMDAOException(
                CMLanguageController.getAppProp("Title"),
                Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                sql + ";\n" +
                 ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }

        // config version check
        // with version 1.0, there would have been no prior version
        // just force defaults
        if (CMPrefsController.getTPC_PROPERTIES().
            getProperty("TPCPrefsVersion") == null)
        {
            for (String[] KEY_VALUE1 : KEY_VALUE)
            {
                CMPrefsController.getTPC_PROPERTIES().
                    setProperty(KEY_VALUE1[0], KEY_VALUE1[1]);
            }

            CMTPCPrefsController.write();
        }

        // do any actions for a newer version
        configVersionChange();
    }

    public synchronized static void write()
    {
        for (Map.Entry<?, ?> entry : CMPrefsController.
            getTPC_PROPERTIES().entrySet())
        {
            CMDBController.upsertRow(CMPrefsController.getSQL_TPC_UPDATE() +
                 "KeyValue = '" + entry.getValue() + "' " +
                 "where JoomlaId = '" +
                 CMDBModel.getUserId().toString() + "' " +
                 "and KeyId = '" + entry.getKey() + "';",
                CMPrefsController.getSQL_TPC_INSERT() + "'" +
                 CMDBModel.getUserId().toString() + "', '" +
                 entry.getKey() + "', '" +
                 entry.getValue() + "'" +
                 ");");
        }
    }
}
