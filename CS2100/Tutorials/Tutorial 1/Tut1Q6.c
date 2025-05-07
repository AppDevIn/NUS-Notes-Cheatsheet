#include <stdio.h>

int main(void) {
	int a = 3, *b, c, *d, e, *f;

	b = &a;
	*b = 5; 
	c = *b * 3;
	d = b;
	e = *b + c;
	*d = c + e; 
	f = &e;
	a = *f + *b; 
	*f = *d - *b;

	printf("a = %d, c = %d, e = %d\n", a, c, e);
	printf("*b = %d, *d = %d, *f = %d\n", *b, *d, *f);

	return 0;
}
