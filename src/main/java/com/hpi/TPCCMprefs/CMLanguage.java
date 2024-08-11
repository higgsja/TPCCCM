package com.hpi.TPCCMprefs;

/**
 * Provide the details for a given language.
 */
public class CMLanguage
{
    private final String sName;
    private final String sFileSubstr;
    private final Boolean bAvailable;

    public CMLanguage()
    {
        this.sName = null;
        this.sFileSubstr = null;
        this.bAvailable = false;
    }

    public CMLanguage(String sName, String sFileSubstr, Boolean bAvailable)
    {
        this.sName = sName;
        this.sFileSubstr = sFileSubstr;
        this.bAvailable = bAvailable;
    }

    public CMLanguage(String sName, String sFileSubstr)
    {
        this(sName, sFileSubstr, false);
    }

    @Override
    public String toString()
    {
        return sName;
    }

    public String getsFileSubstr()
    {
        return sFileSubstr;
    }

    public Boolean getbAvailable()
    {
        return bAvailable;
    }
}
