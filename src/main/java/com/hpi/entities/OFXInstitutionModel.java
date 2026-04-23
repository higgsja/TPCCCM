package com.hpi.entities;

import com.hpi.TPCCMcontrollers.*;
import static com.hpi.TPCCMcontrollers.CMDBController.getConnection;
import com.hpi.hpiUtils.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;

public class OFXInstitutionModel
{

    String nameString;
    String fIdString;
    String orgString;
    String brokerIdString;
    String urlString;
    String ofxFailString;
    String sslFailString;
    String lastOfxValidationString;
    String lastSslValidationString;
    String profileString;
    
    public static ArrayList<OFXInstitutionModel> OFXINSTITUTION_MODELS;
    public static final String SQLINSERT;
    public static final String SQLUPDATE;
    public static final String SQLGETALL;

    static
    {
        OFXINSTITUTION_MODELS = new ArrayList<>();
         SQLINSERT = "insert into hlhtxc5_dmOfx.OfxInstitutions (Name, FId, Org, BrokerId, Url, OfxFail, SSlFail, LastOfxValidation, LastSslValidation) values (";
        SQLUPDATE = "update hlhtxc5_dmOfx.OfxInstitutions set ";
        SQLGETALL = "SELECT * FROM `OfxInstitutions` order by Name;";
    }

    public OFXInstitutionModel(String nameString, String fId) {
        this(nameString, fId, null, null, null, null, null, null, null, null);
    }
    
    public OFXInstitutionModel(String nameString, String fId, String org,
          String brokerId, String url, String ofxFail, String sslFail,
          String lastOfxValidation, String lastSslValidation,
          String profile)
    {
        this.nameString = nameString;
        this.fIdString = fId;
        this.orgString = org;
        this.brokerIdString = brokerId;
        this.urlString = url;
        this.ofxFailString = ofxFail;
        this.sslFailString = sslFail;
        this.lastOfxValidationString = lastOfxValidation;
        this.lastSslValidationString = lastSslValidation;
        this.profileString = profile;
    }
    
    public static final void doQuery()
    {
        ResultSet rs;
        String sql, s;
        OFXInstitutionModel tempOFXInstitutionModel;

        OFXINSTITUTION_MODELS.clear();

        sql = SQLGETALL;

        // query to get ohlcv data
        try (Connection con = getConnection();
             PreparedStatement pStmt = con.prepareStatement(sql))
        {
            pStmt.clearWarnings();
            rs = pStmt.executeQuery();

            while (rs.next())
            {
                tempOFXInstitutionModel = new OFXInstitutionModel(
                      rs.getString("Name"),
                      rs.getString("FId"),
                      rs.getString("Org"),
                      rs.getString("BrokerId"),
                      rs.getString("Url"),
                      rs.getString("OfxFail"),
                      rs.getString("SslFail"),
                      rs.getString("LastOfxValidation"),
                      rs.getString("LastSslValidation"),
                      rs.getString("Profile"));
                OFXINSTITUTION_MODELS.add(tempOFXInstitutionModel);
            }
            pStmt.close();
            con.close();
        }
        catch (SQLException e)
        {
            s = String.format(CMLanguageController.getErrorProps().
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
    
    @Override
    public String toString()
    {
        return this.nameString;
    }

    public static ArrayList<OFXInstitutionModel> getDataList()
    {
        return OFXINSTITUTION_MODELS;
    }

    public String getNameString()
    {
        return nameString;
    }

    public void setNameString(String nameString)
    {
        this.nameString = nameString;
    }

    public String getfIdString()
    {
        return fIdString;
    }

    public void setfIdString(String fIdString)
    {
        this.fIdString = fIdString;
    }

    public String getOrgString()
    {
        return orgString;
    }

    public void setOrgString(String orgString)
    {
        this.orgString = orgString;
    }

    public String getBrokerIdString()
    {
        return brokerIdString;
    }

    public void setBrokerIdString(String brokerIdString)
    {
        this.brokerIdString = brokerIdString;
    }

    public String getUrlString()
    {
        return urlString;
    }

    public void setUrlString(String urlString)
    {
        this.urlString = urlString;
    }

    public String getOfxFailString()
    {
        return ofxFailString;
    }

    public void setOfxFailString(String ofxFailString)
    {
        this.ofxFailString = ofxFailString;
    }

    public String getSslFailString()
    {
        return sslFailString;
    }

    public void setSslFailString(String sslFailString)
    {
        this.sslFailString = sslFailString;
    }

    public String getLastOfxValidationString()
    {
        return lastOfxValidationString;
    }

    public void setLastOfxValidationString(String lastOfxValidationString)
    {
        this.lastOfxValidationString = lastOfxValidationString;
    }

    public String getLastSslValidationString()
    {
        return lastSslValidationString;
    }

    public void setLastSslValidationString(String lastSslValidationString)
    {
        this.lastSslValidationString = lastSslValidationString;
    }

    public String getProfileString()
    {
        return profileString;
    }

    public void setProfileString(String profileString)
    {
        this.profileString = profileString;
    }

}
