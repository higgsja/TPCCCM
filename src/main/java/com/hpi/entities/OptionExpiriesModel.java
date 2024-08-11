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
public class OptionExpiriesModel
{
    final TreeSet<OptionExpiryModel> expiries;
    
    public OptionExpiriesModel()
    {
        this.expiries = new TreeSet<>();
    }
}
