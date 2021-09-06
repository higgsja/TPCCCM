package com.hpi.TPCCMprefs;

public class CMOfxDLAccountModel
{
    private String acctName;
    private String acctNumber;
    private String acctUId;
    private String acctPW;
    private String acctActive;

    public CMOfxDLAccountModel()
    {
    }
    
    public CMOfxDLAccountModel(String acctName, String acctNumber,
          String acctUId, String acctPW, String acctActive)
    {
        this.acctName = acctName;
        this.acctNumber = acctNumber;
        this.acctUId = acctUId;
        this.acctPW = acctPW;
        this.acctActive = acctActive;
    }
    
    @Override
    public String toString()
    {
        return this.acctName;
    }

    public void setAcctName(String acctName)
    {
        this.acctName = acctName;
    }

    public void setAcctNumber(String acctNumber)
    {
        this.acctNumber = acctNumber;
    }

    public void setAcctUId(String acctUId)
    {
        this.acctUId = acctUId;
    }

    public void setAcctPW(String acctPW)
    {
        this.acctPW = acctPW;
    }

    public void setAcctActive(String acctActive)
    {
        this.acctActive = acctActive;
    }
    
    public String getAcctName()
    {
        return acctName;
    }

    public String getAcctNumber()
    {
        return acctNumber;
    }

    public String getAcctUId()
    {
        return acctUId;
    }

    public String getAcctPW()
    {
        return acctPW;
    }

    public String getAcctActive()
    {
        return acctActive;
    }
}

