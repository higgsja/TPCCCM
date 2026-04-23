package com.hpi.entities.eTrade;

import java.time.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
/**
 * Json so do not change field names
 */
public class EtradeEquityHistoryModel
{

    private String tickerIEX;
    private String ticker;
    private String dateString;
    private Double open;
    private Double high;
    private Double low;
    private Double previousClose;
    private Double close;
    private Integer volume;
    private String symbolDescription;
    private Double optionPreviousAskPrice;
    private Double optionPreviousBidPrice;
    private String osiKey;

    //not from etrade
    //from EquityTypeEnum
    private LocalDate date;
    private String equityType;
    private String occSymbol;
    private String etradeString;

    public void setTickerIEX(String tickerIEX)
    {
        this.tickerIEX = tickerIEX;
        this.ticker = tickerIEX;
    }

    public void setTicker(String ticker)
    {
        this.ticker = ticker;
        this.tickerIEX = ticker;
    }
}
