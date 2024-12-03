public class Pair<S, T> {
  private S first;
  private T second;

  /**
   * Does not work if first/second is null.
   * 
   * @param first
   * @param second
   */
  public Pair(S first, T second) {
    this.first = first;
    this.second = second;
  }

  public S getFirst() {
    return this.first;
  }

  public T getSecond() {
    return this.second;
  }

  @Override
  public String toString() {
    return "<" + this.first + ", " + this.second + ">";
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj instanceof Pair<?, ?>) {
      Pair<?, ?> pair = (Pair<?, ?>) obj;
      return this.first.equals(pair.first)
          && this.second.equals(pair.second);
    }
    return false;
  }
}
