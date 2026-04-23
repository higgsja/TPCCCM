/*
 * Copyright 2015 all rights reserved.
 * HLH LLC
 */
package com.hpi.controls;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JFormattedTextField;

/**
 *
 * @author Joe@Higgs-Tx.com
 */
public class CMInputField extends JFormattedTextField
    implements PropertyChangeListener
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private boolean bBueno;



    public CMInputField()
    {
        this("");
    }

    public CMInputField(String text)
    {
        super(text);

        bBueno = true;

        customInit();
    }

    private void customInit()
    {
        /*
         * JFormattedTextField has two attributes that can differ from one
         * another.
         * text: what is displayed and returned by getText
         * value: what is validated and returned via getValue, which returns an
         * object
         */
        setupListeners();
        setupInputVerifiers();
    }

    public void setupListeners()
    {
        // only interested in changes to the value property
        addPropertyChangeListener("value", this);
    }

    public void setupInputVerifiers()
    {
        // none for this
        bBueno = true;
    }

    @Override
    public void propertyChange(PropertyChangeEvent e)
    {
        /*
         * Called when field value changes.
         * value is the previous value, text holds the new.
         * This event happens when the new is put to the old.
         * This is the opportunity to look at new and ensure it is valid.
         */
        // do nothing as any input is legitimate for this field
    }



    public boolean esBueno()
    {
        return bBueno;
    }

    public void setBueno(boolean bBueno)
    {
        this.bBueno = bBueno;
    }
}
