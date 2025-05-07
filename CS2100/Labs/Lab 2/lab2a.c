#include <stdio.h>

void display(int);

int main() {
	int ageArray[] = { 2, 15, 4 };
	display(ageArray[2]);
	return 0;
}

void display(int age) {
	printf("%d\n", age);
}
