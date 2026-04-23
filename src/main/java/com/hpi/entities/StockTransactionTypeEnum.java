package com.hpi.entities;

import java.util.*;

public enum StockTransactionTypeEnum {
    BUY("buy"),
    BUYTOCOVER("buytocover"),
    SELL("sell"),
    SELLSHORT("sellshort");
    
    private static final Map<String, StockTransactionTypeEnum> BY_LABEL = new HashMap<>();
    
    static {
        for (StockTransactionTypeEnum e: values()){
            BY_LABEL.put(e.label, e);
        }
    }
    
    public final String label;
    
    private StockTransactionTypeEnum(String label){
        this.label = label;
    }
    
    public static StockTransactionTypeEnum valueOfLabel(String label){
        return BY_LABEL.get(label);
    }
    
    @Override
    public String toString(){
        return this.label;
    }
    
//    public static EquityEnum valueOfLabel(String label) {
//    for (EquityEnum e : values()) {
//        if (e.label.equals(label)) {
//            return e;
//        }
//    }
//    return null;
//}
    
}
