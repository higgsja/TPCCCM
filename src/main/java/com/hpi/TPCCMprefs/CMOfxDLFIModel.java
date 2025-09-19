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
    private String clientSecret;
    private String httpSchema;
    private String httpHost;
    private String httpPath;
    private String debugBytes;
    private String redirectUrl;
    private String httpTimeout;
    private String scope;
    private String authUrl;
    private String authTokenUrl;
    private String marketUrl;
    private String traderUrl;
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
        this.clientSecret = "";
        this.httpSchema = "";
        this.httpHost = "";
        this.httpPath = "";
        this.debugBytes = "";
        this.redirectUrl = "";
        this.httpTimeout = "";
        this.scope = "";
        this.authUrl = "";
        this.authTokenUrl = "";
        this.marketUrl = "";
        this.traderUrl = "";
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
        this.clientSecret = "";
        this.httpSchema = "";
        this.httpHost = "";
        this.httpPath = "";
        this.redirectUrl = "";
        this.debugBytes = "";
        this.redirectUrl = "";
        this.httpTimeout = "";
        this.scope = "";
        this.authUrl = "";
        this.authTokenUrl = "";
        this.marketUrl = "";
        this.traderUrl = "";
        accountModels = new ArrayList<>();
    }

    @Override
    public String toString()
    {
        return this.fiName;
    }
}
