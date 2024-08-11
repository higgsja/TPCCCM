package com.hpi.TPCCMprefs;

import com.hpi.TPCCMcontrollers.CMLanguageController;
import com.hpi.hpiUtils.CMHPIUtils;
import javax.swing.JOptionPane;

/**
 * Provides for managing directories preferences.
 *
 */
public class CMDirectoriesModel
      extends CMXMLModelBase
{

    //*** singleton
    private static final String[][] KEYVALUE = new String[][]
    {
        {
            "Default", ""
        },
        {
            "Home", ""
        },
        {
            "Reports", ""
        },
        {
            "OfxFiles", ""
        },
        {
            "OptionHistory", ""
        }
    };
    private static CMDirectoriesModel instance;

    protected CMDirectoriesModel(String sFile)
    {
        // protected prevents instantiation outside package
        super(sFile);
        this.sKeyValue = CMDirectoriesModel.KEYVALUE.clone();
    }

    public synchronized static CMDirectoriesModel getInstance()
    {
        String sFile;
        sFile = null;

        // discover operating system and set the .config file to use
        if (CMDirectoriesModel.instance == null)
        {
            if (System.getProperty("os.name").startsWith("Mac"))
            {
//                CMDirectoriesModel.sFile = "mac.config";
            }

            if (System.getProperty("os.name").startsWith("Windows"))
            {
                sFile = "windows.config";
            }

            if (System.getProperty("os.name").startsWith("Linux"))
            {
                sFile = "linux.config";
            }

            if (sFile == null)
            {
                String s;
                s = String.format(CMLanguageController.
                      getErrorProp("Formatted4"),
                      System.getProperty("os.name"));

                CMHPIUtils.showMsgInitializing(
                      "DirectoriesModel", "getInstance",
                      s, JOptionPane.ERROR_MESSAGE);

                System.exit(-1);
            }

            CMDirectoriesModel.instance = new CMDirectoriesModel(sFile);

            CMDirectoriesModel.instance.read();
        }

        return CMDirectoriesModel.instance;
    }
    //***

    public void read()
    {
        String sHome;

        sHome = System.getProperty("user.home");

        this.read(false);

        for (String[] s : CMDirectoriesModel.KEYVALUE)
        {
            if (this.getModelProp(s[0]).isEmpty())
            {
                // Set any empty ones to the user home dir
                this.bDirty = true;
                this.setModelProp(s[0], sHome);
            }
        }

        this.write();
    }

    @Override
    public void write()
    {
        super.write();
    }

    public static String[][] getKEYVALUE()
    {
        return KEYVALUE;
    }
    
}
