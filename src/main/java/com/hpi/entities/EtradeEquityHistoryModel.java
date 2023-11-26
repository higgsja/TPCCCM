package com.hpi.entities;

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
        @Setter private String tickerIEX;
        @Setter private String ticker;
        @Setter private String dateString;
        @Setter private LocalDate date;
        @Setter private Double open;
        @Setter private Double high;
        @Setter private Double low;
        @Setter private Double close;
        @Setter private Integer volume;
}
