package com.hpi.entities;

import java.util.*;
import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Builder @Getter @Setter
public class PositionClosedModel
{

    public static final String POSITION_INSERT
        = "insert ignore into hlhtxc5_dmOfx.PositionsClosed (DMAcctId, JoomlaId, Ticker, EquityId, PositionName, TacticId, Units, PriceOpen, Price, GainPct, DateOpen, DateClose, Days, Gain, PositionType, TransactionType, TotalOpen, TotalClose, EquityType) values (%s, %s, '%s', '%s', '%s', %s, %s, %s, %s, %s, '%s', '%s', %s, %s, '%s', '%s', %s, %s, '%s')";

//    public static final String POSITION_UPDATE_TACTICID
//        = "update hlhtxc5_dmOfx.PositionsClosed set TacticId = '%s' where PositionId = '%s'";
//    public static final String POSITION_UPDATE_POSITIONNAME
//        = "update hlhtxc5_dmOfx.PositionsClosed set PositionName = '%s' where PositionId = '%s'";
//    public static final String POSITION_UPDATE_FIELDS
//        = "update hlhtxc5_dmOfx.PositionsClosed set Units = '%s', PriceOpen = '%s', Price = '%s', GainPct = '%s', DateOpen = '%s', Days = '%s', TacticId = '%s' where PositionId = '%s'";
    public PositionClosedModel(PositionClosedModel pcm)
    {
        this.positionId = pcm.positionId;
        this.dmAcctId = pcm.dmAcctId;
        this.joomlaId = pcm.joomlaId;
        this.ticker = pcm.ticker;
        this.equityId = pcm.equityId;
        this.positionName = pcm.positionName;
        this.tacticId = pcm.tacticId;
        this.units = pcm.units;
        this.priceOpen = pcm.priceOpen;
        this.price = pcm.price;
        this.gainPct = pcm.gainPct;
        this.dateOpen = pcm.dateOpen;
        this.dateClose = pcm.dateClose;
        this.days = pcm.days;
        this.comment = pcm.comment;
        this.gain = pcm.gain;
        this.positionType = pcm.positionType;
        this.transactionType = pcm.transactionType;
        this.totalOpen = pcm.totalOpen;
        this.totalClose = pcm.totalClose;
        this.equityType = pcm.equityType;

        this.positionClosedTransactionModels = new ArrayList<>();

        for (PositionClosedTransactionModel pctm : pcm.getPositionClosedTransactionModels())
        {
            this.positionClosedTransactionModels.add(new PositionClosedTransactionModel(pctm));
        }
    }

    //do not store ClientSectorId; we get it from the source, ClientEquityAttributes, when required
    private Integer positionId;
    private Integer dmAcctId;
    private Integer joomlaId;
    private String ticker;
    private String equityId;
    private String positionName;
    private Integer tacticId;
    private Double units;
    private Double priceOpen;
    private Double price;
    private Double gainPct;
    private java.sql.Date dateOpen;
    private java.sql.Date dateClose;
    private Integer days;
    private String comment;
    private Double gain;
    private String positionType;    //long, short
    private String transactionType;
    private Double totalOpen;
    private Double totalClose;
    private String equityType;

    @Builder.Default @Getter private final ArrayList<PositionClosedTransactionModel> positionClosedTransactionModels = new ArrayList<>();
}
