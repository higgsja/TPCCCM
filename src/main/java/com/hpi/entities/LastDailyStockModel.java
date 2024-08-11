package com.hpi.entities;

import lombok.*;

@AllArgsConstructor
@Setter @Getter
public class LastDailyStockModel {
    //
    public static final String TRUNCATE = "truncate hlhtxc5_dmOfx.Util_LastDailyStock;";
    //this would time out
//    public static final String UPDATE =
//        "insert ignore into hlhtxc5_dmOfx.Util_LastDailyStock select eh.Ticker, max(`Date`) as `Date`, eh.Open, eh.High, eh.Low, eh.Close, eh.Volume from (select distinct Ticker from hlhtxc5_dmOfx.EquityHistory) as A, hlhtxc5_dmOfx.EquityHistory as eh where A.Ticker = eh.Ticker group by Ticker order by Ticker";
//
//    public static final String UPDATE =
//        "insert ignore into hlhtxc5_dmOfx.Util_LastDailyStock select eh.Ticker, eh.`Date`, eh.Open, eh.High, eh.Low, eh.Close, eh.Volume from hlhtxc5_dmOfx.EquityHistory as eh, (select Ticker, max(`Date`) `Date` from hlhtxc5_dmOfx.EquityHistory where `Date` > date_sub(now(), interval 5 day) group by Ticker) as A where A.Ticker = eh.Ticker and A.`Date` = eh.`Date`";
    
    public static final String UPDATE = "insert ignore into hlhtxc5_dmOfx.Util_LastDailyStock (EquityId, `Date`, Open, High, Low, Close, Volume) values ('%s', '%s', '%s', '%s', '%s', '%s', '%s')";

    private String equityId;
    private java.sql.Date dataDate;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Double volume;
}
