package com.hpi.entities;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
/**
 * Json so do not change field names
 */
public class EtradeEquityHistoryDatumModel
{
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Integer volume;
}
