### Problem 1. Quicksort Review
a) Suppose that the pivot choice is the median of the first, middle and last keys, can you find a bad input for QuickSort?

Answer:
1, 12, 15, 33, 2, 44, 55, 60, 10, 3. This array will produce the worst- case scenario since the median of this will be 2 this will create the partition where 1 will be the left and 12, 15, 33, 44, 55, 60, 10, 3 will be the right partition 

b) Are any of the partitioning algorithms we have seen for QuickSort stable? Can you design a stable partitioning algorithm? Would it be efficient?

Answer:
We can do that by manually creating two different array left and right. We will add all the elements to the left and the right side of the array depending on the value and the pivot. This ensure stability is to maintain since no swapping is done but the space efficiency does drops since it will be $O(n)$ instead of $O(1)$

#### Implementation Code

```java
import java.util.ArrayList;
import java.util.Arrays;

public class QuickSortStable {
    int[] arr;

    public QuickSortStable(int[] arr) {
        this.arr = arr;
        this.arr = sort(arr);
    }

    public int[] sort(int[] arr) {
        if (arr.length <= 1) {
            return arr;
        }

        int pivotIdx = arr.length / 2;
        int pivotValue = arr[pivotIdx];

        int[][] partitions = partition(arr, pivotValue);

        int[] leftArr = sort(partitions[0]);
        int[] rightArr = sort(partitions[1]);

        return merge(leftArr, new int[]{pivotValue}, rightArr);
    }

    private int[][] partition(int[] arr, int pivotValue) {
        ArrayList<Integer> leftList = new ArrayList<>();
        ArrayList<Integer> rightList = new ArrayList<>();

        for (int num : arr) {
            if (num < pivotValue) {
                leftList.add(num);
            } else if (num > pivotValue) {
                rightList.add(num);
            }
        }

        return new int[][]{listToArray(leftList), listToArray(rightList)};
    }

    private int[] merge(int[] left, int[] pivot, int[] right) {
        int[] merged = new int[left.length + pivot.length + right.length];
        System.arraycopy(left, 0, merged, 0, left.length);
        System.arraycopy(pivot, 0, merged, left.length, pivot.length);
        System.arraycopy(right, 0, merged, left.length + pivot.length, right.length);
        return merged;
    }

    private int[] listToArray(ArrayList<Integer> list) {
        return list.stream().mapToInt(i -> i).toArray();
    }
}

```
### Problem 2.
a) Given an array A, decide if there are any duplicated elements in the array.

Answer:
I will solve this problem by using a dictionary (or a hash map) to keep track of the elements in the array. As I iterate through the array, I will add each element as a key in the dictionary.

- If I encounter an element that already exists as a key in the dictionary, it means a duplicate has been found, and I will return `true` immediately.
- If I reach the end of the array without finding any duplicates, I will return `false`.

This approach has a time complexity of O(n) and a space complexity of O(n), where n is the size of the array.

##### Code Implementation 
```java
import java.util.Dictionary;
import java.util.HashMap;

public class DuplicateChecker {

    public static boolean hasDuplicates(int[] arr) {
        HashMap<Integer, Integer> kvMap = new HashMap<>();

        for (int i = 0; i < arr.length; i++) {
            if (!kvMap.containsKey(arr[i])) {
                kvMap.put(arr[i], 0);
            } else {
                return true;
            }
        }

        return false;
    }
}
```


#### Part B
Given an array A, output another array B with all the duplicates removed. Note the order of the elements in B does not need to follow the same order in A. That means if array A is {3, 2, 1, 3, 2, 1}, then your algorithm can output {1, 2, 3}.


Answer: 
I sort the array first. After sorting, I check each element with its adjacent element. If they are the same, I skip the duplicate. Otherwise, I add the unique element to the output array.

The time complexity of this algorithm is **O(n log n)** due to the sorting step, which dominates the overall complexity.
##### Code Implementation 
```java
public static List<Integer> uniqueArr(List<Integer> list) {
    Collections.sort(list);


    List<Integer> newList = new ArrayList<>();
    for (int i = 0; i < list.size()-1; i++) {
        if (list.get(i).equals(list.get(i+1)))
            continue;
        newList.add(list.get(i));
    }

    return newList;
}
```



#### Part C
Given arrays A and B, output a new array C containing all the distinct items in both A and B. You are given that array A and array B already have their duplicates removed.

Answer:
The most efficient way to solve this problem is to first sort both arrays (if they are not already sorted). Once sorted, I will traverse through both arrays simultaneously using a two-pointer approach to merge them while checking for duplicates.

Since each element in both arrays is already unique, this ensures we only add distinct elements to the output array **C**. The time complexity of this approach is $O(m + n)$, where **m** and **n** are the lengths of arrays **A** and **B**, respectively.

#### Part D
Given array A and a target value, output two elements x and y in A where (x + y) equals the target value.

Answer:
Use a hashmap as follows: If a key does not exist, set the key to  ( target − current element ) (target−current element) and the value to the current element. Then, when iterating through the array, let  ans = target − current element ans=target−current element. If  ans ans exists as a key in the hashmap, retrieve its corresponding value.

### Problem 3: Child Jumble 
In this matching problem, you cannot compare two shoes directly or two children’s feet directly; you only learn if a given shoe is too big, too small, or just right for a particular child. A classic solution is the “nuts and bolts” or partition-based method: choose any shoe as a pivot, identify which child it fits by trying it on each child, then use that child to separate the remaining shoes into “smaller” and “bigger,” and use the pivot shoe to separate the remaining children into “smaller-footed” and “bigger-footed.” You then recurse on these smaller and bigger groups. This partitioning strategy, done carefully, mirrors quicksort’s divide-and-conquer approach and runs in average O(nlog⁡n)O(n log n)O(nlogn) time.

### Problem 4: More Pivots
#### Part A
Assuming that you possess k “ideal” pivots that divide the array, you can partition by iterating through all n elements and comparing them sequentially with the pivots. Based on the comparisons made, each element will be allocated to one of the k+1 partitions.

#### Part B
Partitioning takes $O(k \cdot n)$ time in a straightforward implementation, because for each of the n elements, you may need to compare against up to k pivots to find its correct partition.

#### Part C

$$
T(n)=O(kn)+(k+1) \cdot T(\frac{k+1}{n}​)
$$

#### Part D
Solving this recurrence shows an overall time of $O(n \log n)$. Increasing k changes the base of the logarithm, but in Big-O notation it’s still $O(n \log n)$, so asymptotically it does **not** improve beyond that (it just affects the constant factors).

### Problem 5: Integer Sort 

#### Part A
I 
