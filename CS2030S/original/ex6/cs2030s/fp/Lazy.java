package cs2030s.fp;

/**
 * This class implements a lazily evaluated value.
 * The value is computed only when needed and the
 * value is not recomputed.
 *
 * @author XXX
 * @version CS2030S AY24/25 Semester 1
 */

public class Lazy<T> {
  private Producer<T> producer;
  private Maybe<T> value;
}