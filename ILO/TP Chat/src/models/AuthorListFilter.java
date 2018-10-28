package models;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

/**
 * Filter allowing to check if a {@link Message} comes from one of the authors
 * registered in this filter. Thus this class contains a set of unique names
 * corresponding to the authors registered in this filter.
 * This filter is created using a {@link ListModel} and a
 * {@link ListSelectionModel} allowing to populate the filter with currently
 * selected names
 * @author Afiz
 */
public class AuthorListFilter implements Predicate<Message>//, ListDataListener
{
	/**
	 * Set of unique authors registered in this filter
	 */
	private Set<String> authors;

	/**
	 * Flag indicating if filtering is active or not
	 */
	private boolean filtering;

	/**
	 * Default constructor
	 * Builds an empty {@link #authors} name set and initialize
	 * {@link #filtering} state to false
	 */
	public AuthorListFilter()
	{
		authors = new TreeSet<String>();
		filtering = false;
	}

	/**
	 * Constructor from a {@link ListModel} and a {@link ListSelectionModel}.
	 * Initialize the {@link #authors} and populate it with names of the
	 * {@link ListModel} selected in the {@link ListSelectionModel}
	 * @param listModel the list model containing the elements
	 * @param selectionModel the list selection model containing the indices of
	 * selected elements
	 */
	public AuthorListFilter(ListModel<String> listModel,
	                        ListSelectionModel selectionModel)
	{
		/*
		 * TODO
		 * 	- Add All selected elements of listModel (from selectionModel)
		 * 	into authors
		 * DONE
		 */
		
		this();
		
		if(!(selectionModel.isSelectionEmpty())){
			int minSelectionIndex = selectionModel.getMinSelectionIndex();
			int maxSelectionIndex = selectionModel.getMaxSelectionIndex();

			for(int i = minSelectionIndex; i<= maxSelectionIndex;i++){
				String auteur = listModel.getElementAt(i);
				authors.add(auteur);
			}	
		}
	}

	/**
	 * Adds an author to the authors set
	 * @param author the author to add
	 * @return true if the author was not already in the set and has been
	 * successfully added
	 */
	public boolean add(String author)
	{
		/*
		 * TODO Add author to authors if it is not already in and
		 * return true
		 * DONE
		 */
		if (authors.contains(author)){
			return false;
		}
		else{
			authors.add(author);
			return true;
		}

	}

	/**
	 * Removes an author from the authors set
	 * @param author the author to remove from the set
	 * @return true if the author has been successfully removed from the set
	 */
	public boolean remove(String author)
	{
		/*
		 * TODO remove author from authors if it was there and return true
		 * DONE
		 */
		Iterator<String> iterator = authors.iterator();
	    while(iterator.hasNext()) {
	        String setElement = iterator.next();
	        if(setElement==author) {
	            iterator.remove();
	            return true;
	        }
	    }
		return false;
	}

	/**
	 * Clears all authors from the authors set
	 */
	public boolean clear()
	{
		/*
		 * TODO clear authors if it's not empty then return true
		 * DONE
		 */
		Iterator<String> iterator = authors.iterator();
		boolean bool = false;
	    while(iterator.hasNext()) {
	    	bool = true;
	        iterator.next();
	        iterator.remove();
	    }

		return bool;
	}

	/**
	 * Filtering state accessor
	 * @return true if filtering is active, false otherwise
	 */
	public boolean isFiltering()
	{
		return filtering;
	}

	/**
	 * Filtering state setter
	 * @param filtering the new filtering state
	 */
	public void setFiltering(boolean filtering)
	{
		this.filtering = filtering;
	}

	/**
	 * Predicate test on a specific message
	 * @param m the message to test
	 * @return true filtering is off. True if the message has an author
	 * registered in this filter when filtering is on. False otherwise.
	 */
	@Override
	public boolean test(Message m)
	{
		/*
		 * TODO
		 * if authors is not empty and filtering is on then
		 * Check if message has author within the registerd authors and
		 * if so return true
		 * Otherwise return false
		 * DONE
		 */
		if (authors.isEmpty()==false && this.isFiltering()==true ){
			if (m.hasAuthor()==true) {
				return true;
			}
			else{
				return false;
			}
		}	
		return false;
	}

	/**
	 * String representation of the filter
	 * @return A String represneting this filter's content
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("Filtering ");
		for (Iterator<String> it = authors.iterator(); it.hasNext(); )
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
