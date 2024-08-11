package com.hpi.TPCCMprefs;

import java.util.*;

public class CMNotesPrefsModel 
{
    
    private static final ArrayList<CMNotesPrefsModel> PREFS_LIST;
    
    private String language;
    private String currVersion;
    private String lastVersion;
    private String lastOS;
    private String currOS;
    
    static {
        PREFS_LIST = new ArrayList<>();
    }

    public static ArrayList<CMNotesPrefsModel> getPREFS_LIST()
    {
        return PREFS_LIST;
    }

    public String getLanguage()
    {
        return language;
    }

    public String getCurrVersion()
    {
        return currVersion;
    }

    public String getLastVersion()
    {
        return lastVersion;
    }

    public String getLastOS()
    {
        return lastOS;
    }

    public String getCurrOS()
    {
        return currOS;
    }
}
