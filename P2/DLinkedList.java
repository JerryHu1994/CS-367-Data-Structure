/**
 * The DLinkedList is used to products and users which is impletmented in the
 * main class.
 * 
 * @author Jerry
 *
 * @param <E>
 */
public class DLinkedList<E> implements ListADT {
	private Listnode<E> head;// points to the node in front
	private int numItems;// Represents the number of products or the users in
							// the list
	private Listnode<E> tail;// points to the node in the back

	/**
	 * The constructor sets the head and the tail to null, and also set the
	 * numItems to zero;
	 */
	DLinkedList() {
		head = null;
		numItems = 0;
		tail = null;
	}

	/**
	 * Adds item to the end of the List.
	 * 
	 * @param item
	 *            the item to add
	 * @throws IllegalArgumentException
	 *             if item is null
	 */
	public void add(Object item) {
		if (item == null) {
			throw new IllegalArgumentException();
		}
		if (numItems == 0) {
			Listnode<E> newNode = new Listnode<E>((E) item, null, null);
			head = newNode;
			tail = newNode;
		} else {
			Listnode<E> newNode = new Listnode<E>((E) item, null, tail);
			tail.setNext(newNode);
			tail = newNode;

		}
		numItems++;
	}

	/**
	 * Adds item at position pos in the List, moving the items originally in
	 * positions pos through size() - 1 one place to the right to make room.
	 * 
	 * @param pos
	 *            the position at which to add the item
	 * @param item
	 *            the item to add
	 * @throws IllegalArgumentException
	 *             if item is null
	 * @throws IndexOutOfBoundsException
	 *             if pos is less than 0 or greater than size()
	 */
	public void add(int pos, Object item) {
		if (item == null) {
			throw new IllegalArgumentException();
		}
		if (pos < 0 || pos > numItems) {
			throw new IndexOutOfBoundsException();
		}
		Listnode<E> curr = head;
		if (pos == 0) {
			Listnode<E> newNode = new Listnode<E>((E) item, head, null);
			head.setPrev(newNode);
			head = newNode;
		} else {
			for (int i = 0; i < pos - 1; i++) {
				curr = curr.getNext();
			}
			Listnode<E> newNode = new Listnode<E>((E) item, curr.getNext(),
					curr);
			curr.setNext(newNode);
			if (pos != numItems) {
				newNode.getNext().setPrev(newNode);
			}
		}
		numItems++;

	}

	/**
	 * Returns true iff item is in the List (i.e., there is an item x in the
	 * List such that x.equals(item))
	 * 
	 * @param item
	 *            the item to check
	 * @return true if item is in the List, false otherwise
	 */
	public boolean contains(Object item) {
		if (item == null) {
			throw new IllegalArgumentException();
		}
		Listnode<E> curr = head;
		while (curr.getNext() != null) {
			if (curr.getData().equals(item)) {
				return true;
			}
			curr = curr.getNext();
		}
		return false;
	}

	/**
	 * Returns the item at position pos in the List.
	 * 
	 * @param pos
	 *            the position of the item to return
	 * @return the item at position pos
	 * @throws IndexOutOfBoundsException
	 *             if pos is less than 0 or greater than or equal to size()
	 */
	public Object get(int pos) {
		if (pos < 0 || pos >= numItems) {
			throw new IndexOutOfBoundsException();
		}
		Listnode<E> curr = head;
		for (int i = 0; i < pos; i++) {
			curr = curr.getNext();
		}
		return curr.getData();
	}

	/**
	 * Returns true iff the List is empty.
	 * 
	 * @return true if the List is empty, false otherwise
	 */
	public boolean isEmpty() {
		if (head == null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Removes and returns the item at position pos in the List, moving the
	 * items originally in positions pos+1 through size() - 1 one place to the
	 * left to fill in the gap.
	 * 
	 * @param pos
	 *            the position at which to remove the item
	 * @return the item at position pos
	 * @throws IndexOutOfBoundsException
	 *             if pos is less than 0 or greater than or equal to size()
	 */
	public Object remove(int pos) {
		if (pos < 0 || pos >= numItems) {
			throw new IndexOutOfBoundsException();
		}
		Listnode<E> curr = head;
		if (pos == 0) {
			head = curr.getNext();
			numItems--;
			return curr.getData();
		} else {
			for (int i = 0; i < pos; i++) {
				curr = curr.getNext();
			}
			curr.getPrev().setNext(curr.getNext());
			if (pos != numItems) {
				curr.getNext().setPrev(curr.getPrev());
			}
			numItems--;
			return curr.getData();
		}
	}

	/**
	 * Returns the number of items in the List.
	 * 
	 * @return the number of items in the List
	 */
	public int size() {
		return numItems;
	}

}
