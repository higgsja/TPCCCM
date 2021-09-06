package com.hpi.TPCCMprefs;

import com.hpi.TPCCMcontrollers.CMLanguageController;
import com.hpi.hpiUtils.CMHPIUtils;
import java.awt.*;
import java.io.*;
import java.util.jar.*;
import javax.swing.*;

/**
 * Provides for managing global preferences.
 * <p>
 */
public class CMGlobalsModel
      extends CMXMLModelBase
{

    private static Boolean gui;
    private static Frame frame;
    private static String CURRENT_BUILD_VERSION;
    private static String CURRENT_BUILD;
    private static String BUILD_TIMESTAMP;
    private static String language;
    private static final int LAST_OS = 0;
    private static final int CURRENT_OS = 1;
    private static final int LAST_VERSION = 2;
    private static final int CURRENT_VERSION = 3;
    private static final int LANGUAGE = 4;
    private static final String[][] KEY_VALUE = new String[][]
    {
        {
            "LastOS", ""
        },
        {
            "CurrentOS", ""
        },
        {
            "LastVersion", ""
        },
        {
            "CurrentVersion", ""
        },
        {
            "Language", ""
        }
    };
    //*** singleton
    private static CMGlobalsModel instance;

    protected CMGlobalsModel()
    {
        // protected prevents instantiation outside package
        super("globals.config");

        CMGlobalsModel.CURRENT_BUILD_VERSION = "";
        CMGlobalsModel.CURRENT_BUILD = "";

        this.sKeyValue = CMGlobalsModel.KEY_VALUE.clone();
    }

    /**
     *
     * @return
     */
    public synchronized static final CMGlobalsModel getInstance()
    {
        if (CMGlobalsModel.instance == null)
        {
            CMGlobalsModel.instance = new CMGlobalsModel();

            CMGlobalsModel.instance.read();
        }

        return CMGlobalsModel.instance;
    }
    //***

    public final void configVersionChange()
    {

    }

    private void read()
    {
        this.read(false);

        if ((CMGlobalsModel.CURRENT_BUILD_VERSION = this.getClass().
              getPackage().getImplementationVersion()) == null)
        {
            CMGlobalsModel.CURRENT_BUILD_VERSION = "1.0";
        }

        if ((CMGlobalsModel.CURRENT_BUILD = this.getClass().
              getPackage().getSpecificationVersion()) == null)
        {
            CMGlobalsModel.CURRENT_BUILD = "1";
        }

// lastOS
// move CurrentOS to LastOS
        if (modelProps.getProperty(CMGlobalsModel.KEY_VALUE[LAST_OS][0], "").isEmpty())
        {
            // first run
            this.bDirty = true;
            modelProps.setProperty(CMGlobalsModel.KEY_VALUE[LAST_OS][0],
                  System.getProperty("os.name"));
        }

// current operating system
        modelProps.setProperty(CMGlobalsModel.KEY_VALUE[CURRENT_OS][0],
              System.getProperty("os.name"));

        if (!modelProps.getProperty(CMGlobalsModel.KEY_VALUE[LAST_OS][0], "").
              equalsIgnoreCase(modelProps.
                    getProperty(CMGlobalsModel.KEY_VALUE[CURRENT_OS][0], "")))
        {
            // last OS different from current
            // take any action necessary

            // set last to current
            this.bDirty = true;
            modelProps.setProperty(CMGlobalsModel.KEY_VALUE[LAST_OS][0],
                  modelProps.getProperty(CMGlobalsModel.KEY_VALUE[CURRENT_OS][0]));
        }

// last version
        if (modelProps.getProperty(CMGlobalsModel.KEY_VALUE[LAST_VERSION][0], "").isEmpty())
        {
            // first run, set last to current
            this.bDirty = true;
            modelProps.setProperty(CMGlobalsModel.KEY_VALUE[LAST_VERSION][0],
                  CMGlobalsModel.CURRENT_BUILD_VERSION
                  + "." + CMGlobalsModel.CURRENT_BUILD);
        }

// current version
        modelProps.setProperty(CMGlobalsModel.KEY_VALUE[CURRENT_VERSION][0],
              CMGlobalsModel.CURRENT_BUILD_VERSION
              + "." + CMGlobalsModel.CURRENT_BUILD);

        if (!modelProps.getProperty(CMGlobalsModel.KEY_VALUE[LAST_VERSION][0]).equalsIgnoreCase(modelProps.getProperty(CMGlobalsModel.KEY_VALUE[CURRENT_VERSION][0])))
        {
            // current version different from last
            // take any action necessary
            // set last to current
            this.bDirty = true;
            modelProps.setProperty(CMGlobalsModel.KEY_VALUE[LAST_VERSION][0],
                  modelProps.getProperty(CMGlobalsModel.KEY_VALUE[CURRENT_VERSION][0]));
        }

// language
        if (modelProps.getProperty(CMGlobalsModel.KEY_VALUE[LANGUAGE][0], "").isEmpty())
        {
            this.bDirty = true;
            // set to default value
            modelProps.setProperty(CMGlobalsModel.KEY_VALUE[LANGUAGE][0], "English (US)");
        }

        this.write();
    }

    @Override
    public void write()
    {
        super.write();
    }

    private static synchronized void getVersionInfo()
    {
        Manifest manifest;
        InputStream manifestStream;

        // null
        manifestStream = CMGlobalsModel.class.getClassLoader().
              getResourceAsStream("META-INF/MANIFEST.MF");

        manifest = null;
        if (manifestStream != null)
        {
            try
            {
                manifest = new Manifest(manifestStream);
            }
            catch (IOException ex)
            {
                String s = String.format(CMLanguageController.
                      getErrorProp("GeneralError"),
                      ex.toString());

                CMHPIUtils.showDefaultMsg(CMLanguageController.getAppProp("Title") + 
                            CMLanguageController.getErrorProp("Title"),
                      Thread.currentThread().getStackTrace()[1].getClassName(),
                      Thread.currentThread().getStackTrace()[1].getMethodName(),
                      s,
                      JOptionPane.ERROR_MESSAGE);
            }

            if (manifest != null)
            {
                Attributes attributes = manifest.getMainAttributes();
                if (attributes.getValue("Implementation-Version") == null)
                {
                    CMGlobalsModel.setCURRENT_BUILD_VERSION("1.1");
                }
                else
                {
                    CMGlobalsModel.setCURRENT_BUILD_VERSION(attributes.
                          getValue("Implementation-Version"));
                }

                if (attributes.getValue("Specification-Version") == null)
                {
                    CMGlobalsModel.setCURRENT_BUILD("20");

                }
                else
                {
                    CMGlobalsModel.setCURRENT_BUILD(attributes.
                          getValue("Specification-Version"));
                }
            }
        }
    }

    @Override
    public void setbDirty(Boolean bDirty)
    {
        this.bDirty = bDirty;
    }

    public static void setCURRENT_BUILD_VERSION(String CURRENT_BUILD_VERSION)
    {
        CMGlobalsModel.CURRENT_BUILD_VERSION = CURRENT_BUILD_VERSION;
    }

    public static void setCURRENT_BUILD(String CURRENT_BUILD)
    {
        CMGlobalsModel.CURRENT_BUILD = CURRENT_BUILD;
    }

    public static Boolean getGui()
    {
        return gui;
    }

    public static void setGui(Boolean gui)
    {
        CMGlobalsModel.gui = gui;
    }

    public static String getCURRENT_BUILD_VERSION()
    {
        return CURRENT_BUILD_VERSION;
    }

    public static String getCURRENT_BUILD()
    {
        return CURRENT_BUILD;
    }

    public static Frame getFrame()
    {
        return frame;
    }

    public static void setFrame(Frame frame)
    {
        CMGlobalsModel.frame = frame;
    }

    public static int getLANGUAGE()
    {
        return LANGUAGE;
    }

    public static String[][] getKEY_VALUE()
    {
        return KEY_VALUE;
    }    
}
