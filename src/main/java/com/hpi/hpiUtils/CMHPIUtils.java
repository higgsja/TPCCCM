package com.hpi.hpiUtils;

import com.hpi.TPCCMcontrollers.*;
import com.hpi.TPCCMprefs.*;
import java.text.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.Date;
import java.util.Properties;
import java.util.logging.*;
import javax.swing.*;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.time.*;

public class CMHPIUtils
{

    public static synchronized void showMsgInitializing(String aClass,
        String aMethod,
        String aMsg, Integer iIcon)
    {
        // use prior to having the language file read
        String s;
        System.out.print("Title: Trader Performance Coach" + "; Class: "
            + aClass + "; Method: " + aMethod + ";\n Message: "
            + aMsg + "\n\n");
    }

    public static void showDefaultMsg(String aTitle, String aClass,
        String aMethod, String aMsg, Integer iIcon)
    {
        showMsgTitleClassMethodMsgIcon(aTitle, aClass, aMethod,
            aMsg, iIcon, false);
    }

    /**
     * Shows a dialog message or a console message
     *
     * @param aTitle
     * @param aClass
     * @param aMethod
     * @param aMsg
     * @param iIcon
     * @param wait
     */
    public static void showMsgTitleClassMethodMsgIcon(String aTitle,
        String aClass,
        String aMethod, String aMsg, Integer iIcon, Boolean wait)
    {
        Properties errProps;
        StringBuffer sb;

        errProps = CMLanguageController.getErrorProps();
        sb = new StringBuffer();
        // use after the language file read
        String s;

        if (CMGlobalsModel.getGui() != null && !CMGlobalsModel.getGui())
        {
            sb.append(aTitle); // title
            sb.append("; ");
            sb.append(errProps.getProperty("Err1")); // class
            sb.append(aClass);
            sb.append("; ");
            sb.append(errProps.getProperty("Err2")); // method
            sb.append(aMethod);
            sb.append("; ");
            sb.append(errProps.getProperty("Err3")); // message
            sb.append(aMsg);
            sb.append("\n");

            System.out.print(sb.toString());
            return;
        }

        s = formatMessage(aMsg, 40);

        sb.append(errProps.getProperty("Err1")); // class
        sb.append(aClass);
        sb.append("\n");
        sb.append(errProps.getProperty("Err2")); // method
        sb.append(aMethod);
        sb.append("\n");
        sb.append(errProps.getProperty("Err3")); // message
        sb.append(s);
        sb.append("\n");

        if (wait)
        {
            JOptionPane.showMessageDialog(CMGlobalsModel.getFrame(),
                sb.toString(), aTitle, iIcon);
        } else
        {
            SwingUtilities.invokeLater(() ->
            {
                JOptionPane.showMessageDialog(CMGlobalsModel.getFrame(),
                    sb.toString(), aTitle, iIcon);
            });
        }
    }

    /**
     * Formats a message to fit a given width
     *
     * @param aString
     * @param intMaxLength
     *
     * @return
     */
    public static String formatMessage(String aString, Integer intMaxLength)
    {
        StringBuffer sbRet;
        sbRet = new StringBuffer();
        String s1;
        String[] sLines;
        String sLine;
        String[] sWords;
        int iLength;

        // replace all returns so we just deal with line feeds
        aString = aString.replace("\r\n", "\n");

        //	ensure the end of the string has a line feed
        if (!aString.endsWith("\n"))
        {
            aString += "\n";
        }

        // have a string that ends with \n. could be many \n 
        // that we want to preserve.
        // do string split on \n
        sLines = aString.split("\n");

        // then white space in each of those lines to insert more line feeds
        for (String saLine : sLines)
        {
            // loop through all strings and ensure none exceed maxlength
            if (saLine.length() <= intMaxLength)
            {
                sbRet.append(saLine);
                sbRet.append("\n");
            } else
            {
                // get array of strings on white space in the line
                sWords = saLine.split("\\s");
                sLine = "";
                for (String sWord : sWords)
                {
                    iLength = sLine.length() + sWord.length();
                    if (iLength <= intMaxLength)
                    {
                        sLine = sLine + sWord + " ";
                    } else
                    {
                        sbRet.append(sLine);
                        sbRet.append("\n");
                        sLine = sWord + " ";
                    }
                }

                if (sLine.length() > 0)
                {
                    sbRet.append(sLine);
                    sbRet.append("\n");
                }
            }
        }

        return sbRet.toString();
    }

    public static synchronized String charFill(int length, char charFill)
    {
        if (length > 0)
        {
            char[] array = new char[length];
            Arrays.fill(array, charFill);
            return new String(array);
        }
        return "";
    }

    public static synchronized java.sql.Date convertStringToSQLDate(String date)
    {
        SimpleDateFormat format;
        Date parsed;

        format = new SimpleDateFormat("yyyy-MM-dd");
        parsed = null;

        try
        {
            parsed = format.parse(date);
        } catch (ParseException ex)
        {
            Logger.getLogger(CMHPIUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new java.sql.Date(parsed.getTime());

    }

    public static synchronized java.sql.Date convertDateToSQLDate(java.util.Date date)
    {
        return new java.sql.Date(date.getTime());
    }

    /**
     * Convert string YYYY-MM-DD to LocalDateTime
     *
     * @param date
     *
     * @return
     */
    public static LocalDateTime convertStringToLocalDateTime(String date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date, formatter);
    }

    /**
     *
     * @return String, short date
     */
    public static synchronized String getShortDateISO()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar currentDate = Calendar.getInstance();
        return formatter.format(currentDate.getTime());
    }

    public static synchronized String getShortDateISOOfx(String sDate)
    {
        return sDate.substring(0, 4) + "-" + sDate.substring(4, 6) + "-" + sDate
            .substring(6, 8) + " " + sDate.substring(8, 10) + ":" + sDate
            .substring(10, 12) + ":" + sDate.substring(12, 14);
    }

    /**
     * Formats current local date to yyyyMMddhhmmss
     *
     * @return String, long date
     */
    public static synchronized String getLongDate()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss");
        Calendar currentDate = Calendar.getInstance();
        return formatter.format(currentDate.getTime());
    }

    /**
     * Convert OCC Ticker to etrade ticker where:
     * occ = ssssssyymmddP12345678
     * etrade ticker=underlier:year:month:day:optionType:strikePrice.
     *
     * @param occTicker
     * @return
     */
    public static String getEtradeOption(String occTicker)
    {
        String etradeOption;
        String symbol;
        String year;
        String month;
        String day;
        String type;
        String strike;

        symbol = occTicker.substring(0, 6 - 1).trim();

        year = "20" + occTicker.substring(6, 8);

        month = occTicker.substring(8, 10);

        day = occTicker.substring(10, 12);

        type = occTicker.substring(12, 13);

        strike = occTicker.substring(13, 21);

        int i = 0;
        while (i < strike.length() && strike.charAt(i) == '0')
        {
            i++;
        }
        StringBuilder sb = new StringBuilder(strike);
        sb.replace(0, i, "");

        etradeOption = symbol + ":" + year + ":" + month + ":" + day + ":"
            + type + ":" + Double.parseDouble(strike) / 1000.0;

        return etradeOption;
    }

    public static String getOCCTicker(String ticker, Date expireDate,
        String putcallString, Double strikeDouble)
    {
        String occString;
        String strike;
        String putcall;
        String expiration;
        String symbol;

        /*
         * root symbol to 6
         * expiration to 6, yymmdd
         * type, P/C
         * strike, price * 1000, front padded to 8
         */
        strike = Double.toString(strikeDouble * 1000.0);
        strike = strike.substring(0, strike.length() - 2);
        strike = charFill(8 - strike.length(), "0".charAt(0)) + strike;
        putcall = putcallString.substring(0, 1).toUpperCase();
        expiration = DateFormatUtils.format(expireDate, "yyMMdd");
//        expiration = expireDate.toString().substring(2);
        symbol = ticker.toUpperCase() + charFill(6 - ticker.length(), " "
            .charAt(0));

        occString = symbol + expiration + putcall + strike;
//        occString = occString.replace("-", "");

        return occString;
    }

    public static String getTransactionName(String occString)
    {
        String transactionName;
        String ticker;
        String strike;
        Double dStrike;
        String putcall;
        String expYear;
        String expMonth;
        String expDay;
//        String symbol;

        /*
         * AAPL  210319C00130000
         * root symbol to 6
         * expiration to 6, yymmdd
         * type, P/C
         * strike, price * 1000, front padded to 8
         */
        ticker = (occString.substring(0, 6)).trim();
        expYear = occString.substring(6, 8);
        expMonth = occString.substring(8, 10);
        expDay = occString.substring(10, 12);
        putcall = occString.substring(12, 13);
        dStrike = Double.parseDouble(StringUtils.stripStart(occString.substring(
            13), "0")) / 1000.0;
        strike = dStrike.toString();

        transactionName = ticker;
        transactionName += " ";
        transactionName += expDay;
        transactionName += (Month.of(Integer.parseInt(expMonth))).getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        transactionName += expYear;
        transactionName += " ";
        transactionName += strike;
        transactionName += " ";
        transactionName += putcall.equalsIgnoreCase("c") ? "Call" : "Put";

        return transactionName;
    }
}
