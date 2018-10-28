package models;

import java.util.Iterator;

import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.ListModel;

/**
 * Special ListModel containig only unique names.
 * names list acces must be atomic in order to be thread safe (1st thread
 * to gain atomic access to the list, blocks other threads access to this list).
 * So all access to {@link #nameSet} are performed in synchronized blocks
 * {@code
 * synchronized(nameSet)
 * {
 * 	...
 * }
 * }
 * Adding or removing elements from the {@link #nameSet} also triggers
 * a fireContentsChanged on all elements of the list (which might have
 * been resorted) which allow the {@link ListModel} to notify any
 * associated widget (such as a {@link JList}).
 * @see {@link javax.swing.AbstractListModel}
 */
public class NameSetListModel extends AbstractListModel<String>
{
	/**
	 * Serial ID (because {@link AbstractListModel} is serializable)
	 */
	private static final long serialVersionUID = -4064595795097980179L;

	/**
	 * Observable unique names set (eventually sorted)
	 * @see ObservableSortedSet
	 */
	private ObservableSortedSet<String> nameSet = null;

	/**
	 * Constructor
	 */
	public NameSetListModel()
	{
		nameSet = new ObservableSortedSet<String>();
	}

	/**
	 * Name set accessor
	 * @return the internal observable name set
	 */
	public ObservableSortedSet<String> getSet()
	{
		return nameSet;
	}

	/**
	 * Add a name to the name set
	 * @param value the name to add
	 * @return true the name to add was non null, non empty, not already present
	 * in the name set and has been added to the name set
	 * @warning don't forget to trigger a
	 * {@link #fireContentsChanged(Object, int, int)} when a name has
	 * been successfully added in order for the widgets to be notified
	 * of any change
	 */
	public boolean add(String value)
	{
		/*
		 * TODO Add a new name to nameSet (iff non null and non empty)
		 * Caution :
		 * 	- nameSet should be modified in a synchronized(namSet){...}
		 * 	block
		 * 	- is nameSet is actually modified observers should be
		 * 	notified with fireContentsChanged(this, 0, nameSet.size()-1)
		 */
		if (nameSet != null && !(nameSet.isEmpty())){
			synchronized (nameSet) {
				nameSet.add(value);
				fireContentsChanged(this, 0, nameSet.size()-1);
				return true;	
			}
		}
		else{
			return false;
		}
	}

	/**
	 * Check if name set contains a specific name
	 * @param value the name to search for
	 * @return true if name set already contains this name, false otherwise
	 */
	public boolean contains(String value)
	{
		/*
		 * TODO return true if value is part of nameSet
		 * Caution :
		 * 	- nameSet should be accessed in a synchronized(namSet){...}
		 * 	block
		 */
		synchronized (nameSet) {
			if (nameSet.contains(value)==true){
				return true;			
			}
			else{
				return false;
			}
		}
	}

	/**
	 * Remove the name at specific index from the name set
	 * @param index the index of the name to remove from name set
	 * @return true if element at index has been successfully removed, false
	 * otherwise
	 * @warning don't forget to trigger a
	 * {@link #fireContentsChanged(Object, int, int)} when a name has
	 * been successfully removed in order for the widgets to be notified
	 * of any change.
	 */
	public boolean remove(int index)
	{
		/*
		 * TODO Remove the element at index "index" of nameSet
		 * Caution :
		 * 	- nameSet should be modified in a synchronized(namSet){...}
		 * 	block
		 * 	- is nameSet is actually modified observers should be
		 * 	notified with fireContentsChanged(this, 0, nameSet.size()-1)
		 */
		
		Iterator<String> iterator = nameSet.iterator();
		synchronized (nameSet) {
		    while(iterator.hasNext()) {
		        if(iterator.next()==this.getElementAt(index)) {
		        	iterator.remove();
		        	fireContentsChanged(this, 0, nameSet.size()-1);
		        	return true;
		        }
		    }
		}
		return false;
	}

	/**
	 * Clears names et content
	 * @warning don't forget to trigger a
	 * {@link #fireContentsChanged(Object, int, int)} when the name set content
	 * changes in order for the widgets to be notified of any change.
	 */
	public void clear()
	{
		/*
		 * TODO clear nameSet
		 * Caution :
		 * 	- nameSet should be modified in a synchronized(namSet){...}
		 * 	block
		 * 	- is nameSet is actually modified observers should be
		 * 	notified with fireContentsChanged(this, 0, nameSet.size()-1)
		 */
		Iterator<String> iterator = nameSet.iterator();
		synchronized (nameSet) {
			while(iterator.hasNext()) {
				iterator.next(); 
				iterator.remove();
				fireContentsChanged(this, 0, nameSet.size()-1);
			}
		}
	}

	/**
	 * Number of elements in the name set
	 * @return the number of elements in the name set
	 * @see javax.swing.ListModel#getSize()
	 */
	@Override
	public int getSize()
	{
		int count = 0;

		synchronized (nameSet) {

			Iterator<String> iterator = nameSet.iterator();
			while(iterator.hasNext()) {
				iterator.next();
				count++;
			}
		}
		return count;
	}

	/**
	 * Accessor to ith element in the name set
	 * @param the index of deisred element
	 * @return The String corresponding to the desired element or null
	 * if there is no such element.
	 * @see javax.swing.ListModel#getElementAt(int)
	 */
	@Override
	public String getElementAt(int index)
	{
		/*
		 * Get nameSet at index "index"
		 * Caution :
		 * 	- nameSet should be accessed in a synchronized(namSet){...}
		 */

		synchronized (nameSet) {

			Iterator<String> iterator = nameSet.iterator();
			
			for(int i = 0; i < this.getSize();i++){
				if(iterator.hasNext()){
					if(i==index){
						return iterator.next();
					}
					else {
						iterator.next();
					}
				}
			}
			return null;
		}
	}

	/**
	 * String representation of the name set
	 * @return a string representation of the name set
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (Iterator<String> it = nameSet.iterator(); it.hasNext();)
		{
			sb.append(it.next());
			if (it.hasNext())
			{
				sb.append(", ");
			}
		}
		return sb.toString();
	}
}
