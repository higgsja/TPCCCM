package com.hpi.TPCCMprefs;

import com.hpi.TPCCMcontrollers.CMLanguageController;
import com.hpi.hpiUtils.CMHPIUtils;
import java.io.*;
import java.util.Properties;
import javax.swing.JOptionPane;
import static org.apache.commons.io.FileUtils.copyToFile;

/**
 * Provides base model class for reading xml config files.
 * <p>
 */
public class CMXMLModelBase
{

    String configDirPath;
    String configFilename;
    String configFullPath;
    Properties modelProps;
    Properties initProps;
    Boolean bDirty;
    String[][] sKeyValue;

    @SuppressWarnings("DM_EXIT")
    public CMXMLModelBase(String sConfigFilename)
    {
        this.configDirPath = null;
        this.configFilename = sConfigFilename;
        this.configFullPath = null;
        this.modelProps = new Properties();
        this.initProps = null;
        this.bDirty = false;
        this.sKeyValue = null;

        initModel();
    }

    private void initModel()
    {
        this.configDirPath = System.getProperty("user.home");
        this.configDirPath += System.getProperty("file.separator");
        this.configDirPath += ".config";
        this.configDirPath += System.getProperty("file.separator");
        this.configDirPath += "hpi";

        this.configFullPath = this.configDirPath
              + System.getProperty("file.separator")
              + this.configFilename;
    }

    public void read(Boolean bReportFailure)
    {
        File file;
//        this.configFullPath = this.configDirPath;
//        this.configFullPath += System.getProperty("file.separator");
//        this.configFullPath += this.configFilename;

        for (String[] s : this.sKeyValue)
        {
            // set all to defaults (WHY: ensures all are put to config files)
            this.modelProps.setProperty(s[0], s[1]);
        }

        file = new File(this.configFullPath);
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

        try (FileInputStream is = new FileInputStream(this.configFullPath))
        {
            this.modelProps.loadFromXML(is);
        }
        catch (IOException | NullPointerException ex)
        {
            // not always important that the read failed
            if (bReportFailure)
            {
                CMHPIUtils.showMsgInitializing(
                      Thread.currentThread().getStackTrace()[1].getClassName(),
                      Thread.currentThread().getStackTrace()[1].getMethodName(),
                      ex.getMessage(),
                      JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void write()
    {
        String s;
        
        if (!bDirty) return;

        try (FileOutputStream os = new FileOutputStream(this.configFullPath))
        {
            modelProps.storeToXML(os, null);
            this.bDirty = false;
        }
        catch (IOException | NullPointerException ex)
        {
            s = String.format(CMLanguageController.
                  getErrorProp("GeneralError"),
                  ex.toString());

            CMHPIUtils.showDefaultMsg(CMLanguageController.getAppProp("Title") + 
                        CMLanguageController.getErrorProp("Title"),
                  Thread.currentThread().getStackTrace()[1].
                        getClassName(),
                  Thread.currentThread().getStackTrace()[1].
                        getMethodName(),
                  s,
                  JOptionPane.ERROR_MESSAGE);

            throw new UnsupportedOperationException(s);
        }
    }

    /**
     *
     * @return
     */
    public Properties getProps()
    {
        return modelProps;
    }

    /**
     *
     * @return
     */
    public String getConfigDirPath()
    {
        return configDirPath;
    }

    public String getModelProp(String sKey)
    {
        return this.modelProps.getProperty(sKey,
              "[Key '" + sKey + "' not found]");
    }

    public void setModelProp(String sKey, String sValue)
    {
        this.modelProps.setProperty(sKey, sValue);
    }

    public final void setConfigFilename(String configFilename)
    {
        this.configFilename = configFilename;
    }

    public String getConfigFullPath()
    {
        return configFullPath;
    }

    public void setConfigFullPath(String configFullPath)
    {
        this.configFullPath = configFullPath;
    }

    public String getConfigFilename()
    {
        return configFilename;
    }

    public void setbDirty(Boolean bDirty)
    {
        this.bDirty = bDirty;
    }

    public Properties getModelProps()
    {
        return modelProps;
    }
}
