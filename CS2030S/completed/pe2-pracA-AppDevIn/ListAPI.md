# List API (Abridged)
This abridged List API contains documentation for the following methods:

- size
- isEmpty
- contains
- add
- remove
- get
- set
- indexOf
- stream
- of

## size

    int size()

    Returns the number of elements in this list. If this list contains more than Integer.MAX_VALUE elements, returns Integer.MAX_VALUE.
    
    Returns:
    
    the number of elements in this list

## isEmpty

    boolean isEmpty()

    Returns true if this list contains no elements.
    
    Returns:

    true if this list contains no elements

## contains

    boolean contains(Object o)

    Returns true if this list contains the specified element.

    Parameters:

    o - element whose presence in this list is to be tested

    Returns:

    true if this list contains the specified element

## add

    boolean add(E e)

    Appends the specified element to the end of this list.

    Parameters:

    e - element to be appended to this list

    Returns:

    true (as specified by Collection.add(E))

    Throws:

    UnsupportedOperationException - if the add operation is not supported by this list

    ClassCastException - if the class of the specified element prevents it from being added to this list

    NullPointerException - if the specified element is null and this list does not permit null elements

    IllegalArgumentException - if some property of this element prevents it from being added to this list

## remove

    boolean remove(Object o)

    Removes the first occurrence of the specified element from this list, if it is present (optional operation). If this list does not contain the element, it is unchanged.

    Parameters:

    o - element to be removed from this list, if present

    Returns:

    true if this list contained the specified element

    Throws:

    ClassCastException - if the type of the specified element is incompatible with this list (optional)

    NullPointerException - if the specified element is null and this list does not permit null elements (optional)

    UnsupportedOperationException - if the remove operation is not supported by this list

## get

    E get(int index)

    Returns the element at the specified position in this list.

    Parameters:

    index - index of the element to return

    Returns:

    the element at the specified position in this list

    Throws:

    IndexOutOfBoundsException - if the index is out of range (index < 0 || index >= size())

## set

    E set(int index, E element)

    Replaces the element at the specified position in this list with the specified element (optional operation).

    Parameters:

    index - index of the element to replace

    element - element to be stored at the specified position

    Returns:

    the element previously at the specified position

    Throws:

    UnsupportedOperationException - if the set operation is not supported by this list

    ClassCastException - if the class of the specified element prevents it from being added to this list

    NullPointerException - if the specified element is null and this list does not permit null elements

    IllegalArgumentException - if some property of the specified element prevents it from being added to this list

    IndexOutOfBoundsException - if the index is out of range (index < 0 || index >= size())

## indexOf

    int indexOf(Object o)

    Returns the index of the first occurrence of the specified element in this list, or -1 if this list does not contain the element.

    Parameters:

    o - element to search for

    Returns:

    the index of the first occurrence of the specified element in this list, or -1 if this list does not contain the element

    Throws:

    ClassCastException - if the type of the specified element is incompatible with this list (optional)

    NullPointerException - if the specified element is null and this list does not permit null elements (optional)

## stream

    default Stream<E> stream()

    Returns a sequential Stream with this list as its source.

    Returns:

    a sequential Stream over the elements in this collection

## of

    @SafeVarargs
    static <E> List<E> of(E... elements)

    Returns an unmodifiable list containing an arbitrary (zero or more) number of elements. 

    Type Parameters:

    E - the List's element type

    Parameters:

    elements - the elements to be contained in the list

    Returns:

    a List containing the specified elements

    Throws:
    
    NullPointerException - if an element is null or if the array is null                            
