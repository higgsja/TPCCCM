package com.hpi.entities;

import java.sql.*;
import lombok.*;

/**
 * @author Joe@Higgs-Tx.com
 */
@RequiredArgsConstructor
@Getter
public class AccountTotals
{

    public static final String INSERT_ACCT_TOTALS
        = "insert ignore into hlhtxc5_dmOfx.AccountTotals(DMAcctId, JoomlaId, StmtDt, Cost, MktValue) select fp.DMAcctId, fp.JoomlaId, A.DtAsOf, 0, sum(fp.MktVal) as MktValue from hlhtxc5_dmOfx.FIFOOpenTransactions as fp, (select DMAcctId, JoomLaId, max(DtAsOf) as DtAsOf from hlhtxc5_dmOfx.Balances where JoomlaId = '%s' group by DMAcctId, JoomLaId) as A where fp.DMAcctId = A.DMAcctId and fp.JoomLaId = A.JoomlaId and fp.JoomlaId = '%s' group by fp.DMAcctId, fp.JoomlaId;";

    private final Integer dmAcctId;
    private final Integer joomlaId;
    private final Date stmtDt;
    private final Double Cost;
    private final Double MktValue;

    public static final String ALL_FIELDS = "DMAcctId, JoomlaId, StmtDt, Cost, MktValue";
    public static final String DEMO_FIELDS = "StmtDt, Cost, MktValue";
}
