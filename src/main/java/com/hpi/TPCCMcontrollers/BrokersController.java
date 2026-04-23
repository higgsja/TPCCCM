/*
 * Copyright 2015 all rights reserved.
 * HLH LLC
 */
package com.hpi.TPCCMcontrollers;

import com.hpi.entities.BrokersModel;
import com.hpi.hpiUtils.CMHPIUtils;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import static java.nio.file.Files.newBufferedWriter;
import java.nio.file.Paths;
import javax.swing.JOptionPane;
import lombok.*;

@Getter
public class BrokersController {
    private final BrokersModel brokersModel;
    private final StringBuffer sbDetail;
    private final StringBuffer sbSummary;
    private final String errorPrefix;
    private String fErrorPrefix;
    //*** Singleton
    private static BrokersController instance;

    protected BrokersController() {
        this.brokersModel = new BrokersModel();
        this.sbDetail = new StringBuffer();
        this.sbSummary = new StringBuffer();

        this.errorPrefix = "BrokersController";
        this.fErrorPrefix = null;
    }

    public synchronized static BrokersController getInstance() {
        if (BrokersController.instance == null) {
            BrokersController.instance = new BrokersController();
        }
        return BrokersController.instance;
    }
    //***

    public Boolean doBrokers() {
        fErrorPrefix = Thread.currentThread().getStackTrace()[1].getMethodName();

        return brokersModel.doBrokers();
    }

    public Boolean exportBrokers() {
        this.fErrorPrefix = "exportBrokers";

        this.sbDetail.setLength(0);
        this.sbDetail.append("Broker\tID\tAcct\tSecId\tTicker\t");
        this.sbDetail.append("TName\tTType\tUnits\tShPer\tPrice\t");
        this.sbDetail.append("Cmmsn\tFees\tTax\tTotal\tDate\n");

        this.sbSummary.setLength(0);
        this.sbSummary.append("Broker\tID\tAcct\tSecId\tTicker\t");
        this.sbSummary.append("TName\tTType\tUnits\tShPer\tPrice\t");
        this.sbSummary.append(
            "Cmmsn\tFees\tTax\tTotal\tDateOpened\tDateClosed\tResult\n");

        if (!this.brokersModel.exportBrokers()) {
            return false;
        }

        // deal with the buffer
        try (BufferedWriter write = new BufferedWriter(newBufferedWriter(
            Paths.get(
                "/home/white/Documents/Investments/Database/Detail.csv"),
            StandardCharsets.UTF_8))) {
            write.write(sbDetail.toString());
            write.flush();
            write.close();

            CMHPIUtils.showDefaultMsg(
                CMLanguageController.getErrorProp("Title"),
                errorPrefix, fErrorPrefix,
                "\nLots details: /home/blue/oneDrive/Documents/Investments/Database/Detail.csv",
                JOptionPane.ERROR_MESSAGE
            );
        }
        catch (IOException ex) {
        }

        try (BufferedWriter write = new BufferedWriter(newBufferedWriter(
            Paths.get(
                "/home/white/Documents/Investments/Database/Summary.csv"),
            StandardCharsets.UTF_8))) {
            write.write(sbSummary.toString());
            write.flush();
            write.close();

            CMHPIUtils.showDefaultMsg(
                CMLanguageController.getErrorProp("Title"),
                errorPrefix, fErrorPrefix,
                "\nLots summary: /home/blue/oneDrive/Documents/Investments/Database/Summary.csv\n",
                JOptionPane.ERROR_MESSAGE);
        }
        catch (IOException ex) {
            //todo: handle?
        }

        return true;
    }
}
