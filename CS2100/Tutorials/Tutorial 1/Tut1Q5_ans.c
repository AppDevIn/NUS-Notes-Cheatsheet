#include <stdio.h>
#define MAX 10

int readArray(int [], int);
void printArray(int [], int);
void reverseArray(int [], int);
void reverseArrayV2(int [], int);
void reverseArrayRec(int [], int, int);

int main(void) {
	int array[MAX], numElements;

	numElements = readArray(array, MAX);
	reverseArray(array, numElements);
	// reverseArrayV2(array, numElements);
	printArray(array, numElements);

	return 0;
}

int readArray(int arr[], int limit) {
	int i, input;

	printf("Enter up to %d integers, terminating with a negative integer.\n", limit);
	i = 0;
	scanf("%d", &input);
	while (input >= 0) {
		arr[i] = input;
		i++;
		scanf("%d", &input);
	}
	return i;
}

// Iterative version
// Other solutions possible
void reverseArray(int arr[], int size) {
	int left=0, right=size-1, temp;

	while (left < right) {
		temp = arr[left]; arr[left] = arr[right]; arr[right] = temp;
		left++; right--;
	}
}

// Recursive version
// Auxiliary/driver function for the recursive function 
// reverseArrayRec()
void reverseArrayV2(int arr[], int size) {
	reverseArrayRec(arr, 0, size-1);
}

void reverseArrayRec(int arr[], int left, int right) {
	int temp;

	if (left < right) {
		temp = arr[left]; arr[left] = arr[right]; arr[right] = temp;
		reverseArrayRec(arr, left+1, right-1);
	}
}

void printArray(int arr[], int size) {
	int i;

	for (i=0; i<size; i++) {
		printf("%d ", arr[i]);
	}
	printf("\n");
}

