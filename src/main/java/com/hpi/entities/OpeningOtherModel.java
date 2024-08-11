package com.hpi.entities;

import java.time.*;
import lombok.*;

@RequiredArgsConstructor
@Getter
@Setter
public class OpeningOtherModel {
    private final Integer dmAcctId;
    private final Integer joomlaId;
    private final String fiTId;
    private final String secId;
    private final String equityId;
//    private final String secName;
    private final String ticker;
    private final LocalDateTime gmtDtTrade;
    private final LocalDateTime gmtDtSettle;
    private final Double units;
    private final Double unitPrice;
    private final Double markUpDn;
    private final Double commission;
    private final Double taxes;
    private final Double fees;
    private final Double transLoad;
    private final Double total;
    private final String curSym;
    private final String subAcctSec;
    private final String subAcctFund;
    private final String reversalFiTId;

    public static final String ALL_FIELDS =
        "DMAcctId, JoomlaId, FiTId, SecId, EquityId, Ticker, GMTDtTrade, GMTDtSettle, Units, UnitPrice, MarkUpDn, Commission, Taxes, Fees, TransLoad, Total, CurSym, SubAcctSec, SubAcctFund, ReversalFiTId";

    public static final String DEMO_FIELDS =
        "FiTId, SecId, EquityId, Ticker, GMTDtTrade, GMTDtSettle, Units, UnitPrice, MarkUpDn, Commission, Taxes, Fees, TransLoad, Total, CurSym, SubAcctSec, SubAcctFund, ReversalFiTId";

}
