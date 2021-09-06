package com.hpi.entities;

import java.util.*;

public enum OptionTypeEnum {

    CALL("Call"),
    PUT("Put");

    private static final Map<String, OptionTypeEnum> BY_LABEL = new HashMap<>();

    static {
        for (OptionTypeEnum e : values()) {
            BY_LABEL.put(e.label, e);
        }
    }

    public final String label;

    private OptionTypeEnum(String label) {
        this.label = label;
    }

    public static OptionTypeEnum valueOfLabel(String label) {
        return BY_LABEL.get(label);
    }

    @Override
    public String toString() {
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
