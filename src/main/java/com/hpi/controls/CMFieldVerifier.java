/*
 * Copyright 2015 all rights reserved.
 * HLH LLC
 */
package com.hpi.controls;

import javax.swing.InputVerifier;
import javax.swing.JComponent;

/**
 * Class creates an InputVerifier that always returns true.
 * Used to distinguish between gain/lose focus by client changing fields
 * v. application gain/loss of focus.
 *
 * @author Joe@Higgs-Tx.com
 */
public class CMFieldVerifier extends InputVerifier
{

    //private InputFieldTextUCSymbol inputField;
    private Boolean bFocusLostClient;

    public CMFieldVerifier()
    {
        this.bFocusLostClient = false;
    }
    
    

    @Override
    public boolean shouldYieldFocus(JComponent input)
    {
        /**
         * This method is called only if the client takes action to leave
         * the field. We use it to distinguish between between losing
         * application focus, etc.
         * As such, always return true as we aren't actually validating
         * anything. Tried to use IB callback here without any success.
         * Likely some threading and timing issues.
         */
        // this method can cause side effects, e.g., dialog, etc.
        /*if (input instanceof InputFieldTextUCSymbol)
        {
            inputField = (InputFieldTextUCSymbol) input;
        }

        inputField.setbFocusLostClient(true);
        */
        bFocusLostClient = true;
        return true;
    }

    @Override
    public boolean verify(JComponent input)
    {
        // this method can cause no side effects
        // no checks, always allow
        return true;
    }

    public Boolean getbFocusLostClient()
    {
        return bFocusLostClient;
    }

    public void setbFocusLostClient(Boolean bFocusLostClient)
    {
        this.bFocusLostClient = bFocusLostClient;
    }
    
    
}
