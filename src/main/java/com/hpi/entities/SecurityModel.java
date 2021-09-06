/*
 * Copyright 2015 all rights reserved.
 * HLH LLC
 */
package com.hpi.entities;

/**
 * Data model for a security lot.
 *
 * @author Joe@Higgs-Tx.com
 */
public class SecurityModel
{
    private String sSecUniqueId = null;
    private String sTicker = null;
    private final LotsModel lotsModel = new LotsModel();

    protected Boolean doSecurity(String sBrokerId, String sAcctId,
                              String sSecUniqueId, String sTicker)
    {
        this.sSecUniqueId = sSecUniqueId;
        this.sTicker = sTicker;

        return (this.lotsModel.doLots(sBrokerId, sAcctId, sSecUniqueId));
    }

    protected Boolean exportSecurity(String sBrokerName,
                                  String sBrokerId,
                                  String sAcctId)
    {
        return lotsModel.exportLots(sBrokerName, sBrokerId, sAcctId,
                                    this.sSecUniqueId, this.sTicker);
    }
}
