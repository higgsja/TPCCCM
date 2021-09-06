/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hpi.TPCCMprefs;

/**
 * Provide the details for a given language.
 *
 * @author Joe@Higgs-Tx.com
 */
public class CMDirectory
{
    private final String sOS;
    private final String sHome;
    private final String sDefault;
    private final String sReports;

    public CMDirectory()
    {
        this.sOS = null;
        this.sHome = null;
        this.sDefault = null;
        this.sReports = null;
    }

    public CMDirectory(String sOs, String sHome, String sDefault, String sReports)
    {
        this.sOS = sOs;
        this.sHome = sHome;
        this.sDefault = sDefault;
        this.sReports = sReports;
    }
    
    public String getsOS()
    {
        return sOS;
    }

    public String getsHome()
    {
        return sHome;
    }

    public String getsDefault()
    {
        return this.sDefault;
    }
    
    public String getsReports()
    {
        return this.sReports;
    }
}
