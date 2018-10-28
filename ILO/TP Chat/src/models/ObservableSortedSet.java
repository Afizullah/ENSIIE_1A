package models;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Observable;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A {@link SortedSet} that can be {@link Observable}.
 * Each operation of {@link SortedSet} is reimplemented with an eventual
 * nofication of observers when an operation changes the set content.
 * @author davidroussel
 */
public class ObservableSortedSet<E> extends Observable implements SortedSet<E>
{
	/**
	 * The internal sorted set
	 */
	private SortedSet<E> set;

	/**
	 * Default Constructor
	 */
	public ObservableSortedSet()
	{
		set = Collections.synchronizedSortedSet(new TreeSet<E>());
	}

	/**
	 * Constructor from collection
	 * @param c the collection to copy
	 */
	public ObservableSortedSet(Collection<? extends E> c)
	{
		set = Collections.synchronizedSortedSet(new TreeSet<E>(c));
	}

	/**
	 * Constructor from comparator
	 * @param comparator the comparator to use for partial ordering
	 */
	public ObservableSortedSet(Comparator<? super E> comparator)
	{
		set = Collections.synchronizedSortedSet(new TreeSet<E>(comparator));
	}

	/**
	 * Copy constructor from {@link SortedSet}
	 * @param s the sorted set to copy
	 */
	public ObservableSortedSet(SortedSet<E> s)
	{
		set = Collections.synchronizedSortedSet(new TreeSet<E>(s));
	}

	/* (non-Javadoc)
	 * @see java.util.Set#size()
	 */
	@Override
	public int size()
	{
		return set.size();
	}

	/* (non-Javadoc)
	 * @see java.util.Set#isEmpty()
	 */
	@Override
	public boolean isEmpty()
	{
		return set.isEmpty();
	}

	/* (non-Javadoc)
	 * @see java.util.Set#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(Object o)
	{
		return set.contains(o);
	}

	/* (non-Javadoc)
	 * @see java.util.Set#iterator()
	 */
	@Override
	public Iterator<E> iterator()
	{
		return set.iterator();
	}

	/* (non-Javadoc)
	 * @see java.util.Set#toArray()
	 */
	@Override
	public Object[] toArray()
	{
		return set.toArray();
	}

	/* (non-Javadoc)
	 * @see java.util.Set#toArray(java.lang.Object[])
	 */
	@Override
	public <T> T[] toArray(T[] a)
	{
		return set.toArray(a);
	}

	/* (non-Javadoc)
	 * @see java.util.Set#add(java.lang.Object)
	 */
	@Override
	public boolean add(E e)
	{
		boolean added = set.add(e);
		if (added)
		{
			System.out.println(getClass().getSimpleName() + " add(): " + this);
			setChanged();
			notifyObservers(set);
		}
		return added;
	}

	/* (non-Javadoc)
	 * @see java.util.Set#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Object o)
	{
		boolean removed = set.remove(o);
		if (removed)
		{
			System.out.println(getClass().getSimpleName() + " remove(): " + this);
			setChanged();
			notifyObservers(set);
		}
		return removed;
	}

	/* (non-Javadoc)
	 * @see java.util.Set#containsAll(java.util.Collection)
	 */
	@Override
	public boolean containsAll(Collection<?> c)
	{
		return set.containsAll(c);
	}

	/* (non-Javadoc)
	 * @see java.util.Set#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		boolean added = set.addAll(c);
		if (added)
		{
			System.out.println(getClass().getSimpleName() + " addAll(): " + this);
			setChanged();
			notifyObservers(set);
		}
		return added;
	}

	/* (non-Javadoc)
	 * @see java.util.Set#retainAll(java.util.Collection)
	 */
	@Override
	public boolean retainAll(Collection<?> c)
	{
		boolean retained = set.retainAll(c);
		if (retained)
		{
			System.out.println(getClass().getSimpleName() + " retainAll(): " + this);
			setChanged();
			notifyObservers(set);
		}
		return retained;
	}

	/* (non-Javadoc)
	 * @see java.util.Set#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(Collection<?> c)
	{
		boolean removed = set.removeAll(c);
		if (removed)
		{
//			System.out.println(getClass().getSimpleName() + " removeAll(): " + this);
			setChanged();
			notifyObservers(set);
		}
		return removed;
	}

	/* (non-Javadoc)
	 * @see java.util.Set#clear()
	 */
	@Override
	public void clear()
	{
		set.clear();
//		System.out.println(getClass().getSimpleName() + " clear(): " + this);
		setChanged();
		notifyObservers(set);
	}

	/* (non-Javadoc)
	 * @see java.util.SortedSet#comparator()
	 */
	@Override
	public Comparator<? super E> comparator()
	{
		return set.comparator();
	}

	/* (non-Javadoc)
	 * @see java.util.SortedSet#subSet(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SortedSet<E> subSet(E fromElement, E toElement)
	{
		return set.subSet(fromElement, toElement);
	}

	/* (non-Javadoc)
	 * @see java.util.SortedSet#headSet(java.lang.Object)
	 */
	@Override
	public SortedSet<E> headSet(E toElement)
	{
		return set.headSet(toElement);
	}

	/* (non-Javadoc)
	 * @see java.util.SortedSet#tailSet(java.lang.Object)
	 */
	@Override
	public SortedSet<E> tailSet(E fromElement)
	{
		return set.tailSet(fromElement);
	}

	/* (non-Javadoc)
	 * @see java.util.SortedSet#first()
	 */
	@Override
	public E first()
	{
		return set.first();
	}

	/* (non-Javadoc)
	 * @see java.util.SortedSet#last()
	 */
	@Override
	public E last()
	{
		return set.last();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return set.toString();
	}
}
