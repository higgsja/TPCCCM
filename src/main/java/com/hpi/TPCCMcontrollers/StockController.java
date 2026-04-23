package com.hpi.TPCCMcontrollers;

import com.hpi.hpiUtils.CMHPIUtils;
import com.hpi.TPCCMsql.CMDAOException;
import com.hpi.TPCCMprefs.CMDBModel;
import com.hpi.entities.*;
import com.hpi.hpiUtils.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import lombok.*;

public class StockController
{

    @Getter private final ArrayList<Account> accountList;
    @Getter private final ArrayList<String> equityIdList;
    @Getter private final ArrayList<OpeningStockModel> stockOpeningList;
    @Getter private final ArrayList<ClosingStockModel> stockClosingList;
    @Getter private final ArrayList<ClosingStockModel> stockClosedList;
    @Getter private final ArrayList<ClosingStockModel> stockClosedTransList;
    @Getter private final ArrayList<OpeningStockModel> stockOpenList;
    @Setter private Integer userId;

    private final CMProgressBarCLI progressBarCLI;

    //*** Singleton
    private static StockController instance;

    static
    {

    }

    protected StockController()
    {
        // protected prevents instantiation outside of package
        this.userId = CMDBModel.getUserId();

        //force a progress bar
        this.progressBarCLI = new CMProgressBarCLI("true");

        this.accountList = new ArrayList<>();
        this.equityIdList = new ArrayList<>();
        this.stockOpeningList = new ArrayList<>();
        this.stockClosingList = new ArrayList<>();
        this.stockClosedList = new ArrayList<>();
        this.stockClosedTransList = new ArrayList<>();
        this.stockOpenList = new ArrayList<>();
    }

    public synchronized static StockController getInstance()
    {
        if (StockController.instance == null)
        {
            StockController.instance
                = new StockController();
        }
        return StockController.instance;
    }
    //***

    void initCustom()
    {

    }

    /*
     * loop through accounts; get equities; fill stockOpeningList/stockClosingList
     */
    public void processFIFOStockLotsAccounts()
    {
        this.accountList.clear();
        this.equityIdList.clear();
        this.stockOpeningList.clear();
        this.stockClosingList.clear();
        this.stockClosedList.clear();
        this.stockClosedTransList.clear();
        this.stockOpenList.clear();

        // query Accounts for list of DMAcctId
        this.getAccounts();

        // Iterate through the accounts
        for (Account account : this.accountList)
        {
            System.out.println("      Processing account: " + account.getClientAcctName());

            // get unique list of equityId from OpeningStockModel
            this.getDistinctEquityId(account.getDmAcctId());

            // iterate through equityId
            for (String equityId : this.equityIdList)
            {
                // get opening list
                getAcctOpeningStock(account.getDmAcctId(), equityId);

                // get closing list
                getAcctClosingStock(account.getDmAcctId(), equityId);

                processFIFOStockLots();

                // clear the arrays
                this.stockOpeningList.clear();
                this.stockClosingList.clear();
                this.stockClosedList.clear();
                this.stockClosedTransList.clear();
                this.stockOpenList.clear();
            }
        }
    }

    void getAccounts()
    {
        //tested
        String sql;
        ResultSet rs;
        Account accountTemp;

        sql
            = "select Accounts.DMAcctId, BrokerId, AcctId, Org, FId, InvAcctIdFi, ClientAcctName from Accounts, ClientAccts where Accounts.DMAcctId = ClientAccts.DMAcctId and ClientAccts.Active = 'Yes' and ClientAccts.JoomlaId = '%s';";

        sql = String.format(sql,
            this.userId);

        try (Connection con = CMDBController.getConnection();
            PreparedStatement pStmt = con.prepareStatement(sql))
        {
            con.clearWarnings();
            rs = pStmt.executeQuery();

            while (rs.next())
            {
                accountTemp = new Account(
                    rs.getInt("DMAcctId"),
                    rs.getInt("BrokerId"),
                    rs.getInt("AcctId"),
                    rs.getString("Org"),
                    rs.getString("FId"),
                    rs.getString("InvAcctIdFi"),
                    rs.getString("ClientAcctName"));

                this.accountList.add(accountTemp);
            }
            rs.close();
        } catch (SQLException ex)
        {
            throw new CMDAOException(CMLanguageController.
                getDBErrorProp("Title"),
                Thread.currentThread()
                    .getStackTrace()[1].getClassName(),
                Thread.currentThread()
                    .getStackTrace()[1].getMethodName(),
                ex.getMessage(),
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * In equityIdList create a list of unique IDs from opening, closing, clientOpening and clientClosing
     * stock tables
     *
     * @param account: AcctId to query
     */
    public void getDistinctEquityId(Integer account)
    {
        //tested
        ResultSet rs;
        String sql;

        sql
            = "select distinct A.EquityId from (select EquityId from hlhtxc5_dmOfx.OpeningStock where DMAcctId = '%s' and JoomlaId = '%s' and not(Units = 0) union select EquityId from hlhtxc5_dmOfx.ClientOpeningStock where DMAcctId = '%s' and JoomlaId = '%s' and not(Units = 0) union select EquityId from hlhtxc5_dmOfx.ClosingStock where DMAcctId = '%s' and JoomlaId = '%s' and not(Units = 0) union select EquityId from hlhtxc5_dmOfx.ClientClosingStock where DMAcctId = '%s' and JoomlaId = '%s' and not(Units = 0)) as A order by EquityId;";

        sql = String.format(sql,
            account,
            this.userId,
            account,
            this.userId,
            account,
            this.userId,
            account,
            this.userId);

        try (Connection con = CMDBController.getConnection();
            PreparedStatement pStmt = con.prepareStatement(sql))
        {

            con.clearWarnings();
            rs = pStmt.executeQuery();

            while (rs.next())
            {
                this.equityIdList.add(rs.getString("EquityId"));
            }

            rs.close();
        } catch (SQLException ex)
        {
            throw new CMDAOException(CMLanguageController.
                getDBErrorProp("Title"),
                Thread.currentThread()
                    .getStackTrace()[1].getClassName(),
                Thread.currentThread()
                    .getStackTrace()[1].getMethodName(),
                ex.getMessage(),
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /*
     * Add to stockOpeningList from OpeningStockModel where units are not 0;
     * stockOpeningList is then the list of stocks that have not been handled yet
     */
    private void getAcctOpeningStock(Integer account,
        String equityId)
    {
        OpeningStockModel osTemp;

////        sql =
////            "select * from hlhtxc5_dmOfx.OpeningStock where DMAcctId = '%s' and EquityId = '%s' and JoomlaId = '%s' and Units <> 0 union select %s from hlhtxc5_dmOfx.clientOpeningStock where DMAcctId = '%s' and EquityId = '%s' and JoomlaId = '%s' and Units <> 0 order by EquityId, GMTDtTrade, FiTId;";
//
//        sql = String.format(sql,
//            account, equityId, this.userId,
//            ClientOpeningStockModel.MATCH_OPENING_STOCK, account, equityId,
//            this.userId);
        try (Connection con = CMDBController.getConnection();
            PreparedStatement pStmt = con.prepareStatement(
                String.format(OpeningStockModel.GET_OPENING_STOCK_BY_ACCT,
                    account,
                    equityId,
                    this.userId,
                    account,
                    equityId,
                    this.userId));
            ResultSet rs = pStmt.executeQuery())
        {

            while (rs.next())
            {

                osTemp = OpeningStockModel.builder()
                    .dmAcctId(rs.getInt("DMAcctId"))
                    .joomlaId(rs.getInt("JoomlaId"))
                    .fiTId(rs.getString("FiTId"))
                    .ticker(rs.getString("Ticker"))
                    .equityId(rs.getString("EquityId"))
                    .dateOpen(rs.getDate("DateOpen"))
                    .dateClose(rs.getDate("DateClose"))
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
                    .equityType(rs.getString("EquityType"))
                    .optionType(rs.getString("OptionType"))
                    .transactionType(rs.getString("TransactionType"))
                    .reversalFiTId(rs.getString("ReversalFiTId"))
                    .comment(rs.getString("Comment"))
                    .build();

                this.stockOpeningList.add(osTemp);
            }

//            rs.close();
        } catch (SQLException ex)
        {
            throw new CMDAOException(CMLanguageController.
                getDBErrorProp("Title"),
                Thread.currentThread()
                    .getStackTrace()[1].getClassName(),
                Thread.currentThread()
                    .getStackTrace()[1].getMethodName(),
                ex.getMessage(),
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void getAcctClosingStock(Integer account,
        String equityId)
    {
        ClosingStockModel csTemp;

//        sql =
//            "select * from hlhtxc5_dmOfx.ClosingStock where DMAcctId = '%s' and EquityId = '%s' and JoomlaId = '%s' and not(Units = 0) union select %s from hlhtxc5_dmOfx.clientClosingStock where DMAcctId = '%s' and EquityId = '%s' and JoomlaId = '%s' and not(Units = 0) order by EquityId, GMTDtTrade, FiTId;";
//        sql = String.format(sql,
//            account, equityId, this.userId,
//            ClientClosingStockModel.MATCH_CLOSING_STOCK, account, equityId,
//            this.userId);
        try (Connection con = CMDBController.getConnection();
            PreparedStatement pStmt = con.prepareStatement(
                String.format(ClosingStockModel.GET_CLOSING_STOCK_BY_ACCT,
                    account,
                    equityId,
                    this.userId,
                    account,
                    equityId,
                    this.userId));
            ResultSet rs = pStmt.executeQuery();)
        {

            while (rs.next())
            {
                csTemp = ClosingStockModel.builder()
                    .dmAcctId(rs.getInt("DMAcctId"))
                    .joomlaId(rs.getInt("JoomlaId"))
                    .fiTId(rs.getString("FiTId"))
                    .ticker(rs.getString("Ticker"))
                    .equityId(rs.getString("EquityId"))
                    .dateOpen(rs.getDate("DateOpen"))
                    .dateClose(rs.getDate("DateClose"))
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
                    .equityType(rs.getString("EquityType"))
                    .optionType(rs.getString("OptionType"))
                    .transactionType(rs.getString("TransactionType"))
                    .reversalFiTId(rs.getString("ReversalFiTId"))
                    .comment(rs.getString("Comment"))
                    .build();

                this.stockClosingList.add(csTemp);
            }

            rs.close();
        } catch (SQLException ex)
        {
            throw new CMDAOException(CMLanguageController.
                getDBErrorProp("Title"),
                Thread.currentThread()
                    .getStackTrace()[1].getClassName(),
                Thread.currentThread()
                    .getStackTrace()[1].getMethodName(),
                ex.getMessage(),
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void processFIFOStockLots()
    {
        String s;
        Integer openingStock;

        // iterate through all OpeningStockModel of one EquityId
        openingStock = 0;
        //todo: do not like using labeled goto
        openingStock:
        for (OpeningStockModel os : this.stockOpeningList)
        {
//            this.progressBarCLI.barUpdate(openingStock,
//                this.stockOpeningList.size(), os.getEquityId());
            openingStock++;

            // iterate through all ClosingStockModel for this EquityId
            this.doStockClosingList(os);

            // handle closing transactions
            this.doStockClosedTransListSQL(os);
        }//for (OpeningStockModel os : this.stockOpeningList)

        // should be no remaining closing transactions
        for (ClosingStockModel cs : this.stockClosingList)
        {
            if (!cs.getUnits()
                .equals(0.0))
            {
                s = String.format(CMLanguageController.getErrorProp(
                    "GeneralError"),
                    "Stock Opening and closing transactions mismatch.\n") + cs.getEquityId() + "\n" + cs
                    .getFiTId();

                CMHPIUtils.showDefaultMsg(
                    CMLanguageController.getAppProp("Title") + CMLanguageController.getErrorProp("Title"),
                    Thread.currentThread()
                        .getStackTrace()[1].
                        getClassName(),
                    Thread.currentThread()
                        .getStackTrace()[1].
                        getMethodName(),
                    s,
                    JOptionPane.ERROR_MESSAGE);
            }
        }

        // handle open positions
        this.doStockOpenListSQL();
    }

    /*
     * Adjust ClosingStockModel and OpeningStockModel units by units used to close any open lots
     */
    private void doStockClosingList(OpeningStockModel os)
    {
        int comp;
        Double oUnits;
        Double cUnits;
        Double alloc;
        OpeningStockModel osTemp;
        ClosingStockModel csTemp;
        String sql;

//        closingStock:
        for (ClosingStockModel cs : this.stockClosingList)
        {

            if (cs.getUnits() == 0)
            {
                // no units left in this closing
                continue;
            }

            if (os.getUnits() == 0.0)
            {
                // opening has been completely covered
                break;
            }

            // type of openingStock matters
            switch (StockTransactionTypeEnum.valueOf(os.getTransactionType()))
            {
                case BUY:
                    // only look at sell
                    if (StockTransactionTypeEnum.valueOf(cs.getTransactionType())
                        != StockTransactionTypeEnum.SELL)
                    {
                        continue;
                    }
                    break;

                case SELLSHORT:
                    // only look at buytocover
                    if (StockTransactionTypeEnum.valueOf(cs.getTransactionType())
                        != StockTransactionTypeEnum.BUYTOCOVER)
                    {
                        continue;
                    }
                    break;
                default:
                    int i = 0;
            }

            // short opening positions are negative units
            // closing units are negative
            oUnits = Math.abs(os.getUnits());
            cUnits = Math.abs(cs.getUnits());
            comp = 0;

            if ((oUnits.compareTo(cUnits)) > 0)
            {
                comp = 1;
            }

            if ((oUnits.compareTo(cUnits)) < 0)
            {
                comp = -1;
            }

            switch (comp)
            {
                case -1:
                    // opening less than closing, need to split closing
                    // alloc is what remains in closing
                    // 1.0 - alloc goes to opening
                    alloc = (cUnits - oUnits) / cUnits;

                    // part of closing goes to stockClosedTransList
                    csTemp = ClosingStockModel.builder()
                        .dmAcctId(cs.getDmAcctId())
                        .joomlaId(cs.getJoomlaId())
                        .fiTId(cs.getFiTId())
                        .equityId(cs.getEquityId())
                        .ticker(cs.getTicker())
                        .dateOpen(cs.getDateOpen())
                        .dateClose(cs.getDateClose())
                        .shPerCtrct(cs.getShPerCtrct())
                        .units(cs.getUnits() * (1.0 - alloc))
                        .priceOpen(cs.getPriceOpen())
                        .priceClose(cs.getPriceClose())
                        .markUpDn(cs.getMarkUpDn())
                        .commission(cs.getCommission() * (1.0 - alloc))
                        .taxes(cs.getTaxes() * (1.0 - alloc))
                        .fees(cs.getFees() * (1.0 - alloc))
                        .transLoad(cs.getTransLoad() * (1.0 - alloc))
                        .totalOpen(cs.getTotalOpen())
                        .totalClose(cs.getTotalClose() * (1.0 - alloc))
                        .curSym(cs.getCurSym())
                        .subAcctSec(cs.getSubAcctSec())
                        .subAcctFund(cs.getSubAcctFund())
                        .equityType(cs.getEquityType())
                        .optionType(cs.getOptionType())
                        .transactionType(cs.getTransactionType())
                        .reversalFiTId(cs.getReversalFiTId())
                        .comment(cs.getComment())
                        .build();

                    //ok
                    this.stockClosedTransList.add(csTemp);

                    // closing gets reduced
                    cs.setUnits(cs.getUnits() + os.getUnits());

                    // opening gets reduced
                    os.setUnits(0.0);

                    cs.setCommission(cs.getCommission() == null ? null : cs.getCommission() * alloc);
                    cs.setTaxes(cs.getTaxes() == null ? null : cs.getTaxes() * alloc);
                    cs.setFees(cs.getFees() == null ? null : cs.getFees() * alloc);
                    cs.setTransLoad(cs.getTransLoad() == null ? null : cs.getTransLoad() * alloc);
                    cs.setTotalClose(cs.getTotalClose() * alloc);
                    break;
                case 0:
                    // opening equals closing
                    // opening gets closed
                    csTemp = ClosingStockModel.builder()
                        .dmAcctId(cs.getDmAcctId())
                        .joomlaId(cs.getJoomlaId())
                        .fiTId(cs.getFiTId())
                        .equityId(cs.getEquityId())
                        .ticker(cs.getTicker())
                        .dateOpen(cs.getDateOpen())
                        .dateClose(cs.getDateClose())
                        .units(cs.getUnits())
                        .priceOpen(cs.getPriceOpen())
                        .priceClose(cs.getPriceClose())
                        .shPerCtrct(cs.getShPerCtrct())
                        .markUpDn(cs.getMarkUpDn())
                        .commission(cs.getCommission())
                        .taxes(cs.getTaxes())
                        .fees(cs.getFees())
                        .transLoad(cs.getTransLoad())
                        .totalOpen(cs.getTotalOpen())
                        .totalClose(cs.getTotalClose())
                        .curSym(cs.getCurSym())
                        .subAcctSec(cs.getSubAcctSec())
                        .subAcctFund(cs.getSubAcctFund())
                        .equityType(cs.getEquityType())
                        .optionType(cs.getOptionType())
                        .transactionType(cs.getTransactionType())
                        .reversalFiTId(cs.getReversalFiTId())
                        .comment(cs.getComment())
                        .build();

                    this.stockClosedTransList.add(csTemp);

                    // closing gets reduced
                    cs.setUnits(cs.getUnits() + os.getUnits());

                    // opening gets reduced
                    os.setUnits(0.0);
                    break;
                case 1:
                    // opening greater than closing
                    // alloc is what remains in opening
                    // 1.0 - alloc goes to closing
                    alloc = (oUnits - cUnits) / oUnits;

                    // all of closing goes to stockClosedTransList
                    csTemp = ClosingStockModel.builder()
                        .dmAcctId(cs.getDmAcctId())
                        .joomlaId(cs.getJoomlaId())
                        .fiTId(cs.getFiTId())
                        .equityId(cs.getEquityId())
                        .ticker(cs.getTicker())
                        .dateOpen(cs.getDateOpen())
                        .dateClose(cs.getDateClose())
                        .units(cs.getUnits())
                        .priceOpen(cs.getPriceOpen())
                        .priceClose(cs.getPriceClose())
                        .shPerCtrct(cs.getShPerCtrct())
                        .markUpDn(cs.getMarkUpDn())
                        .commission(cs.getCommission())
                        .taxes(cs.getTaxes())
                        .fees(cs.getFees())
                        .transLoad(cs.getTransLoad())
                        .totalOpen(cs.getTotalOpen())
                        .totalClose(cs.getTotalClose())
                        .curSym(cs.getCurSym())
                        .subAcctSec(cs.getSubAcctSec())
                        .subAcctFund(cs.getSubAcctFund())
                        .equityType(cs.getEquityType())
                        .optionType(cs.getOptionType())
                        .transactionType(cs.getTransactionType())
                        .reversalFiTId(cs.getReversalFiTId())
                        .comment(cs.getComment())
                        .build();

                    this.stockClosedTransList.add(csTemp);

                    // opening gets reduced
                    os.setUnits(cs.getUnits() + os.getUnits());

                    // closing gets reduced
                    cs.setUnits(0.0);

                    os.setCommission(cs.getCommission() == null ? null : cs.getCommission() * alloc);
                    os.setTaxes(cs.getTaxes() == null ? null : cs.getTaxes() * alloc);
                    os.setFees(cs.getFees() == null ? null : cs.getFees() * alloc);
                    os.setTransLoad(cs.getTransLoad() == null ? null : cs.getTransLoad() * alloc);
                    os.setTotalOpen(os.getTotalOpen() * alloc);
                    break;
                default:
            } //switch (comp)

            sql
                = "update hlhtxc5_dmOfx.ClosingStock set Units = '%s' where DMAcctId = '%s' and JoomlaId = '%s' and FiTId = '%s';";
            CMDBController.executeSQL(String.format(sql,
                cs.getUnits(),
                cs.getDmAcctId(),
                cs.getJoomlaId(),
                cs.getFiTId()));
        }//for (ClosingStockModel cs : this.stockClosingList)

        sql
            = "update hlhtxc5_dmOfx.OpeningStock set Units = '%s' where DMAcctId = '%s' and JoomlaId = '%s' and FiTId = '%s';";
        CMDBController.executeSQL(String.format(sql,
            os.getUnits(),
            os.getDmAcctId(),
            os.getJoomlaId(),
            os.getFiTId()));

        if (os.getUnits() != 0)
        {
            // there is a remaining open position
            // add to stockOpenList
            osTemp = OpeningStockModel.builder()
                .dmAcctId(os.getDmAcctId())
                .joomlaId(os.getJoomlaId())
                .fiTId(os.getFiTId())
                .equityId(os.getEquityId())
                .ticker(os.getTicker())
                .dateOpen(os.getDateOpen())
                .dateClose(os.getDateClose())
                .shPerCtrct(os.getShPerCtrct())
                .units(os.getUnits())
                .priceOpen(os.getPriceOpen())
                .priceClose(os.getPriceClose())
                .markUpDn(os.getMarkUpDn())
                .commission(os.getCommission())
                .taxes(os.getTaxes())
                .fees(os.getFees())
                .transLoad(os.getTransLoad())
                .totalOpen(os.getTotalOpen())
                .totalClose(os.getTotalClose())
                .curSym(os.getCurSym())
                .subAcctSec(os.getSubAcctSec())
                .subAcctFund(os.getSubAcctFund())
                .equityType(os.getEquityType())
                .optionType(os.getOptionType())
                .transactionType(os.getTransactionType())
                .reversalFiTId(os.getReversalFiTId())
                .comment(os.getComment())
                .build();

            this.stockOpenList.add(osTemp);
        }
    }


    /*
     * put closed transactions into closedStockFIFO
     */
    private void doStockClosedTransListSQL(OpeningStockModel os)
    {
        String sql;
        Integer rowIndex;
        Double updateUnits;
        Double updateCommission;
        Double updateTaxes;
        Double updateFees;
        Double updateTransLoad;
        Double updateTotalClose;
        Double updateUnitPriceClose;
        java.sql.Date updateCloseDate;

        updateUnits = 0.0;
        updateCommission = 0.0;
        updateTaxes = 0.0;
        updateFees = 0.0;
        updateTransLoad = 0.0;
        updateTotalClose = 0.0;

        // no closing transactions
        if (!this.stockClosedTransList.isEmpty())
        {
            // there were closing transactions
            // create object for ClosedStockFIFOModel, get ClosedGrp id
            // erase os.units always 0 here
            sql = String.format(ClosedStockFIFOModel.INSERT_VALUES,
                ClosedStockFIFOModel.STUB_FIELDS)
                + ClosedStockFIFOModel.insertAll(os,
                    this.userId);

            rowIndex = CMDBController.insertAutoRow(sql);

            // add stockClosedTransList to ClosedStockTransModel
            updateCloseDate = null;
            for (ClosingStockModel cs : this.stockClosedTransList)
            {
                //every transList should have the same date
                updateCloseDate = cs.getDateClose();

                sql = String.format(ClosedStockTransModel.INSERT_VALUES,
                    ClosedStockTransModel.ALL_FIELDS)
                    + ClosedStockTransModel.insertAll(os,
                        cs,
                        rowIndex,
                        this.userId);

                CMDBController.executeSQL(sql);

                updateUnits += cs.getUnits();
                if (cs.getCommission() != null)
                {
                    updateCommission += cs.getCommission();
                }

                if (cs.getTaxes() != null)
                {
                    updateTaxes += cs.getTaxes();
                }

                if (cs.getFees() != null)
                {
                    updateFees += cs.getFees();
                }

                if (cs.getTransLoad() != null)
                {
                    updateTransLoad += cs.getTransLoad();
                }

                updateTotalClose += cs.getTotalClose();
            }

            // strange situation where updateUnits = 0
            //todo: actually put good data into the elements rather than this
            //or remove the row from ClosedStockFIFOModel
            if (updateUnits.equals(0.0))
            {
                updateUnitPriceClose = 0.0;
            } else
            {
                updateUnitPriceClose = Math.abs(Math.round(
                    (updateTotalClose + updateCommission + updateTaxes + updateFees + updateTransLoad) * 10000)
                    / (updateUnits * 10000));                
            }

            // update ClosedStockFIFOModel object
            sql = "update hlhtxc5_dmOfx.ClosedStockFIFO set "
                + "Units = " + Double.toString(updateUnits)
                + " , Commission = " + Double.toString(updateCommission)
                + " , Taxes = " + Double.toString(updateTaxes)
                + " , Fees = " + Double.toString(updateFees)
                + " , TransLoad = " + Double.toString(updateTransLoad)
                + " , TotalOpen = " + Double.toString(os.getPriceOpen() * updateUnits)
                + " , TotalClose = " + Double.toString(updateTotalClose)
                + " , PriceClose = " + Double.toString(updateUnitPriceClose)
                + " , DateClose = " + "'" + updateCloseDate + "'"
                + //               " , GMTDtSettleClose = " + "'" + settleDate + "'" +
                " where TransactionGrp = " + rowIndex + ";";

            CMDBController.executeSQL(sql);

            this.stockClosedTransList.clear();
        }
    }

    void doStockOpenListSQL()
    {
        String sSQL;

        if (!this.stockOpenList.isEmpty())
        {
            // there are open positions
            // stockOpen -> OpenStockFIFO

            for (OpeningStockModel os : this.stockOpenList)
            {
                sSQL = String.format(OpenStockFIFOModel.INSERT_ALL_VALUES,
                    OpenStockFIFOModel.ALL_FIELDS);
                sSQL += " " + OpenStockFIFOModel.insertAll(os,
                    userId);

                CMDBController.executeSQL(sSQL);
            }
        }
    }
}
