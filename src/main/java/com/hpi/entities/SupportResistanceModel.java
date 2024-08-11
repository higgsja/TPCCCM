package com.hpi.entities;

import com.hpi.TPCCMcontrollers.*;
import static com.hpi.TPCCMcontrollers.CMDBController.getConnection;
import com.hpi.hpiUtils.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;

public class SupportResistanceModel
{
    private static final String SQL;
    private static final ArrayList<SupportResistanceModel> SR_MODELS;
    private static Integer joomlaId;
    private static String equityId;
    private Double srLevel;
    private Integer weight;

    static
    {
        SQL =
            "select * from hlhtxc5_dmOfx.SupResDay where JoomlaId = '%s' and EquityId = '%s';";
        SR_MODELS = new ArrayList<>();
    }

    public SupportResistanceModel(Double srLevel, Integer weight)
    {
        this.srLevel = srLevel;
        this.weight = weight;
    }

    public static void doSR(String equityId, Integer joomlaId)
    {
        String sql;
        ResultSet rs;
        SupportResistanceModel srModel;
        
        SupportResistanceModel.SR_MODELS.clear();

        SupportResistanceModel.equityId = equityId;
        SupportResistanceModel.joomlaId = joomlaId;

        sql = String.format(SupportResistanceModel.SQL,
            SupportResistanceModel.joomlaId,
            SupportResistanceModel.equityId);

        try (Connection con = getConnection();
             PreparedStatement pStmt = con.prepareStatement(sql))
        {
            pStmt.clearWarnings();
            rs = pStmt.executeQuery();
            
            while(rs.next())
            {
                srModel = new SupportResistanceModel(
                rs.getDouble("S/Rday"),
                rs.getInt("Weight"));
                SupportResistanceModel.SR_MODELS.add(srModel);
            }

            pStmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            String s = String.format(CMLanguageController.getErrorProps().
                getProperty("Formatted14"),
                e.toString());

            CMHPIUtils.showDefaultMsg(
                CMLanguageController.getErrorProps().getProperty("Title"),
                Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                s,
                JOptionPane.ERROR_MESSAGE);
        }

    }

    public static String getSQL()
    {
        return SQL;
    }

    public static ArrayList<SupportResistanceModel> getSR_MODELS()
    {
        return SR_MODELS;
    }

    public Integer getJoomlaId()
    {
        return joomlaId;
    }

    public void setJoomlaId(Integer joomlaId)
    {
        this.joomlaId = joomlaId;
    }

    public String getEquityId()
    {
        return equityId;
    }

    public void setEquityId(String equityId)
    {
        this.equityId = equityId;
    }

    public Double getSrLevel()
    {
        return srLevel;
    }

    public void setSrLevel(Double srLevel)
    {
        this.srLevel = srLevel;
    }

    public Integer getWeight()
    {
        return weight;
    }

    public void setWeight(Integer weight)
    {
        this.weight = weight;
    }
}
