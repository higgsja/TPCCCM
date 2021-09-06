package com.hpi.TPCCMcontrollers;

import com.hpi.TPCCMprefs.CMOfxDirectModel;
import com.hpi.TPCCMprefs.CMDBModel;
import java.util.*;

public class CMPrefsController
{
    private static final Properties TPC_PROPERTIES;
    private static final String SQL_TPC_GET;
    private static final String SQL_TPC_INSERT;
    private static final String SQL_TPC_UPDATE;

    private static final Properties NOTES_PROPERTIES;
    private static final String SQL_NOTES_GET;
    private static final String SQL_NOTES_INSERT;
    private static final String SQL_NOTES_UPDATE;

    //*** Singleton
    private static CMPrefsController instance;

    static
    {
        NOTES_PROPERTIES = new Properties();
        SQL_NOTES_GET =
            "select * from hlhtxc5_dmOfx.NotesPreferences where JoomlaId = '%s' order by KeyId;";
        SQL_NOTES_INSERT =
            "insert into hlhtxc5_dmOfx.NotesPreferences (JoomlaId, KeyId, KeyValue) values (";
        SQL_NOTES_UPDATE = "update hlhtxc5_dmOfx.NotesPreferences set ";

        TPC_PROPERTIES = new Properties();
        SQL_TPC_GET =
            "select * from hlhtxc5_dmOfx.TPCPreferences where JoomlaId = '%s' order by KeyId;";
        SQL_TPC_INSERT =
            "insert into hlhtxc5_dmOfx.TPCPreferences (JoomlaId, KeyId, KeyValue) values (";
        SQL_TPC_UPDATE = "update hlhtxc5_dmOfx.TPCPreferences set ";
    }

    protected CMPrefsController()
    {
        // protected prevents instantiation outside of package
        super();
    }

    public synchronized static final CMPrefsController getInstance()
    {
        if (CMPrefsController.instance == null)
        {
            CMPrefsController.instance = new CMPrefsController();
        }

        return CMPrefsController.instance;
    }
    //***

    public void initConfigFiles()
    {
        // ensure instantiated
        CMDBModel.getInstance("databases.config");
        CMOfxDirectModel.getInstance("ofxDirect.config");
    }

    public static String getNOTES_PROPERTY(String sKey)
    {
        return NOTES_PROPERTIES.getProperty(sKey, "[Key '" + sKey +
                                                  "' not found]");
    }

    public static Properties getNOTES_PROPERTIES()
    {
        return NOTES_PROPERTIES;
    }

    public static String getSQL_NOTES_GET()
    {
        return SQL_NOTES_GET;
    }

    public static String getSQL_NOTES_INSERT()
    {
        return SQL_NOTES_INSERT;
    }

    public static String getSQL_NOTES_UPDATE()
    {
        return SQL_NOTES_UPDATE;
    }

    public static Properties getTPC_PROPERTIES()
    {
        return TPC_PROPERTIES;
    }

    public static String getSQL_TPC_GET()
    {
        return SQL_TPC_GET;
    }

    public static String getSQL_TPC_INSERT()
    {
        return SQL_TPC_INSERT;
    }

    public static String getSQL_TPC_UPDATE()
    {
        return SQL_TPC_UPDATE;
    }

    public static String getTPC_PROPERTY(String sKey)
    {
        return TPC_PROPERTIES.getProperty(sKey,
            "[Key '" + sKey + "' not found]");
    }
}
