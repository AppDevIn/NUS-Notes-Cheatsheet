/**
 * The Seq<T> for CS2030S 
 *
 * @author XXX
 * @version CS2030S AY24/25 Semester 1
 */
public class Seq<T> { // TODO: Change to bounded type parameter
  private T[] array;

  public Seq(int size) {
    // TODO: add implementation
  }

  public void set(int index, T item) {
    // TODO: add implementation
  }

  public T get(int index) {
    return null; // TODO: add implementation
  }

  public T min() {
    return null; // TODO: add implementation
  }

  @Override
  public String toString() {
    String out = "[ ";
    for (int i = 0; i < array.length; i++) {
      out = out + i + ":" + array[i];
      if (i != array.length - 1) {
        out = out + ", ";
      }
    }
    return out + " ]";
  }
}
