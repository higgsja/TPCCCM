package com.hpi.entities;

import lombok.*;

@AllArgsConstructor
@Setter @Getter

public class LastDailyOptionModel 
{
    // Util_LastDailyOption schema: (EquityId, Date, LastPrice)
    // Populated by TPCcli --schwabOptionQuote (OptionQuotesSchwabController)
    public static final String TRUNCATE = "truncate hlhtxc5_dmOfx.Util_LastDailyOption;";

    public static final String SELECT_BY_EQUITY_ID =
            "select EquityId, Date, LastPrice from hlhtxc5_dmOfx.Util_LastDailyOption where EquityId = ?";

    private String equityId;
    private java.sql.Date date;
    private Double lastPrice;
}
