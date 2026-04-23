package com.hpi.entities;

import java.util.Objects;

/**
 *
 * @author Joe@Higgs-Tx.com
 */
public class ClientSector
{

    // todo: delete these
    public static final int SECTOR_INDEX = 0;
    public static final int SHORT_INDEX = 1;
    public static final int ACTPCT_INDEX = 2;
    public static final int TGTPCT_INDEX = 3;
    public static final int TGTPCTADJ_INDEX = 4;
    public static final int TGTLOCKED_INDEX = 5;
    public static final int DESCR_INDEX = 6;
    Integer clientSectorId;
    String clientSector;
    String cSecShort;
    String active;
    private Boolean bActive;
    Double tgtPct;
    String description;
    String tgtLocked;
    private Boolean bTgtLocked;
    Double tgtPctAdj;
    Double actPct;
    Integer iCustomSector;
    Double mktVal;
    Double leveragedMktVal;

    public static final int ORDERBYTARGET = 0;
    public static final int ORDERBYSECTOR = 1;

    public ClientSector(Integer clientSectorId, String clientSector,
          String cSecShort, String active, String tgtLocked,
          Integer iCustomSector, Double tgtPct, Double mktVal,
          Double leveragedMktVal, String description, Double actPct)
    {
        this.clientSectorId = clientSectorId;
        this.clientSector = clientSector;
        this.cSecShort = cSecShort;
        this.active = active;
        this.bActive = this.active.equalsIgnoreCase("Yes");
        this.tgtLocked = tgtLocked;
        this.bTgtLocked = this.tgtLocked.equalsIgnoreCase("Yes");
        this.iCustomSector = iCustomSector;
        this.tgtPct = tgtPct;
        this.mktVal = mktVal;
        this.leveragedMktVal = leveragedMktVal;
        this.description = description;
        this.actPct = actPct;
        this.tgtPctAdj = tgtPct;
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

    public Double getTgtPct()
    {
        return tgtPct;
    }

    public void setTgtPct(Double tgtPct)
    {
        this.tgtPct = tgtPct;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getTgtLocked()
    {
        return tgtLocked;
    }

    public void setTgtLocked(String tgtLocked)
    {
        this.tgtLocked = tgtLocked;
        this.bTgtLocked = this.tgtLocked.equalsIgnoreCase("yes");
    }

    public Boolean getbTgtLocked()
    {
        return bTgtLocked;
    }

    public void setbTgtLocked(Boolean bTgtLocked)
    {
        this.bTgtLocked = bTgtLocked;
        this.tgtLocked = bTgtLocked ? "Yes" : "No";
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

    public String getActive()
    {
        return this.active;
    }

    public void setActive(String active)
    {
        this.active = active;
        this.bActive = this.active.equalsIgnoreCase("yes");
    }

    public Double getTgtPctAdj()
    {
        return tgtPctAdj;
    }

    public void setTgtPctAdj(Double tgtPctAdj)
    {
        this.tgtPctAdj = tgtPctAdj;
    }

    public Double getActPct()
    {
        return actPct;
    }

    public void setActPct(Double actPct)
    {
        this.actPct = actPct;
    }

    public Integer getClientSectorId()
    {
        return clientSectorId;
    }

    public void setClientSectorId(Integer clientSectorId)
    {
        this.clientSectorId = clientSectorId;
    }

    public Integer getiCustomSector()
    {
        return iCustomSector;
    }

    public void setiCustomSector(Integer iCustomSector)
    {
        this.iCustomSector = iCustomSector;
    }

    public Double getMktVal()
    {
        return this.mktVal;
    }

    public void setMktVal(Double mktVal)
    {
        this.mktVal = mktVal;
    }

    public Double getLeveragedMktVal()
    {
        return this.leveragedMktVal;
    }

    public void setLeveragedMktVal(Double leveragedMktVal)
    {
        this.leveragedMktVal = leveragedMktVal;
    }
    
    @Override
    public String toString()
    {
        return this.clientSector;
    }

    /**
     * Returns true/false on equality of the objects.
     *
     * @param cs
     *
     * @return
     */
    @Override
    public boolean equals(Object cs)
    {
        ClientSector tClientSector;

        if (!(cs instanceof ClientSector))
        {
            return false;
        }

        tClientSector = (ClientSector) cs;

        return this.clientSectorId.equals(tClientSector.clientSectorId)
              && this.clientSector.equals(tClientSector.clientSector)
              && this.cSecShort.equals(tClientSector.cSecShort)
              && this.active.equals(tClientSector.active)
              && this.bActive.equals(tClientSector.bActive)
              && this.tgtPct.equals(tClientSector.tgtPct)
              && String.valueOf(this.description).
                    equals(String.valueOf(tClientSector.description))
              && this.tgtLocked.equals(tClientSector.tgtLocked)
              && this.bTgtLocked.equals(tClientSector.bTgtLocked)
              && this.tgtPctAdj.equals(tClientSector.tgtPctAdj)
              && this.actPct.equals(tClientSector.actPct)
              && this.iCustomSector.equals(tClientSector.iCustomSector)
              && this.mktVal.equals(tClientSector.mktVal)
              && this.leveragedMktVal.equals(tClientSector.leveragedMktVal);
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.clientSectorId);
        return hash;
    }
}
