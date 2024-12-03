package cs2030s.fp;

/**
 * Represent a function that consumes a value.
 *
 * @param <T> The type of the value produced.
 */
@FunctionalInterface
public interface Consumer<T> {
  /**
   * The functional method to consume a value.
   *
   * @param t the value to consume
   */
  void consume(T t);
}
