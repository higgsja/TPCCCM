package com.hpi.entities;

import com.hpi.TPCCMcontrollers.*;
import java.util.ArrayList;
import lombok.*;

/**
 * Array of transactionModel
 *
 * @author Joe@Higgs-Tx.com
 */
@Getter
public class TransactionsModel
{

    private final ArrayList<TransactionModel> listTransactions = 
        new ArrayList<>();

    protected Boolean exportTransactionsSummary(Double dStart, Boolean bHaveOpen)
    {
        Double dResult;
        StringBuffer sbSummary;

        dResult = dStart;
        sbSummary = BrokersController.getInstance().getSbSummary();

        for (TransactionModel t : this.listTransactions)
        {
            dResult += t.getDTotal();
        }

        if (bHaveOpen)
        {
            sbSummary.append(
                listTransactions.get(listTransactions.size() - 1).
                getDateDtTrade());
            sbSummary.append("\t");
        }

        sbSummary.append(dResult);

        return true;
    }
}
