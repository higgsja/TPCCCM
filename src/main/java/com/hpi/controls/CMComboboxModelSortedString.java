/*
 * Copyright 2015 all rights reserved.
 * HLH LLC
 */
package com.hpi.controls;

import java.util.Iterator;
import java.util.TreeSet;
import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;

/**
 * Duplicate DefaultComboBoxModel to use a sorted list
 */
/**
 *
 * @param <String>
 */
public class CMComboboxModelSortedString<String>
    extends AbstractListModel<String>
    implements MutableComboBoxModel<String>
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    protected final TreeSet<String> listObjects;
    protected String selectedObject;

    /**
     * Constructs an empty SortedJListModel object.
     */
    public CMComboboxModelSortedString()
    {
        this(null);
    }

    /**
     * Constructs a SortedJListModel object initialized with
     * an array of objects.
     *
     * @param items
     */
    public CMComboboxModelSortedString(final String items[])
    {
        int i, c;
        listObjects = new TreeSet<>();

        if (items != null)
        {
            for (i = 0, c = items.length; i < c; i++)
            {
                listObjects.add(items[i]);
            }

            if (listObjects.size() > 0)
            {
                selectedObject = listObjects.first();
            }
        }
    }

    @Override
    public int getSize()
    {
        return listObjects.size();
    }

    // public CMComboboxModelSortedString getModel()
    // {
    //     return this.getModel();
    // }

    /**
     *
     * @return
     */
    // public Iterator iterator()
    // {
    //     return listObjects.iterator();
    // }

    /**
     * Clear the model
     */
    public void clear()
    {
        listObjects.clear();
        fireContentsChanged(this, 0, getSize());
    }

    @SuppressWarnings (value="unchecked")
    @Override
    public void removeElement(Object obj)
    {
        int firstIndex = 0;
        int lastIndex = listObjects.size() - 1;
        selectedObject = null;
        listObjects.remove((String) obj);
        fireIntervalRemoved(this, firstIndex, lastIndex);
    }

    @Override
    public void removeElementAt(int index)
    {
        if (getElementAt(index) == selectedObject)
        {
            if (index == 0)
            {
                setSelectedItem(getSize() == 1 ? null : getElementAt(index + 1));
            }
            else
            {
                setSelectedItem(getElementAt(index - 1));
            }
        }

        int firstIndex = 0;
        int lastIndex = listObjects.size() - 1;

        removeElement(getElementAt(index));

        fireIntervalRemoved(this, firstIndex, lastIndex);
    }

    @SuppressWarnings (value="unchecked")
    @Override
    public void setSelectedItem(Object anItem)
    {
        if ((selectedObject != null && !selectedObject.equals(anItem))
            || selectedObject == null && anItem != null)
        {
            selectedObject = (String) anItem;
            fireContentsChanged(this, -1, -1);
        }
    }

    @Override
    public Object getSelectedItem()
    {
        return selectedObject;
    }

    @Override
    public void addElement(String item)
    {
        //todo:
        listObjects.add(item);
        fireIntervalAdded(this, listObjects.size() - 1, listObjects.size() - 1);
        if (listObjects.size() == 1 && selectedObject == null && item != null)
        {
            setSelectedItem(item);
        }
    }

    @Override
    public void insertElementAt(String item, int index)
    {
        // because this is a sorted list, you can call this,
        // however the element will go where it goes.
        listObjects.add(item);
        int firstIndex = 0;
        int lastIndex = listObjects.size() + 1;
        fireIntervalAdded(this, firstIndex, lastIndex);
    }

    @SuppressWarnings (value="unchecked")
    @Override
    public String getElementAt(int index)
    {
        Object[] list;
        list = listObjects.toArray(new Object[listObjects.size()]);
        return (String) list[index];
    }

    public void removeAllElements()
    {
        if (listObjects.size() > 0)
        {
            int firstIndex = 0;
            int lastIndex = listObjects.size() - 1;
            listObjects.clear();
            selectedObject = null;
            fireIntervalRemoved(this, firstIndex, lastIndex);
        }
        else
        {
            selectedObject = null;
        }
    }

    public boolean isEmpty()
    {
        return listObjects.isEmpty();
    }

    @SuppressWarnings (value="unchecked")
    public boolean contains(Object obj)
    {
        return listObjects.contains((String) obj);
    }

    public int indexOf(Object obj)
    {
        return indexOf(obj, 0);
    }

    /**
     *
     * @param obj
     * @param index
     *
     * @return
     */
    @SuppressWarnings (value="unchecked")
    public int indexOf(Object obj, int index)
    {
        if (obj == null)
        {
            return -1;
        }

        if (listObjects.contains((String) obj))
        {
            String sObj;
            Iterator itr;

            itr = listObjects.iterator();

            // loop and find
            while (itr.hasNext())
            {
                sObj = (String) itr.next();
                if (sObj.equals(obj))
                {
                    return index;
                }
                index++;
            }
        }
        return -1;
    }
}
