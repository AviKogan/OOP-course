import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Wrapper class for LinkedList of type String, implement by delegating only the method that needed for use
 * this class.
 *
 * @author Avi Kogan.
 */
public class LinkedListString {

    /**
     * The linkList that holds the added data.
     */
    private LinkedList<String> _linkedList = new LinkedList<String>();

    /**
     * Inserts the specified element at the beginning of this list.
     * @param e the element to add
     */
    public void addFirst(String e){ _linkedList.addFirst(e);}

    /**
     * Returns the first element in this list.
     *
     * @return the first element in this list
     * @throws NoSuchElementException if this list is empty
     */
    public String getFirst() { return _linkedList.getFirst(); }

    /**
     * Returns {@code true} if this list contains the specified element.
     * More formally, returns {@code true} if and only if this list contains
     * at least one element {@code e} such that
     * {@code Objects.equals(o, e)}.
     *
     * @param o element whose presence in this list is to be tested
     * @return {@code true} if this list contains the specified element
     */
    public boolean contains(String o){ return _linkedList.contains(o); }

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    public int size() { return _linkedList.size(); }

    /**
     * Removes the first occurrence of the specified element from this list,
     * if it is present.  If this list does not contain the element, it is
     * unchanged.  More formally, removes the element with the lowest index
     * {@code i} such that
     * {@code Objects.equals(o, get(i))}
     * (if such an element exists).  Returns {@code true} if this list
     * contained the specified element (or equivalently, if this list
     * changed as a result of the call).
     *
     * @param o element to be removed from this list, if present
     * @return {@code true} if this list contained the specified element
     */
    public boolean remove(String o){return _linkedList.remove(o); }

    /**
     * Removes and returns the first element from this list.
     *
     * @return the first element from this list
     * @throws NoSuchElementException if this list is empty
     */
    public String removeFirst(){ return _linkedList.removeFirst(); }
}
