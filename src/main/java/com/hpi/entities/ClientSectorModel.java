package com.hpi.entities;

import com.hpi.TPCCMsql.CMDAOException;
import com.hpi.TPCCMprefs.CMDBModel;
import com.hpi.TPCCMcontrollers.CMDBController;
import com.hpi.TPCCMcontrollers.CMLanguageController;
import java.sql.*;
import java.util.*;
import javax.swing.*;

public class ClientSectorModel
{

    public static final String SELECT_SECTOR
        = "select ClientSectorId, ClientSector from hlhtxc5_dmOfx.ClientSectorList where JoomlaId = '%s' and ClientSector = '%s';";

    public static final String CASH_SECTORID
        = "select ClientSectorId from hlhtxc5_dmOfx.ClientSectorList where ClientSector = 'CASH' and JoomlaId = '%s';";

    public static final String FORCE_UPDATE_ACTIVE
        = "update hlhtxc5_dmOfx.ClientSectorList set Active = if(isnull(ActPct), if(isnull(TgtPct), Active, if(TgtPct > 0, 'Yes', Active)), if(ActPct > 0, 'Yes', Active)) where JoomlaId = '%s';";

    //
    public static final String UPDATE_MKTVAL_LMKTVAL
        = "update hlhtxc5_dmOfx.ClientSectorList as CSL left join (select FIFOOpenTransactions.ClientSectorId, sum(FIFOOpenTransactions.MktVal) as sumMktVal, sum(FIFOOpenTransactions.LMktVal) as sumLMktVal from hlhtxc5_dmOfx.ClientSectorList as CSL, hlhtxc5_dmOfx.FIFOOpenTransactions where CSL.JoomlaId = '%s' and CSL.JoomlaId = FIFOOpenTransactions.JoomlaId and CSL.ClientSectorId = FIFOOpenTransactions.ClientSectorId group by FIFOOpenTransactions.ClientSectorId) as A on A.ClientSectorId = CSL.ClientSectorId set CSL.MktVal = if(isnull(A.sumMktVal), 0, A.sumMktVal), CSL.LMktVal = if(isnull(A.sumLMktVal), 0, A.sumLMktVal);";

    Integer joomlaId;
    Integer clientSectorId;
    String clientSector;
    String cSecShort;
    String active;
    Boolean bActive;
    Double tgtPct;
    String comment;
    String tgtLocked;
    Boolean bTgtLocked;
    Double actPct;
    Double mktVal;
    Double lMktVal;
    Integer customSector;
    private static ArrayList<ClientSectorModel> CLIENT_SECTOR_DB_MODELS;
    public static final String SQLSELECT;
    public static final String SQLINSERT;
    public static final String SQLUPDATE;

    static {
        SQLSELECT
            = "select CSecShort, ClientSectorId, Active, Comment, JoomlaId, ClientSector, TgtPct, TgtLocked, ActPct, MktVal, LMktVal, CustomSector from hlhtxc5_dmOfx.ClientSectorList where JoomlaId = 'jid' order by CSecShort asc;";
        SQLINSERT
            = "insert into hlhtxc5_dmOfx.ClientSectorList (JoomlaId, ClientSectorId, ClientSector, CSecShort, Active, TgtPct, Comment, TgtLocked, ActPct, MktVal, LMktVal, CustomSector) values (";
        SQLUPDATE = "update hlhtxc5_dmOfx.ClientSectorList set ";
        ClientSectorModel.CLIENT_SECTOR_DB_MODELS = new ArrayList<>();
    }

    /**
     *
     * @param joomlaId
     * @param clientSectorId
     * @param clientSector
     * @param cSecShort
     * @param active
     * @param tgtPct
     * @param comment
     * @param tgtLocked
     * @param actPct
     * @param mktVal
     * @param lMktVal
     * @param customSector
     */
    public ClientSectorModel(Integer joomlaId,
        Integer clientSectorId,
        String clientSector,
        String cSecShort,
        String active,
        Double tgtPct,
        String comment,
        String tgtLocked,
        Double actPct,
        Double mktVal,
        Double lMktVal,
        Integer customSector)
    {
        this.joomlaId = joomlaId;
        this.clientSectorId = clientSectorId;
        this.clientSector = clientSector;
        this.cSecShort = cSecShort;
        this.active = active;
        this.bActive = active.equalsIgnoreCase("Yes");
        this.tgtPct = tgtPct;
        this.comment = comment;
        this.tgtLocked = tgtLocked;
        this.bTgtLocked = tgtLocked.equalsIgnoreCase("Yes");
        this.actPct = actPct;
        this.mktVal = mktVal;
        this.lMktVal = lMktVal;
        this.customSector = customSector;
    }

    @Override
    public final String toString()
    {
        return this.cSecShort;
    }

    public static final ClientSectorModel createNew(ResultSet rs)
        throws SQLException
    {
        return new ClientSectorModel(
            rs.getInt("JoomlaId"),
            rs.getInt("ClientSectorId"),
            rs.getString("ClientSector"),
            rs.getString("CSecShort"),
            rs.getString("Active"),
            rs.getDouble("TgtPct"),
            rs.getString("Comment"),
            rs.getString("TgtLocked"),
            rs.getDouble("ActPct"),
            rs.getDouble("MktVal"),
            rs.getDouble("LMktVal"),
            rs.getInt("CustomSector"));
    }

    public static final ClientSectorModel createNew(ClientSectorModel csm)
    {
        return new ClientSectorModel(csm.getJoomlaId(),
            csm.getClientSectorId(),
            csm.getClientSector(),
            csm.getcSecShort(),
            csm.getActive(),
            csm.getTgtPct(),
            csm.getComment(),
            csm.getTgtLocked(),
            csm.getActPct(),
            csm.getMktVal(),
            csm.getlMktVal(),
            csm.getCustomSector());
    }

    public static final void getSectors()
    {
        ResultSet rs;
        ClientSectorModel csm;
        Integer userId;
        String sql;

        userId = CMDBModel.getUserId();

        ClientSectorModel.CLIENT_SECTOR_DB_MODELS.clear();

        sql = ClientSectorModel.SQLSELECT.replace("jid",
            userId.toString());

        try (Connection con = CMDBController.getConnection();
            PreparedStatement pStmt = con.prepareStatement(sql)) {
            rs = pStmt.executeQuery();

            while (rs.next()) {
                csm = ClientSectorModel.createNew(rs);

                ClientSectorModel.CLIENT_SECTOR_DB_MODELS.add(csm);
            }
            rs.close();
            pStmt.close();
            con.close();
        } catch (SQLException ex) {
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
     * Update the database for Sectors
     *
     * @param listCSM
     */
    public static void updateSectors(ArrayList<ClientSectorModel> listCSM)
    {
        Integer userId;
        ArrayList<ClientSectorModel> listNewClientSectorModel;
        Integer row;
        String sqlInsert, sqlUpdate;

        userId = CMDBModel.getUserId();
        listNewClientSectorModel = new ArrayList<>();

        row = 0;

        for (ClientSectorModel csm : listCSM) {
            // 3 cases:
            // added a new one
            // changed Sector or Comment
            // no change
            // Comment can be null causing a problem
            // If added one, arrays are different sizes
            if ((row <= ClientSectorModel.CLIENT_SECTOR_DB_MODELS.size() - 1)) {
                if (csm.clientSector.equalsIgnoreCase(ClientSectorModel.CLIENT_SECTOR_DB_MODELS.
                    get(row).clientSector)
                    && csm.cSecShort.equalsIgnoreCase(ClientSectorModel.CLIENT_SECTOR_DB_MODELS.
                        get(row).cSecShort)
                    && Objects.equals(csm.comment,
                        CLIENT_SECTOR_DB_MODELS.get(row).comment)) {
                    listNewClientSectorModel.add(csm);
                    row++;
                    continue;
                }
            }

            row++;
            sqlInsert = ClientSectorModel.SQLINSERT
                + "'" + csm.getJoomlaId() + "', "
                //                  + csm.getClientSectorId() + "', '"
                + null + ", '"
                + csm.getClientSector() + "', '"
                + csm.getcSecShort() + "', '"
                + csm.getActive() + "', '"
                + csm.getTgtPct() + "', '"
                + csm.getComment() + "', '"
                + csm.getTgtLocked() + "', '"
                + csm.getActPct() + "', '"
                + csm.getMktVal() + "', '"
                + csm.getlMktVal() + "', '"
                + csm.getCustomSector() + "'"
                + ");";

            sqlUpdate = ClientSectorModel.SQLUPDATE
                // + "JoomlaId = '" + cem.getJoomlaId() + "', "
                // + "ClientSectorId = '" + cem.getTicker() + "', "
                + "ClientSector = '" + csm.getClientSector() + "', "
                + "CSecShort = '" + csm.getcSecShort() + "', "
                + "Active = '" + csm.getActive() + "', "
                + "TgtPct = '" + csm.getTgtPct() + "', "
                + "Comment = '" + csm.getComment() + "', "
                + "TgtLocked = '" + csm.getTgtLocked() + "', "
                + "ActPct = '" + csm.getActPct() + "', "
                + "MktVal = '" + csm.getMktVal() + "',  "
                + "LMktVal = '" + csm.getlMktVal() + "',  "
                + "CustomSector = '" + csm.getCustomSector() + "' "
                + "where JoomlaId = '" + userId + "' "
                + "and ClientSectorId = '" + csm.getClientSectorId() + "';";

            CMDBController.upsertRow(sqlUpdate,
                sqlInsert);

            listNewClientSectorModel.add(csm);
        }

        ClientSectorModel.CLIENT_SECTOR_DB_MODELS = listNewClientSectorModel;
    }

    public static Boolean checkDupeSector(String sectorString)
    {
        Boolean ret = false;

        for (ClientSectorModel csm : ClientSectorModel.CLIENT_SECTOR_DB_MODELS) {
            if (csm.getClientSector()
                .equalsIgnoreCase(sectorString)) {
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

    public Integer getClientSectorId()
    {
        return clientSectorId;
    }

    public void setClientSectorId(Integer clientSectorId)
    {
        this.clientSectorId = clientSectorId;
    }

    public String getClientSector()
    {
        return clientSector;
    }

    public void setClientSector(String clientSector)
    {
        this.clientSector = clientSector;
    }

    public String getcSecShort()
    {
        return cSecShort;
    }

    public void setcSecShort(String cSecShort)
    {
        this.cSecShort = cSecShort;
    }

    public String getActive()
    {
        return active;
    }

    public void setbActive(Boolean bActive)
    {
        this.bActive = bActive;
        this.setActive(bActive == true ? "Yes" : "No");
    }

    public void setActive(String active)
    {
        this.active = active;
        this.bActive = active.equalsIgnoreCase("Yes");
    }

    public Double getTgtPct()
    {
        return tgtPct;
    }

    public void setTgtPct(Double tgtPct)
    {
        this.tgtPct = tgtPct;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public String getTgtLocked()
    {
        return tgtLocked;
    }

    public void setTgtLocked(String tgtLocked)
    {
        this.tgtLocked = tgtLocked;
        this.bTgtLocked = tgtLocked.equalsIgnoreCase("Yes");
    }

    public Double getMktVal()
    {
        return mktVal;
    }

    public void setMktVal(Double mktVal)
    {
        this.mktVal = mktVal;
    }

    public Double getlMktVal()
    {
        return lMktVal;
    }

    public void setlMktVal(Double lMktVal)
    {
        this.lMktVal = lMktVal;
    }

    public Integer getCustomSector()
    {
        return customSector;
    }

    public void setCustomSector(Integer customSector)
    {
        this.customSector = customSector;
    }

    public static ArrayList<ClientSectorModel> getListClientSectorModel()
    {
        return CLIENT_SECTOR_DB_MODELS;
    }

    public Boolean getbActive()
    {
        return bActive;
    }

    public Boolean getbTgtLocked()
    {
        return bTgtLocked;
    }

    public Double getActPct()
    {
        return actPct;
    }

    public void setbTgtLocked(Boolean bTgtLocked)
    {
        this.bTgtLocked = bTgtLocked;
        this.tgtLocked = bTgtLocked ? "Yes" : "No";
    }

    public void setActPct(Double actPct)
    {
        this.actPct = actPct;
    }
}
