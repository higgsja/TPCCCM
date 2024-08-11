/*
 * Copyright 2015 all rights reserved.
 * HLH LLC
 */
package com.hpi.entities;

import com.hpi.TPCCMcontrollers.*;
import lombok.*;

/**
 * Data model for a lot. Will have 0 (a truncated dataset problem) or 1
 * opening transaction and 0 or more closing transactions.
 *
 * @author Joe@Higgs-Tx.com
 */
@Getter
public class LotModel {
    private TransactionModel openingTransaction;
    private final TransactionsModel closingTransactions;
    @Setter private Double dOpenUnitsRemaining;

    public LotModel() {
        this.openingTransaction = null;
        this.closingTransactions = new TransactionsModel();
        this.dOpenUnitsRemaining = 0.0;
    }

    public Boolean doOpenLot(TransactionModel opening) {
        this.openingTransaction = opening;
        this.dOpenUnitsRemaining = this.openingTransaction.getDUnits();

        return true;
    }

    protected Boolean exportLot(String sBrokerName, String sBrokerId,
        String sAcctId, String sSecUniqueId,
        String sTicker) {
        StringBuffer sbDetail;
        StringBuffer sbSummary;

        sbDetail = BrokersController.getInstance().getSbDetail();
        sbSummary = BrokersController.getInstance().getSbSummary();

        sbDetail.append(sBrokerName);
        sbDetail.append("\t");
        sbDetail.append(sBrokerId);
        sbDetail.append("\t");
        sbDetail.append(sAcctId);
        sbDetail.append("\t");
        sbDetail.append(sSecUniqueId);
        sbDetail.append("\t");
        sbDetail.append(sTicker);
        sbDetail.append("\t");

        if (this.openingTransaction != null) {
            // have an opening transaction
            this.openingTransaction.exportTransaction();

            if (closingTransactions.getListTransactions().size() > 0) {
                // have closing transactions
                sbSummary.append(sBrokerName);
                sbSummary.append("\t");
                sbSummary.append(sBrokerId);
                sbSummary.append("\t");
                sbSummary.append(sAcctId);
                sbSummary.append("\t");
                sbSummary.append(sSecUniqueId);
                sbSummary.append("\t");
                sbSummary.append(sTicker);
                sbSummary.append("\t");
                this.openingTransaction.exportSummaryTransaction();

                for (TransactionModel t
                         : this.closingTransactions.getListTransactions()) {
                    sbDetail.append(sBrokerName);
                    sbDetail.append("\t");
                    sbDetail.append(sBrokerId);
                    sbDetail.append("\t");
                    sbDetail.append(sAcctId);
                    sbDetail.append("\t");
                    sbDetail.append(sSecUniqueId);
                    sbDetail.append("\t");
                    sbDetail.append(sTicker);
                    sbDetail.append("\t");
                    t.exportTransaction();
                }

                if (!closingTransactions.
                    exportTransactionsSummary(
                        this.openingTransaction.getDTotal(),
                        true)) {
                    return false;
                }
                sbSummary.append("\n");
            }
            else {
                // no closing transactions
            }
        }
        else {
            // no opening transaction
            this.closingTransactions.
                getListTransactions().get(0).exportTransaction();

            this.closingTransactions.
                getListTransactions().get(0).exportSummaryTransaction();
        }

        return true;
    }
}
