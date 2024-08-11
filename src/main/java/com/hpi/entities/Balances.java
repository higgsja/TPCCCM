package com.hpi.entities;

import lombok.*;

/**
 *
 * @author Joe@Higgs-Tx.com
 */
@RequiredArgsConstructor
@Getter
public class Balances
{

    public static final String INSERT_BAL_DB_2_DM
        = "insert ignore into hlhtxc5_dmOfx.Balances (DMAcctId, JoomlaId, Name, `Value`, DtAsOf) select dmAccts.DMAcctId, dbAccts.JoomlaId, 'AvailCash', ib.AvailCash, max(ib.DtAsOf) as DtAsOf from hlhtxc5_dbOfx.InvBal as ib, hlhtxc5_dbOfx.Accounts as dbAccts, hlhtxc5_dmOfx.Accounts as dmAccts where ib.AcctId = dbAccts.AcctId and dmAccts.AcctId = dbAccts.AcctId and dbAccts.JoomlaId = '%s' group by dbAccts.AcctId;";

    private final Integer dmAcctId;
    private final Integer joomlaId;
    private final String name;
    private final String descr;
    private final String balType;
    private final Double value;
    private final String dtAsOf;
    private final String curSym;

    public static final String ALL_FIELDS = "DMAcctId, JoomlaId, Name, Descr, BalType, Value, DtAsOf, CurSym";
    public static final String DEMO_FIELDS = "Name, Descr, BalType, Value, DtAsOf, CurSym";
}
