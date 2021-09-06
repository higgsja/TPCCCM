package com.hpi.entities;

import java.util.*;

public enum EquityTypeEnum {
    DEBT("Debt"),
    MF("Mutual Fund"),
    OPTION("Option"),
    OTHER("Other"),
    STOCK("Stock"),
    CASH("Cash");
    
    private static final Map<String, EquityTypeEnum> BY_LABEL = new HashMap<>();
    
    static {
        for (EquityTypeEnum e: values()){
            BY_LABEL.put(e.label, e);
        }
    }
    
    public final String label;
    
    private EquityTypeEnum(String label){
        this.label = label;
    }
    
    public static EquityTypeEnum valueOfLabel(String label){
        return BY_LABEL.get(label);
    }
    
    @Override
    public String toString(){
        return this.label;
    }
    
//    public static EquityTypeEnum valueOfLabel(String label) {
//    for (EquityTypeEnum e : values()) {
//        if (e.label.equals(label)) {
//            return e;
//        }
//    }
//    return null;
//}
    
}
