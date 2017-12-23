/**
 * An ordered collection of items, where items are added to the back and removed
 * from the front.
 * 
 * @author
 * 
 */
public class SimpleQueue<E> implements QueueADT {

	// *** fields ***
	private static final int INITSIZE = 10; // initial array size
	private E[] items; // the items in the queue
	private int numItems; // the number of items in the queue
	private int frontIndex = 0; // the index of the front item
	private int rearIndex = -1; // the index of the rear item

	// *** constructor ***
	/**
	 * Constructs a SimpleQueue Set the items to a Array with its size
	 * initalized to be 10; Set the number items to zero;
	 */
	public SimpleQueue() {
		items = (E[]) (new Object[INITSIZE]);
		numItems = 0;
	}

	/**
	 * Checks if the queue is empty.
	 * 
	 * @return true if queue is empty; otherwise false.
	 */
	public boolean isEmpty() {
		if (numItems == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * removes and returns the front item of the queue.
	 * 
	 * @return the front item of the queue.
	 * @throws EmptyQueueException
	 *             if the queue is empty.
	 */
	public Object dequeue() throws EmptyQueueException {
		if (numItems == 0) {
			throw new EmptyQueueException();
		}
		E temp = items[frontIndex];
		numItems--;
		frontIndex = incrementIndex(frontIndex);
		return temp;
	}

	/**
	 * Adds an item to the rear of the queue.
	 * 
	 * @param item
	 *            the item to add to the queue.
	 * @throws IllegalArgumentException
	 *             if item is null.
	 */
	public void enqueue(Object item) {
		if (item == null) {
			throw new IllegalArgumentException();
		}
		if (items.length == numItems) {
			E[] tmp = (E[]) (new Object[items.length * 2]);
			System.arraycopy(items, frontIndex, tmp, frontIndex, items.length
					- frontIndex);
			if (frontIndex != 0) {
				System.arraycopy(items, 0, tmp, items.length, frontIndex);
			}
			items = tmp;
			rearIndex = frontIndex + numItems - 1;
		}

		// use auxiliary method to increment rear index with wraparound
		rearIndex = incrementIndex(rearIndex);

		// insert new item at rear of queue
		items[rearIndex] = (E) item;
		numItems++;
	}

	/**
	 * Returns (but does not remove) the front item of the queue.
	 * 
	 * @return the top item of the stack.
	 * @throws EmptyQueueException
	 *             if the queue is empty.
	 */
	public Object peek() throws EmptyQueueException {
		if (numItems == 0) {
			throw new EmptyQueueException();
		}
		return items[numItems - 1];
	}

	/**
	 * Returns the size of the queue.
	 * 
	 * @return the size of the queue
	 */
	public int size() {
		return numItems;
	}

	/**
	 * Increase the index by one, and set the index to 0 if the index is at the
	 * last index of the array
	 * 
	 * @param index
	 * @return the result number after the increase
	 */
	private int incrementIndex(int index) {
		if (index == items.length - 1)
			return 0;
		else {
			return index + 1;
		}
	}

	/**
	 * Returns a string contains the toString() of all the items starting from
	 * the frontIndex to the rearIndex;
	 */
	public String toString() {
		String output = "";
		int pos = frontIndex;
		for (int i = frontIndex; i < numItems; i++) {
			output += items[pos].toString() + "\n";
			if (pos == numItems - 1) {
				pos = 0;
			} else {
				pos++;
			}
		}
		return output;
	}
}
