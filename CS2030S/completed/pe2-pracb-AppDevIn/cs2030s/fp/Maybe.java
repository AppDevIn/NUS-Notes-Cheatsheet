package cs2030s.fp;

import java.util.NoSuchElementException;

/**
 * A container class the encapsulate a value that may or may not be there.
 * CS2030S Lab 5
 * AY20/21 Semester 2
 *
 * @author Put Your Name (Lab Group)
 */
public abstract class Maybe<T> {

  private static final Maybe<?> NONE = new None();

  /**
   * Create an instance of Maybe with a missing value.  The same 
   * instance is returned everytime.
   *
   * @param <T> The type of the "missing" value in the None instance.
   * @return A Maybe instance with a missing value. 
   */
  public static <T> Maybe<T> none() {
    @SuppressWarnings("unchecked")
    Maybe<T> m = (Maybe<T>) NONE;
    return m;
  }

  /**
   * Create an instance of Maybe with a given value t.  
   *
   * @param <T> The type of the value in the Some instance.
   * @param t The value to be wrapped within this Maybe container
   * @return A new Maybe instance initialized with value t. 
   */
  public static <T> Maybe<T> some(T t) {
    return new Some<T>(t);
  }

  /**
   * Create an instance of Maybe containing the value t if it is is not 
   * null; an instance of Maybe with missing value otherwise.
   *
   * @param <T> The type of the value to be 
   * @param t The value to be wrapped within this Maybe container
   * @return A new Maybe instance that may or may not have the value t. 
   */
  public static <T> Maybe<T> of(T t) {
    return (t == null) ? none() : some(t);
  }

  /**
   * Return the value within this Maybe if exists, thrown an exception
   * otherwise.
   *
   * @return A value in the container 
   * @throw NoSuchElementException 
   */
  protected abstract T get();

  /**
   * If the value within this Maybe does not exist, just return the current
   * instance; otherwise, check the value against the given condition and 
   * return a Maybe with missing value (if cond test is false), or return
   * a Maybe with the value intact.  Note if the value contains is null, 
   * the condition is not tested.
   *
   * @param cond The boolean condition to check (must not be null)
   * @return A Maybe containing the same value (if the value is null or 
   *     passes * the test), or a Maybe with missing value otherwise.
   */
  public abstract Maybe<T> filter(BooleanCondition<? super T> cond);

  /**
   * If the value within this Maybe is missing, just return the current
   * instance; otherwise, apply the given transformation on the value 
   * and return a Maybe with the new value intact.  
   *
   * @param <U> The type of the transformed value.
   * @param transformer The tranformer to apply on the value.
   * @return A Maybe containing the transformed value (if exists) or
   *     containing the missing value otherwise.
   */
  public abstract <U> Maybe<U> map(Transformer<? super T, ? extends U> transformer);

  /**
   * If the value within this Maybe is missing, just return the current
   * instance; otherwise, apply the given transformation on the value and 
   * return a Maybe with the new value intact.   This is similar to map
   * except that the given transformation returns a Maybe and flatMap does
   * not wrap the new value with an additional layer of Maybe.
   *
   * @param <U> The type of the transformed value.
   * @param transformer The tranformer to apply on the value.
   * @return A Maybe containing the transformed value (if exists) or
   *     containing the missing value otherwise.
   */
  public abstract <U> Maybe<U> flatMap(
      Transformer<? super T, ? extends Maybe<? extends U>> transformer);

  /**
   * If the value within this Maybe is missing, return the given u;
   * otherwise, return the value in this Maybe.   
   *
   * @param <U> The type of u.
   * @param u The value to return if this Maybe does not contain any.
   * @return The given u if no value is contained in this Maybe; 
   *     the value contained otherwise.
   */
  public abstract <U extends T> T orElse(U u);

  /**
   * If the value within this Maybe is missing, return the value
   * produced by the given producer.  Otherwise, return the value 
   * in this Maybe.   
   *
   * @param producer The producer to produce the value to return if 
   *     this Maybe does not contain any.
   * @return The value produced by the given producer if no value 
   *     is contained in this Maybe; * the value contained otherwise.
   */
  public abstract T orElseGet(Producer<? extends T> producer);

  /**
   * If the value within this Maybe is missing, does nothing;
   * Otherwise, consume the value with the given consumer.
   *
   * @param consumer The consumer to consume the value 
   * @return none
   */
  public abstract void ifPresent(Consumer<? super T> consumer);

  static final class None extends Maybe<Object> {
    @Override
    public boolean equals(Object obj) {
      if (obj instanceof None) {
        return true;
      }
      return false;
    }

    @Override
    protected Object get() {
      throw new NoSuchElementException();
    }

    @Override
    public Maybe<Object> filter(BooleanCondition<? super Object> cond) {
      return Maybe.<Object>none();
    }

    @Override
    public String toString() {
      return "[]";
    }

    @Override
    public <U> Maybe<U> map(Transformer<? super Object, ? extends U> transformer) {
      return Maybe.<U>none();
    }

    @Override
    public <U> Maybe<U> flatMap(
        Transformer<? super Object, ? extends Maybe<? extends U>> transformer) {
      return Maybe.<U>none();
    }

    @Override
    public <U> U orElse(U u) {
      return u;
    }

    @Override
    public Object orElseGet(Producer<? extends Object> producer) {
      return producer.produce();
    }

    @Override
    public void ifPresent(Consumer<? super Object> consumer) {
      return;
    }
  }

  static final class Some<T> extends Maybe<T> {
    private final T t;

    protected Some(T t) {
      this.t = t;
    }

    @Override
    public String toString() {
      return "[" + t + "]";
    }

    @Override
    protected T get() {
      return t;
    }

    @Override
    public <U> Maybe<U> map(Transformer<? super T, ? extends U> transformer) {
      return Maybe.<U>some(transformer.transform(this.get()));
    }

    @Override
    public <U> Maybe<U> flatMap(Transformer<? super T, ? extends Maybe<? extends U>> transformer) {
      @SuppressWarnings("unchecked")
      Maybe<U> m = (Maybe<U>) transformer.transform(this.get());
      return m;
    }

    @Override
    public Maybe<T> filter(BooleanCondition<? super T> cond) {
      if (this.get() == null || cond.test(this.get())) {
        return this;
      } 
      return Maybe.<T>none();
    }

    @Override
    public <U extends T> T orElse(U u) {
      return this.get();
    }

    @Override
    public T orElseGet(Producer<? extends T> producer) {
      return this.get();
    }

    @Override
    public void ifPresent(Consumer<? super T> consumer) {
      consumer.consume(this.get());
    }
   
    @Override
    public boolean equals(Object obj) {
      if (obj == this) {
        return true;
      }
      if (obj instanceof Some<?>) {
        Some<?> some = (Some<?>) obj;
        if (this.t == some.t) {
          return true;
        }
        if (this.t == null || t == null) {
          return false;
        }
        return this.t.equals(some.t);
      } 
      return false;
    }
  }
}
