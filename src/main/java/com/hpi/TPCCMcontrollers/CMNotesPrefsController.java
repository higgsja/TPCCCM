package com.hpi.TPCCMcontrollers;

import com.hpi.hpiUtils.CMHPIUtils;
import com.hpi.TPCCMprefs.CMDBModel;
import com.hpi.TPCCMsql.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;

/**
 * Changes NotesPrefsVersion number when changes are made to this
 *
 * @author Joe@Higgs-Tx.com
 */
public class CMNotesPrefsController
{

    private static String CURRENT_BUILD_VERSION;
    private static String CURRENT_BUILD;
    private static String BUILD_TIMESTAMP;

    private static final String[][] KEY_VALUE = new String[][]
    {
        {
            "NotesPrefsVersion", "1.0"
        },
        {
            "LastOS", "NA"
        },
        {
            "CurrentOS", "NA"
        },
        {
            "LastVersion", "1.0"
        },
        {
            "CurrentVersion", "1.0"
        },
        {
            "Language", "English (US)"
        },
        {
            "LossCriticalColor", "#FF0000"
        },
        {
            "LossCriticalColorAlpha", "60"
        },
        {
            "LossWarnColor", "#ffff00"
        },
        {
            "LossWarnColorAlpha", "80"
        },
        {
            "GainCriticalColor", "#2b5900"
        },
        {
            "GainCriticalColorAlpha", "60"
        },
        {
            "GainWarnColor", "#ffff00"
        },
        {
            "GainWarnColorAlpha", "80"
        }
    };

    public static void initCustom()
    {

    }

    public static void configVersionChange()
    {

    }

    public static final void read()
    {
        String sql;
        ResultSet rs;

        sql = String.format(CMPrefsController.getSQL_NOTES_GET(),
              CMDBModel.getUserId().toString());

        // read from database
        try (Connection con = CMDBController.getConnection();
             PreparedStatement sSQL = con.prepareStatement(sql))
        {
            sSQL.clearWarnings();
            rs = sSQL.executeQuery();

            while (rs.next())
            {
                CMPrefsController.getNOTES_PROPERTIES().
                      setProperty(rs.getString("KeyId"),
                            rs.getString("KeyValue"));
            }

            sSQL.close();
            con.close();
        }
        catch (SQLException ex)
        {
            CMHPIUtils.showDefaultMsg(
                  CMLanguageController.getAppProp("Title")
                  + CMLanguageController.getAppProp("NotesTitle"),
                  Thread.currentThread().getStackTrace()[1].getClassName(),
                  Thread.currentThread().getStackTrace()[1].getMethodName(),
                  ex.getMessage(), JOptionPane.ERROR_MESSAGE);

            throw new CMDAOException(
                  CMLanguageController.getAppProp("Title")
                  + CMLanguageController.getAppProp("NotesTitle"),
                  Thread.currentThread().getStackTrace()[1].getClassName(),
                  Thread.currentThread().getStackTrace()[1].getMethodName(),
                  sql + ";\n"
                  + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }

        // config version check
        // with version 1.0, there would have been no prior version
        // just force defaults
        if (CMPrefsController.getNOTES_PROPERTIES().
              getProperty("NotesPrefsVersion") == null)
        {
            for (String[] KEY_VALUE1 : KEY_VALUE)
            {
                CMPrefsController.getNOTES_PROPERTIES().
                      setProperty(KEY_VALUE1[0], KEY_VALUE1[1]);
            }

            CMNotesPrefsController.write();
        }

        // do any actions for a newer version
        configVersionChange();
    }

    public static void write()
    {
        for (Map.Entry<?, ?> entry : CMPrefsController.
              getNOTES_PROPERTIES().entrySet())
        {
            CMDBController.upsertRow(CMPrefsController.getSQL_NOTES_UPDATE()
                  + "KeyValue = '" + entry.getValue() + "' "
                  + "where JoomlaId = '"
                  + CMDBModel.getUserId().toString() + "' "
                  + "and KeyId = '" + entry.getKey() + "';",
                  CMPrefsController.getSQL_NOTES_INSERT() + "'"
                  + CMDBModel.getUserId().toString() + "', '"
                  + entry.getKey() + "', '"
                  + entry.getValue() + "'"
                  + ");");
        }
    }

    public static String getCURRENT_BUILD_VERSION()
    {
        return CURRENT_BUILD_VERSION;
    }

    public static String getCURRENT_BUILD()
    {
        return CURRENT_BUILD;
    }

    public static String getBUILD_TIMESTAMP()
    {
        return BUILD_TIMESTAMP;
    }
}
