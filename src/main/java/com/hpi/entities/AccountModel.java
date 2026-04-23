/*
 * Copyright 2015 all rights reserved.
 * HLH LLC
 */
package com.hpi.entities;

/**
 *  Data for an account
 * @author Joe@Higgs-Tx.com
 */
public class AccountModel
{
    private String sAcctId;
    private final SecuritiesModel securitiesModel;

    public AccountModel()
    {
        sAcctId = null;
        securitiesModel = new SecuritiesModel();
    }
    
    Boolean doAccount(String sBrokerId, String sAcctId)
    {
        this.sAcctId = sAcctId;
        
        // work out the securities in this account
        return this.securitiesModel.doSecurities(sBrokerId, sAcctId);
    }
    
    public Boolean exportAccount(String sBrokerName, String sBrokerId)
    {
        return securitiesModel.exportSecurities(sBrokerName, 
                                                sBrokerId, 
                                                this.sAcctId);
    }
}
