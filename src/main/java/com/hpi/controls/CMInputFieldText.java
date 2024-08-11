/*
 * Copyright 2015 all rights reserved.
 * HLH LLC
 */
package com.hpi.controls;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;

public class CMInputFieldText extends JFormattedTextField
    implements PropertyChangeListener, FocusListener
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private boolean bBueno;

    private Color colorBackground;
//    private Color colorForeground;
    private Color colorBgHighlight;

    private Font fontInitial;
    private Font fontInitialBold;

    public CMInputFieldText()
    {
        this("");
    }

    public CMInputFieldText(String text)
    {
        super(text);

        bBueno = true;

        customInit();
    }

    private void customInit()
    {

        colorBackground = getBackground();
//        colorForeground = getForeground();
        colorBgHighlight = new Color(250, 243, 112);

        fontInitial = getFont();
        fontInitialBold = fontInitial.deriveFont(Font.BOLD);

        /**
         * defaultFormat JFormattedTextField.AbstractFormatter to be used
         * if a more specific JFormattedTextField.AbstractFormatter can not be
         * found.
         * displayFormat JFormattedTextField.AbstractFormatter to be used
         * when the JFormattedTextField does not have focus.
         * editFormat JFormattedTextField.AbstractFormatter to be used
         * when the JFormattedTextField has focus.
         * nullFormat JFormattedTextField.AbstractFormatter to be used
         * when the JFormattedTextField has a null value.
         */
        DefaultFormatterFactory factory;
        TxtFormatter defaultFormat;
        TxtFormatter displayFormat;
        TxtFormatter editFormat;
        TxtFormatter nullFormat;

        defaultFormat = new TxtFormatter();
        displayFormat = new TxtFormatter();
        editFormat = new TxtFormatter();
        nullFormat = new TxtFormatter();
        defaultFormat.setOverwriteMode(false);
        defaultFormat.setAllowsInvalid(true);
        editFormat.setOverwriteMode(false);
        editFormat.setAllowsInvalid(true);

        factory = new DefaultFormatterFactory(defaultFormat, displayFormat,
                                              editFormat, nullFormat);
        this.setFormatterFactory(factory);
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
        addFocusListener(this);
        addPropertyChangeListener("value", this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent e)
    {
        // use getNewValue(), getOldValue()
        // validate that only alpha characters input
    }

    public void setupInputVerifiers()
    {
        // none for this
        bBueno = true;

    }

    @Override
    public void focusGained(FocusEvent e)
    {
        if (bBueno)
        {
            this.setBackground(colorBackground);
            this.setFont(fontInitial);
        }
        else
        {
            // field is not selected
            this.selectAll();
            // change the background color
            this.setBackground(colorBgHighlight);
            this.setFont(fontInitialBold);

        }
    }

    @Override
    public void focusLost(FocusEvent e)
    {
        if (bBueno)
        {
            this.setBackground(colorBackground);
            this.setFont(fontInitial);
        }
        else
        {
            // ensure field not selected
            this.setCaretPosition(0);
            this.moveCaretPosition(0);

            // change the background color
            this.setBackground(colorBgHighlight);
            this.setFont(fontInitialBold);
        }
    }

    public boolean esBueno()
    {
        return bBueno;
    }

    public void setBueno(boolean bBueno)
    {
        this.bBueno = bBueno;
    }

    private class TxtFormatter extends DefaultFormatter
    {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        public TxtFormatter()
        {
            super();

        }

        /**
         * Converts the passed in Object into a String by way of the
         * <code>toString</code> method.
         *
         * @throws ParseException if there is an error in the conversion
         * @param value Value to convert
         *
         * @return String representation of value
         */
        @Override
        public String valueToString(Object value) throws ParseException
        {
            return value.toString();
        }

        @Override
        public Object stringToValue(String text) throws ParseException
        {
            /*
             * Class<?> valueClass;
             *
             * // in case you need to do instanceof, etc.
             * valueClass = getValueClass();
             */

            // can do validations here
            // this is called on every keystroke
            char c;

            // text is onscreen. if valid will later be copied to value.
            setBueno(true);

            if (text.length() == 0)
            {
                setBueno(false);
            }
            else
            {
                // check only alpha characters
                for (int i = text.length() - 1; i >= 0; i--)
                {
                    c = text.charAt(i);
                    if (!Character.isLetter(c))
                    {
                        setBueno(false);
                    }
                }
            }

            // whether valid or not, return the string
            if (esBueno())
            {
                if (!(getBackground() == colorBackground))
                {
                    setBackground(colorBackground);
                    setFont(fontInitial);
                }

                return text;
            }
            else
            {
                // change the background color
                if (getBackground() == colorBackground)
                {
                    setBackground(colorBgHighlight);
                    setFont(fontInitialBold);
                }

                return text;
            }
        }
    }
}
