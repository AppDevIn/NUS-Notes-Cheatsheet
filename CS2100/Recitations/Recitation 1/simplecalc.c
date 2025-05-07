#include <stdio.h>

int main() {

    int exit=0;

    while(!exit) {
        char op;
        float x, y;

        printf("Please enter an operation (+, -, *, / or q to quit): ");
        scanf("%c", &op);

        switch(op) {
            case '+':
            case '-':
            case '*':
            case '/':
                printf("\nEnter the first nunber: ");
                scanf("%f", &x);

                printf("\nEnter the second number: ");
                scanf("%f", &y);
        }

        float res;
        int valid=1;
        switch(op) {
            case '+':
                res = x + y;
                break;
            case '-':
                res = x - y;
                break;

            case '*':
                res = x * y;
                break;

            case '/': 
                res = x / y;
                break;

            case 'q':
                exit = 1;
                break;

            default:
                printf("\nUnrecognized operation\n");
                valid=0;
            
        }

        if(!exit && valid)
            printf("\n%.2f %c %.2f = %.2f\n\n", x, op, y, res);
        
        if(exit)
            printf("\nBye!\n");

        fflush(stdin);
    }

    return 0;
}

