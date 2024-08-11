package com.hpi.entities;

import java.util.*;

public enum OptionTransactionTypeEnum {
    BUYTOOPEN("buytoopen"),
    BUYTOCLOSE("buytoclose"),
    SELLTOOPEN("selltoopen"),
    SELLTOCLOSE("selltoclose"),
    EXPIRE("expire"),
    ASSIGN("assign");
    
    private static final Map<String, OptionTransactionTypeEnum> BY_LABEL = new HashMap<>();
    
    static {
        for (OptionTransactionTypeEnum e: values()){
            BY_LABEL.put(e.label, e);
        }
    }
    
    public final String label;
    
    private OptionTransactionTypeEnum(String label){
        this.label = label;
    }
    
    public static OptionTransactionTypeEnum valueOfLabel(String label){
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
