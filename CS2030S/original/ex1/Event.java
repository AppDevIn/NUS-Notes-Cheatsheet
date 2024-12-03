import java.text.DecimalFormat;

/**
 * The Event class is an abstract class that encapsulates a 
 * discrete event to be simulated. An event encapsulates the 
 * time the event occurs. A subclass of event _must_ override 
 * the simulate() method to implement the logic of the 
 * simulation when this event is simulated. The simulate method
 * returns an array of events, which the simulator will then 
 * add to the event queue. Note that an event also implements 
 * the Comparable interface so that a PriorityQueue can 
 * arrange the events in the order of event time.
 *
 * @author dcsaysp
 * @version CS2030S AY24/25 Semester 1
 */
abstract class Event implements Comparable<Event> {
  /** To format decimal numbers to 3 decimal places */
  private static final DecimalFormat decimalFormat = new DecimalFormat("0.000");

  /** The time this event occurs in the simulation. */
  private final double time;

  /**
   * Creates an event that occurs at the given time.
   *
   * @param time The time the event occurs.
   */
  public Event(double time) {
    this.time = time;
  }

  /**
   * Getter to return the time of this event.
   *
   * @return The time this event occurs.
   */
  public double getTime() {
    return this.time;
  }

  /**
   * Compare this event with a given event e.
   *
   * @param e The other event to compare to.
   * @return 1 if this event occurs later than e; 
   *         0 if they occur the same time; 
   *         -1 if this event occurs earlier.
   */
  @Override
  public int compareTo(Event e) {
    if (this.time > e.time) {
      return 1;
    } else if (this.time == e.time) {
      return 0;
    } else {
      return -1;
    }
  }

  /**
   * Return the string representation this event.
   *
   * @return A string consists of the time this event occurs.
   */
  @Override
  public String toString() {
    return decimalFormat.format(this.time);
  }

  /**
   * Simulate this event.
   *
   * @return An array of new events to be scheduled by the simulator.
   */
  public abstract Event[] simulate();
}
