package com.hpi.TPCCMprefs;

import java.util.ArrayList;
import lombok.*;
@Getter @Setter
public class CMOfxDLFIModel
{
    private String fiName;
    private String fiOrg;
    private String brokerId;
    private String fiId;
    private String fiUrl;
    private String active;
    private String tokenRefresh;
    private String clientId;
    private String httpSchema;
    private String httpHost;
    private String httpPath;
    private String debugBytes;
    private ArrayList<CMOfxDLAccountModel> accountModels;

    public CMOfxDLFIModel()
    {
        this.fiName = "";
        this.fiOrg = "";
        this.brokerId = "";
        this.fiId = "";
        this.fiUrl = "";
        this.active = "";
        this.tokenRefresh = "";
        this.clientId = "";
        this.httpSchema = "";
        this.httpHost = "";
        this.httpPath = "";
        this.debugBytes = "";
        this.accountModels = new ArrayList<>();
    }

    public CMOfxDLFIModel(String fiName, String fiOrg, String brokerId,
        String fiId, String fiUrl, String active)
    {
        this.fiName = fiName;
        this.fiOrg = fiOrg;
        this.brokerId = brokerId;
        this.fiId = fiId;
        this.fiUrl = fiUrl;
        this.active = active;
        this.tokenRefresh = "";
        this.clientId = "";
        this.httpSchema = "";
        this.httpHost = "";
        this.httpPath = "";
        this.debugBytes = "";
        accountModels = new ArrayList<>();
    }

    @Override
    public String toString()
    {
        return this.fiName;
    }

/*    public String getFiName()
    {
        return fiName;
    }

    public String getFiOrg()
    {
        return fiOrg;
    }

    public String getBrokerId()
    {
        return brokerId;
    }

    public String getFiId()
    {
        return fiId;
    }

    public String getFiUrl()
    {
        return fiUrl;
    }

    public ArrayList<CMOfxDLAccountModel> getAccountModels()
    {
        return accountModels;
    }

    public String getActive()
    {
        return active;
    }

    public void setFiName(String fiName)
    {
        this.fiName = fiName;
    }

    public void setFiOrg(String fiOrg)
    {
        this.fiOrg = fiOrg;
    }

    public void setBrokerId(String brokerId)
    {
        this.brokerId = brokerId;
    }

    public void setFiId(String fiId)
    {
        this.fiId = fiId;
    }

    public void setFiUrl(String fiUrl)
    {
        this.fiUrl = fiUrl;
    }

    public void setsActive(String active)
    {
        this.active = active;
    }

    public String getTokenRefresh()
    {
        return tokenRefresh;
    }

    public void setTokenRefresh(String tokenRefresh)
    {
        this.tokenRefresh = tokenRefresh;
    }

    public String getClientId()
    {
        return clientId;
    }

    public void setClientId(String clientId)
    {
        this.clientId = clientId;
    }

    public String getHttpHost()
    {
        return httpHost;
    }

    public void setHttpHost(String httpHost)
    {
        this.httpHost = httpHost;
    }

    public String getHttpPath()
    {
        return httpPath;
    }

    public void setHttpPath(String httpPath)
    {
        this.httpPath = httpPath;
    }

    public String getDebugBytes()
    {
        return debugBytes;
    }

    public void setDebugBytes(String debugBytes)
    {
        this.debugBytes = debugBytes;
    }

    public String getHttpSchema()
    {
        return httpSchema;
    }

    public void setHttpSchema(String httpSchema)
    {
        this.httpSchema = httpSchema;
    }
*/
}
