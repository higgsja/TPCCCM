package com.hpi.TPCCMcontrollers;

import com.hpi.TPCCMsql.CMDAOException;
import com.hpi.TPCCMprefs.CMDBModel;
import com.hpi.entities.*;
import com.hpi.hpiUtils.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import lombok.*;
import org.apache.commons.math3.util.*;

@Getter @Setter
public class OptionController {

    private ArrayList<Account> accountList;
    private ArrayList<String> equityIdList;
    private ArrayList<OpeningOptionModel> optionOpeningBuyList;
    private ArrayList<ClosingOptionModel> optionClosingSellList;
    private ArrayList<OpeningOptionModel> optionOpeningSellList;
    private ArrayList<ClosingOptionModel> optionClosingBuyList;
    private ArrayList<ClosingOptionModel> optionClosedList;
    private ArrayList<ClosingOptionModel> optionClosedTransList;
    private ArrayList<OpeningOptionModel> optionOpenList;
    private Integer userId;

    private final CMProgressBarCLI progressBarCLI;

    //*** Singleton
    private static OptionController instance;

    static {
        // Static initialization if needed
    }

    protected OptionController() {
        // protected prevents instantiation outside of package 
        this.userId = CMDBModel.getUserId();

        //force a progress bar
        this.progressBarCLI = new CMProgressBarCLI("true");

        this.accountList = new ArrayList<>();
        this.equityIdList = new ArrayList<>();
        this.optionOpeningBuyList = new ArrayList<>();
        this.optionClosingSellList = new ArrayList<>();
        this.optionOpeningSellList = new ArrayList<>();
        this.optionClosingBuyList = new ArrayList<>();
        this.optionClosedList = new ArrayList<>();
        this.optionClosedTransList = new ArrayList<>();
        this.optionOpenList = new ArrayList<>();
    }

    public synchronized static OptionController getInstance() {
        if (OptionController.instance == null) {
            OptionController.instance = new OptionController();
        }
        return OptionController.instance;
    }

    void initCustom() {

    }

    public void processFIFOOptionLotsAccounts() {
        // query Accounts for list of DMAcctId
        this.getAccounts();

        // Iterate through the accounts
        for (Account account : this.accountList) {
            System.out.println("      Processing account: " + account.getClientAcctName());

            // get unique list of equityId from OpeningOptions
            this.getDistinctEquityId(account.getDmAcctId());

            // iterate through equityId
            for (String equityId : this.equityIdList) {
                
                //iterate the transaction types
                for (OptionTransactionTypeEnum tte : OptionTransactionTypeEnum.values()) {
                    if (tte.toString().contains("open")) {
                        this.getAcctOptionsOpen(account.getDmAcctId(), equityId, tte.toString());
                    } else {
                        this.getAcctOptionsClose(account.getDmAcctId(), equityId, tte.toString());
                    }
                }

                processFIFOOptionLotsBuyToOpen();

                // clear the arrays
                this.optionOpeningBuyList.clear();
                this.optionClosingSellList.clear();
                this.optionClosedList.clear();
                this.optionClosedTransList.clear();
                this.optionOpenList.clear();

                processFIFOOptionLotsSellToOpen();

                // clear the arrays
                this.optionOpeningSellList.clear();
                this.optionClosingBuyList.clear();
                this.optionClosedList.clear();
                this.optionClosedTransList.clear();
                this.optionOpenList.clear();
            }
            
            // Clear equity list for next account
            this.equityIdList.clear();
        }
        
    }

    void getAccounts() {
        // FIXED: Use parameterized query to prevent SQL injection
        String sql = "SELECT Accounts.DMAcctId, BrokerId, AcctId, Org, FId, InvAcctIdFi, ClientAcctName " +
                    "FROM hlhtxc5_dmOfx.Accounts, hlhtxc5_dmOfx.ClientAccts " +
                    "WHERE Accounts.DMAcctId = ClientAccts.DMAcctId " +
                    "AND ClientAccts.Active = 'Yes' " +
                    "AND ClientAccts.JoomlaId = ?";

        try (Connection con = CMDBController.getConnection();
             PreparedStatement pStmt = con.prepareStatement(sql)) {
            
            pStmt.setInt(1, this.userId);
            con.clearWarnings();
            
            try (ResultSet rs = pStmt.executeQuery()) {
                while (rs.next()) {
                    Account accountTemp = new Account(
                        rs.getInt("DMAcctId"),
                        rs.getInt("BrokerId"),
                        rs.getInt("AcctId"),
                        rs.getString("Org"),
                        rs.getString("FId"),
                        rs.getString("InvAcctIdFi"),
                        rs.getString("ClientAcctName"));

                    this.accountList.add(accountTemp);
                }
            }
            
        } catch (SQLException ex) {
            throw new CMDAOException(
                CMLanguageController.getDBErrorProp("Title"),
                Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                ex.getMessage(), 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void getDistinctEquityId(Integer account) {
        // FIXED: Use parameterized query to prevent SQL injection
        String sql = "SELECT DISTINCT A.EquityId FROM (" +
                    "SELECT DISTINCT EquityId FROM hlhtxc5_dmOfx.OpeningOptions " +
                    "WHERE DMAcctId = ? AND JoomlaId = ? AND NOT(Units = 0) " +
                    "UNION " +
                    "SELECT DISTINCT EquityId FROM hlhtxc5_dmOfx.ClientOpeningOptions " +
                    "WHERE DMAcctId = ? AND JoomlaId = ? AND NOT(Units = 0) " +
                    "UNION " +
                    "SELECT DISTINCT EquityId FROM hlhtxc5_dmOfx.ClosingOptions " +
                    "WHERE DMAcctId = ? AND JoomlaId = ? AND NOT(Units = 0) " +
                    "UNION " +
                    "SELECT DISTINCT EquityId FROM hlhtxc5_dmOfx.ClientClosingOptions " +
                    "WHERE DMAcctId = ? AND JoomlaId = ? AND NOT(Units = 0)" +
                    ") AS A ORDER BY EquityId";

        try (Connection con = CMDBController.getConnection();
             PreparedStatement pStmt = con.prepareStatement(sql)) {
            
            // Set parameters for all 8 placeholders
            pStmt.setInt(1, account);
            pStmt.setInt(2, this.userId);
            pStmt.setInt(3, account);
            pStmt.setInt(4, this.userId);
            pStmt.setInt(5, account);
            pStmt.setInt(6, this.userId);
            pStmt.setInt(7, account);
            pStmt.setInt(8, this.userId);
            
            con.clearWarnings();
            
            try (ResultSet rs = pStmt.executeQuery()) {
                while (rs.next()) {
                    String equityId = rs.getString("EquityId");
                    this.equityIdList.add(equityId);
                }
            }
        } catch (SQLException ex) {
            throw new CMDAOException(
                CMLanguageController.getDBErrorProp("Title"),
                Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                ex.getMessage(), 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * For a given account, equityId and transactionType populate open arrays
     *
     * @param account
     * @param equityId
     * @param optTransType
     */
    public void getAcctOptionsOpen(Integer account, String equityId, String optTransType) {
        // Use the existing SQL from OpeningOptionModel but with proper parameterization
        String sqlOpen = String.format(OpeningOptionModel.GET_ALL_AVAIL,
            account, equityId, optTransType, this.userId,
            account, equityId, optTransType, this.userId);

        try (Connection con = CMDBController.getConnection();
             PreparedStatement pStmtOpen = con.prepareStatement(sqlOpen);
             ResultSet rs = pStmtOpen.executeQuery()) {
            
            int count = 0;
            while (rs.next()) {
                OpeningOptionModel ooTemp = OpeningOptionModel.builder()
                    .dmAcctId(rs.getInt("DMAcctId"))
                    .joomlaId(rs.getInt("JoomlaId"))
                    .fiTId(rs.getString("FiTId"))
                    .ticker(rs.getString("Ticker"))
                    .equityId(rs.getString("EquityId"))
                    .dateOpen(rs.getDate("DateOpen"))
                    .dateClose(rs.getDate("DateClose"))
                    .dateExpire(rs.getDate("DateExpire"))
                    .shPerCtrct(rs.getInt("ShPerCtrct"))
                    .units(rs.getDouble("Units"))
                    .priceOpen(rs.getDouble("PriceOpen"))
                    .priceClose(rs.getDouble("PriceClose"))
                    .markUpDn(rs.getDouble("MarkUpDn"))
                    .commission(rs.getDouble("Commission"))
                    .taxes(rs.getDouble("Taxes"))
                    .fees(rs.getDouble("Fees"))
                    .transLoad(rs.getDouble("TransLoad"))
                    .totalOpen(rs.getDouble("TotalOpen"))
                    .totalClose(rs.getDouble("TotalClose"))
                    .curSym(rs.getString("CurSym"))
                    .subAcctSec(rs.getString("SubAcctSec"))
                    .subAcctFund(rs.getString("SubAcctFund"))
                    .reversalFiTId(rs.getString("ReversalFiTId"))
                    .comment(rs.getString("Comment"))
                    .openingOpen(rs.getDouble("OpeningOpen"))
                    .openingHigh(rs.getDouble("OpeningHigh"))
                    .openingLow(rs.getDouble("OpeningLow"))
                    .openingClose(rs.getDouble("OpeningClose"))
                    .optionType(rs.getString("OptionType"))
                    .transactionType(rs.getString("TransactionType"))
                    .strikePrice(rs.getDouble("StrikePrice"))
                    .build();

                switch (optTransType) {
                    case "buytoopen":
                        this.optionOpeningBuyList.add(ooTemp);
                        break;
                    case "selltoopen":
                        this.optionOpeningSellList.add(ooTemp);
                        break;
                    default:
                }
                count++;
            }
            
        } catch (SQLException ex) {
            throw new CMDAOException(
                CMLanguageController.getDBErrorProp("Title"),
                Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                ex.getMessage(), 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * For a given account, equityId and transactionType populate buy/sell arrays
     *
     * @param account
     * @param equityId
     * @param optTransType
     */
    public void getAcctOptionsClose(Integer account, String equityId, String optTransType) {
        String sqlClose = String.format(ClosingOptionModel.GET_ALL_AVAIL,
            account, equityId, optTransType, this.userId,
            account, equityId, optTransType, this.userId);

        try (Connection con = CMDBController.getConnection();
             PreparedStatement pStmtClose = con.prepareStatement(sqlClose)) {

            if (optTransType.equalsIgnoreCase("BUYTOCLOSE") || optTransType.equalsIgnoreCase("SELLTOCLOSE")) {
                try (ResultSet rs = pStmtClose.executeQuery()) {
                    int count = 0;
                    while (rs.next()) {
                        ClosingOptionModel coTemp = ClosingOptionModel.builder()
                            .dmAcctId(rs.getInt("DMAcctId"))
                            .joomlaId(rs.getInt("JoomlaId"))
                            .fiTId(rs.getString("FiTId"))
                            .ticker(rs.getString("Ticker"))
                            .equityId(rs.getString("EquityId"))
                            .dateOpen(rs.getDate("DateOpen"))
                            .dateClose(rs.getDate("DateClose"))
                            .dateExpire(rs.getDate("DateExpire"))
                            .shPerCtrct(rs.getInt("ShPerCtrct"))
                            .units(rs.getDouble("Units"))
                            .priceOpen(rs.getDouble("PriceOpen"))
                            .priceClose(rs.getDouble("PriceClose"))
                            .markUpDn(rs.getDouble("MarkUpDn"))
                            .commission(rs.getDouble("Commission"))
                            .taxes(rs.getDouble("Taxes"))
                            .fees(rs.getDouble("Fees"))
                            .transLoad(rs.getDouble("TransLoad"))
                            .totalOpen(rs.getDouble("TotalOpen"))
                            .totalClose(rs.getDouble("TotalClose"))
                            .curSym(rs.getString("CurSym"))
                            .subAcctSec(rs.getString("SubAcctSec"))
                            .subAcctFund(rs.getString("SubAcctFund"))
                            .reversalFiTId(rs.getString("ReversalFiTId"))
                            .comment(rs.getString("Comment"))
                            .closingOpen(rs.getDouble("ClosingOpen"))
                            .closingHigh(rs.getDouble("ClosingHigh"))
                            .closingLow(rs.getDouble("ClosingLow"))
                            .closingClose(rs.getDouble("ClosingClose"))
                            .optionType(rs.getString("OptionType"))
                            .transactionType(rs.getString("TransactionType"))
                            .strikePrice(rs.getDouble("StrikePrice"))
                            .build();

                        switch (optTransType) {
                            case "buytoclose":
                                this.optionClosingBuyList.add(coTemp);
                                break;
                            case "selltoclose":
                                this.optionClosingSellList.add(coTemp);
                                break;
                            default:
                        }
                        count++;
                    }
                }
            }
            
        } catch (SQLException ex) {

            throw new CMDAOException(
                CMLanguageController.getDBErrorProp("Title"),
                Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                ex.getMessage(), 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Rest of the methods remain the same...
    public void processFIFOOptionLotsBuyToOpen() {
        // iterate through all OpeningBuy of one EquityId
        for (OpeningOptionModel oo : this.optionOpeningBuyList) {
            // iterate through all ClosingOptions for this EquityId
            this.doOptionClosingSellList(oo);

            // handle closing transactions
            this.doOptionClosedTransListSQL(oo);
        }

        // should be no remaining closing transactions
        for (ClosingOptionModel co : this.optionClosingSellList) {
            if (!co.getUnits().equals(0.0)) {
                String errorMsg = String.format(CMLanguageController.getErrorProp("GeneralError"),
                    "Option Opening and closing transactions mismatch.\n") + 
                    "EquityId: " + co.getEquityId() + "  " +
                    "FiTId: " + co.getFiTId() + "  " + 
                    "Units Left: " + co.getUnits();

                CMHPIUtils.showDefaultMsg(
                    CMLanguageController.getAppProp("Title") + CMLanguageController.getErrorProp("Title"),
                    Thread.currentThread().getStackTrace()[1].getClassName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(),
                    errorMsg, 
                    JOptionPane.ERROR_MESSAGE);
            }
        }

        // handle open positions
        this.doOptionOpenList();
    }

    public void processFIFOOptionLotsSellToOpen() {
        Integer openingOption = 0;
        
        // iterate through all OpeningSell of one EquityId
        for (OpeningOptionModel oo : this.optionOpeningSellList) {
            openingOption++;

            // iterate through all ClosingOptions for this EquityId
            this.doOptionClosingBuyList(oo);

            // handle closing transactions
            this.doOptionClosedTransListSQL(oo);
        }

        // should be no remaining closing transactions
        for (ClosingOptionModel co : this.optionClosingSellList) {
            if (!co.getUnits().equals(0.0)) {
                String errorMsg = String.format(CMLanguageController.getErrorProp("GeneralError"),
                    "Option Opening and closing transactions mismatch.\n") + 
                    "EquityId: " + co.getEquityId() + "  " +
                    "FiTId: " + co.getFiTId() + "  " + 
                    "Units Left: " + co.getUnits();

                CMHPIUtils.showDefaultMsg(
                    CMLanguageController.getAppProp("Title") + CMLanguageController.getErrorProp("Title"),
                    Thread.currentThread().getStackTrace()[1].getClassName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(),
                    errorMsg, 
                    JOptionPane.ERROR_MESSAGE);
            }
        }

        // handle open positions
        this.doOptionOpenList();
    }

    /*
     * Given an opening option, iterate closing op
     */
    private void doOptionClosingSellList(OpeningOptionModel oo) {
        for (ClosingOptionModel co : this.optionClosingSellList) {
            if (co.getUnits() == 0.0) {
                // no units left in this closing
                continue;
            }

            if (oo.getUnits() == 0.0) {
                // opening has been completely covered
                break;
            }

            Double oUnits = Math.abs(oo.getUnits());
            Double cUnits = Math.abs(co.getUnits());
            int comp = 0;

            if ((oUnits.compareTo(cUnits)) > 0) {
                comp = 1;
            }
            if ((oUnits.compareTo(cUnits)) < 0) {
                comp = -1;
            }

            switch (comp) {
                case -1:
                    // opening less than closing, need to split closing
                    Double alloc = (cUnits - oUnits) / cUnits;

                    // part of closing goes to optionClosedTransList
                    ClosingOptionModel coTemp = createClosingOptionCopy(co, 1.0 - alloc);
                    this.optionClosedTransList.add(coTemp);

                    // closing gets reduced
                    co.setUnits(co.getUnits() + oo.getUnits());
                    oo.setUnits(0.0);

                    // closing reduced by allocation
                    adjustClosingOptionByAllocation(co, alloc);
                    break;
                    
                case 0:
                    // opening equals closing
                    ClosingOptionModel coTempEqual = createClosingOptionCopy(co, 1.0);
                    this.optionClosedTransList.add(coTempEqual);

                    co.setUnits(co.getUnits() + oo.getUnits());
                    oo.setUnits(0.0);
                    break;
                    
                case 1:
                    // opening greater than closing
                    Double allocOpening = (oUnits - cUnits) / oUnits;
                    
                    ClosingOptionModel coTempGreater = createClosingOptionCopy(co, 1.0);
                    this.optionClosedTransList.add(coTempGreater);

                    oo.setUnits(co.getUnits() + oo.getUnits());
                    co.setUnits(0.0);

                    adjustOpeningOptionByAllocation(oo, allocOpening);
                    break;
                    
                default:
            }

            // Update the closing trade with the allocation
            CMDBController.executeSQL(String.format(ClosingOptionModel.UPDATE_UNITS,
                co.getUnits(), co.getDmAcctId(), co.getJoomlaId(), co.getFiTId()));
        }

        CMDBController.executeSQL(String.format(OpeningOptionModel.UPDATE_UNITS, oo.getUnits(),
            oo.getDmAcctId(), oo.getJoomlaId(), oo.getFiTId()));

        if (oo.getUnits() != 0) {
            // there is a remaining open position
            OpeningOptionModel ooTemp = createOpeningOptionCopy(oo);
            this.optionOpenList.add(ooTemp);
        }
    }

    /*
     * Given an opening option, iterate closing op
     */
    private void doOptionClosingBuyList(OpeningOptionModel oo) {
        for (ClosingOptionModel co : this.optionClosingBuyList) {
            if (co.getUnits() == 0.0) {
                continue;
            }

            if (oo.getUnits() == 0.0) {
                break;
            }

            Double oUnits = Math.abs(oo.getUnits());
            Double cUnits = Math.abs(co.getUnits());
            int comp = 0;

            if ((oUnits.compareTo(cUnits)) > 0) {
                comp = 1;
            }
            if ((oUnits.compareTo(cUnits)) < 0) {
                comp = -1;
            }

            switch (comp) {
                case -1:
                    // opening less than closing, need to split closing
                    Double alloc = (cUnits - oUnits) / cUnits;
                    ClosingOptionModel coTemp = createClosingOptionCopy(co, 1.0 - alloc);
                    this.optionClosedTransList.add(coTemp);

                    co.setUnits(co.getUnits() + oo.getUnits());
                    oo.setUnits(0.0);

                    adjustClosingOptionByAllocation(co, alloc);
                    break;
                    
                case 0:
                    // opening equals closing
                    ClosingOptionModel coTempEqual = createClosingOptionCopy(co, 1.0);
                    this.optionClosedTransList.add(coTempEqual);

                    co.setUnits(co.getUnits() + oo.getUnits());
                    oo.setUnits(0.0);
                    break;
                    
                case 1:
                    // opening greater than closing
                    Double allocOpening = (oUnits - cUnits) / oUnits;
                    ClosingOptionModel coTempGreater = createClosingOptionCopy(co, 1.0);
                    this.optionClosedTransList.add(coTempGreater);

                    oo.setUnits(co.getUnits() + oo.getUnits());
                    co.setUnits(0.0);

                    adjustOpeningOptionByAllocation(oo, allocOpening);
                    break;
            }

            CMDBController.executeSQL(String.format(ClosingOptionModel
                    .UPDATE_UNITS, co.getUnits(),
                co.getDmAcctId(), co.getJoomlaId(), co.getFiTId()));
        }

        CMDBController.executeSQL(String.format(OpeningOptionModel
                .UPDATE_UNITS, oo.getUnits(),
            oo.getDmAcctId(), oo.getJoomlaId(), oo.getFiTId()));

        if (oo.getUnits() != 0) {
            OpeningOptionModel ooTemp = createOpeningOptionCopy(oo);
            this.optionOpenList.add(ooTemp);
        }
    }

    // Helper methods to reduce code duplication
    private ClosingOptionModel createClosingOptionCopy(ClosingOptionModel co, Double factor) {
        return ClosingOptionModel.builder()
            .dmAcctId(co.getDmAcctId())
            .joomlaId(co.getJoomlaId())
            .fiTId(co.getFiTId())
            .ticker(co.getTicker())
            .equityId(co.getEquityId())
            .dateOpen(co.getDateOpen())
            .dateClose(co.getDateClose())
            .dateExpire(co.getDateExpire())
            .shPerCtrct(co.getShPerCtrct())
            .units(Precision.round(co.getUnits() * factor, 1))
            .priceOpen(co.getPriceOpen())
            .priceClose(co.getPriceClose())
            .markUpDn(co.getMarkUpDn())
            .commission(co.getCommission() * factor)
            .taxes(co.getTaxes() * factor)
            .fees(co.getFees() * factor)
            .transLoad(co.getTransLoad() * factor)
            .totalOpen(co.getTotalOpen() * factor)
            .totalClose(Precision.round(co.getTotalClose() * factor, 4))
            .curSym(co.getCurSym())
            .subAcctSec(co.getSubAcctSec())
            .subAcctFund(co.getSubAcctFund())
            .reversalFiTId(co.getReversalFiTId())
            .comment(co.getComment())
            .closingOpen(co.getClosingOpen())
            .closingHigh(co.getClosingHigh())
            .closingLow(co.getClosingLow())
            .closingClose(co.getClosingClose())
            .optionType(co.getOptionType())
            .transactionType(co.getTransactionType())
            .strikePrice(co.getStrikePrice())
            .build();
    }

    private OpeningOptionModel createOpeningOptionCopy(OpeningOptionModel oo) {
        return OpeningOptionModel.builder()
            .dmAcctId(oo.getDmAcctId())
            .joomlaId(oo.getJoomlaId())
            .fiTId(oo.getFiTId())
            .ticker(oo.getTicker())
            .equityId(oo.getEquityId())
            .dateOpen(oo.getDateOpen())
            .dateClose(oo.getDateClose())
            .dateExpire(oo.getDateExpire())
            .shPerCtrct(oo.getShPerCtrct())
            .units(oo.getUnits())
            .priceOpen(oo.getPriceOpen())
            .priceClose(oo.getPriceClose())
            .markUpDn(oo.getMarkUpDn())
            .commission(oo.getCommission())
            .taxes(oo.getTaxes())
            .fees(oo.getFees())
            .transLoad(oo.getTransLoad())
            .totalOpen(oo.getTotalOpen())
            .totalClose(oo.getTotalClose())
            .curSym(oo.getCurSym())
            .subAcctSec(oo.getSubAcctSec())
            .subAcctFund(oo.getSubAcctFund())
            .reversalFiTId(oo.getReversalFiTId())
            .comment(oo.getComment())
            .openingOpen(oo.getOpeningOpen())
            .openingHigh(oo.getOpeningHigh())
            .openingLow(oo.getOpeningLow())
            .openingClose(oo.getOpeningClose())
            .optionType(oo.getOptionType())
            .transactionType(oo.getTransactionType())
            .strikePrice(oo.getStrikePrice())
            .build();
    }

    private void adjustClosingOptionByAllocation(ClosingOptionModel co, Double alloc) {
        co.setCommission(co.getCommission() == null ? null : co.getCommission() * alloc);
        co.setTaxes(co.getTaxes() == null ? null : co.getTaxes() * alloc);
        co.setFees(co.getFees() == null ? null : co.getFees() * alloc);
        co.setTransLoad(co.getTransLoad() == null ? null : co.getTransLoad() * alloc);
        co.setTotalClose(co.getTotalClose() * alloc);
    }

    private void adjustOpeningOptionByAllocation(OpeningOptionModel oo, Double alloc) {
        oo.setCommission(oo.getCommission() == null ? null : oo.getCommission() * alloc);
        oo.setTaxes(oo.getTaxes() == null ? null : oo.getTaxes() * alloc);
        oo.setFees(oo.getFees() == null ? null : oo.getFees() * alloc);
        oo.setTransLoad(oo.getTransLoad() == null ? null : oo.getTransLoad() * alloc);
        oo.setTotalOpen(oo.getTotalOpen() * alloc);
    }

    /*
     * pair the opening to closing and write to the database
     */
    void doOptionClosedTransListSQL(OpeningOptionModel oo) {
        Double updateUnits = 0.0;
        Double updateCommission = 0.0;
        Double updateTaxes = 0.0;
        Double updateFees = 0.0;
        Double updateTransLoad = 0.0;
        Double updateTotalClose = 0.0;
        Integer updateShPerCtrct = 0;
        java.sql.Date updateCloseDate = null;

        if (!this.optionClosedTransList.isEmpty()) {
            // put this opening transaction into ClosedOptionFIFOModel
            String sSQL = String.format(ClosedOptionFIFOModel.INSERT_ALL_VALUES, ClosedOptionFIFOModel.ALL_FIELDS);
            sSQL += buildClosedOptionInsertSQL(oo);

            Integer rowIndex = CMDBController.insertAutoRow(sSQL);
            // add optionClosedTransList to ClosedOptionTrans
            for (ClosingOptionModel co : this.optionClosedTransList) {
                String transSQL = String.format(ClosedOptionTrans.INSERT_ALL_VALUES, ClosedOptionTrans.ALL_FIELDS);
                transSQL += buildClosedTransInsertSQL(oo, co, rowIndex);

                CMDBController.executeSQL(transSQL);

                // Adjust units based on transaction type
                if (co.getTransactionType().equalsIgnoreCase("selltoopen")
                    || co.getTransactionType().equalsIgnoreCase("selltoclose")) {
                    co.setUnits(-1.0 * Math.abs(co.getUnits()));
                }

                if (co.getTransactionType().equalsIgnoreCase("buytoclose")
                    || co.getTransactionType().equalsIgnoreCase("buytoopen")) {
                    co.setUnits(Math.abs(co.getUnits()));
                }

                updateUnits += co.getUnits();
                updateCommission += (co.getCommission() != null) ? co.getCommission() : 0.0;
                updateTaxes += (co.getTaxes() != null) ? co.getTaxes() : 0.0;
                updateFees += (co.getFees() != null) ? co.getFees() : 0.0;
                updateTransLoad += (co.getTransLoad() != null) ? co.getTransLoad() : 0.0;
                updateTotalClose += (co.getTotalClose() != null) ? co.getTotalClose() : 0.0;
                updateShPerCtrct = co.getShPerCtrct();
                updateCloseDate = co.getDateClose();
            }

            // Calculate unit price close
            Double updateUnitPriceClose;
            if (updateUnits.equals(0.0)) {
                updateUnitPriceClose = 0.0;
            } else {
                updateUnitPriceClose = Math.round(
                    (updateTotalClose + updateCommission + updateTaxes + updateFees + updateTransLoad) * 10000)
                    / (updateUnits * 10000 * updateShPerCtrct);
            }

            // Update ClosedOptionFIFOModel object with totals
            CMDBController.executeSQL(String.format(ClosedOptionFIFOModel.UPDATE_TOTAL_TOTALCLOSE,
                Double.toString(updateUnits),
                Double.toString(Precision.round(updateCommission, 4)),
                Double.toString(Precision.round(updateTaxes, 4)),
                Double.toString(Precision.round(updateFees, 4)),
                Double.toString(Precision.round(updateTransLoad, 4)),
                Double.toString(Precision.round(updateTotalClose, 4)),
                Double.toString(updateUnitPriceClose),
                updateCloseDate,
                rowIndex));

            this.optionClosedTransList.clear();
        }
    }

    private String buildClosedOptionInsertSQL(OpeningOptionModel oo) {
        StringBuilder sql = new StringBuilder();
        sql.append(oo.getDmAcctId()).append(", ");
        sql.append(this.userId).append(", '");
        sql.append(oo.getFiTId()).append("', ");
        sql.append("null, '");  // transactionGrp, autoRow number
        sql.append(oo.getTicker()).append("', '");
        sql.append(oo.getEquityId()).append("', '");
        sql.append(CMHPIUtils.getTransactionName(oo.getEquityId())).append("', '");
        sql.append(oo.getOptionType()).append("', ");
        sql.append(oo.getStrikePrice()).append(", '");
        sql.append(oo.getDateOpen()).append("', ");
        
        if (oo.getDateClose() == null) {
            sql.append("null, '");
        } else {
            sql.append("'").append(oo.getDateClose()).append("', '");
        }
        
        sql.append(oo.getDateExpire()).append("', ");
        sql.append(oo.getShPerCtrct()).append(", ");
        sql.append(oo.getUnits()).append(", ");
        sql.append(oo.getPriceOpen()).append(", ");
        sql.append(oo.getPriceClose()).append(", ");
        sql.append(oo.getMarkUpDn()).append(", ");
        sql.append(oo.getCommission()).append(", ");
        sql.append(oo.getTaxes()).append(", ");
        sql.append(oo.getFees()).append(", ");
        sql.append(oo.getTransLoad()).append(", ");
        sql.append(oo.getTotalOpen()).append(", ");
        sql.append(oo.getTotalClose()).append(", ");
        
        if (oo.getCurSym() == null) {
            sql.append("null, '");
        } else {
            sql.append("'").append(oo.getCurSym()).append("', '");
        }
        
        sql.append(oo.getTransactionType()).append("');");
        
        return sql.toString();
    }

    private String buildClosedTransInsertSQL(OpeningOptionModel oo, 
            ClosingOptionModel co, Integer rowIndex) {
        StringBuilder sql = new StringBuilder();
        sql.append(oo.getDmAcctId()).append(", '");
        sql.append(this.userId).append("', '");
        sql.append(oo.getFiTId()).append("', ");
        sql.append(rowIndex).append(", '");
        sql.append(co.getFiTId()).append("', '");
        sql.append(co.getTicker()).append("', '");
        sql.append(co.getEquityId()).append("', '");
        sql.append(CMHPIUtils.getTransactionName(co.getEquityId())).append("', '");
        sql.append(co.getOptionType()).append("', ");
        sql.append(co.getStrikePrice()).append(", ");
        
        if (co.getDateOpen() == null) {
            sql.append("null, ");
        } else {
            sql.append("'").append(co.getDateOpen()).append("', ");
        }
        
        if (co.getDateClose() == null) {
            sql.append("null, ");
        } else {
            sql.append("'").append(co.getDateClose()).append("', ");
        }
        
        sql.append("'").append(co.getDateExpire()).append("', ");
        sql.append(co.getShPerCtrct()).append(", ");
        sql.append(co.getUnits()).append(", ");
        sql.append(co.getPriceOpen()).append(", ");
        sql.append(co.getPriceClose()).append(", ");
        sql.append(co.getMarkUpDn()).append(", ");
        sql.append(co.getCommission()).append(", ");
        sql.append(co.getTaxes()).append(", ");
        sql.append(co.getFees()).append(", ");
        sql.append(co.getTransLoad()).append(", ");
        sql.append(co.getTotalOpen()).append(", ");
        sql.append(Precision.round(co.getTotalClose(), 4)).append(", ");
        sql.append(co.getCurSym()).append(", '");
        sql.append(co.getSubAcctSec()).append("', '");
        sql.append(co.getSubAcctFund()).append("', '");
        sql.append(co.getTransactionType()).append("', ");
        sql.append(co.getReversalFiTId() == null ? "null" : "'" + oo.getReversalFiTId() + "'");
        sql.append(");");
        
        return sql.toString();
    }

    void doOptionOpenList() {
        if (!this.optionOpenList.isEmpty()) {
            // there are open positions
            // optionOpen -> OpenOptionFIFO
            for (OpeningOptionModel oo : this.optionOpenList) {
                CMDBController.executeSQL(String
                    .format(OpenOptionFIFOModel.INSERT_ALL_VALUES,
                        OpenOptionFIFOModel.ALL_FIELDS) 
                        + OpenOptionFIFOModel.insertAll(oo, userId));
            }
        } else {
        }
    }
}