package com.hpi.entities;

import java.util.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
/**
 * final open position
 */
public class PositionOpenModel
{

    public static final int TACTICID_CUSTOM = 0;
    public static final int TACTICID_LONG = 1;
    public static final int TACTICID_SHORT = 2;
    public static final int TACTICID_VERTICAL = 3;
    public static final int TACTICID_STRANGLE = 4;
    public static final int TACTICID_CALENDAR = 5;
    public static final int TACTICID_LEAP = 6;
    public static final int TACTICID_COVERED = 7;
    public static final int TACTICID_STRADDLE = 8;
    public static final int TACTICID_IRONCONDOR = 9;
    public static final int TACTICID_BUTTERFLY = 10;
    public static final int TACTICID_CONDOR = 11;
    public static final int TACTICID_COLLAR = 12;
    public static final int TACTICID_DIAGONAL = 13;
    public static final int TACTICID_VERTICAL_CUSTOM = 14;
    public static final int TACTICID_STRANGLE_CUSTOM = 15;
    public static final int TACTICID_CALENDAR_CUSTOM = 16;
    public static final int TACTICID_STRADDLE_CUSTOM = 17;
    public static final int TACTICID_COLLAR_CUSTOM = 18;
    public static final int TACTICID_DIAGONAL_CUSTOM = 19;

    //call it a leap if expiry is >=
    public static final int LEAP_DAYS = 180;

    public PositionOpenModel(PositionOpenModel pom)
    {
        this.positionId = pom.positionId;
//        this.dmAcctId = pom.dmAcctId;
        this.joomlaId = pom.joomlaId;
        this.ticker = pom.ticker;
        this.equityId = pom.equityId;
        this.positionName = pom.positionName;
        this.tacticId = pom.tacticId;
        this.units = pom.units;
        this.priceOpen = pom.priceOpen;
        this.price = pom.price;
        this.gain = pom.gain;
        this.gainPct = pom.gainPct;
        this.dateOpen = pom.dateOpen;
        this.days = pom.days;
        this.comment = pom.comment;

        this.positionType = pom.positionType;
        this.totalOpen = pom.totalOpen;
        this.totalClose = pom.totalClose;
        this.transactionType = pom.transactionType;
        this.mktVal = pom.mktVal;
//        this.lMktVal = pom.lMktVal;
//        this.actPct = pom.actPct;
        this.positionOpenTransactionModels = new ArrayList<>();

        for (PositionOpenTransactionModel potm : pom.getPositionOpenTransactionModels())
        {
            this.positionOpenTransactionModels.add(potm);
        }
    }

//    public static final String POSITION_INSERT3 = "insert into hlhtxc5_dmOfx.PositionsOpen(DMAcctId, JoomlaId, Ticker, EquityId, PositionName, TacticId, Units, PriceOpen, Price, GainPct, DateOpen, Days, Gain, PositionType, MktVal, LMktVal, ActPct) select '%s','%s','%s', '%s','%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s';";
    public static final String POSITION_INSERT3
        //        = "insert into hlhtxc5_dmOfx.PositionsOpen(JoomlaId, Ticker, EquityId, PositionName, TacticId, Units, PriceOpen, Price, GainPct, DateOpen, Days, Gain, PositionType, MktVal, LMktVal, ActPct) select '%s','%s','%s', '%s','%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s';";
        = "insert into hlhtxc5_dmOfx.PositionsOpen(DmAcctId, JoomlaId, Ticker, EquityId, PositionName, TacticId, Units, PriceOpen, Price, GainPct, DateOpen, Days, Gain, PositionType, TotalOpen, TotalClose, EquityType, TransactionType, MktVal) select %s, %s,'%s','%s', '%s', %s, %s, %s, %s, %s, '%s', %s, %s, '%s', %s, %s, '%s', '%s', %s;";

//    public static final String POSITION_UPDATE_TACTICID
//        = "update hlhtxc5_dmOfx.PositionsOpen set TacticId = '%s' where PositionId = '%s'";

//    public static final String POSITION_UPDATE_POSITIONNAME
//        = "update hlhtxc5_dmOfx.PositionsOpen set PositionName = '%s' where PositionId = '%s'";

//    public static final String POSITION_UPDATE_FIELDS
//        = "update hlhtxc5_dmOfx.PositionsOpen set Units = '%s', PriceOpen = '%s', Price = '%s', GainPct = '%s', DateOpen = '%s', Days = '%s', TacticId = '%s' where PositionId = '%s'";

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
    private Double gain;
    private Double gainPct;
    private java.sql.Date dateOpen;
    private Integer days;
    private String comment;

    private String positionType; //long, short
    private Double totalOpen;
    private Double totalClose;
    private String equityType;
    private String transactionType;
    private Double mktVal;
//    private Double lMktVal;
//    private Double actPct;

    @Builder.Default @Getter private final ArrayList<PositionOpenTransactionModel> positionOpenTransactionModels
        = new ArrayList<>();
}
