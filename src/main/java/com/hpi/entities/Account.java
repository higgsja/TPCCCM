package com.hpi.entities;

import lombok.*;

@Getter
public class Account
        implements ISqlTableArray
{
    Integer dmAcctId;
    Integer brokerId;
    Integer acctId;
    String org;
    String fId;
    String invAcctIdFi;
    String clientAcctName;
    
    // deprecated
    public Account(Integer dmAcctId, String clientAcctName)
    {
        this(dmAcctId, null, null, "", "", "", clientAcctName);
    }
    
    public Account(Integer dmAcctId, Integer brokerId, Integer acctId,
          String org, String fId, String invAcctIdFi)
    {
        this(dmAcctId, brokerId, acctId, org, fId, invAcctIdFi, "");
    }

    public Account(Integer dmAcctId, Integer brokerId, Integer acctId,
          String org, String fId, String invAcctIdFi, String clientAcctName)
    {
        this.dmAcctId = dmAcctId;
        this.brokerId = brokerId;
        this.acctId = acctId;
        this.org = org;
        this.fId = fId;
        this.invAcctIdFi = invAcctIdFi;
        
        this.clientAcctName = clientAcctName; // from ClientAccts
    }

    @Override
    public void doSQL()
    {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

//    public Integer getDMAcctId()
//    {
//        return dmAcctId;
//    }
//
//    public String getClientAcctName()
//    {
//        return clientAcctName;
//    }    
}
