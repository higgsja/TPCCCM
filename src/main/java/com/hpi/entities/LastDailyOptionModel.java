package com.hpi.entities;

import lombok.*;

@AllArgsConstructor
@Setter @Getter

public class LastDailyOptionModel 
{
    //
    public static final String TRUNCATE = "truncate hlhtxc5_dmOfx.Util_LastDailyOption;";

    //set Price to midpoint of bid and ask
    public static final String UPDATE = "insert ignore into hlhtxc5_dmOfx.Util_LastDailyOption select distinct oof.EquityId, oh.DataDate, oh.BidPrice, oh.AskPrice, oh.LastPrice, round((oh.AskPrice + oh.BidPrice) / 2, 2) as Price, oh.PutCall, oh.StrikePrice from hlhtxc5_dmOfx.OpenOptionFIFO as oof, hlhtxc5_dmOfx.OptionHistory as oh, (select B.EquityId, max(C.DataDate) as TopDate from hlhtxc5_dmOfx.OpenOptionFIFO as B, hlhtxc5_dmOfx.OptionHistory as C where B.EquityId = C.EquityId and C.DataDate >= subdate(now(), interval 7 day) group by B.EquityId) as A where oof.EquityId = oh.EquityId and oof.EquityId = A.EquityId and oh.DataDate = A.Topdate and oof.EquityId = oh.EquityId;";
//        "insert ignore into hlhtxc5_dmOfx.Util_LastDailyOption select oof.EquityId, max(oh.DataDate) as DataDate, oh.BidPrice, oh.AskPrice, oh.LastPrice, round((oh.AskPrice + oh.BidPrice) / 2, 2) as Price, oh.PutCall, oh.StrikePrice from hlhtxc5_dmOfx.OpenOptionFIFO as oof left join hlhtxc5_dmOfx.OptionHistory as oh on oh.EquityId = oof.EquityId where oh.DataDate >= subdate(now(), interval 7 day) group by oof.EquityId";
    
    //arbitrarily using 4 days prior in an attempt to reduce processing time
    public static final String CREATE_VIEW = "create view Util_LastDailyOption select oof.EquityId, max(oh.DataDate) as DataDate, oh.BidPrice, oh.AskPrice, oh.LastPrice, round((oh.AskPrice + oh.BidPrice) / 2, 2) as Price, oh.PutCall, oh.StrikePrice from hlhtxc5_dmOfx.OpenOptionFIFO as oof left join hlhtxc5_dmOfx.OptionHistory as oh on oh.EquityId = oof.EquityId where oh.DataDate >= subdate(now(), interval 4 day)";
    
    public static final String CREATE_TABLE = "insert ignore into hlhtxc5_dmOfx.Util_LastDailyOption select oof.EquityId, max(oh.DataDate) as DataDate, oh.BidPrice, oh.AskPrice, oh.LastPrice, round((oh.AskPrice + oh.BidPrice) / 2, 2) as Price, oh.PutCall, oh.StrikePrice from hlhtxc5_dmOfx.OpenOptionFIFO as oof left join hlhtxc5_dmOfx.OptionHistory as oh on oh.EquityId = oof.EquityId where oh.DataDate >= subdate(now(), interval 4 day) group by oof.EquityId";
    
    private String equityId;
    private java.sql.Date dataDate;
    private Double bidPrice;
    private Double askPrice;
    private Double lastPrice;
    private Double price;
    private String putCall;
    private Double strikePrice;
}
