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
public class StockCurrenciesModel
{
    final TreeSet<StockCurrencyModel> currenciesList;
    
    public StockCurrenciesModel()
    {
        this.currenciesList = new TreeSet<>();
    }
}
