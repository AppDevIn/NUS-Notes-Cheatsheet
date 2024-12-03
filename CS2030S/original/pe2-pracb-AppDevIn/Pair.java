/**
 * Represents a generic Pair of two elements.
 *
 * @param <S> the type of the first element
 * @param <T> the type of the second element
 */
class Pair<S, T> {
  private S first;
  private T second;

  /**
   * Constructs a new Pair with the given first and second values.
   *
   * @param first  the first value of the pair
   * @param second the second value of the pair
   */
  public Pair(S first, T second) {
    this.first = first;
    this.second = second;
  }

  /**
   * Returns the first value of the pair.
   *
   * @return the first value of the pair
   */
  public S getFirst() {
    return this.first;
  }

  /**
   * Returns the second value of the pair.
   *
   * @return the second value of the pair
   */
  public T getSecond() {
    return this.second;
  }
}
