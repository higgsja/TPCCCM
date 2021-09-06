package com.hpi.TPCCMsql;

import com.hpi.hpiUtils.CMHPIUtils;

public class CMDAOException
        extends RuntimeException
{

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a DAOException with the given detail message.
     *
     * @param message The detail message of the DAOException.
     */
    public CMDAOException(String message)
    {
        super(message);
    }

    /**
     * Constructs a DAOException with the given root cause.
     *
     * @param cause The root cause of the DAOException.
     */
    public CMDAOException(Throwable cause)
    {
        super(cause);
    }

    /**
     * Constructs a DAOException with the given detail message and root cause.
     *
     * @param message The detail message of the DAOException.
     * @param cause The root cause of the DAOException.
     */
    public CMDAOException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Constructs a DAOException from component parts
     *
     * @param sTitle: title bar title
     * @param sErrorPrefix; class
     * @param sFErrorPrefix; method
     * @param sErrorMsg; exception error message
     * @param iIcon; icon to display, e.g., JOptionPane.ERROR_MESSAGE
     */
    public CMDAOException(String sTitle, String sErrorPrefix,
            String sFErrorPrefix, String sErrorMsg, Integer iIcon)
    {
        CMHPIUtils.showDefaultMsg(
                sTitle,
                sErrorPrefix, sFErrorPrefix,
                sErrorMsg, iIcon);
    }
}
