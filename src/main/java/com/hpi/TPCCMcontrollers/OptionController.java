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
public class OptionController
{

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

    static
    {
    }

    protected OptionController()
    {
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

    public synchronized static OptionController getInstance()
    {
        if (OptionController.instance == null)
        {
            OptionController.instance
                = new OptionController();
        }
        return OptionController.instance;
    }
    //***

    void initCustom()
    {

    }

    public void processFIFOOptionLotsAccounts()
    {
        // query Accounts for list of DMAcctId
        this.getAccounts();

        // Iterate through the accounts
        for (Account account : this.accountList)
        {
            System.out.println("      Processing account: " + account.getClientAcctName());

            // get unique list of equityId from OpeningOptions
            this.getDistinctEquityId(account.getDmAcctId());

            // iterate through equityId
            for (String equityId : this.equityIdList)
            {
                //iterate the transaction types
                for (OptionTransactionTypeEnum tte : OptionTransactionTypeEnum.values())
                {
                    if (tte.toString().contains("open"))
                    {
                        this.getAcctOptionsOpen(account.getDmAcctId(), equityId, tte.toString());
                    } else
                    {
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
        }
    }

    void getAccounts()
    {
        String sql;
        ResultSet rs;
        Account accountTemp;

        sql
            = "select Accounts.DMAcctId, BrokerId, AcctId, Org, FId, InvAcctIdFi, ClientAcctName from hlhtxc5_dmOfx.Accounts, hlhtxc5_dmOfx.ClientAccts where Accounts.DMAcctId = ClientAccts.DMAcctId and ClientAccts.Active = 'Yes' and ClientAccts.JoomlaId = '%s';";

        sql = String.format(sql, this.userId);

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
                Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                ex.getMessage(), JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void getDistinctEquityId(Integer account)
    {
        ResultSet rs;
        String sql;

        sql
            = "select distinct A.EquityId from (select distinct EquityId from hlhtxc5_dmOfx.OpeningOptions where DMAcctId = '%s' and JoomlaId = '%s' and not(Units = 0) union select distinct EquityId from hlhtxc5_dmOfx.ClientOpeningOptions where DMAcctId = '%s' and JoomlaId = '%s' and not(Units = 0) union select distinct EquityId from hlhtxc5_dmOfx.ClosingOptions where DMAcctId = '%s' and JoomlaId = '%s' and not(Units = 0) union select distinct EquityId from hlhtxc5_dmOfx.ClientClosingOptions where DMAcctId = '%s' and JoomlaId = '%s' and not(Units = 0)) as A  order by EquityId;";
        sql = String.format(sql,
            account, this.userId,
            account, this.userId,
            account, this.userId,
            account, this.userId);

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
                Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                ex.getMessage(), JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * For a given account, equityId and transactionType populate open arrays
     *
     * @param account
     * @param equityId
     * @param optTransType
     */
    public void getAcctOptionsOpen(
        Integer account, String equityId,
        String optTransType)
    {
        String sqlOpen;
        OpeningOptionModel ooTemp;

        sqlOpen = String.format(OpeningOptionModel.GET_ALL_AVAIL,
            account, equityId, optTransType, this.userId,
            account, equityId, optTransType, this.userId);

        try (Connection con = CMDBController.getConnection();
            PreparedStatement pStmtOpen = con.prepareStatement(sqlOpen);
            ResultSet rs = pStmtOpen.executeQuery();)
        {
            while (rs.next())
            {
                ooTemp = OpeningOptionModel.builder()
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

                switch (optTransType)
                {
                    case "buytoopen":
                        this.optionOpeningBuyList.add(ooTemp);
                        break;
                    case "selltoopen":
                        this.optionOpeningSellList.add(ooTemp);
                        break;
                    default:
                }
            }
        } catch (SQLException ex)
        {
            throw new CMDAOException(CMLanguageController.
                getDBErrorProp("Title"),
                Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                ex.getMessage(), JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * For a given account, equityId and transactionType populate buy/sell arrays
     *
     * @param account
     * @param equityId
     * @param optTransType
     */
    public void getAcctOptionsClose(
        Integer account, String equityId,
        String optTransType)
    {
        ResultSet rs;
        String sqlClose;
        ClosingOptionModel coTemp;

        rs = null;

        sqlClose = String.format(ClosingOptionModel.GET_ALL_AVAIL,
            account, equityId, optTransType, this.userId,
            account, equityId, optTransType, this.userId);

        try (Connection con = CMDBController.getConnection();
            PreparedStatement pStmtClose = con.prepareStatement(sqlClose);)
        {

            if (optTransType.equalsIgnoreCase("BUYTOCLOSE") || optTransType.equalsIgnoreCase("SELLTOCLOSE"))
            {
                rs = pStmtClose.executeQuery();
                while (rs.next())
                {
                    coTemp = ClosingOptionModel.builder()
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

                    switch (optTransType)
                    {
                        case "buytoclose":
                            this.optionClosingBuyList.add(coTemp);
                            break;
                        case "selltoclose":
                            this.optionClosingSellList.add(coTemp);
                            break;
                        default:
                    }
                }
            }

            if (rs != null)
            {
                rs.close();
            }
        } catch (SQLException ex)
        {
            throw new CMDAOException(CMLanguageController.
                getDBErrorProp("Title"),
                Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                ex.getMessage(), JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void processFIFOOptionLotsBuyToOpen()
    {
        String s;

        // iterate through all OpeningBuy of one EquityId
        for (OpeningOptionModel oo : this.optionOpeningBuyList)
        {
            // iterate through all ClosingOptions for this EquityId
            this.doOptionClosingSellList(oo);

            // handle closing transactions
            this.doOptionClosedTransListSQL(oo);
        }//for (OpeningOptionModel os : this.optionOpeningList)

        // should be no remaining closing transactions
        for (ClosingOptionModel co : this.optionClosingSellList)
        {
            if (!co.getUnits().equals(0.0))
            {
                s = String.format(CMLanguageController.
                    getErrorProp("GeneralError"),
                    "Option Opening and closing transactions mismatch.\n") + "EquityId: " + co.getEquityId() + "  "
                    + "FiTId: " + co.getFiTId() + "  " + "Units Left: " + co.getUnits();

                CMHPIUtils.showDefaultMsg(
                    CMLanguageController.getAppProp("Title") + CMLanguageController.getErrorProp("Title"),
                    Thread.currentThread().getStackTrace()[1].getClassName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(),
                    s, JOptionPane.ERROR_MESSAGE);
            }
        }

        // handle open positions
        this.doOptionOpenList();
    }

    public void processFIFOOptionLotsSellToOpen()
    {
        String s;
        Integer openingOption;

        // iterate through all OpeningSell of one EquityId
        openingOption = 0;
        for (OpeningOptionModel oo : this.optionOpeningSellList)
        {
            openingOption++;

            // iterate through all ClosingOptions for this EquityId
            this.doOptionClosingBuyList(oo);

            // handle closing transactions
            this.doOptionClosedTransListSQL(oo);
        }//for (OpeningOptionModel os : this.optionOpeningList)

        // should be no remaining closing transactions
        for (ClosingOptionModel co : this.optionClosingSellList)
        {
            if (!co.getUnits().equals(0.0))
            {
                s = String.format(CMLanguageController.
                    getErrorProp("GeneralError"),
                    "Option Opening and closing transactions mismatch.\n") + "EquityId: " + co.getEquityId() + "  "
                    + "FiTId: " + co.getFiTId() + "  " + "Units Left: " + co.getUnits();

                CMHPIUtils.showDefaultMsg(
                    CMLanguageController.getAppProp("Title") + CMLanguageController.getErrorProp("Title"),
                    Thread.currentThread().getStackTrace()[1].getClassName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(),
                    s, JOptionPane.ERROR_MESSAGE);
            }
        }

        // handle open positions
        this.doOptionOpenList();
    }

    /*
     * Given an opening option, iterate closing op
     */
    private void doOptionClosingSellList(OpeningOptionModel oo)
    {
        int comp;
        Double oUnits;
        Double cUnits;
        Double alloc;
        OpeningOptionModel ooTemp;
        ClosingOptionModel coTemp;

        for (ClosingOptionModel co : this.optionClosingSellList)
        {

            if (co.getUnits() == 0.0)
            {
                // no units left in this closing
                continue;
            }

            if (oo.getUnits() == 0.0)
            {
                // opening has been completely covered
                break; // closingOption;
            }

            oUnits = Math.abs(oo.getUnits());
            cUnits = Math.abs(co.getUnits());
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
                    // 1.0 - alloc goes to closingTrans
                    // closing units are negative
                    alloc = (cUnits - oUnits) / cUnits;

                    // part of closing goes to optionClosedTransList
                    coTemp = ClosingOptionModel.builder()
                        .dmAcctId(co.getDmAcctId())
                        .joomlaId(co.getJoomlaId())
                        .fiTId(co.getFiTId())
                        .ticker(co.getTicker())
                        .equityId(co.getEquityId())
                        .dateOpen(co.getDateOpen())
                        .dateClose(co.getDateClose())
                        .dateExpire(co.getDateExpire())
                        .shPerCtrct(co.getShPerCtrct())
                        .units(Precision.round(co.getUnits() * (1.0 - alloc), 1))
                        .priceOpen(co.getPriceOpen())
                        .priceClose(co.getPriceClose())
                        .markUpDn(co.getMarkUpDn())
                        .commission(co.getCommission() * (1.0 - alloc))
                        .taxes(co.getTaxes() * (1.0 - alloc))
                        .fees(co.getFees() * (1.0 - alloc))
                        .transLoad(co.getTransLoad() * (1.0 - alloc))
                        .totalOpen(co.getTotalOpen())
                        .totalClose(Precision.round(co.getTotalClose() * (1.0 - alloc), 4))
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

                    this.optionClosedTransList.add(coTemp);

                    // closing gets reduced
                    co.setUnits(co.getUnits() + oo.getUnits());
                    //co.setUnits(co.getUnits() + oo.getUnits());

                    // opening gets reduced
                    oo.setUnits(0.0);

                    //closing reduced by allocation
                    co.setCommission(co.getCommission() == null ? null : co.getCommission() * alloc);
                    co.setTaxes(co.getTaxes() == null ? null : co.getTaxes() * alloc);
                    co.setFees(co.getFees() == null ? null : co.getFees() * alloc);
                    co.setTransLoad(co.getTransLoad() == null ? null : co.getTransLoad() * alloc);
                    co.setTotalClose(co.getTotalClose() * alloc);
                    break;
                case 0:
                    // opening equals closing
                    // opening gets closed
                    coTemp = ClosingOptionModel.builder()
                        .dmAcctId(co.getDmAcctId())
                        .joomlaId(co.getJoomlaId())
                        .fiTId(co.getFiTId())
                        .ticker(co.getTicker())
                        .equityId(co.getEquityId())
                        .dateOpen(co.getDateOpen())
                        .dateClose(co.getDateClose())
                        .dateExpire(co.getDateExpire())
                        .shPerCtrct(co.getShPerCtrct())
                        .units(co.getUnits())
                        .priceOpen(co.getPriceOpen())
                        .priceClose(co.getPriceClose())
                        .markUpDn(co.getMarkUpDn())
                        .commission(co.getCommission())
                        .taxes(co.getTaxes())
                        .fees(co.getFees())
                        .transLoad(co.getTransLoad())
                        .totalOpen(co.getTotalOpen())
                        .totalClose(co.getTotalClose())
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

                    this.optionClosedTransList.add(coTemp);

                    // closing gets reduced
                    co.setUnits(co.getUnits() + oo.getUnits());

                    // opening gets reduced
                    oo.setUnits(0.0);
                    break;
                case 1:
                    // opening greater than closing
                    // alloc is what remains in opening
                    // 1.0 - alloc goes to closing
                    alloc = (oUnits - cUnits) / oUnits;

                    // all of closing goes to optionClosedTransList
                    coTemp = ClosingOptionModel.builder()
                        .dmAcctId(co.getDmAcctId())
                        .joomlaId(co.getJoomlaId())
                        .fiTId(co.getFiTId())
                        .ticker(co.getTicker())
                        .equityId(co.getEquityId())
                        .dateOpen(co.getDateOpen())
                        .dateClose(co.getDateClose())
                        .dateExpire(co.getDateExpire())
                        .shPerCtrct(co.getShPerCtrct())
                        .units(co.getUnits())
                        .priceOpen(co.getPriceOpen())
                        .priceClose(co.getPriceClose())
                        .markUpDn(co.getMarkUpDn())
                        .commission(co.getCommission())
                        .taxes(co.getTaxes())
                        .fees(co.getFees())
                        .transLoad(co.getTransLoad())
                        .totalOpen(co.getTotalOpen())
                        .totalClose(co.getTotalClose())
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

                    this.optionClosedTransList.add(coTemp);

                    // opening gets reduced
                    oo.setUnits(co.getUnits() + oo.getUnits());

                    // closing gets reduced
                    co.setUnits(0.0);

                    oo.setCommission(co.getCommission() == null ? null : co.getCommission() * alloc);
                    oo.setTaxes(co.getTaxes() == null ? null : co.getTaxes() * alloc);
                    oo.setFees(co.getFees() == null ? null : co.getFees() * alloc);
                    oo.setTransLoad(co.getTransLoad() == null ? null : co.getTransLoad() * alloc);
                    oo.setTotalOpen(oo.getTotalOpen() * alloc);
                    break;
                default:
            } //switch (comp)

            //update the closing trade with the allocation
            CMDBController.executeSQL(String.format(ClosingOptionModel.UPDATE_UNITS,
                co.getUnits(), co.getDmAcctId(), co.getJoomlaId(), co.getFiTId()));
        }//for (ClosingOptionModel oo : this.optionClosingList)

        CMDBController.executeSQL(String.format(OpeningOptionModel.UPDATE_UNITS, oo.getUnits(),
            oo.getDmAcctId(), oo.getJoomlaId(), oo.getFiTId()));

        if (oo.getUnits() != 0)
        {
            // there is a remaining open position
            // add to optionOpenList
            ooTemp = OpeningOptionModel.builder()
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

            this.optionOpenList.add(ooTemp);
        }
    }

    /*
     * Given an opening option, iterate closing op
     */
    private void doOptionClosingBuyList(OpeningOptionModel oo)
    {
        int comp;
        Double oUnits;
        Double cUnits;
        Double alloc;
        OpeningOptionModel ooTemp;
        ClosingOptionModel coTemp;
        String sql;

        for (ClosingOptionModel co : this.optionClosingBuyList)
        {
            if (co.getUnits() == 0.0)
            {
                // no units left in this closing
                continue;
            }

            if (oo.getUnits() == 0.0)
            {
                // opening has been completely covered
                break; // closingOption;
            }

            oUnits = Math.abs(oo.getUnits());
            cUnits = Math.abs(co.getUnits());
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
                    // 1.0 - alloc goes to closingTrans
                    // closing units are negative
                    alloc = (cUnits - oUnits) / cUnits;

                    // part of closing goes to optionClosedTransList
                    coTemp = ClosingOptionModel.builder()
                        .dmAcctId(co.getDmAcctId())
                        .joomlaId(co.getJoomlaId())
                        .fiTId(co.getFiTId())
                        .ticker(co.getTicker())
                        .equityId(co.getEquityId())
                        .dateOpen(co.getDateOpen())
                        .dateClose(co.getDateClose())
                        .dateExpire(co.getDateExpire())
                        .shPerCtrct(co.getShPerCtrct())
                        .units(Precision.round(co.getUnits() * (1.0 - alloc), 1))
                        .priceOpen(co.getPriceOpen())
                        .priceClose(co.getPriceClose())
                        .markUpDn(co.getMarkUpDn())
                        .commission(co.getCommission() * (1.0 - alloc))
                        .taxes(co.getTaxes() * (1.0 - alloc))
                        .fees(co.getFees() * (1.0 - alloc))
                        .transLoad(co.getTransLoad() * (1.0 - alloc))
                        .totalOpen(co.getTotalOpen() * (1.0 - alloc))
                        .totalClose(co.getTotalClose() * (1.0 - alloc))
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

                    this.optionClosedTransList.add(coTemp);

                    // closing gets reduced
                    co.setUnits(co.getUnits() + oo.getUnits());

                    // opening gets reduced
                    oo.setUnits(0.0);

                    co.setCommission(co.getCommission() == null ? null : co.getCommission() * alloc);
                    co.setTaxes(co.getTaxes() == null ? null : co.getTaxes() * alloc);
                    co.setFees(co.getFees() == null ? null : co.getFees() * alloc);
                    co.setTransLoad(co.getTransLoad() == null ? null : co.getTransLoad() * alloc);
                    co.setTotalClose(co.getTotalClose() * alloc);
                    break;
                case 0:
                    // opening equals closing
                    // opening gets closed
                    coTemp = ClosingOptionModel.builder()
                        .dmAcctId(co.getDmAcctId())
                        .joomlaId(co.getJoomlaId())
                        .fiTId(co.getFiTId())
                        .ticker(co.getTicker())
                        .equityId(co.getEquityId())
                        .dateOpen(co.getDateOpen())
                        .dateClose(co.getDateClose())
                        .dateExpire(co.getDateExpire())
                        .shPerCtrct(co.getShPerCtrct())
                        .units(co.getUnits())
                        .priceOpen(co.getPriceOpen())
                        .priceClose(co.getPriceClose())
                        .markUpDn(co.getMarkUpDn())
                        .commission(co.getCommission())
                        .taxes(co.getTaxes())
                        .fees(co.getFees())
                        .transLoad(co.getTransLoad())
                        .totalOpen(co.getTotalOpen())
                        .totalClose(co.getTotalClose())
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

                    this.optionClosedTransList.add(coTemp);

                    // closing gets reduced
                    co.setUnits(co.getUnits() + oo.getUnits());

                    // opening gets reduced
                    oo.setUnits(0.0);
                    break;
                case 1:
                    // opening greater than closing
                    // alloc is what remains in opening
                    // 1.0 - alloc goes to closing
                    alloc = (oUnits - cUnits) / oUnits;

                    // all of closing goes to optionClosedTransList
                    coTemp = ClosingOptionModel.builder()
                        .dmAcctId(co.getDmAcctId())
                        .joomlaId(co.getJoomlaId())
                        .fiTId(co.getFiTId())
                        .ticker(co.getTicker())
                        .equityId(co.getEquityId())
                        .dateOpen(co.getDateOpen())
                        .dateClose(co.getDateClose())
                        .dateExpire(co.getDateExpire())
                        .shPerCtrct(co.getShPerCtrct())
                        .units(co.getUnits())
                        .priceOpen(co.getPriceOpen())
                        .priceClose(co.getPriceClose())
                        .markUpDn(co.getMarkUpDn())
                        .commission(co.getCommission())
                        .taxes(co.getTaxes())
                        .fees(co.getFees())
                        .transLoad(co.getTransLoad())
                        .totalOpen(co.getTotalOpen())
                        .totalClose(co.getTotalClose())
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

                    this.optionClosedTransList.add(coTemp);

                    // opening gets reduced
                    oo.setUnits(co.getUnits() + oo.getUnits());

                    // closing gets reduced
                    co.setUnits(0.0);

                    oo.setCommission(co.getCommission() == null ? null : co.getCommission() * alloc);
                    oo.setTaxes(co.getTaxes() == null ? null : co.getTaxes() * alloc);
                    oo.setFees(co.getFees() == null ? null : co.getFees() * alloc);
                    oo.setTransLoad(co.getTransLoad() == null ? null : co.getTransLoad() * alloc);
                    //opening and closing have opposite signs
                    oo.setTotalOpen(oo.getTotalOpen() * alloc);
                    break;
                default:
            } //switch (comp)

            CMDBController.executeSQL(String.format(ClosingOptionModel.UPDATE_UNITS, co.getUnits(),
                co.getDmAcctId(), co.getJoomlaId(), co.getFiTId()));
        }//for (ClosingOptionModel oo : this.optionClosingList)

        CMDBController.executeSQL(String.format(OpeningOptionModel.UPDATE_UNITS, oo.getUnits(),
            oo.getDmAcctId(), oo.getJoomlaId(), oo.getFiTId()));

        if (oo.getUnits() != 0)
        {
            // there is a remaining open position
            // add to optionOpenList
            ooTemp = OpeningOptionModel.builder()
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

            this.optionOpenList.add(ooTemp);
        }
    }

    /*
     * pair the opening to closing and write to the database
     */
    void doOptionClosedTransListSQL(OpeningOptionModel oo)
    {
        String sSQL;
        Integer rowIndex;
        Double updateUnits;
        Double updateCommission;
        Double updateTaxes;
        Double updateFees;
        Double updateTransLoad;
        Double updateTotalClose;
        Integer updateShPerCtrct;
        Double updateUnitPriceClose;
        java.sql.Date updateCloseDate;

        updateUnits = 0.0;
        updateCommission = 0.0;
        updateTaxes = 0.0;
        updateFees = 0.0;
        updateTransLoad = 0.0;
        updateTotalClose = 0.0;
        updateShPerCtrct = 0;
        updateCloseDate = null;

        if (!this.optionClosedTransList.isEmpty())
        {
            //closing transactions available

            // put this opening transaction into ClosedOptionFIFOModel
            // with group of closing transactions
            sSQL = String.format(ClosedOptionFIFOModel.INSERT_ALL_VALUES, ClosedOptionFIFOModel.ALL_FIELDS);
            sSQL += oo.getDmAcctId();
            sSQL += ", ";
            sSQL += this.userId;
            sSQL += ", '";
            sSQL += oo.getFiTId();
            sSQL += "', ";
            sSQL += null;     //transactionGrp, autoRow number
            sSQL += ", '";
            sSQL += oo.getTicker();
            sSQL += "', '";
            sSQL += oo.getEquityId();
            sSQL += "', '";
            sSQL += CMHPIUtils.getTransactionName(oo.getEquityId());
            sSQL += "', '";
            sSQL += oo.getOptionType();
            sSQL += "', ";
            sSQL += oo.getStrikePrice();
            sSQL += ", '";
            sSQL += oo.getDateOpen();
            sSQL += "', ";
            if (oo.getDateClose() == null)
            {
                sSQL += "null, '";
            } else
            {
                sSQL += "'" + oo.getDateClose() + "', '";
            }
            sSQL += oo.getDateExpire();
            sSQL += "', ";
            sSQL += oo.getShPerCtrct();
            sSQL += ", ";
            sSQL += oo.getUnits();
            sSQL += ", ";
            sSQL += oo.getPriceOpen();
            sSQL += ", ";
            sSQL += oo.getPriceClose();
            sSQL += ", ";
            sSQL += oo.getMarkUpDn();
            sSQL += ", ";
            sSQL += oo.getCommission();
            sSQL += ", ";
            sSQL += oo.getTaxes();
            sSQL += ", ";
            sSQL += oo.getFees();
            sSQL += ", ";
            sSQL += oo.getTransLoad();
            sSQL += ", ";
            sSQL += oo.getTotalOpen();
            sSQL += ", ";
            sSQL += oo.getTotalClose(); //openingOptions.TotalClose is 0.0
            sSQL += ", ";
            if (oo.getCurSym() == null)
            {
                sSQL += "null, '";
            } else
            {
                sSQL += "'" + oo.getCurSym() + "', '";
            }
            sSQL += oo.getTransactionType();
            sSQL += "'";
            sSQL += ");";

            rowIndex = CMDBController.insertAutoRow(sSQL);

            // add optionClosedTransList to ClosedOptionTrans
            // why? That is where the group of closing transactions will be
            //  even if a group of 1
            for (ClosingOptionModel co : this.optionClosedTransList)
            {
                sSQL = String.format(ClosedOptionTrans.INSERT_ALL_VALUES, ClosedOptionTrans.ALL_FIELDS);
                sSQL += oo.getDmAcctId();
                sSQL += ", '";
                sSQL += this.userId;
                sSQL += "', '";
                sSQL += oo.getFiTId();
                sSQL += "', ";
                sSQL += rowIndex;
                sSQL += ", '";
                sSQL += co.getFiTId();
                sSQL += "', '";
                sSQL += co.getTicker();
                sSQL += "', '";
                sSQL += co.getEquityId();
                sSQL += "', '";
                sSQL += CMHPIUtils.getTransactionName(co.getEquityId());
                sSQL += "', '";
                sSQL += co.getOptionType();
                sSQL += "', ";
                sSQL += co.getStrikePrice();
                sSQL += ", ";
                if (co.getDateOpen() == null)
                {
                    sSQL += "null, ";
                } else
                {
                    sSQL += "'" + co.getDateOpen() + "', ";
                }
                if (co.getDateClose() == null)
                {
                    sSQL += "null, ";
                } else
                {
                    sSQL += "'" + co.getDateClose() + "', ";
                }
                sSQL += "'" + co.getDateExpire();
                sSQL += "', ";
                sSQL += co.getShPerCtrct();
                sSQL += ", ";
                sSQL += co.getUnits();
                sSQL += ", ";
                sSQL += co.getPriceOpen();
                sSQL += ", ";
                sSQL += co.getPriceClose();
                sSQL += ", ";
                sSQL += co.getMarkUpDn();
                sSQL += ", ";
                sSQL += co.getCommission();
                sSQL += ", ";
                sSQL += co.getTaxes();
                sSQL += ", ";
                sSQL += co.getFees();
                sSQL += ", ";
                sSQL += co.getTransLoad();
                sSQL += ", ";
                sSQL += co.getTotalOpen();  //closingOptions.totalOpen is null
                sSQL += ", ";
                sSQL += Precision.round(co.getTotalClose(), 4); //closingOptions.totalClose has values
                sSQL += ", ";
                sSQL += co.getCurSym();
                sSQL += ", '";
                sSQL += co.getSubAcctSec();
                sSQL += "', '";
                sSQL += co.getSubAcctFund();
                sSQL += "', '";
                sSQL += co.getTransactionType();
                sSQL += "', ";
                sSQL += co.getReversalFiTId() == null ? null : "'" + oo.getReversalFiTId() + "'";
                sSQL += ");";

                CMDBController.executeSQL(sSQL);

                // one selltoclose has -1 units, the other has +1 units
                // todo: seems bogus, but setting units to -x if it is a sell
                //  transaction
                if (co.getTransactionType().equalsIgnoreCase("selltoopen")
                    || co.getTransactionType().equalsIgnoreCase("selltoclose"))
                {
                    co.setUnits(-1.0 * Math.abs(co.getUnits()));
                }

                if (co.getTransactionType().equalsIgnoreCase("buytoclose")
                    || co.getTransactionType().equalsIgnoreCase("buytoopen"))
                {
                    co.setUnits(Math.abs(co.getUnits()));
                }
                
                updateUnits += co.getUnits();
                
                if (co.getCommission() != null)
                {
                    updateCommission += co.getCommission();
                }

                if (co.getTaxes() != null)
                {
                    updateTaxes += co.getTaxes();
                }

                if (co.getFees() != null)
                {
                    updateFees += co.getFees();
                }

                if (co.getTransLoad() != null)
                {
                    updateTransLoad += co.getTransLoad();
                }

                if (co.getTotalClose() != null)
                {
                    updateTotalClose += co.getTotalClose();
                }

                updateShPerCtrct = co.getShPerCtrct();
                updateCloseDate = co.getDateClose();
            }//for (ClosingOptionModel cs : this.OptionClosedTransList)

            //  have to add fees back into totalClose to get gross from which price can be derived
            if (updateUnits.equals(0.0))
            {
                updateUnitPriceClose = 0.0;
            } else
            {
                updateUnitPriceClose = Math.round(
                    (updateTotalClose + updateCommission + updateTaxes + updateFees + updateTransLoad) * 10000)
                    / (updateUnits * 10000 * updateShPerCtrct);
            }

            // update ClosedOptionFIFOModel object with Total and 
            //  TotalClose amounts
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

    void doOptionOpenList()
    {
        if (!this.optionOpenList.isEmpty())
        {
            // there are open positions
            // optionOpen -> OpenOptionFIFO
            for (OpeningOptionModel oo : this.optionOpenList)
            {
                // optTransType selltoopen? buytoclose? No, never happens
                CMDBController.executeSQL(String
                    .format(OpenOptionFIFOModel.INSERT_ALL_VALUES,
                        OpenOptionFIFOModel.ALL_FIELDS) + OpenOptionFIFOModel.insertAll(oo, userId));
            }
        }
    }
}
