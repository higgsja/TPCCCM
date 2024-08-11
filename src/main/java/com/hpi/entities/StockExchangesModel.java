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
public class StockExchangesModel
{
    final TreeSet<StockExchangeModel> exchangesList;
    
    public StockExchangesModel()
    {
        this.exchangesList = new TreeSet<>();
    }   
}
