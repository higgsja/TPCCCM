package com.hpi.TPCCMprefs;

import com.hpi.TPCCMcontrollers.CMLanguageController;
import com.hpi.hpiUtils.CMHPIUtils;
import java.io.*;
import java.nio.charset.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import javax.swing.JOptionPane;
import static org.apache.commons.io.FileUtils.copyToFile;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Reads the ofx_download.config file to set up the financial institutions
 * and accounts to directly download OFX data from.
 */
public class CMOfxDirectModel
    extends CMXMLModelBase
{

    private final static ArrayList<CMOfxDLFIModel> FIMODELS;
    // singleton //
    private static CMOfxDirectModel instance;

    static
    {
        CMOfxDirectModel.instance = null;
        FIMODELS = new ArrayList<>();
    }

    protected CMOfxDirectModel(String sConfigFilename)
    {
        super(sConfigFilename);

        this.initModel();
    }

    public synchronized static CMOfxDirectModel getInstance(
        String sConfigFilename)
    {
        if (CMOfxDirectModel.instance == null)
        {
            CMOfxDirectModel.instance =
                 new CMOfxDirectModel(sConfigFilename);
        }

        return CMOfxDirectModel.instance;
    }

    public synchronized static CMOfxDirectModel getInstance()
    {
        if (CMOfxDirectModel.instance == null)
        {
            throw (new UnsupportedOperationException(
                "OfxDLModel instance not initialized."));
        }

        return CMOfxDirectModel.instance;
    }
    // singleton

    public final void configVersionChange()
    {

    }

    /**
     * Initialized the data model, enables read and write of config file
     */
    public final void initModel()
    {
        File file;
        Document doc;
        Element eTemp;

        file = new File(this.getConfigFullPath());

        if (!file.exists())
        {
            // copy default file
            try
            {
                copyToFile(getClass().getClassLoader().
                    getResourceAsStream("res/" + this.configFilename),
                    new File(this.configFullPath));
            }
            catch (IOException e)
            {
                CMHPIUtils.showDefaultMsg(
                    CMLanguageController.getErrorProp("Error"),
                    this.getClass().getName(),
                    Thread.currentThread().getStackTrace()[1].
                        getMethodName(),
                    e.toString(), JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try (InputStream is = new FileInputStream(
            this.getConfigFullPath()))
        {

            doc = Jsoup.parse(is, "UTF-8", "");
        }
        catch (IOException e)
        {
            CMHPIUtils.showDefaultMsg(
                CMLanguageController.getErrorProp("Error"),
                this.getClass().getName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                e.toString(), JOptionPane.ERROR_MESSAGE);
            return;
        }

        eTemp = doc.select("ofxDownload").first();

        this.readOfxDirect(eTemp);
    }

    public void readOfxDirect(Element aElement)
    {
        Element element;
        Iterator<Element> iterator;
        String s;

        FIMODELS.clear();

        if (aElement == null)
        {
            CMHPIUtils.showDefaultMsg(
                CMLanguageController.getErrorProp("Error"),
                this.getClass().getName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                "File error: element is null.",
                JOptionPane.ERROR_MESSAGE);

            throw (new UnsupportedOperationException());
        }

        // aElement points to 'ofxDownload'
        iterator = aElement.children().iterator();

        while (iterator.hasNext())
        {
            element = iterator.next();

            switch (element.tagName().toLowerCase(Locale.US))
            {
                case "financialinstitutions":
                    this.readFIs(element);
                    break;
                default:
                    s = String.format(CMLanguageController.getErrorProp(
                        "Formatted3"), element.tagName());

                    CMHPIUtils.showDefaultMsg(
                        CMLanguageController.getErrorProp("Title"),
                        this.getClass().getName(),
                        Thread.currentThread().getStackTrace()[1].
                            getMethodName(),
                        s,
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     *
     */
    @Override
    public void write()
    {
        Integer iTab;
        String endLine;

        if (this.bDirty == false)
        {
            return;
        }

        iTab = 0;
        endLine = System.getProperty("line.separator");

        try (BufferedWriter writer = new BufferedWriter(
            new OutputStreamWriter(
                new FileOutputStream(this.configFullPath),
                StandardCharsets.UTF_8)))
        {
            writer.write("<ofxDownload>");
            writer.write(endLine);
            iTab++;

            writer.write(CMHPIUtils.charFill(iTab, "\t".charAt(0)));
            writer.write("<financialInstitutions>");
            writer.write(endLine);
            iTab++;

            // loop through the institutions
            for (CMOfxDLFIModel fiModel : FIMODELS)
            {
                writer.write(CMHPIUtils.
                    charFill(iTab, "\t".charAt(0)));
                writer.write("<financialInstitution>");
                writer.write(endLine);
                iTab++;

                writer.write(CMHPIUtils.
                    charFill(iTab, "\t".charAt(0)));
                writer.write("<finame>");
                writer.write(fiModel.getFiName());
                writer.write("</finame>");
                writer.write(endLine);

                writer.write(CMHPIUtils.
                    charFill(iTab, "\t".charAt(0)));
                writer.write("<fiOrg>");
                writer.write(fiModel.getFiOrg());
                writer.write("</fiorg>");
                writer.write(endLine);

                writer.write(CMHPIUtils.
                    charFill(iTab, "\t".charAt(0)));
                writer.write("<brokerId>");
                writer.write(fiModel.getBrokerId());
                writer.write("</brokerId>");
                writer.write(endLine);

                writer.write(CMHPIUtils.
                    charFill(iTab, "\t".charAt(0)));
                writer.write("<fiId>");
                writer.write(fiModel.getFiId());
                writer.write("</fiId>");
                writer.write(endLine);

                writer.write(CMHPIUtils.
                    charFill(iTab, "\t".charAt(0)));
                writer.write("<fiUrl>");
                writer.write(fiModel.getFiUrl());
                writer.write("</fiUrl>");
                writer.write(endLine);

                writer.write(CMHPIUtils.
                    charFill(iTab, "\t".charAt(0)));
                writer.write("<Active>");
                writer.write(fiModel.getActive());
                writer.write("</Active>");
                writer.write(endLine);

                writer.write(CMHPIUtils.
                    charFill(iTab, "\t".charAt(0)));
                writer.write("<directAccess>");
                writer.write(endLine);
                iTab++;

//                writer.write(CMHPIUtils.
//                    charFill(iTab, "\t".charAt(0)));
//                writer.write("<tokenRefresh>");
//                writer.write(fiModel.getTokenRefresh());
//                writer.write("</tokenRefresh>");
//                writer.write(endLine);

                writer.write(CMHPIUtils.
                    charFill(iTab, "\t".charAt(0)));
                writer.write("<tokenRefresh>");
                writer.write(fiModel.getTokenRefresh());
                writer.write("</tokenRefresh>");
                writer.write(endLine);

                writer.write(CMHPIUtils.
                    charFill(iTab, "\t".charAt(0)));
                writer.write("<clientId>");
                writer.write(fiModel.getClientId());
                writer.write("</clientId>");
                writer.write(endLine);

                writer.write(CMHPIUtils.
                    charFill(iTab, "\t".charAt(0)));
                writer.write("<httpSchema>");
                writer.write(fiModel.getHttpSchema());
                writer.write("</httpSchema>");
                writer.write(endLine);

                writer.write(CMHPIUtils.
                    charFill(iTab, "\t".charAt(0)));
                writer.write("<httpHost>");
                writer.write(fiModel.getHttpHost());
                writer.write("</httpHost>");
                writer.write(endLine);

                writer.write(CMHPIUtils.
                    charFill(iTab, "\t".charAt(0)));
                writer.write("<httpPath>");
                writer.write(fiModel.getHttpPath());
                writer.write("</httpPath>");
                writer.write(endLine);

                writer.write(CMHPIUtils.
                    charFill(iTab, "\t".charAt(0)));
                writer.write("<debugbytes>");
                writer.write(fiModel.getDebugBytes());
                writer.write("</debugBytes>");
                writer.write(endLine);

                iTab--;
                writer.write(CMHPIUtils.
                    charFill(iTab, "\t".charAt(0)));
                writer.write("</directAccess>");
                writer.write(endLine);

                writer.write(CMHPIUtils.
                    charFill(iTab, "\t".charAt(0)));
                writer.write("<Accounts>");
                writer.write(endLine);
                iTab++;

                // accounts
                for (CMOfxDLAccountModel acctModel : fiModel.getAccountModels())
                {
                    writer.write(CMHPIUtils.
                        charFill(iTab, "\t".charAt(0)));
                    writer.write("<Account>");
                    writer.write(endLine);
                    iTab++;

                    writer.write(CMHPIUtils.
                        charFill(iTab, "\t".charAt(0)));
                    writer.write("<acctNumber>");
                    writer.write(acctModel.getAcctNumber());
                    writer.write("</acctNumber>");
                    writer.write(endLine);

                    writer.write(CMHPIUtils.
                        charFill(iTab, "\t".charAt(0)));
                    writer.write("<acctName>");
                    writer.write(acctModel.getAcctName());
                    writer.write("</acctName>");
                    writer.write(endLine);

                    writer.write(CMHPIUtils.
                        charFill(iTab, "\t".charAt(0)));
                    writer.write("<acctUId>");
                    writer.write(acctModel.getAcctUId());
                    writer.write("</acctUId>");
                    writer.write(endLine);

                    writer.write(CMHPIUtils.
                        charFill(iTab, "\t".charAt(0)));
                    writer.write("<acctPW>");
                    writer.write(acctModel.getAcctPW());
                    writer.write("</acctPW>");
                    writer.write(endLine);

                    writer.write(CMHPIUtils.
                        charFill(iTab, "\t".charAt(0)));
                    writer.write("<Active>");
                    writer.write(acctModel.getAcctActive());
                    writer.write("</Active>");
                    writer.write(endLine);

                    iTab--;
                    writer.write(CMHPIUtils.
                        charFill(iTab, "\t".charAt(0)));
                    writer.write("</Account>");
                    writer.write(endLine);
                }

                iTab--;
                writer.write(CMHPIUtils.
                    charFill(iTab, "\t".charAt(0)));
                writer.write("</Accounts>");
                writer.write(endLine);

                iTab--;
                writer.write(CMHPIUtils.
                    charFill(iTab, "\t".charAt(0)));
                writer.write("</financialInstitution>");
                writer.write(endLine);
            }

            iTab--;
            writer.write(CMHPIUtils.charFill(iTab, "\t".charAt(0)));
            writer.write("</financialInstitutions>");
            writer.write(endLine);

            iTab--;
            writer.write(CMHPIUtils.charFill(iTab, "\t".charAt(0)));
            writer.write("</ofxDownload>");
            writer.write(endLine);

            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
            CMHPIUtils.showDefaultMsg(
                CMLanguageController.getErrorProp("Error"),
                this.getClass().getName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                e.toString(), JOptionPane.ERROR_MESSAGE);
            return;
        }

        this.bDirty = false;
    }

    private void readFIs(Element aElement)
    {
        Element element;
        Iterator<Element> iterator;
        String s;

        if (aElement == null)
        {
            CMHPIUtils.showDefaultMsg(
                CMLanguageController.getErrorProp("Error"),
                this.getClass().getName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                "File error: element is null.",
                JOptionPane.ERROR_MESSAGE);

            throw (new UnsupportedOperationException());
        }

        // aElement points to 'financialInstitutions'
        iterator = aElement.children().iterator();

        while (iterator.hasNext())
        {
            element = iterator.next();

            switch (element.tagName().toLowerCase(Locale.US))
            {
                case "financialinstitution":
                    this.readFI(element);
                    break;
                default:
                    s = String.format(CMLanguageController.
                        getErrorProp("Formatted3"), element.tagName());

                    CMHPIUtils.showDefaultMsg(
                        CMLanguageController.getErrorProp("Title"),
                        this.getClass().getName(),
                        Thread.currentThread().getStackTrace()[1].
                            getMethodName(),
                        s,
                        JOptionPane.ERROR_MESSAGE);

                    throw (new UnsupportedOperationException());
            }
        }
    }

    private void readFI(Element aElement)
    {
        Element element;
        Iterator<Element> iterator;
        String s;
        CMOfxDLFIModel tempFIModel;

        // aElement points to 'financialInstitution'
        if (aElement == null)
        {
            CMHPIUtils.showDefaultMsg(
                CMLanguageController.getErrorProp("Error"),
                this.getClass().getName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                "File error: element is null.",
                JOptionPane.ERROR_MESSAGE);

            throw (new UnsupportedOperationException());
        }

        iterator = aElement.children().iterator();

        tempFIModel = new CMOfxDLFIModel();

        while (iterator.hasNext())
        {
            element = iterator.next();

            switch (element.tagName().toLowerCase(Locale.US))
            {
                case "finame":
                    tempFIModel.setFiName(element.ownText());
                    break;
                case "fiorg":
                    tempFIModel.setFiOrg(element.ownText());
                    break;
                case "brokerid":
                    tempFIModel.setBrokerId(element.ownText());
                    break;
                case "fiid":
                    tempFIModel.setFiId(element.ownText());
                    break;
                case "fiurl":
                    tempFIModel.setFiUrl(element.ownText());
                    break;
                case "accounts":
                    readAccounts(element, tempFIModel);
                    break;
                case "active":
                    tempFIModel.setActive(element.ownText());
                    break;
                case "directaccess":
                    readDirectAccess(element, tempFIModel);
                    break;
                default:
                    s = String.format(CMLanguageController.getErrorProp(
                        "Formatted3"), element.tagName());

                    CMHPIUtils.showDefaultMsg(
                        CMLanguageController.getErrorProp("Title"),
                        this.getClass().getName(),
                        Thread.currentThread().getStackTrace()[1].
                            getMethodName(),
                        s,
                        JOptionPane.ERROR_MESSAGE);

                    throw (new UnsupportedOperationException());
            }
        }

        if (tempFIModel.getFiName().isEmpty() //              || tempFIModel.getFiOrg().isEmpty()
            //              || tempFIModel.getBrokerId().isEmpty()
            //              || tempFIModel.getFiId().isEmpty()
            ||
             tempFIModel.getFiUrl().isEmpty())
        {
            return;
        }
        FIMODELS.add(tempFIModel);
    }

    private void readAccounts(Element aElement, CMOfxDLFIModel tempFIModel)
    {
        Element element;
        Iterator<Element> iterator;
        String s;

        // aElement points to 'accounts'
        if (aElement == null)
        {
            CMHPIUtils.showDefaultMsg(
                CMLanguageController.getErrorProp("Error"),
                this.getClass().getName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                "File error: element is null.",
                JOptionPane.ERROR_MESSAGE);

            throw (new UnsupportedOperationException());
        }

        iterator = aElement.children().iterator();

        while (iterator.hasNext())
        {
            element = iterator.next();

            switch (element.tagName().toLowerCase(Locale.US))
            {
                case "account":
                    readAccount(element, tempFIModel);
                    break;
                default:
                    s = String.format(CMLanguageController.getErrorProp(
                        "Formatted3"), element.tagName());

                    CMHPIUtils.showDefaultMsg(
                        CMLanguageController.getErrorProp("Title"),
                        this.getClass().getName(),
                        Thread.currentThread().getStackTrace()[1].
                            getMethodName(),
                        s,
                        JOptionPane.ERROR_MESSAGE);

                    throw (new UnsupportedOperationException());
            }
        }
    }

    private void readDirectAccess(Element aElement, CMOfxDLFIModel tempFIModel)
    {
        Element element;
        Iterator<Element> iterator;
        String s;

        // aElement points to 'directAccess'
        iterator = aElement.children().iterator();

        while (iterator.hasNext())
        {
            element = iterator.next();

            switch (element.tagName().toLowerCase(Locale.US))
            {
                case "tokenrefresh":
                    tempFIModel.setTokenRefresh(element.ownText());
                    break;
                case "clientid":
                    tempFIModel.setClientId(element.ownText());
                    break;
                case "httpschema":
                    tempFIModel.setHttpSchema(element.ownText());
                    break;
                case "httphost":
                    tempFIModel.setHttpHost(element.ownText());
                    break;
                case "httppath":
                    tempFIModel.setHttpPath(element.ownText());
                    break;
                case "debugbytes":
                    tempFIModel.setDebugBytes(element.ownText());
                    break;
                default:
                    s = String.format(CMLanguageController.getErrorProp(
                        "Formatted3"), element.tagName());

                    CMHPIUtils.showDefaultMsg(
                        CMLanguageController.getErrorProp("Title"),
                        this.getClass().getName(),
                        Thread.currentThread().getStackTrace()[1].
                            getMethodName(),
                        s,
                        JOptionPane.ERROR_MESSAGE);

                    throw (new UnsupportedOperationException());
            }
        }
    }

    private void readAccount(Element aElement,
        CMOfxDLFIModel tempFiModel)
    {
        Element element;
        Iterator<Element> iterator;
        String s;
        CMOfxDLAccountModel tempAcctModel;

        // aElement points to 'account'
        if (aElement == null)
        {
            CMHPIUtils.showDefaultMsg(
                CMLanguageController.getErrorProp("Error"),
                this.getClass().getName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                "File error: element is null.",
                JOptionPane.ERROR_MESSAGE);

            throw (new UnsupportedOperationException());
        }

        tempAcctModel = new CMOfxDLAccountModel();

        iterator = aElement.children().iterator();

        while (iterator.hasNext())
        {
            element = iterator.next();

            switch (element.tagName().toLowerCase(Locale.US))
            {
                case "acctnumber":
                    tempAcctModel.setAcctNumber(element.ownText());
                    break;
                case "acctname":
                    tempAcctModel.setAcctName(element.ownText());
                    break;
                case "acctuid":
                    tempAcctModel.setAcctUId(element.ownText());
                    break;
                case "acctpw":
                    tempAcctModel.setAcctPW(element.ownText());
                    break;
                case "active":
                    tempAcctModel.setAcctActive(element.ownText());
                    break;
                default:
                    s = String.format(CMLanguageController.getErrorProp(
                        "Formatted3"), element.tagName());

                    CMHPIUtils.showDefaultMsg(
                        CMLanguageController.getErrorProp("Title"),
                        this.getClass().getName(),
                        Thread.currentThread().
                            getStackTrace()[1].getMethodName(),
                        s,
                        JOptionPane.ERROR_MESSAGE);

                    throw (new UnsupportedOperationException());
            }
        }
        tempFiModel.getAccountModels().add(tempAcctModel);
    }

    public Boolean getbDirty()
    {
        return bDirty;
    }

    @Override
    public void setbDirty(Boolean bDirty)
    {
        this.bDirty = bDirty;
    }

    public static ArrayList<CMOfxDLFIModel> getFIMODELS()
    {
        return FIMODELS;
    }

}
