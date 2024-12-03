/**
 * The CoffeeQueue class implements a simple FIFO data structure
 * with limited capacity that can store any Object instances.
 * This is the generic version of Queue from Ex 2.
 * 
 * Items are entered from the back and removed from the front.
 *
 * @author dcsaysp
 * @version CS2030S AY24/25 Semester 1
 */
public class CoffeeQueue<T> implements Comparable<CoffeeQueue<T>> {
  /** An array to store the items in the queue. */
  private T[] items;

  /** Index of the front of the queue. */
  private int front;

  /** Index of the back of the queue. */
  private int back;

  /** Number of elements in the queue. */
  private int size;

  /**
   * Constructor for a queue.
   *
   * @param size The maximum num of elements we can put in the queue.
   */
  public CoffeeQueue(int size) {
    // The only way we can put an object into array is through
    // the method enq() and we only put object of type T inside.
    // So it is safe to cast `Object[]` to `T[]`.
    @SuppressWarnings("unchecked")
    T[] tmp = (T[]) new Object[size + 1];
    this.items = tmp;
    this.front = 0;
    this.back = 0;
    this.size = 0;
  }

  /**
   * Add the object elem into the queue.
   *
   * @param elem The item to put in the queue.
   * @return false if the queue is full;
   *         true if elem is added successfully.
   */
  public boolean enq(T elem) {
    if (this.isFull()) {
      return false;
    }
    this.items[this.back] = elem;
    this.back = (this.back + 1) % this.items.length;
    this.size += 1;
    return true;
  }

  /**
   * Remove the object from the queue.
   *
   * @return null if the queue is empty;
   *              the object removed from the queue otherwise.
   */
  public T deq() {
    if (this.isEmpty()) {
      return null;
    }
    T item = this.items[this.front];
    this.front = (this.front + 1) % this.items.length;
    this.size -= 1;
    return item;
  }

  /**
   * Checks if the queue is full.
   *
   * @return true if the queue is full; false otherwise.
   */
  public boolean isFull() {
    return this.front == ((this.back + 1) % this.items.length);
  }

  /**
   * Checks if the queue is empty.
   *
   * @return true if the queue is empty; false otherwise.
   */
  public boolean isEmpty() {
    return this.front == this.back;
  }
  
  /**
   * Compares two CoffeeQueue according to the size.
   * If result is negative, it means this < other.
   * If result is positive, it meanst this > other.
   * Otherwise, they are treated as equal.
   *
   * @return the difference of the two sizes.
   */
  @Override
  public int compareTo(CoffeeQueue<T> other) {
    return this.size - other.size;
  }

  /**
   * Returns the string representation of the queue.
   * 
   * @return A string consisting of the string representation of 
   *         every object in the queue.
   */
  @Override
  public String toString() {
    String str = "[ ";
    int idx = this.front;
    while (idx != this.back) {
      str += this.items[idx] + " ";
      idx = (idx + 1) % this.items.length;
    } 
    return str + "]";
  }
}
