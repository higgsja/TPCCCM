package com.hpi.TPCCMcontrollers;

import com.hpi.TPCCMprefs.CMGlobalsModel;
import com.hpi.TPCCMprefs.CMLanguagesModel;
import com.hpi.TPCCMsql.CMDAOException;
import com.hpi.hpiUtils.CMHPIUtils;
import java.io.IOException;
import java.net.*;
import java.util.Properties;
import javax.swing.JOptionPane;

@SuppressWarnings("DM_EXIT")
public class CMLanguageController
{

    private static String sSuffix;
    private static Properties appProps;
    private static Properties dbErrorProps;
    private static Properties dmOfxSqlProps;
    private static Properties statusProps;
    private static Properties errorProps;
    private static Properties ofxSqlProps;
    private static Properties equity_info_SqlProps;
    private static Properties notesAppProps;
    //*** singleton
    private static CMLanguageController instance;

    protected CMLanguageController()
    {
        // protected prevents instantiation outside package
        super();
    }

    public synchronized static final CMLanguageController getInstance()
    {
        if (CMLanguageController.instance == null)
        {
            CMLanguageController.instance = new CMLanguageController();

            CMLanguageController.sSuffix = CMLanguagesModel.getInstance().
                  getModelProp(CMGlobalsModel.getInstance().
                        getModelProp("Language"));

            CMLanguageController.appProps = new Properties();

            CMLanguageController.dbErrorProps = new Properties();

            CMLanguageController.dmOfxSqlProps = new Properties();

            CMLanguageController.statusProps = new Properties();
            CMLanguageController.errorProps = new Properties();
            CMLanguageController.ofxSqlProps = new Properties();
            CMLanguageController.equity_info_SqlProps = new Properties();
            CMLanguageController.notesAppProps = new Properties();

            CMLanguageController.read();
        }

        return CMLanguageController.instance;
    }
    //***

    public static final void configVersionChange()
    {

    }

    /**
     * Standard read of configuration files
     * <p>
     */
    public static synchronized void read()
    {
        String[] prefix;
        String sFile;
        Properties props;
        ClassLoader cl;

        prefix = new String[]
        {
            "app-", "db-", "db-error-", "dm_Ofx_SQL-",
            "equity_info_SQL-", "error-", "ofx_SQL-", "status-"
        };

        // only need to eliminate warning while switching to new approach
        props = CMLanguageController.appProps;

        cl = CMLanguageController.class.getClassLoader();

        // cycle through the separate language files
        for (String sPrefix : prefix)
        {
            switch (sPrefix)
            {
                case "app-":
                    props = CMLanguageController.appProps;
                    break;
                case "db-":
//                    props = LanguageModel.ofxDBProps;
                    break;
                case "db-error-":
                    props = CMLanguageController.dbErrorProps;
                    break;
                case "dm_Ofx_SQL-":
                    props = CMLanguageController.dmOfxSqlProps;
                    break;
                case "equity_info_SQL-":
                    props = CMLanguageController.equity_info_SqlProps;
                    break;
                case "error-":
                    props = CMLanguageController.errorProps;
                    break;
                case "ofx_SQL-":
                    props = CMLanguageController.ofxSqlProps;
                    break;
                case "status-":
                    props = CMLanguageController.statusProps;
                    break;
                default:
                    String s = String.format(CMLanguageController.
                          getErrorProp("Formatted3"), sPrefix);

                    CMHPIUtils.showMsgInitializing(
                          Thread.currentThread().getStackTrace()[1].getClassName(),
                          Thread.currentThread().getStackTrace()[1].getMethodName(),
                          s,
                          JOptionPane.ERROR_MESSAGE);

                    throw new UnsupportedOperationException(s);
            }

            sFile = sPrefix + CMLanguageController.sSuffix + ".config";

            if (sPrefix.equalsIgnoreCase("db-"))
            {
                //new style, ignore
//                DatabaseModelNew.getInstance(sFile);
            }
            else
            {
                // old style
                URL resourceURL = cl.getResource("res/" + sFile);

//                sFile = (new File(CMGlobalsModel.getInstance().getConfigDirPath(),
//                        sFile)).getPath();
                try
                {

//                    props.loadFromXML(is);
                    props.loadFromXML(resourceURL.openStream());
                }
                catch (IOException | NullPointerException ex)
                {
                    // initializing so may not have any language files
                    throw new CMDAOException("TWSApp",
                          Thread.currentThread().getStackTrace()[1].getClassName(),
                          Thread.currentThread().getStackTrace()[2].getMethodName(),
                          ex.getMessage(), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * returns
     *
     * @return Properties Application
     */
    public static synchronized Properties getAppProps()
    {
        return appProps;
    }

    /**
     * Given a Key, returns appropriate Application Property
     *
     * @param sKey
     *
     * @return
     */
    public static synchronized String getAppProp(String sKey)
    {
        return CMLanguageController.appProps.
              getProperty(sKey, "[Key '" + sKey + "' not found]");
    }

    /**
     * Given a Key returns the appropriate DBError property
     *
     * @param sKey
     *
     * @return
     */
    public static synchronized String getDBErrorProp(String sKey)
    {
        return CMLanguageController.dbErrorProps.
              getProperty(sKey, "[Key '" + sKey + "' not found]");
    }

    /**
     *
     * @return Properties DBError
     */
    public static synchronized Properties getDBErrorProps()
    {
        return dbErrorProps;
    }

    /**
     *
     * @return Properties DMOfxDB
     */
    public static synchronized Properties getDMOfxDBProps()
    {
        return dmOfxSqlProps;
    }

    /**
     * Given a key,
     *
     * @param sKey
     *
     * @return String Property
     */
    public static synchronized String getDMOfxDBProp(String sKey)
    {
        return CMLanguageController.dmOfxSqlProps.
              getProperty(sKey, "[Key '" + sKey + "' not found]");
    }

    /**
     * Given a Key
     *
     * @param sKey
     *
     * @return String Property
     */
    public static synchronized String getStatusProp(String sKey)
    {
        return CMLanguageController.statusProps.
              getProperty(sKey, "[Key '" + sKey + "' not found]");
    }

    /**
     * Given a Key,
     *
     * @param sKey
     *
     * @return Property of Equity_info_SqlProp
     */
    public static synchronized String getEquity_info_SqlProp(String sKey)
    {
        return CMLanguageController.equity_info_SqlProps.
              getProperty(sKey, "[Key '" + sKey + "' not found]'");
    }

    /**
     *
     * @return Properties of Status
     */
    public static synchronized Properties getStatusProps()
    {
        return statusProps;
    }

    /**
     * Given a Key
     *
     * @param sKey
     *
     * @return String Property
     */
    public static synchronized String getErrorProp(String sKey)
    {
        return CMLanguageController.errorProps.
              getProperty(sKey, "[Key '" + sKey + "' not found]");
    }

    /**
     *
     * @return Properties of Error
     */
    public static synchronized Properties getErrorProps()
    {
        return errorProps;
    }

    /**
     * Given a Key
     *
     * @param sKey
     *
     * @return String OfxSQLProp
     */
    public static synchronized String getOfxSqlProp(String sKey)
    {
        return CMLanguageController.ofxSqlProps.
              getProperty(sKey, "[Key '" + sKey + "' not found]");
    }

    /**
     *
     * @return Properties of OfxSQL
     */
    public static synchronized Properties getOfxSqlProps()
    {
        return ofxSqlProps;
    }

    /**
     *
     * @return Properties of DBError
     */
    public static synchronized Properties getDbErrorProps()
    {
        return dbErrorProps;
    }

    /**
     *
     * @return Properties of dmOfxSql
     */
    public static synchronized Properties getDmOfxSqlProps()
    {
        return dmOfxSqlProps;
    }

    /**
     *
     * @return Properties of Equity_info_Sql
     */
    public static synchronized Properties getEquity_info_SqlProps()
    {
        return equity_info_SqlProps;
    }

    public static Properties getNotesAppProps()
    {
        return notesAppProps;
    }
    
    public static String getNotesAppProp(String sKey)
    {
    return CMLanguageController.notesAppProps.
              getProperty(sKey, "[Key '" + sKey + "' not found]");    
    }
}
