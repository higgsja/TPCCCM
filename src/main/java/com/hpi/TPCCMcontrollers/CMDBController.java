package com.hpi.TPCCMcontrollers;

import com.hpi.TPCCMprefs.*;
import com.hpi.TPCCMsql.CMDAOException;
import com.hpi.hpiUtils.CMHPIUtils;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import javax.swing.JOptionPane;
import org.apache.commons.dbcp2.*;

/**
 * Creates the database connection and handles errors
 * <p>
 */
public class CMDBController
{

    @SuppressWarnings("FieldMayBeFinal")
    /*
     * Singleton
     */
    private static BasicDataSource ds;

    public static void initDBConnection()
    {
        CMDBModel.DB cmDB;

        ds = new BasicDataSource();
        cmDB = null;

        for (CMDBModel.DB tmp : CMDBModel.getDbList())
        {
            if (tmp.getsActive().equalsIgnoreCase("yes"))
            {
                //find first active 
                cmDB = tmp;
                break;
            }
        }

        if (cmDB == null)
        {
            //todo: error message
            return;
        }

        ds.setDriverClassName(cmDB.getsDriver());
        ds.setUrl(cmDB.getsFullURL());
        ds.setUsername(cmDB.getsUId());
        ds.setPassword(cmDB.getsPW());

        ds.setInitialSize(10);
        ds.setValidationQuery("select 1;");
        ds.setValidationQueryTimeout(5);
    }

    public static synchronized List<Integer>
        executeSQLSingleIntegerList(String sql)
    {
        List<Integer> integerList;
        ResultSet rs;

        integerList = new ArrayList<>();

        try (Connection con = getConnection();
            PreparedStatement pStmt = con.prepareStatement(sql))
        {
            pStmt.clearWarnings();
            rs = pStmt.executeQuery();

            while (rs.next())
            {
                integerList.add(rs.getInt(1));
            }

            pStmt.close();
            con.close();
        } catch (SQLException ex)
        {
            CMHPIUtils.showDefaultMsg(
                CMLanguageController.getDBErrorProp("Title"),
                Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                ex.getMessage(), JOptionPane.INFORMATION_MESSAGE);

            throw new CMDAOException(
                CMLanguageController.getDBErrorProp("Title"),
                Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                sql + ";\n" + ex.getMessage(), JOptionPane.INFORMATION_MESSAGE);
        }

        return integerList;
    }

    public static synchronized void callStored(String sStored)
    {
        try (Connection con = CMDBController.getConnection();
            CallableStatement cStmt = con.prepareCall("{call " + sStored + "}");)
        {
            cStmt.clearWarnings();
            cStmt.execute();

            cStmt.close();
            con.close();
        } catch (SQLException ex)
        {
            // even so, the stored proc continues to run
            // todo: still an issue to solve
            CMHPIUtils.showDefaultMsg(
                CMLanguageController.getDBErrorProp("Title"),
                Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                ex.getMessage() + "\nStored Proc: " + sStored,
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static synchronized void executeSQL(String sSQLStatement)
    {
        Integer iCount;
        ResultSet rs;
        try (Connection con = getConnection();
            PreparedStatement pStmt = con.prepareStatement(sSQLStatement))
        {
//            Boolean bRet = pStmt.execute();

            if (pStmt.execute())
            {
                /*
                 * true if the first result is a ResultSet object;
                 * You must call either the method getResultSet
                 * or getUpdateCount to retrieve the result;
                 * call getMoreResults to move to any subsequent result(s).
                 */
                rs = pStmt.getResultSet();

            } else
            {
                /*
                 * false if the first result is an update count
                 * or there is no result
                 * You must call either the method getResultSet
                 * or getUpdateCount to retrieve the result;
                 * call getMoreResults to move to any subsequent result(s).
                 */
                iCount = pStmt.getUpdateCount();
            }
            // so far, do not care what the return is when we use this
        } catch (SQLException ex)
        {
            // failed on duplicate, ok
            if (ex.getErrorCode() != 1062)
            {
                CMHPIUtils.showDefaultMsg(
                    CMLanguageController.getDBErrorProp("Title"),
                    Thread.currentThread().getStackTrace()[1].getClassName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(),
                    ex.getMessage(), JOptionPane.INFORMATION_MESSAGE);

                throw new CMDAOException(
                    CMLanguageController.getDBErrorProp("Title"),
                    Thread.currentThread().getStackTrace()[1].getClassName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(),
                    sSQLStatement + ";\n" + ex.getMessage(), JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public static synchronized void updateSQL(String sql)
    {
        try (Connection con = getConnection();
            PreparedStatement pStmt = con.prepareStatement(sql);)
        {
            pStmt.clearWarnings();
            pStmt.execute();
            pStmt.close();
            con.close();
        } catch (SQLException ex)
        {
            // thinking that those which timeout are working, just taking
            // longer than expected
            CMHPIUtils.showDefaultMsg(
                CMLanguageController.getDBErrorProp("Title"),
                Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                ex.getMessage(), JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static synchronized Integer doSQLAuto(String sTable, String[] keys,
        String[] values, String checkSQL)
    {
        String s;
        String sInsertSQL;
        Integer iAutoId;
        ResultSet rs;

        iAutoId = null;

        // check if already have the row
        // with auto, count goes up on failure so would rather not
        // allow the failure to insert
        //
        // if we do have the row, do an update
        checkSQL = checkSQL.replace("'null'", "null");

        try (Connection con = getConnection();
            PreparedStatement pStmt = con.prepareStatement(checkSQL))
        {
            pStmt.clearWarnings();
            rs = pStmt.executeQuery();

            if (rs.first())
            {
                iAutoId = rs.getInt(1);
                pStmt.close();
                return iAutoId;
            }
            pStmt.close();
            con.close();
        } catch (SQLException e)
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
            return iAutoId;
        }

        // build insert query
        // never update. would need the auto-increment ID.
        // so, anything that uses this must not require that.
        sInsertSQL = createInsertQuery(sTable, keys, values);

        return insertAutoRow(sInsertSQL);
    }

    private static synchronized String createInsertQuery(String sTable,
        String[] keys, String[] values)
    {
        StringBuffer s1, s2;

        s1 = new StringBuffer();
        s2 = new StringBuffer();

        s1.setLength(0);
        s2.setLength(0);

        for (String s3 : keys)
        {
            s1.append(s3);
            s1.append(",");
        }

        // drop the extra comma
        s1.setLength(s1.length() - 1);

        for (String s3 : values)
        {
            if (s3 == null || s3.equalsIgnoreCase("null"))
            {
                //s2.append(null);
                s2.append((String) null);
                s2.append(",");
                //s2 += null + ",";
            } else
            {
                s2.append("\"");
                s2.append(s3);
                s2.append("\",");
                //s2 += "\"" + s3 + "\",";
            }
        }

        s2.setLength(s2.length() - 1);
        //s2 = s2.substring(0, s2.length() - 1);

        return String.format(CMLanguageController.
            getOfxSqlProp("OfxSQLInsert"),
            sTable,
            s1,
            s2);
    }

    public static synchronized Integer insertAutoRow(String sInsertSQL)
    {
        ResultSet rs;
        String s;
        Integer iRet;
        iRet = null;

        try (Connection con = getConnection();
            PreparedStatement pStmt = con.prepareStatement(sInsertSQL,
                Statement.RETURN_GENERATED_KEYS))
        {
            pStmt.clearWarnings();
            pStmt.executeUpdate();
            rs = pStmt.getGeneratedKeys();

            if (rs != null && rs.next())
            {
                iRet = rs.getInt(1);
                return iRet;
            } else
            {
                s = String.format(CMLanguageController.getErrorProps().
                    getProperty("Formatted13"),
                    sInsertSQL);

                CMHPIUtils.showDefaultMsg(
                    CMLanguageController.getDBErrorProps().
                        getProperty("Title"),
                    Thread.currentThread().getStackTrace()[1].getClassName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(),
                    s, JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex)
        {
            //todo: remove this; just using for PCG issue
            if (ex.getErrorCode() == 1054){
                return -1;
            }
            // failed on duplicate, ok
            if (ex.getErrorCode() != 1062)
            {
                //can get Field OfxInstFId doesn't have a default value
                //when trying to insert a dup
                CMHPIUtils.showDefaultMsg(
                    CMLanguageController.getDBErrorProp("Title"),
                    Thread.currentThread().getStackTrace()[1].getClassName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(),
                    ex.getMessage(), JOptionPane.INFORMATION_MESSAGE);
            }
            return iRet;
        }
        return iRet;
    }

    public static synchronized void insertRow(String sInsertSQL)
    {
        insertRow(sInsertSQL, true);
    }

    public static synchronized void insertRow(String sInsertSQL, Boolean bDupeOk)
    {
        Integer iRet;
        String s;

        try (Connection con = getConnection();
            PreparedStatement sInsert = con.prepareStatement(sInsertSQL))
        {
            sInsert.clearWarnings();
            iRet = sInsert.executeUpdate();
            sInsert.close();
            con.close();

            if (iRet == 0)
            {
                s = String.format(CMLanguageController.getErrorProps().
                    getProperty("Formatted13"),
                    sInsertSQL);

                CMHPIUtils.showDefaultMsg(
                    CMLanguageController.getDBErrorProps().
                        getProperty("Title"),
                    Thread.currentThread().getStackTrace()[1].getClassName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(),
                    s, JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex)
        {
            // failed on duplicate, ok
            if (bDupeOk && ex.getErrorCode() == 1062)
            {
                return;
            }

            if (!bDupeOk && ex.getErrorCode() == 1062)
            {
                // just report it
                s = String.format(CMLanguageController.getErrorProps().
                    getProperty("Formatted19"),
                    sInsertSQL);

                CMHPIUtils.showDefaultMsg(
                    CMLanguageController.getDBErrorProps().
                        getProperty("Title"),
                    Thread.currentThread().getStackTrace()[1].getClassName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(),
                    s, JOptionPane.ERROR_MESSAGE);
                return;
            }

            // otherwise, an issue we need to throw
            throw new CMDAOException(
                CMLanguageController.getDBErrorProp("Title"),
                Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                ex.getMessage(), JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static synchronized void updateSQLNoCommit(String sql)
    {
        try (Connection con = getConnection();
            PreparedStatement pStmt1 = con.prepareStatement("set autocommit=0;");
            PreparedStatement pStmt2 = con.prepareStatement(sql);
            PreparedStatement pStmt3 = con.prepareStatement("commit;");
            PreparedStatement pStmt4 = con.prepareStatement("set autocommit=1;");)
        {
            pStmt1.clearWarnings();
            pStmt1.execute();

            pStmt2.clearWarnings();
            pStmt2.execute();

            pStmt3.clearWarnings();
            pStmt3.execute();

            pStmt4.clearWarnings();
            pStmt4.execute();

            pStmt4.close();
            pStmt3.close();
            pStmt2.close();
            pStmt1.close();
            con.close();
        } catch (SQLException ex)
        {
            // thinking that those which timeout are working, just taking
            // longer than expected; so, message and carry on
            CMHPIUtils.showDefaultMsg(
                CMLanguageController.getDBErrorProp("Title"),
                Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                ex.getMessage(), JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static synchronized void upsertRow(String sUpdateSQL,
        String sInsertSQL)
        throws CMDAOException
    {
        Integer iRet;
        String sql;

        sql = "";
        if (!sUpdateSQL.isEmpty())
        {
            sql = sUpdateSQL;
        } else
        {

            if (!sInsertSQL.isEmpty())
            {
                sql = sInsertSQL;
            }
        }

        if (sql.isEmpty())
        {
            return;
        }

        try (Connection con = getConnection();
            PreparedStatement sUpdate = con.prepareStatement(sql))
        {
            if (sUpdateSQL.isEmpty())
            {
                //todo: there are a few cases where no update can happen.
                // in this event, there can be errors posted for
                // attempting insert of a duplicate row.
                iRet = 0;
                sUpdate.close();
                con.close();
            } else
            {
                sUpdate.clearWarnings();
                iRet = sUpdate.executeUpdate();
                sUpdate.close();
                con.close();
            }

            if (iRet == 0 && !sInsertSQL.isEmpty())
            {
                insertRow(sInsertSQL, true);
            }
        } catch (SQLException ex)
        {
            throw new CMDAOException(
                CMLanguageController.getDBErrorProp("Title"),
                Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                ex.getMessage(), JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static synchronized Connection getConnection()
    {
        try
        {
            return ds.getConnection();
        } catch (SQLException e)
        {
            CMHPIUtils.showMsgTitleClassMethodMsgIcon(CMLanguageController.
                getDBErrorProp("Title"),
                Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                CMLanguageController.
                    getDBErrorProp("IPAddressFailed"),
                JOptionPane.ERROR_MESSAGE, true);

            CMDBModel.setJoomlaUserId(null);
            CMDBModel.setJoomlaPassword(null);
            CMDBModel.setUserId(null);
            System.exit(1);
            return null;
        }
    }

    public static BasicDataSource getDs()
    {
        return ds;
    }
}
