import java.util.EmptyStackException;

/**
 * An ordered collection of items, where items are both added and removed
 * exclusively from the front.
 * 
 * @author
 */
public class SimpleStack<E> implements StackADT {

	// *** fields ***
	private static final int INITSIZE = 10; // initial array size
	private E[] items; // the items in the stack
	private int numItems; // the number of items in the stack

	// *** constructor ***
	/**
	 * Constructs a SimpleQueue Set the items to a Array with its size
	 * initalized to be 10; Set the number items to zero;
	 */
	public SimpleStack() {
		items = (E[]) (new Object[INITSIZE]);
		numItems = 0;
	}

	/**
	 * Checks if the stack is empty.
	 * 
	 * @return true if stack is empty; otherwise false
	 */
	public boolean isEmpty() {
		if (numItems == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns (but does not remove) the top item of the stack.
	 * 
	 * @return the top item of the stack
	 * @throws EmptyStackException
	 *             if the stack is empty
	 */
	public Object peek() throws EmptyStackException {
		if (numItems == 0) {
			throw new EmptyStackException();
		}
		return items[numItems - 1];
	}

	/**
	 * Pops the top item off the stack and returns it.
	 * 
	 * @return the top item of the stack
	 * @throws EmptyStackException
	 *             if the stack is empty
	 */
	public Object pop() throws EmptyStackException {
		if (numItems == 0) {
			throw new EmptyStackException();
		}
		if (numItems == 0) {
			throw new EmptyStackException();
		} else {
			numItems--;
			return items[numItems];
		}
	}

	/**
	 * Pushes the given item onto the top of the stack.
	 * 
	 * @param item
	 *            the item to push onto the stack
	 * @throws IllegalArgumentException
	 *             if item is null.
	 */
	public void push(Object item) {
		if (item == null) {
			throw new IllegalArgumentException();
		}
		if (items.length == numItems) {
			E[] tmp = (E[]) (new Object[items.length * 2]);
			System.arraycopy(items, 0, tmp, 0, items.length);
			items = tmp;
		}
		items[numItems] = (E) item;
		numItems++;
	}

	/**
	 * Returns the size of the stack.
	 * 
	 * @return the size of the stack
	 */
	public int size() {
		return numItems;
	}

	/**
	 * Returns a string representation of the stack (for printing).
	 * 
	 * @return a string representation of the stack
	 */
	public String toString() {
		String output = "";
		for (int i = numItems - 1; i >= 0; i--) {
			output += items[i].toString() + "\n";
		}
		return output;

	}

}
