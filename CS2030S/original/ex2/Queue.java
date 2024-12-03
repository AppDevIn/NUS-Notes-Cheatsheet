/**
 * The Queue class implements a simple FIFO data structure
 * with limited capacity that can store any Object instances.
 * Not to be confused with java.util.Queue.
 * 
 * Items are entered from the back and removed from the front.
 *
 * @author dcsaysp
 * @version CS2030S AY24/25 Semester 1
 */
class Queue {
  /** An array to store the items in the queue. */
  private Object[] items;

  /** Index of the front of the queue. */
  private int front;

  /** Index of the back of the queue. */
  private int back;

  /**
   * Constructor for a queue.
   *
   * @param size The maximum num of elements we can put in the queue.
   */
  public Queue(int size) {
    this.items = new Object[size + 1];
    this.front = 0;
    this.back = 0;
  }

  /**
   * Add the object elem into the queue.
   *
   * @param elem The item to put in the queue.
   * @return false if the queue is full;
   *         true if elem is added successfully.
   */
  public boolean enq(Object elem) {
    if (this.isFull()) {
      return false;
    }
    this.items[this.back] = elem;
    this.back = (this.back + 1) % this.items.length;
    return true;
  }

  /**
   * Remove the object from the queue.
   *
   * @return null if the queue is empty;
   *              the object removed from the queue otherwise.
   */
  public Object deq() {
    if (this.isEmpty()) {
      return null;
    }
    Object item = this.items[this.front];
    this.front = (this.front + 1) % this.items.length;
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
