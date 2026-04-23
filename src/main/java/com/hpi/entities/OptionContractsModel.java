/*
 * Copyright 2015 all rights reserved.
 * HLH LLC
 */
package com.hpi.entities;

import java.util.TreeSet;

/**
 *
 * @author Joe@Higgs-Tx.com
 */
public class OptionContractsModel
{
    private final TreeSet<OptionExpiryModel> expiries;
    
    public OptionContractsModel()
    {
        this.expiries = new TreeSet<>();
    }

    public TreeSet<OptionExpiryModel> getExpiries()
    {
        return expiries;
    }
}
