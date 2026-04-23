package com.hpi.TPCCMprefs;

import com.hpi.TPCCMcontrollers.CMDBController;
import com.hpi.TPCCMcontrollers.CMLanguageController;
import com.hpi.hpiUtils.CMHPIUtils;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import javax.swing.JOptionPane;
import static org.apache.commons.io.FileUtils.copyToFile;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Provides for managing database preferences.
 *
 */
public class CMDBModel
      extends CMXMLModelBase
{

    private static ArrayList<String> dbTypeList;
    private static ArrayList<String> dbActiveList;
    private static ArrayList<String> dbDBMSList;
    private static ArrayList<String> dbURLList;
    private static ArrayList<String> dbHostList;
    private static ArrayList<String> dbDriverList;
    private static ArrayList<DB> dbList;
    private static String version;
    private static String joomlaUserName;
    private static String joomlaPassword;
    private static Integer userId;

    //*** singleton
    static
    {
        CMDBModel.instance = null;
        CMDBModel.dbTypeList = new ArrayList<>();
        CMDBModel.dbActiveList = new ArrayList<>();
        CMDBModel.dbDBMSList = new ArrayList<>();
        CMDBModel.dbURLList = new ArrayList<>();
        CMDBModel.dbHostList = new ArrayList<>();
        CMDBModel.dbDriverList = new ArrayList<>();
        CMDBModel.dbList = new ArrayList<>();
        CMDBModel.joomlaUserName = null;
        CMDBModel.joomlaPassword = null;
    }
    private static CMDBModel instance;

    protected CMDBModel(String sConfigFilename)
    {
        // protected prevents instantiation outside package
        super(sConfigFilename);

        this.initModel();
    }

    /**
     *
     * @param sConfigFilename
     *
     * @return
     */
    public synchronized static CMDBModel getInstance(
          String sConfigFilename)
    {
        if (CMDBModel.instance == null)
        {
            CMDBModel.instance = new CMDBModel(sConfigFilename);
        }

        return CMDBModel.instance;
    }

    public synchronized static CMDBModel getInstance()
    {
        if (CMDBModel.instance == null)
        {
            return CMDBModel.getInstance("databases.config");
        }

        return CMDBModel.instance;
    }
    //***

    public final void configVersionChange()
    {
        switch (CMDBModel.version)
        {
            case "1.0":
                try
                {
                    // delete databases.config
                    Files.deleteIfExists(Paths.get(this.configFullPath));

                    // copy in default databases.config
                    copyToFile(getClass().getClassLoader().
                          getResourceAsStream("res/" + this.configFilename),
                          new File(this.configFullPath));
                }
                catch (IOException e)
                {
                    CMHPIUtils.showDefaultMsg(CMLanguageController.getErrorProp("Error"),
                          this.getClass().getName(),
                          Thread.currentThread().getStackTrace()[1].
                                getMethodName(),
                          e.toString(), JOptionPane.ERROR_MESSAGE);
                }

                this.initModel();
                break;
            default:
        }
    }

    public final void initModel()
    {
        File file;
        Document doc;
        Element eTemp;

        // check for file
        file = new File(this.getConfigFullPath());
        if (!file.exists())
        {
            // copy default file
            try
            {
                copyToFile(getClass().getClassLoader().
                      getResourceAsStream("res/" + this.configFilename),
                      new File(this.configFullPath));
            }
            catch (IOException e)
            {
                CMHPIUtils.showDefaultMsg(CMLanguageController.getErrorProp("Error"),
                      this.getClass().getName(),
                      Thread.currentThread().getStackTrace()[1].getMethodName(),
                      e.toString(), JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try (InputStream is = new FileInputStream(
              this.getConfigFullPath()))
        {

            doc = Jsoup.parse(is, "UTF-8", "");
        }
        catch (IOException e)
        {
            CMHPIUtils.showDefaultMsg(CMLanguageController.getErrorProp("Error"),
                  this.getClass().getName(),
                  Thread.currentThread().getStackTrace()[1].getMethodName(),
                  e.toString(), JOptionPane.ERROR_MESSAGE);
            return;
        }

        eTemp = doc.select("Databases").first();

        this.readDatabases(eTemp);
    }

    @Override
    public void setbDirty(Boolean bDirty)
    {
        this.bDirty = bDirty;
    }

    @Override
    public void read(Boolean bReportFailure)
    {

    }

    private void readDatabases(Element aElement)
    {
        Element element;
        Iterator<Element> iterator;
        String s;
        DB dbTemp;

        // set defaults; anything found in databases.config will override
        dbTemp = new DB("DataMart", "hlhtxc5_dmOfx", "OfxDataMart", "Yes",
              "MariaDB", "jdbc:mariadb:",
              "autoReconnect=true&useLegacyDatetimeCode=false&"
              //+ "serverTimezone=UTC"
              + "serverTimezone=America/Chicago"
              //              + "&useSSL=false&connectTimeout=15000&socketTimeout=30000",
              + "&useSSL=false&connectTimeout=60000&socketTimeout=60000",
//              "hlh-tx.com", "org.mariadb.jdbc.Driver", "hlhtxc5", "Jigger01");
        "162.254.37.212:3306", "org.mariadb.jdbc.Driver", "higgsja", "Jigger01");
        CMDBModel.dbList.add(dbTemp);

        dbTemp = new DB("Broker Data", "hlhtxc5_dbOfx", "OfxBroker", "Yes",
              "MariaDB", "jdbc:mariadb:",
              "autoReconnect=true&useLegacyDatetimeCode=false&"
              //+ "serverTimezone=UTC"
              + "serverTimezone=America/Chicago"
              + "&useSSL=false&connectTimeout=60000&socketTimeout=60000",
//              "hlh-tx.com", "org.mariadb.jdbc.Driver", "hlhtxc5", "Jigger01");
        "162.254.37.212:3306", "org.mariadb.jdbc.Driver", "higgsja", "Jigger01");
        CMDBModel.dbList.add(dbTemp);

        // aElement points to 'databases'
        if (aElement == null)
        {
            CMHPIUtils.showDefaultMsg(CMLanguageController.getErrorProp("Error"),
                  this.getClass().getName(),
                  Thread.currentThread().getStackTrace()[1].getMethodName(),
                  "File error: element is null.",
                  JOptionPane.ERROR_MESSAGE);

            throw (new UnsupportedOperationException());
        }

        iterator = aElement.children().iterator();

        while (iterator.hasNext())
        {
            element = iterator.next();

            switch (element.tagName().toLowerCase(Locale.US))
            {
                case "version":
                    CMDBModel.version = element.ownText();
//                    this.processVersion();
                    break;
                case "userid":
                    if (element.ownText().isEmpty())
                    {
                        CMDBModel.joomlaUserName = "";
                    }
                    else
                    {
                        CMDBModel.joomlaUserName = element.ownText();
//                        Integer.parseInt(CMDBModel.joomlaUserId);
                    }
                    break;
                case "pw":
                    if (element.ownText().isEmpty())
                    {
                        CMDBModel.joomlaPassword = "";
                    }
                    else
                    {
                        CMDBModel.joomlaPassword = element.ownText();
                    }
                    break;
                // leaving the following in case want to allow
                //  access to different database
                case "type":
                    CMDBModel.dbTypeList.add(element.ownText());
                    break;
                case "active":
                    CMDBModel.dbActiveList.add(element.ownText());
                    break;
                case "dbms":
                    CMDBModel.dbDBMSList.add(element.ownText());
                    break;
                case "url":
                    s = element.ownText().replace("\"", "");
                    CMDBModel.dbURLList.add(s);
                    break;
                case "host":
                    s = element.ownText().replace("\"", "");
                    CMDBModel.dbHostList.add(s);
                    break;
                case "driver":
                    CMDBModel.dbDriverList.add(element.ownText());
                    break;
//                case "optionhistory":
//                    CMDBModel.sOptionHistoryDir = element.ownText();
//                    break;
//                case "quickendata":
//                    CMDBModel.sQuickenDataDir = element.ownText();
//                    break;
                case "database":
                    readDatabase(element);
                    break;
                default:
                // ignore any spurious elements
            }
        }
    }

    private void readDatabase(Element aElement)
    {
        Element element;
        Iterator<Element> iterator;
        String s;
        DB dbTemp;

        // aElement points to 'database'
        if (aElement == null)
        {
            CMHPIUtils.showDefaultMsg(CMLanguageController.getErrorProp("Error"),
                  this.getClass().getName(),
                  Thread.currentThread().getStackTrace()[1].getMethodName(),
                  "File error: element is null.",
                  JOptionPane.ERROR_MESSAGE);

            throw (new UnsupportedOperationException());
        }

        dbTemp = new DB();

        iterator = aElement.children().iterator();

        while (iterator.hasNext())
        {
            element = iterator.next();

            switch (element.tagName().toLowerCase(Locale.US))
            {
                case "dbdisplayname":
                    s = element.ownText().replace("\"", "");
                    dbTemp.setsDbDisplayName(s);
                    break;
                case "dbname":
                    dbTemp.setsDbName(element.ownText());
                    break;
                case "type":
                    dbTemp.setsType(element.ownText());
                    break;
                case "active":
                    dbTemp.setsActive(element.ownText());
                    break;
                case "dbms":
                    dbTemp.setsDBMS(element.ownText());
                    break;
                case "url":
                    s = element.ownText().replace("\"", "");
                    dbTemp.setsURL(s);
                    break;
                case "urlparameters":
                    s = element.ownText().replace("\"", "");
                    dbTemp.setsURLParameters(s);
                    break;
                case "host":
                    s = element.ownText().replace("\"", "");
                    dbTemp.setsHost(s);
                    break;
                case "driver":
                    dbTemp.setsDriver(element.ownText());
                    break;
                case "uid":
                    dbTemp.setsUId(element.ownText());
                    break;
                case "pw":
                    dbTemp.setsPW(element.ownText());
                    break;
                default:
                    s = String.format(CMLanguageController.
                          getErrorProp("Formatted3"), element.tagName());

                    CMHPIUtils.showDefaultMsg(CMLanguageController.getAppProp("Title") + 
                                CMLanguageController.getErrorProp("Title"),
                          this.getClass().getName(),
                          Thread.currentThread().getStackTrace()[1].getMethodName(),
                          s,
                          JOptionPane.ERROR_MESSAGE);
            }
        }

        dbTemp.setsFullURL();

        CMDBModel.dbList.add(dbTemp);

        // set the default databases to inactive
        CMDBModel.dbList.get(0).setsActive("No");
        CMDBModel.dbList.get(1).setsActive("No");
    }

    public void getJoomlaId()
    {
        String sql;
        ResultSet rs;

        if (CMDBModel.joomlaUserName == null
              || CMDBModel.joomlaPassword == null
              || CMDBModel.joomlaUserName.isEmpty()
              || CMDBModel.joomlaPassword.isEmpty())
        {
            return;
        }
        // access Joomla database for ID
        sql = "select id from hlhtxc5_hpiJoomla.hpiJ_users where username = '"
              + CMDBModel.joomlaUserName + "';";
        try (Connection con = CMDBController.getConnection();
             PreparedStatement pStmt = con.prepareStatement(sql))
        {
            pStmt.clearWarnings();
            rs = pStmt.executeQuery();

            while (rs.next())
            {
                CMDBModel.userId = rs.getInt(1);
            }
            
            rs.close();
            pStmt.close();
            con.close();
        }
        catch (SQLException ex)
        {
            if (((SQLException) ex.getCause()).getErrorCode() == 1045)
            {
                CMHPIUtils.showDefaultMsg(CMLanguageController.getDBErrorProp("Title"),
                      Thread.currentThread().getStackTrace()[1].getClassName(),
                      Thread.currentThread().getStackTrace()[1].getMethodName(),
                      CMLanguageController.getDBErrorProp("IPAddressFailed"),
                      JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            else
            {
                CMHPIUtils.showDefaultMsg(CMLanguageController.getDBErrorProp("Title"),
                      Thread.currentThread().getStackTrace()[1].getClassName(),
                      Thread.currentThread().getStackTrace()[1].getMethodName(),
                      ex.getMessage(), JOptionPane.ERROR_MESSAGE);

                throw new UnsupportedOperationException(ex);
            }
        }
    }

    @Override
    public void write()
    {
        Integer iTab;
        String endLine;

        if (this.bDirty == false)
        {
            return;
        }

        iTab = 0;
        endLine = System.getProperty("line.separator");

        try (BufferedWriter writer = new BufferedWriter(
              new OutputStreamWriter(
                    new FileOutputStream(this.configFullPath),
                    StandardCharsets.UTF_8)))
        {
            writer.write("<Databases>");
            writer.write(endLine);
            iTab++;

            writer.write(CMHPIUtils.charFill(iTab, "\t".charAt(0)));
            writer.write("<Version>");
            writer.write(CMDBModel.version);
            writer.write("</Version>");
            writer.write(endLine);

            writer.write(CMHPIUtils.charFill(iTab, "\t".charAt(0)));
            writer.write("<UserId>");
            writer.write(CMDBModel.joomlaUserName);
            writer.write("</UserId>");
            writer.write(endLine);

            writer.write(CMHPIUtils.charFill(iTab, "\t".charAt(0)));
            writer.write("<PW>");
            writer.write(CMDBModel.joomlaPassword);
            writer.write("</PW>");
            writer.write(endLine);

            if (dbTypeList.size() > 0)
            {
                for (String s : dbTypeList)
                {
                    writer.write(CMHPIUtils.charFill(iTab, "\t".charAt(0)));
                    writer.write("<Type>");
                    writer.write(s);
                    writer.write("</Type>");
                    writer.write(endLine);
                }
                writer.write(endLine);
            }

            if (dbActiveList.size() > 0)
            {
                for (String s : dbActiveList)
                {
                    writer.write(CMHPIUtils.charFill(iTab, "\t".charAt(0)));
                    writer.write("<Active>");
                    writer.write(s);
                    writer.write("</Active>");
                    writer.write(endLine);
                }
                writer.write(endLine);
            }

            if (dbDBMSList.size() > 0)
            {
                for (String s : dbDBMSList)
                {
                    writer.write(CMHPIUtils.charFill(iTab, "\t".charAt(0)));
                    writer.write("<DBMS>");
                    writer.write(s);
                    writer.write("</DBMS>");
                    writer.write(endLine);
                }
                writer.write(endLine);
            }

            if (dbURLList.size() > 0)
            {
                for (String s : dbURLList)
                {
                    writer.write(CMHPIUtils.charFill(iTab, "\t".charAt(0)));
                    writer.write("<URL>\"");
                    writer.write(s);
                    writer.write("\"</URL>");
                    writer.write(endLine);
                }
                writer.write(endLine);
            }

            if (dbHostList.size() > 0)
            {
                for (String s : dbHostList)
                {
                    writer.write(CMHPIUtils.charFill(iTab, "\t".charAt(0)));
                    writer.write("<Host>\"");
                    writer.write(s);
                    writer.write("\"</Host>");
                    writer.write(endLine);
                }
                writer.write(endLine);
            }

            if (dbDriverList.size() > 0)
            {
                for (String s : dbDriverList)
                {
                    writer.write(CMHPIUtils.charFill(iTab, "\t".charAt(0)));
                    writer.write("<Driver>");
                    writer.write(s);
                    writer.write("</Driver>");
                    writer.write(endLine);
                }
                writer.write(endLine);
            }

            if (dbList.size() > 0)
            {
                for (DB db : dbList)
                {
                    writer.write("<Database>");
                    writer.write(endLine);

                    iTab++;
                    writer.write(CMHPIUtils.charFill(iTab, "\t".charAt(0)));
                    writer.write("<DbDisplayName>\"");
                    writer.write(db.sDbDisplayName);
                    writer.write("\"</DbDisplayName>");
                    writer.write(endLine);

                    writer.write(CMHPIUtils.charFill(iTab, "\t".charAt(0)));
                    writer.write("<DbName>");
                    writer.write(db.sDbName);
                    writer.write("</DbName>");
                    writer.write(endLine);

                    writer.write(CMHPIUtils.charFill(iTab, "\t".charAt(0)));
                    writer.write("<Type>");
                    writer.write(db.sType);
                    writer.write("</Type>");
                    writer.write(endLine);

                    writer.write(CMHPIUtils.charFill(iTab, "\t".charAt(0)));
                    writer.write("<Active>");
                    writer.write(db.sActive);
                    writer.write("</Active>");
                    writer.write(endLine);

                    writer.write(CMHPIUtils.charFill(iTab, "\t".charAt(0)));
                    writer.write("<DBMS>");
                    writer.write(db.sDBMS);
                    writer.write("</DBMS>");
                    writer.write(endLine);

                    writer.write(CMHPIUtils.charFill(iTab, "\t".charAt(0)));
                    writer.write("<URL>\"");
                    writer.write(db.sURL);
                    writer.write("\"</URL>");
                    writer.write(endLine);

                    writer.write(CMHPIUtils.charFill(iTab, "\t".charAt(0)));
                    writer.write("<URLParameters>\"");
                    writer.write(db.sURLParameters);
                    writer.write("\"</URLParameters>");
                    writer.write(endLine);

                    writer.write(CMHPIUtils.charFill(iTab, "\t".charAt(0)));
                    writer.write("<Host>\"");
                    writer.write(db.sHost);
                    writer.write("\"</Host>");
                    writer.write(endLine);

                    writer.write(CMHPIUtils.charFill(iTab, "\t".charAt(0)));
                    writer.write("<Driver>");
                    writer.write(db.sDriver);
                    writer.write("</Driver>");
                    writer.write(endLine);

                    writer.write(CMHPIUtils.charFill(iTab, "\t".charAt(0)));
                    writer.write("<UId>");
                    writer.write(db.sUId);
                    writer.write("</UId>");
                    writer.write(endLine);

                    writer.write(CMHPIUtils.charFill(iTab, "\t".charAt(0)));
                    writer.write("<PW>");
                    writer.write(db.sPW);
                    writer.write("</PW>");
                    writer.write(endLine);

                    iTab--;
                    writer.write(CMHPIUtils.charFill(iTab, "\t".charAt(0)));
                    writer.write("</Database>");
                    writer.write(endLine);
                }
            }

            iTab--;
            writer.write(CMHPIUtils.charFill(iTab, "\t".charAt(0)));
            writer.write("</Databases>");

            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
            CMHPIUtils.showDefaultMsg(CMLanguageController.getErrorProp("Error"),
                  this.getClass().getName(),
                  Thread.currentThread().getStackTrace()[1].getMethodName(),
                  e.toString(), JOptionPane.ERROR_MESSAGE);
            return;
        }

        this.bDirty = false;
    }

    public DB getActiveDB(String sType)
    {
        for (DB db : CMDBModel.dbList)
        {
            if (db.getsType().equalsIgnoreCase(sType))
            {
                if (db.getsActive().equalsIgnoreCase("yes"))
                {
                    return db;
                }
            }
        }
        CMHPIUtils.showDefaultMsg(CMLanguageController.getErrorProp("Error"),
              this.getClass().getName(),
              Thread.currentThread().getStackTrace()[2].getMethodName(),
              "return is null.",
              JOptionPane.ERROR_MESSAGE);
        throw (new UnsupportedOperationException());
    }

    public DB getDbByDbName(String sDbName)
    {
        for (DB db : CMDBModel.dbList)
        {
            if (db.getsDbName().equalsIgnoreCase(sDbName))
            {
                return db;
            }
        }

        return null;
    }

    public DB getDbByDbDisplayName(String sDbDisplayName)
    {
        for (DB db : CMDBModel.dbList)
        {
            if (db.getsDbDisplayName().equalsIgnoreCase(sDbDisplayName))
            {
                return db;
            }
        }

        return null;
    }

    public static ArrayList<DB> getDbList()
    {
        return dbList;
    }

    public static ArrayList<String> getDbTypeList()
    {
        return dbTypeList;
    }

    public static ArrayList<String> getDbActiveList()
    {
        return dbActiveList;
    }

    public static ArrayList<String> getDbDBMSList()
    {
        return dbDBMSList;
    }

    public static ArrayList<String> getDbURLList()
    {
        return dbURLList;
    }

    public static ArrayList<String> getDbHostList()
    {
        return dbHostList;
    }

    public static ArrayList<String> getDbDriverList()
    {
        return dbDriverList;
    }

    public static String getVersion()
    {
        return version;
    }

    public static void setVersion(String version)
    {
        CMDBModel.version = version;
    }

    public static Integer getUserId()
    {
        return userId;
    }

    public class DB
          implements Cloneable
    {

        private String sDbDisplayName;
        private String sDbName;
        private String sType;
        private String sActive;
        private String sDBMS;
        private String sURL;
        private String sURLParameters;
        private String sHost;
        private String sDriver;
        private String sUId;
        private String sPW;
        private String sFullURL;

        public DB(String sDbDisplayName, String sDbName, String sType,
              String sActive, String sDBMS, String sURL, String sURLParameters,
              String sHost, String sDriver, String sUId, String sPW)
        {
            this.sDbDisplayName = sDbDisplayName;
            this.sDbName = sDbName;
            this.sType = sType;
            this.sActive = sActive;
            this.sDBMS = sDBMS;
            this.sURL = sURL;
            this.sURLParameters = sURLParameters;
            this.sHost = sHost;
            this.sDriver = sDriver;
            this.sUId = sUId;
            this.sPW = sPW;

            this.setsFullURL();
        }

        public DB()
        {
            this.sDbDisplayName = "";
            this.sDbName = "";
            this.sType = "";
            this.sDBMS = "";
            this.sURL = "";
            this.sURLParameters = "";
            this.sFullURL = "";
            this.sHost = "";
            this.sDriver = "";
            this.sUId = "";
            this.sPW = "";
        }

        @Override
        public DB clone() throws CloneNotSupportedException
        {
            DB db;

            db = null;

            try
            {
                db = (DB) super.clone();
            }
            catch (CloneNotSupportedException e)
            {
                // should never happen
//                throw(new UnsupportedOperationException("!!"));
            }

            return db;
        }

        @Override
        public String toString()
        {
            return this.sDbDisplayName;
        }

        @Override
        public int hashCode()
        {
            assert false : "hashCode not designed";
            return 42; // any arbitrary constant will do
        }

        @Override
        public boolean equals(Object db)
        {
            if (db == this)
            {
                return true;
            }

            if (db == null)
            {
                return false;
            }

            if (db instanceof DB)
            {
                DB other = (DB) db;
                return other.sDbDisplayName.equals(this.sDbDisplayName)
                      && other.sDbName.equals(this.sDbName)
                      && other.sType.equals(this.sType)
                      && other.sActive.equals(this.sActive)
                      && other.sDBMS.equals(this.sDBMS)
                      && other.sURL.equals(this.sURL)
                      && other.sURLParameters.equals(this.sURLParameters)
                      && other.sHost.equals(this.sHost)
                      && other.sDriver.equals(this.sDriver)
                      && other.sUId.equals(this.sUId)
                      && other.sPW.equals(this.sPW)
                      && other.sFullURL.equals(this.sFullURL);
            }
            else
            {
                return false;
            }
        }

        private void setsFullURL()
        {
            sURL = sURL.trim();
            while (sURL.endsWith("/"))
            {
                sURL = sURL.substring(0, sURL.length() - 1);
            }

            this.sFullURL = sURL + "//" + sHost + "/" + this.sDbName;

            if (!this.sURLParameters.isEmpty())
            {
                this.sFullURL += "?" + this.sURLParameters;
            }
        }

        public String getsDBMS()
        {
            return sDBMS;
        }

        public void setsDBMS(String sType)
        {
            this.sDBMS = sType;
        }

        public String getsURL()
        {
            return sURL;
        }

        public void setsURL(String sURL)
        {
            this.sURL = sURL;
        }

        public String getsHost()
        {
            return sHost;
        }

        public void setsHost(String sHost)
        {
            this.sHost = sHost;
        }

        public String getsDriver()
        {
            return sDriver;
        }

        public void setsDriver(String sDriver)
        {
            this.sDriver = sDriver;
        }

        public String getsUId()
        {
            return sUId;
        }

        public void setsUId(String sUId)
        {
            this.sUId = sUId;
        }

        public String getsPW()
        {
            return sPW;
        }

        public void setsPW(String sPW)
        {
            this.sPW = sPW;
        }

        public String getsDbDisplayName()
        {
            return sDbDisplayName;
        }

        public void setsDbDisplayName(String sDbDisplayName)
        {
            this.sDbDisplayName = sDbDisplayName;
        }

        public String getsDbName()
        {
            return sDbName;
        }

        public void setsDbName(String sDbName)
        {
            this.sDbName = sDbName;
        }

        public String getsURLParameters()
        {
            return sURLParameters;
        }

        public void setsURLParameters(String sURLParameters)
        {
            this.sURLParameters = sURLParameters;
        }

        public String getsFullURL()
        {
            return sFullURL;
        }

        public void setsFullURL(String sFullURL)
        {
            this.sFullURL = sFullURL;
        }

        public String getsType()
        {
            return sType;
        }

        public void setsType(String sType)
        {
            this.sType = sType;
        }

        public String getsActive()
        {
            return sActive;
        }

        public void setsActive(String sActive)
        {
            this.sActive = sActive;
        }
    }

    public static void setUserId(Integer userId)
    {
        CMDBModel.userId = userId;
    }

    public static String getJoomlaUserId()
    {
        return joomlaUserName;
    }

    public static void setJoomlaUserId(String userId)
    {
        CMDBModel.joomlaUserName = userId;
    }

    public static String getJoomlaPassword()
    {
        return joomlaPassword;
    }

    public static void setJoomlaPassword(String password)
    {
        CMDBModel.joomlaPassword = password;
    }
}
