/*
 * Copyright 2015 all rights reserved.
 * HLH LLC
 */
package com.hpi.entities;

import com.hpi.entities.AccountsModel;

/**
 *  Data model for a broker.
 * @author Joe@Higgs-Tx.com
 */
public class BrokerModel
{
    private String sBrokerName;
    private String sBrokerId;
    private AccountsModel accountsModel;
    
//    private final String errorPrefix;
//    private String fErrorPrefix;

    public BrokerModel()
    {
        this.sBrokerName = null;
        this.sBrokerId = null;
        this.accountsModel = new AccountsModel();
        
//        this.errorPrefix = "BrokerModel";
//        this.fErrorPrefix = null;
    }
    
    Boolean doBroker(String sName, String sBrokerId)
    {
        this.sBrokerId = sBrokerId;
        this.sBrokerName = sName;
        
        // run a query on this broker to get all accounts
        return this.accountsModel.doAccounts(sBrokerId);
    }
    
    Boolean exportBroker()
    {
        return accountsModel.exportAccounts(this.sBrokerName, this.sBrokerId);
    }

    String getsBrokerName()
    {
        return this.sBrokerName;
    }

    String getsBrokerId()
    {
        return this.sBrokerId;
    }   

    AccountsModel getAccountsModel()
    {
        return accountsModel;
    }   
}
