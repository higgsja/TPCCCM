package com.hpi.hpiUtils;

import lombok.*;
import org.apache.commons.lang3.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OCCclass {

    public OCCclass(String occString) {
        this.ticker = occString.substring(0, 6).trim();
        if(this.ticker.contains(" ")){
            this.ticker = this.ticker.substring(0, ticker.indexOf(" "));
        }
        //strange: trim does not eliminate trailing space
        //but, now it does ...
        this.expYear = occString.substring(6, 8);
        this.expMonth = occString.substring(8, 10);
        this.expDay = occString.substring(10, 12);
        this.putcall = occString.substring(12, 13);
        this.dStrike = Double.parseDouble(StringUtils.stripStart(occString.substring(
            13), "0")) / 1000.0;
        this.strike = dStrike.toString();
        this.dtExpiry = CMHPIUtils.convertStringToSQLDate(
            (this.expYear + "-" + this.expMonth + "-" + this.expDay));
    }
    private String ticker;
    private String strike;
    private Double dStrike;
    private String putcall;
    private String expYear;
    private String expMonth;
    private String expDay;
    private java.sql.Date dtExpiry;
}
