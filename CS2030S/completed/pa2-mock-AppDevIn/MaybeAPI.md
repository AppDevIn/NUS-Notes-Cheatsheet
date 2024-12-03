# Maybe API (Abridged)
This abridged Stream API contains documentation for the following methods:

- of
- some
- none
- map
- filter
- flatMap
- orElse
- ifPresent
    
## of
    
    static <T> Maybe<T> of(T val)
    
    Creates a Maybe<T> with the given content val if val is not null. Otherwise, returns the shared instance of None<T>.
    
    Type Parameters:
    
    T - the type of value
    
    Parameters:
    
    val - the value in the Maybe
    
    Returns:
    
    a new Maybe<T> or None<T>
    
## some
    
    static <T> Maybe<T> some(T val)
    
    Creates a Maybe<T> with the given content val which may be null.
    
    Type Parameters:
    
    T - the type of value
    
    Parameters:
    
    val - the value in the Maybe
    
    Returns:
    
    a new Maybe<T>
    
## none
    
    static <T> Maybe<T> none()
    
    Creates a Maybe<T> without any content, this is guaranteed to return the shared instance of None<T>.
    
    Type Parameters:
    
    T - the type of value
    
    Returns:
    
    a new None<T>
    
## map
    
    <R> Maybe<R> map(Transformer<? super T, ? extends R> fn)
    
    Create a new instance of Maybe<T> by applying the transformer fn to the content and wrapping it in Maybe<T>. If the result of applying the transformer is null, returns None<T>. If the instance is a None<T>, returns None<T>.
    
    Type Parameters:
    
    R - The element type of the new maybe
    
    Parameters:
    
    fn - a function to apply to the value (if any)
    
    Returns:
    
    a new Maybe<R>

## filter
    Maybe<T> filter(BooleanCondition<? super T> pred)
    
    If the content is not null and pred.test(content) returns true, we return the current instance. Otherwise, returns None<T>. If the instance is a None<T>, returns None<T>.
    
    Parameters:
    
    pred - a predicate to apply to the value to determine if it should be kept
    
    Returns:
    
    a new Maybe<R>
    
## flatMap
    
    <R> Maybe<R> flatMap(Transformer<? super T, ? extends Maybe<? extends R>> fn)
    
    Create a new instance of Maybe<T> by applying the transformer fn to the content without wrapping it in Maybe<T> as fn already returns Maybe<R>. If the instance is a None<T>, returns None<T>.
    
    Type Parameters:
    
    R - The element type of the new maybe
    
    Parameters:
    
    fn - a function to apply to the value (if any)
    
    Returns:
    
    a new Maybe<R>
    
## orElse
    
    T orElse(Producer<? extends T> prod)
    
    Returns the content (even if it is null) if the value is present. If the instance is a None<T>, returns the value produced by the producer.
    
    Parameters:
    
    prod - a producer to be used if the instance is None<T>
    
    Returns:
    
    a value of type T
    
## ifPresent
    
    void ifPresent(Consumer<? super T> cons)
    
    Consumer the content (if any) using the given consumer. If the instance is a None<T>, do nothing.
    
    Parameters:
    
    cons - a consumer to be passed the content (if any)
