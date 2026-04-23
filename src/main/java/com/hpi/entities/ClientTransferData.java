package com.hpi.entities;

import java.sql.*;
import lombok.*;

/**
 *
 * @author Joe@Higgs-Tx.com
 */
@RequiredArgsConstructor
@Getter
public class ClientTransferData {
    private final Integer dmAcctId;
    private final Integer joomlaId;
    private final String fiTId;
    private final String subAcctSec;
    private final String tferAction;
    private final Double units;
    private final Double costBasis;
    private final Date gmtDtSettle;
    private final Date gmtDtPurchase;

    public static final String ALL_FIELDS =
        "DMAcctId, JoomlaId, FiTId, SecId, SubAcctSec, TferAction, Units, CostBasis, GMTDtSettle, GMTDtPurchase";
    public static final String DEMO_FIELDS =
        "FiTId, SecId, SubAcctSec, TferAction, Units, CostBasis, GMTDtSettle, GMTDtPurchase";
}
