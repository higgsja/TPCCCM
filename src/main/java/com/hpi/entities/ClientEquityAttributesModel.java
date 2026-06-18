package com.hpi.entities;

import com.hpi.TPCCMsql.CMDAOException;
import com.hpi.TPCCMprefs.CMDBModel;
import com.hpi.TPCCMcontrollers.CMDBController;
import com.hpi.TPCCMcontrollers.CMLanguageController;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import lombok.Getter;

@Getter
public class ClientEquityAttributesModel
{

    public static final String UPDATE_SECTORID
        = "update (select Ticker, Sector, max(`Date`) as `Date` from hlhtxc5_dmOfx.EquityInfo group by Ticker order by Ticker) as A, hlhtxc5_dmOfx.ClientEquityAttributes as CEA, hlhtxc5_dmOfx.ClientSectorList as CSL set CEA.ClientSectorId = CSL.ClientSectorId where CEA.Ticker = A.Ticker and CSL.ClientSector = A.Sector and CEA.JoomlaId = CSL.JoomlaId and isnull(CEA.ClientSectorId) and CEA.JoomlaId = '%s';";

    public static final String FORCE_UPDATE_ACTIVE
        = "update hlhtxc5_dmOfx.ClientEquityAttributes set Active = if(isnull(ActPct), if(isnull(TgtPct), Active, if(TgtPct > 0, 'Yes', Active)), if(ActPct > 0, 'Yes', Active)) where JoomlaId = '%s';";

    public static final String UPDATE_ANALYST_TGTS
        = "update hlhtxc5_dmOfx.ClientEquityAttributes as C inner join (select Ticker, MAX(`Date`) AS `Date` FROM hlhtxc5_dmOfx.EquityInfo GROUP BY Ticker ORDER BY Ticker ASC) as A on C.Ticker = A.Ticker inner join hlhtxc5_dmOfx.EquityInfo on EquityInfo.`Date` = A.`Date` and EquityInfo.Ticker = A.Ticker set C.AnalystTgt = EquityInfo.TgtPrice where A.`Date` = EquityInfo.`Date` and C.Ticker = A.Ticker and JoomlaId = '%s';";

    public static final String UPDATE_CEA_CASH_SECTORID
        = "update hlhtxc5_dmOfx.ClientEquityAttributes cea, hlhtxc5_dmOfx.ClientSectorList csl set cea.ClientSectorId = csl.ClientSectorId where cea.Ticker = 'CASH' and cea.Ticker = csl.ClientSector and cea.JoomlaId = csl.JoomlaId and cea.JoomlaId = '%s'";

    public static final String INSERT_CASH_2_CEA
        = "insert ignore into hlhtxc5_dmOfx.ClientEquityAttributes (JoomlaId, Ticker, TickerIEX, Active, ClientSectorId, TgtPct, AnalystTgt, StkPrice, Comment, TgtLocked, ActPct) values ('%s', 'CASH', 'CASH', 'Yes', '%s', 0, 0, 1, '', 'No', 0);";

    public static final String UPDATE_ACT_PCT
        = "update hlhtxc5_dmOfx.ClientEquityAttributes A left join (select fp.Ticker, sum(fp.ActPct) as sumActPct from hlhtxc5_dmOfx.FIFOOpenTransactions fp where JoomlaId = '%s' group by fp.Ticker) as B on B.Ticker = A.Ticker set A.ActPct = B.sumActPct where A.JoomlaId = '%s' and A.Active = 'Yes';";

    Integer joomlaId;
    String ticker;
    String tickerIEX;
    String active;
    Boolean bActive;
    Integer clientSectorId;
    ClientSectorModel clientSectorModel;
    Double tgtPct;
    Double analystTgt;
    Double stkPrice;
    String comment;
    String tgtLocked;
    Boolean bTgtLocked;
    Double actPct;
    private static ArrayList<ClientEquityAttributesModel> CLIENT_EQUITY_DB_MODELS;
    public static final String SQLSELECT;
    public static final String SQLINSERT;
    public static final String SQLUPDATE;
    public static final int TICKER = 0;
    public static final int SECTORID = 1;
    public static final int ACTIVE = 2;
    public static final int COMMENT = 3;
    public static final int JOOMALID = 4;
    public static final int TICKERIEX = 5;
    public static final int TGTPCT = 6;
    public static final int ANALYSTTGT = 7;
    public static final int STKPRICE = 8;
    public static final int TGTLOCKED = 9;
    public static final int ACTPCT = 10;

    static
    {
        SQLSELECT
            = "select Ticker, ClientSectorId, Active, Comment, JoomlaId, TickerIEX, TgtPct, AnalystTgt, StkPrice, TgtLocked, ActPct from hlhtxc5_dmOfx.ClientEquityAttributes where JoomlaId = '%s' order by Ticker asc;";
        SQLINSERT
            = "insert into hlhtxc5_dmOfx.ClientEquityAttributes (JoomlaId, Ticker, TickerIEX, Active, ClientSectorId, TgtPct, AnalystTgt, StkPrice, Comment, TgtLocked, ActPct) values (";
        SQLUPDATE = "update hlhtxc5_dmOfx.ClientEquityAttributes set ";
        CLIENT_EQUITY_DB_MODELS = new ArrayList<>();
    }

    /**
     *
     * @param joomlaId
     * @param ticker
     * @param tickerIEX
     * @param active
     * @param clientSectorId
     * @param tgtPct
     * @param analystTgt
     * @param stkPrice
     * @param comment
     * @param tgtLocked
     * @param actPct
     */
    public ClientEquityAttributesModel(Integer joomlaId, String ticker,
        String tickerIEX, String active, Integer clientSectorId,
        Double tgtPct, Double analystTgt, Double stkPrice,
        String comment, String tgtLocked, Double actPct)
    {
        this.joomlaId = joomlaId;
        this.ticker = ticker;
        this.tickerIEX = tickerIEX;
        this.active = active;
        this.bActive = active.equalsIgnoreCase("Yes");
        this.clientSectorId = clientSectorId;
        this.clientSectorModel = null;
        this.tgtPct = tgtPct;
        this.analystTgt = analystTgt;
        this.stkPrice = stkPrice;
        this.comment = comment;
        this.tgtLocked = tgtLocked;
        this.bTgtLocked = tgtLocked.equalsIgnoreCase("Yes");
        this.actPct = actPct;

        for (ClientSectorModel csm : ClientSectorModel.getListClientSectorModel())
        {
            if (Objects.equals(csm.getClientSectorId(), this.clientSectorId))
            {
                this.clientSectorModel = ClientSectorModel.createNew(csm);
                break;
            }
        }
    }

    /**
     *
     * @param rs
     *
     * @return
     *
     * @throws SQLException
     */
    public static final ClientEquityAttributesModel createNew(ResultSet rs)
        throws SQLException
    {
        return new ClientEquityAttributesModel(rs.getInt("JoomlaId"),
            rs.getString("Ticker"), rs.getString("TickerIEX"),
            rs.getString("Active"), rs.getInt("ClientSectorId"),
            rs.getDouble("TgtPct"), rs.getDouble("AnalystTgt"),
            rs.getDouble("StkPrice"), rs.getString("Comment"),
            rs.getString("TgtLocked"), rs.getDouble("ActPct"));
    }

    /**
     *
     */
    public static final void getClientEquities()
    {
        ResultSet rs;
        ClientEquityAttributesModel cem;
        Integer userId;
        String sql;

        userId = CMDBModel.getUserId();

        ClientEquityAttributesModel.CLIENT_EQUITY_DB_MODELS.clear();

        sql = String.format(ClientEquityAttributesModel.SQLSELECT, userId.toString());

        try (Connection con = CMDBController.getConnection();
            PreparedStatement pStmt = con.prepareStatement(sql))
        {
            rs = pStmt.executeQuery();

            while (rs.next())
            {
                cem = ClientEquityAttributesModel.createNew(rs);

                ClientEquityAttributesModel.CLIENT_EQUITY_DB_MODELS.add(cem);
            }
            rs.close();
            pStmt.close();
            con.close();
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
     *
     * @param listCEM: TableModelArray, the visual array
     */
    public static final void updateEquities(ArrayList<ClientEquityAttributesModel> listCEM)
    {
        Integer userId;
        ArrayList<ClientEquityAttributesModel> listNewClientEquityModel;
        Integer row;
        String sqlInsert, sqlUpdate;

        userId = CMDBModel.getUserId();
        listNewClientEquityModel = new ArrayList<>();

        //ClientEquityModel.CLIENT_EQUITY_MODELS.clear();
        row = 0;

        for (ClientEquityAttributesModel cem : listCEM)
        {
            // 3 cases:
            // added a new one
            // changed Sector or Comment
            // no change

            if (row <= ClientEquityAttributesModel.CLIENT_EQUITY_DB_MODELS.size() - 1)
            {
                // we add the new items to the end and so that array
                // will be larger than the database array
                if (cem.clientSectorId.equals(
                    ClientEquityAttributesModel.CLIENT_EQUITY_DB_MODELS.get(row).clientSectorModel.clientSectorId)
                    && Objects.equals(cem.comment,
                        ClientEquityAttributesModel.CLIENT_EQUITY_DB_MODELS.
                            get(row).comment))
                {
                    // no change on this row, update new array
                    listNewClientEquityModel.add(cem);
                    row++;
                    continue;
                }
            }

            // new item or
            // some change on the row
            row++;

            // update the database
            sqlInsert = ClientEquityAttributesModel.SQLINSERT
                + "'" + cem.getJoomlaId() + "', '" + cem.getTicker() + "', '"
                + cem.getTickerIEX() + "', '" + cem.getActive() + "', '"
                + cem.getClientSectorModel().getClientSectorId() + "', '"
                + cem.getTgtPct() + "', '"
                + cem.getAnalystTgt() + "', '" + cem.getStkPrice() + "', '"
                + cem.getComment() + "', '" + cem.getTgtLocked() + "', '"
                + cem.actPct + "'"
                + ");";

            sqlUpdate = ClientEquityAttributesModel.SQLUPDATE
                //                + "JoomlaId = '" + cem.getJoomlaId() + "', "
                //                + "Ticker = '" + cem.getTicker() + "', "
                + "TickerIEX = '" + cem.getTickerIEX() + "', "
                + "Active = '" + cem.getActive() + "', "
                + "ClientSectorId = '" + cem.getClientSectorModel().
                    getClientSectorId() + "', "
                + "TgtPct = '" + cem.getTgtPct() + "', "
                + "AnalystTgt = '" + cem.getAnalystTgt() + "', "
                + "StkPrice = '" + cem.getStkPrice() + "', "
                + "Comment = '" + cem.getComment() + "', "
                + "TgtLocked = '" + cem.getTgtLocked() + "', "
                + "ActPct = '" + cem.actPct + "' "
                + "where JoomlaId = '" + userId + "' "
                + "and Ticker = '" + cem.getTicker() + "';";

            CMDBController.upsertRow(sqlUpdate, sqlInsert);

            // update the new array; don't do this as could
            // end with a difference in realities if the SQL update failed
//            listNewClientEquityModel.add(cem);
        }

        ClientEquityAttributesModel.CLIENT_EQUITY_DB_MODELS = listNewClientEquityModel;
    }

    @Override
    public String toString()
    {
        return this.ticker;
    }

    public static Boolean checkDupeTicker(String ticker)
    {
        Boolean ret = false;

        for (ClientEquityAttributesModel cem : ClientEquityAttributesModel.CLIENT_EQUITY_DB_MODELS)
        {
            if (cem.getTicker().equalsIgnoreCase(ticker))
            {
                ret = true;
                break;
            }
        }

        return ret;
    }

    public Integer getJoomlaId()
    {
        return joomlaId;
    }

    public void setJoomlaId(Integer joomlaId)
    {
        this.joomlaId = joomlaId;
    }

    public String getTicker()
    {
        return ticker;
    }

    public void setTicker(String ticker)
    {
        this.ticker = ticker;
    }

    public String getTickerIEX()
    {
        return tickerIEX;
    }

    public void setTickerIEX(String tickerIEX)
    {
        this.tickerIEX = tickerIEX;
    }

    public String getActive()
    {
        return this.active;
    }

    public void setActive(String active)
    {
        this.active = active;
        this.bActive = active.equalsIgnoreCase("Yes");
    }

    public Integer getClientSectorId()
    {
        return this.clientSectorId;
    }

    public void setClientSectorId(Integer clientSectorId)
    {
        this.clientSectorId = clientSectorId;
        if (clientSectorId == null)
        {
            this.clientSectorModel = null;
            return;
        }

        for (ClientSectorModel csm : ClientSectorModel.getListClientSectorModel())
        {
            if (Objects.equals(csm.getClientSectorId(), this.clientSectorId))
            {
                this.clientSectorModel = ClientSectorModel.createNew(csm);
                break;
            }
        }
//        this.clientSectorModel = null;
    }

    public Double getTgtPct()
    {
        return this.tgtPct;
    }

    public void setTgtPct(Double tgtPct)
    {
        this.tgtPct = tgtPct;
    }

    public Double getAnalystTgt()
    {
        return this.analystTgt;
    }

    public void setAnalystTgt(Double analystTgt)
    {
        this.analystTgt = analystTgt;
    }

    public Double getStkPrice()
    {
        return this.stkPrice;
    }

    public void setStkPrice(Double stkPrice)
    {
        this.stkPrice = stkPrice;
    }

    public String getComment()
    {
        return this.comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public String getTgtLocked()
    {
        return this.tgtLocked;
    }

    public void setTgtLocked(String tgtLocked)
    {
        this.tgtLocked = tgtLocked;
        this.bTgtLocked = tgtLocked.equalsIgnoreCase("Yes");
    }

    public Double getActPct()
    {
        return this.actPct;
    }

    public void setActPct(Double actPct)
    {
        this.actPct = actPct;
    }

    public Boolean getbActive()
    {
        return this.bActive;
    }

    public void setbActive(Boolean bActive)
    {
        this.bActive = bActive;
        this.active = bActive ? "Yes" : "No";
    }

    public ClientSectorModel getClientSectorModel()
    {
        return this.clientSectorModel;
    }

    public Boolean getbTgtLocked()
    {
        return this.bTgtLocked;
    }

    public void setbTgtLocked(Boolean bTgtLocked)
    {
        this.bTgtLocked = bTgtLocked;
        this.tgtLocked = bTgtLocked ? "Yes" : "No";
    }

    public static ArrayList<ClientEquityAttributesModel> getListClientEquityModel()
    {
        return ClientEquityAttributesModel.CLIENT_EQUITY_DB_MODELS;
    }

    public void setClientSectorModel(ClientSectorModel clientSectorModel)
    {
        this.clientSectorModel = clientSectorModel;
    }
}
