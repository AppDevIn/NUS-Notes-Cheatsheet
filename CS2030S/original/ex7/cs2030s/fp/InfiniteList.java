package cs2030s.fp;

import java.util.ArrayList;
import java.util.List;

/**
 * The InfiniteList class that may contain an arbitrary number of
 * elements.  The class is a generic class.
 * 
 * @author XXX (Your Group)
 * @param <T> The type to be stored in the InfiniteList.
 */
public class InfiniteList<T> {
  private final Lazy<Maybe<T>> head;
  private final Lazy<InfiniteList<T>> tail;
  private static final InfiniteList<?> SENTINEL = (InfiniteList<?>) new Sentinel();

  /**
   * Private constructor to set all fields null.
   */
  private InfiniteList() {
    this.head = null;
    this.tail = null;
  }

  /**
   * Private constructor given head and tail.
   * 
   * @param head The Lazy instance containing the head of the InfiniteList.
   * @param tail The Lazy instance to produce the tail of the InfiniteList.
   */
  private InfiniteList(Lazy<Maybe<T>> head, Lazy<InfiniteList<T>> tail) {
    this.head = head;
    this.tail = tail;
  }

  /**
   * Generates an InfiniteList.  Given a producer that produces
   * a value x, generate the list as [x, x, x, ...]
   * 
   * @param <T> The type to be stored in the InfiniteList.
   * @param prod The producer to produce the value in the InfiniteList.
   * @return The created InfiniteList.
   */
  public static <T> InfiniteList<T> generate(Producer<T> prod) {
    return new InfiniteList<>(
          Lazy.of(() -> Maybe.some(prod.produce())),
          Lazy.of(() -> InfiniteList.generate(prod))
        );
  }

  /**
   * Generate an InfiniteList.  Given x and a lambda f, 
   * generate the list as [x, f(x), f(f(x)), f(f(f(x))), ...]
   * 
   * @param <T> The type to be stored in the InfiniteList.
   * @param init The first element.
   * @param next The transformation function on the element.
   * @return The created InfiniteList.
   */
  public static <T> InfiniteList<T> iterate(T init,
      Transformer<? super T, ? extends T> next) {
    return new InfiniteList<>(
          Lazy.of(Maybe.some(init)),
          Lazy.of(() -> InfiniteList.iterate(next.transform(init), next))
        );
  }

  /**
   * Generate an InfiniteList.  This is an empty InfiniteList.
   * 
   * @param <T> The type to be stored in the InfiniteList.
   * @return The created InfiniteList.
   */
  public static <T> InfiniteList<T> sentinel() {
    // TODO: Comment on safety
    @SuppressWarnings("unchecked")
    InfiniteList<T> res = (InfiniteList<T>) SENTINEL;
    return res;
  }

  /**
   * Lazily search for the first element of the InfiniteList.
   * Then return the value of the first element of the InfiniteList.
   * 
   * @return the head of the InfiniteList.
   */
  public T head() {
    return this.head.get().orElse(() -> this.tail.get().head());
  }

  /**
   * Lazily search for the first element of the InfiniteList.
   * Then return the tail of the first element of the InfiniteList.
   * 
   * @return the tail of the InfiniteList.
   */
  public InfiniteList<T> tail() {
    return this.head.get()
               .map(x -> this.tail.get())
               .orElse(() -> this.tail.get().tail());
  }

  /**
   * Transform each element in the InfiniteList using
   * the given Transformer and return the resulting InfiniteList.
   * 
   * @param <U> The type of the resulting InfiniteList.
   * @param fn  The Transformer to transform 
   *            the element of the InfiniteList.
   * @return    A lazily evaluated InfiniteList with each
   *            element transformed using fn.
   */
  public <U> InfiniteList<U> map(Transformer<? super T, ? extends U> fn) {
    return new InfiniteList<>(
          this.head.map(mHead -> mHead.map(fn)),
          this.tail.map(mTail -> mTail.map(fn))
        );
  }

  /**
   * Check each element of the InfiniteList and filter out
   * elements that evaluate to `false` using the given
   * BooleanCondition.
   * 
   * @param pred The predicate to check element.
   * @return     A lazily evaluated InfiniteList with element
   *             failing the check removed.
   */
  public InfiniteList<T> filter(BooleanCondition<? super T> pred) {
    return new InfiniteList<>(
          this.head.map(mHead -> mHead.filter(pred)),
          this.tail.map(mTail -> mTail.filter(pred))
        );
  }

  // TODO: Write JavaDoc
  public InfiniteList<T> limit(long n) {
    return null; // TODO: Implement
  }

  // TODO: Write JavaDoc
  public List<T> toList() {
    return null; // TODO: Implement
  }

  // TODO: Write JavaDoc
  public InfiniteList<T> takeWhile(BooleanCondition<? super T> pred) {
    return null; // TODO: Implement
  }

  // TODO: Write JavaDoc
  public <U> U foldRight(U id, Combiner<? super T, U, U> acc) {
    return null; // TODO: Implement
  }

  @Override
  public String toString() {
    return "[" + this.head + " " + this.tail + "]";
  }

  // TODO: Write JavaDoc
  public boolean isSentinel() {
    return false;
  }

  /**
   * A nested static class that represents the end of the list.
   * The class contains nothing and performs no operation.
   */
  private static class Sentinel extends InfiniteList<Object> {
    @Override
    public Object head() {
      throw new java.util.NoSuchElementException();
    }

    @Override
    public InfiniteList<Object> tail() {
      throw new java.util.NoSuchElementException();
    }

    @Override
    public <R> InfiniteList<R> map(Transformer<Object, ? extends R> mapper) {
      return InfiniteList.<R>sentinel();
    }

    @Override
    public InfiniteList<Object> filter(BooleanCondition<Object> predicate) {
      return InfiniteList.<Object>sentinel();
    }

    @Override
    public InfiniteList<Object> limit(long n) {
      return null; // TODO: Implement
    }

    @Override
    public List<Object> toList() {
      return null; // TODO: Implement
    }

    @Override
    public InfiniteList<Object> takeWhile(BooleanCondition<Object> pred) {
      return null; // TODO: Implement
    }

    @Override
    public <U> U foldRight(U id, Combiner<Object, U, U> acc) {
      return null; // TODO: Implement
    }

    @Override
    public String toString() {
      return "~";
    }

    @Override
    public boolean isSentinel() {
      return true;
    }
  }
}