# Map API (Abridged)
This abridged Map API contains documentation for the following methods:

- containsKey
- containsValue
- values
- keySet
- put
- get
- remove

## containsKey
    
  boolean containsKey(Object key)

    Returns true if this map contains a mapping for the specified key. More formally, returns true if and only if this map contains a mapping for a key k such that Objects.equals(key, k). (There can be at most one such mapping.)

    Parameters:
    
    key - key whose presence in this map is to be tested
    
    Returns:
    
    true if this map contains a mapping for the specified key
    
    Throws:
    
    ClassCastException - if the key is of an inappropriate type for this map (optional)
    
    NullPointerException - if the specified key is null and this map does not permit null keys (optional) 
    
## containsValue
    
    boolean containsValue(Object value)
    
    Returns true if this map maps one or more keys to the specified value. More formally, returns true if and only if this map contains at least one mapping to a value v such that Objects.equals(value, v). This operation will probably require time linear in the map size for most implementations of the Map interface.

    Parameters:
    
    value - value whose presence in this map is to be tested
    
    Returns:
    
    true if this map maps one or more keys to the specified value
    
    Throws:
    
    ClassCastException - if the value is of an inappropriate type for this map (optional)
    
    NullPointerException - if the specified value is null and this map does not permit null values (optional) 
    
## values
    
    Collection<V> values()
    
    Returns a Collection view of the values contained in this map. The collection is backed by the map, so changes to the map are reflected in the collection, and vice-versa. If the map is modified while an iteration over the collection is in progress (except through the iterator's own remove operation), the results of the iteration are undefined. The collection supports element removal, which removes the corresponding mapping from the map, via the Iterator.remove, Collection.remove, removeAll, retainAll and clear operations. It does not support the add or addAll operations.

    Returns:
    
    a collection view of the values contained in this map 
    
## keySet
    
    Set<K> keySet()
    
    Returns a Set view of the keys contained in this map. The set is backed by the map, so changes to the map are reflected in the set, and vice-versa. If the map is modified while an iteration over the set is in progress (except through the iterator's own remove operation), the results of the iteration are undefined. The set supports element removal, which removes the corresponding mapping from the map, via the Iterator.remove, Set.remove, removeAll, retainAll, and clear operations. It does not support the add or addAll operations.

    Returns:
    
    a set view of the keys contained in this map 
    
## put
    
    V put(K key, V value)
    
    Associates the specified value with the specified key in this map (optional operation). If the map previously contained a mapping for the key, the old value is replaced by the specified value. (A map m is said to contain a mapping for a key k if and only if m.containsKey(k) would return true.)

    Parameters:
    
    key - key with which the specified value is to be associated
    
    value - value to be associated with the specified key
    
    Returns:
    
    the previous value associated with key, or null if there was no mapping for key. (A null return can also indicate that the map previously associated null with key, if the implementation supports null values.)
    
    Throws:
    
    UnsupportedOperationException - if the put operation is not supported by this map
    
    ClassCastException - if the class of the specified key or value prevents it from being stored in this map
    
    NullPointerException - if the specified key or value is null and this map does not permit null keys or values
    
    IllegalArgumentException - if some property of the specified key or value prevents it from being stored in this map 

## get
    
    V get(Object key)
    
    Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key.

    More formally, if this map contains a mapping from a key k to a value v such that Objects.equals(key, k), then this method returns v; otherwise it returns null. (There can be at most one such mapping.)

    If this map permits null values, then a return value of null does not necessarily indicate that the map contains no mapping for the key; it's also possible that the map explicitly maps the key to null. The containsKey operation may be used to distinguish these two cases.

    Parameters:
    
    key - the key whose associated value is to be returned
    
    Returns:
    
    the value to which the specified key is mapped, or null if this map contains no mapping for the key
    
    Throws:
    
    ClassCastException - if the key is of an inappropriate type for this map (optional)
    
    NullPointerException - if the specified key is null and this map does not permit null keys (optional) 

## remove
    
    V remove(Object key)
    
    Removes the mapping for a key from this map if it is present (optional operation). More formally, if this map contains a mapping from key k to value v such that Objects.equals(key, k), that mapping is removed. (The map can contain at most one such mapping.)

    Returns the value to which this map previously associated the key, or null if the map contained no mapping for the key.

    If this map permits null values, then a return value of null does not necessarily indicate that the map contained no mapping for the key; it's also possible that the map explicitly mapped the key to null.

    The map will not contain a mapping for the specified key once the call returns.

    Parameters:
    
    key - key whose mapping is to be removed from the map
    
    Returns:
    
    the previous value associated with key, or null if there was no mapping for key.
    
    Throws:
    
    UnsupportedOperationException - if the remove operation is not supported by this map
    
    ClassCastException - if the key is of an inappropriate type for this map (optional)
    
    NullPointerException - if the specified key is null and this map does not permit null keys (optional) 
