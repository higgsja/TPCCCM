package com.hpi.TPCCMprefs;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Manages languages data.
 *
 * @author Joe@Higgs-Tx.com
 */
public class CMLanguagesModel
        extends CMXMLModelBase
{
    //*** singleton
    private static final String[][] KEY_VALUE = new String[][]
    {
    };
    private static CMLanguagesModel instance;

    protected CMLanguagesModel()
    {
        // protected prevents instantiation outside package
        super("languages.config");

        this.sKeyValue = CMLanguagesModel.KEY_VALUE.clone();
    }

    public synchronized static final CMLanguagesModel getInstance()
    {
        if (CMLanguagesModel.instance == null)
        {
            CMLanguagesModel.instance = new CMLanguagesModel();
            
            CMLanguagesModel.instance.read(true);
        }

        return CMLanguagesModel.instance;
    }
    //***
    
    public final void configVersionChange()
    {
        
    }
    /**
     *  Provides a structure and access methods for each supported language
     */
    public static class Language
    {
        private final String fileSubstr;
        private final String displayName;
        private final Boolean bAvailable;

        /**
         * Constructor for a language
         * @param fileSubstr    filename suffix for language file
         * @param displayName   text to display concerning this language
         */
        public Language(String fileSubstr, String displayName)
        {
            this.fileSubstr = fileSubstr;
            this.displayName = displayName;
            this.bAvailable = languageAvailable();
        }
        
        private Boolean languageAvailable()
        {
            String sPath;
            
            sPath = CMGlobalsModel.getInstance().getConfigDirPath();
            sPath += System.getProperty("file.separator");
            sPath += "app-" + this.fileSubstr;

            return Files.exists(Paths.get(sPath));
        }

        public String getFileSubstr()
        {
            return fileSubstr;
        }

        public String getDisplayName()
        {
            return displayName;
        }

        public Boolean getbAvailable()
        {
            return bAvailable;
        }
    }
}
