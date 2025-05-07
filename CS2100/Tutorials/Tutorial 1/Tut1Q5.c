#include <stdio.h>
#define MAX 10

int readArray(int [], int);
void printArray(int [], int);
void reverseArray(int [], int);

int main(void) {
	int array[MAX], numElements;

	numElements = readArray(array, MAX);
	reverseArray(array, numElements);
	printArray(array, numElements);

	return 0;
}

int readArray(int arr[], int limit) {
	// ...
	printf("Enter up to %d integers, terminating with a negative integer.\n", limit);
	// ... 
}

void reverseArray(int arr[], int size) {
	// ...
}

void printArray(int arr[], int size) {
	int i;

	for (i=0; i<size; i++) {
		printf("%d ", arr[i]);
	}
	printf("\n");
}

